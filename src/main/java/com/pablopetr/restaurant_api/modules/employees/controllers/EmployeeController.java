package com.pablopetr.restaurant_api.modules.employees.controllers;

import com.pablopetr.restaurant_api.modules.employees.dtos.AuthEmployeeRequestDTO;
import com.pablopetr.restaurant_api.modules.employees.dtos.AuthEmployeeResponseDTO;
import com.pablopetr.restaurant_api.modules.employees.dtos.CreateEmployeeDTO;
import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.enums.EmployeeRole;
import com.pablopetr.restaurant_api.modules.employees.useCases.AuthEmployeeUseCase;
import com.pablopetr.restaurant_api.modules.employees.useCases.CreateEmployeeUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private CreateEmployeeUseCase createEmployeeUseCase;

    @Autowired
    private AuthEmployeeUseCase authEmployeeUseCase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateEmployeeDTO createEmployeeDTO) {
        try {
            var role = EmployeeRole.valueOf(createEmployeeDTO.role().trim().toUpperCase());

            var password = passwordEncoder.encode(createEmployeeDTO.password());

            var employeeEntity = EmployeeEntity.builder()
                    .firstName(createEmployeeDTO.firstName())
                    .lastName(createEmployeeDTO.lastName())
                    .email(createEmployeeDTO.email())
                    .password(password)
                    .phone(createEmployeeDTO.phone())
                    .role(role)
                    .build();

            var result = this.createEmployeeUseCase.execute(employeeEntity);

            return ResponseEntity.ok().body(result);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@Valid @RequestBody AuthEmployeeRequestDTO authEmployeeRequestDTO) {
        try {
            var tokenResult = this.authEmployeeUseCase.execute(authEmployeeRequestDTO);

            System.out.println(tokenResult);

            var responseDTO = AuthEmployeeResponseDTO.builder()
                    .access_token(tokenResult.getAccess_token())
                    .expires_in(tokenResult.getExpires_in())
                    .build();

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
        }
    }
}
