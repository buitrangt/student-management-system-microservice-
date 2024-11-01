package org.aibles.gateway.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aibles.gateway.utils.dto.TokenVerificationResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
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

        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String accessToken = request.getHeaders().getFirst("Authorization");
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            log.warn("(AuthenFilter) No Authorization header or token not starting with 'Bearer'");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String jwtToken = accessToken.substring(7);

        if (!verifyTokenWithAuthService(jwtToken)) {
            log.warn("Invalid token, access denied.");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-Original-Token", accessToken)
                .build();

        ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
        return chain.filter(modifiedExchange);
    }

    private boolean verifyTokenWithAuthService(String jwtToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + jwtToken);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("token", jwtToken);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<TokenVerificationResponse> authResponse = restTemplate.exchange(
                    AUTH_SERVICE_URL,
                    HttpMethod.POST,
                    entity,
                    TokenVerificationResponse.class
            );

            return authResponse.getBody() != null && authResponse.getBody().getData().isValid();
        } catch (Exception e) {
            log.error("Failed to authenticate token: {}", e.getMessage());
            return false;
        }
    }
}
