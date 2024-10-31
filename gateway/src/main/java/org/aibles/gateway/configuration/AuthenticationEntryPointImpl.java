package org.aibles.gateway.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aibles.gateway.utils.dto.ApplicationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AuthenticationEntryPointImpl implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
        log.info("Authentication failed for request: {}", exchange.getRequest().getURI().getPath());

        ApplicationResponse<Void> errorResponse = ApplicationResponse.error(HttpStatus.UNAUTHORIZED.value(), "Error: Unauthorized");

        return Mono.fromRunnable(() -> {
            try {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                byte[] bytes = objectMapper.writeValueAsString(errorResponse).getBytes(StandardCharsets.UTF_8);
                exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))).subscribe();
            } catch (IOException e) {
                log.error("Error writing response", e);
            }
        });
    }
}
