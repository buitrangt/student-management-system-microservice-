package org.aibles.gateway.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aibles.gateway.utils.dto.TokenVerificationResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
@Order(1)
public class AuthenFilter implements WebFilter {

    private final RestTemplate restTemplate;
    private static final String AUTH_SERVICE_URL = "http://localhost:8089/api/v1/users/verify-token";

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/v1/users/register",
            "/api/v1/users/login",
            "/api/v1/users/verify-token"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();

        // Bỏ qua xác thực cho các đường dẫn không yêu cầu token
        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            log.info("Path {} is excluded from authentication.", path);
            return chain.filter(exchange);
        }

        // Lấy token từ header Authorization
        String accessToken = request.getHeaders().getFirst("Authorization");
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            log.warn("(AuthenFilter) No Authorization header or token not starting with 'Bearer'");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String jwtToken = accessToken.substring(7);

        // Xác thực token với Auth Service
        if (!verifyTokenWithAuthService(jwtToken)) {
            log.warn("Invalid token for request to path {}. Access denied.", path);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // Đặt Authentication vào SecurityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        // Tạo request mới với header "X-Original-Token"
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-Original-Token", accessToken)
                .build();

        ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
        log.info("Token validated successfully for path {}. Proceeding with request.", path);

        // Thêm SecurityContext vào ReactiveSecurityContextHolder
        return chain.filter(modifiedExchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
    }

    private boolean verifyTokenWithAuthService(String jwtToken) {
        try {
            log.info("Starting token verification for token: {}", jwtToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + jwtToken);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("token", jwtToken);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            log.info("Sending request to AUTH_SERVICE_URL: {}", AUTH_SERVICE_URL);
            ResponseEntity<TokenVerificationResponse> authResponse = restTemplate.exchange(
                    AUTH_SERVICE_URL,
                    HttpMethod.POST,
                    entity,
                    TokenVerificationResponse.class
            );

            log.info("Received response from auth service with status code: {}", authResponse.getStatusCode());

            boolean isValid = authResponse.getBody() != null && authResponse.getBody().getData().isValid();
            log.info("Token verification result: {}", isValid ? "Valid" : "Invalid");

            return isValid;
        } catch (Exception e) {
            log.error("Failed to authenticate token: {}. Error message: {}", jwtToken, e.getMessage(), e);
            return false;
        }
    }
}
