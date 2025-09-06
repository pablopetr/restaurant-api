package com.pablopetr.restaurant_api.modules.employees.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pablopetr.restaurant_api.modules.employees.dtos.AuthEmployeeRequestDTO;
import com.pablopetr.restaurant_api.modules.employees.dtos.AuthEmployeeResponseDTO;
import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthEmployeeUseCase {
    @Value("${security.token.secret.employee}")
    public String secretKey;

    @Autowired
    public EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthEmployeeResponseDTO execute(AuthEmployeeRequestDTO authEmployeeRequestDTO) {
        var employee = this.employeeRepository.findByEmail(authEmployeeRequestDTO.email())
                .orElseThrow(() -> new UsernameNotFoundException("Email or password incorrect!"));

        var passwordMatches = this.passwordEncoder
                .matches(authEmployeeRequestDTO.password(), employee.getPassword());

        if(!passwordMatches) {
            throw new RuntimeException("Email or password incorrect!");
        }

        Algorithm algorithm =  Algorithm.HMAC256(secretKey);

        var expiresIn = Instant.now().plus(Duration.ofHours(2));
        var jti = UUID.randomUUID().toString();
        String roleName = employee.getRole().name().toLowerCase();

        var token = JWT.create()
                .withIssuer("restaurant-api")
                .withSubject(employee.getId().toString())
                .withClaim("roles", List.of(roleName))
                .withClaim("token_version", employee.getTokenVersion())
                .withJWTId(jti)
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        return AuthEmployeeResponseDTO.builder()
                .access_token(token)
                .expires_in(expiresIn.toEpochMilli())
                .build();
    }
}
