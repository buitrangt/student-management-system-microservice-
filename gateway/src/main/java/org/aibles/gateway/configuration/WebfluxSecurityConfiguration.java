package org.aibles.gateway.configuration;

import lombok.AllArgsConstructor;
import org.aibles.gateway.security.AuthenFilter;
import org.aibles.gateway.utils.constants.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@ComponentScan(basePackages = {"org.aibles"})
@AllArgsConstructor
@EnableWebFluxSecurity
public class WebfluxSecurityConfiguration {

    private final AuthenFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPointImpl authenticationErrorHandle;

    private static final String REGISTER_CUSTOMER = "/api/v1/users/register/**";
    private static final String ACTIVE_CUSTOMER = "/api/v1/users/login";
    private static final String VERIFY_OTP_CUSTOMER = "/api/v1/users/verify-token";

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(REGISTER_CUSTOMER, ACTIVE_CUSTOMER,VERIFY_OTP_CUSTOMER).permitAll()
                        .anyExchange().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationErrorHandle))
                .build();
    }
}
