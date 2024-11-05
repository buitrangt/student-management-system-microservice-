package org.aibles.classservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aibles.classservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final PermissionService permissionService;
    @Value("${Secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("X-Original-Token");


        String jwtToken = token.substring(7);
        String roleName;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            roleName = claims.get("role", String.class);
        } catch (Exception e) {
            log.error("Failed to decode token", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        Long roleId = permissionService.findRoleIdByRoleName(roleName);
        if (roleId == null) {
            log.warn("Role '{}' does not exist in the system.", roleName);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        String resource = request.getRequestURI();
        String method = request.getMethod();

        if (!permissionService.hasAccess(roleId, resource, method)) {
            log.warn("Role ID '{}' does not have permission to access {} {}", roleId, method, resource);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
