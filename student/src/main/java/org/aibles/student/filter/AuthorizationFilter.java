package org.aibles.student.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aibles.student.entity.Permission;
import org.aibles.student.service.PermissionService;
import org.aibles.student.service.UserRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter implements WebFilter {

    private final PermissionService permissionService;
    private final UserRoleService userRoleService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst("user_id");
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();



        if (userId == null) {
            log.warn("Missing user_id in header.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return checkUserPermission(userId, path, method)
                .flatMap(hasPermission -> {
                    if (hasPermission) {
                        return chain.filter(exchange);
                    } else {
                        log.warn("User {} does not have permission to access {} {}", userId, method, path);
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                });
    }

    private Mono<Boolean> checkUserPermission(String userId, String resource, String method) {
        return Mono.fromSupplier(() -> userRoleService.findRolesByUserId(userId))
                .flatMapMany(roles -> Flux.fromIterable(roles))
                .flatMap(role -> permissionService.findPermissionByRoleAndResource(role.getId(), resource, method))
                .hasElements();
    }



}
