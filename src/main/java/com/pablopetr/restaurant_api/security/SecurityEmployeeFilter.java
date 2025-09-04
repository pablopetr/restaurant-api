package com.pablopetr.restaurant_api.security;

import com.pablopetr.restaurant_api.providers.JWTEmployeeProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityEmployeeFilter extends OncePerRequestFilter {

    private final JWTEmployeeProvider jwtEmployeeProvider;

    public SecurityEmployeeFilter(JWTEmployeeProvider jwtEmployeeProvider) {
        this.jwtEmployeeProvider = jwtEmployeeProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException
    {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String rawToken = header.substring("Bearer ".length()).trim();

        var token = jwtEmployeeProvider.validateToken(rawToken);

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        var roles = token.getClaim("roles").asList(String.class);
        var authorities = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.toUpperCase()))
                .toList();

        var auth = new UsernamePasswordAuthenticationToken(token.getSubject(), null, authorities);
        request.setAttribute("employee_id", token.getSubject());
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }
}
