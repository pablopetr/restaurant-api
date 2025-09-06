package com.pablopetr.restaurant_api.security;

import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import com.pablopetr.restaurant_api.providers.JWTEmployeeProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityEmployeeFilter extends OncePerRequestFilter {
    @Autowired
    private EmployeeRepository employeeRepository;

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

        String employeeId = token.getSubject();
        Integer tokenVersion = token.getClaim("token_version").asInt();

        if(employeeId == null || tokenVersion == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UUID employeeIdFromToken;

        try {
            employeeIdFromToken = UUID.fromString(employeeId);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Optional<EmployeeEntity> employeeEntityOptional = this.employeeRepository.findById(employeeIdFromToken);

        if(employeeEntityOptional.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        EmployeeEntity employee = employeeEntityOptional.get();

        Integer tokenVersionFromDatabase = employee.getTokenVersion();

        if(tokenVersionFromDatabase == null || !tokenVersionFromDatabase.equals(tokenVersion)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }

        List<String> rolesNamesFromToken = token.getClaim("roles").asList(String.class);

        List<SimpleGrantedAuthority> grantedAuthorities = rolesNamesFromToken.stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()))
                .toList();

        var auth = new UsernamePasswordAuthenticationToken(token.getSubject(), null, grantedAuthorities);

        request.setAttribute("employee_id", token.getSubject());

        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }
}
