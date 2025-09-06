package com.pablopetr.restaurant_api.modules.employees.controllers;

import com.pablopetr.restaurant_api.modules.employees.dtos.AuthEmployeeRequestDTO;
import com.pablopetr.restaurant_api.modules.employees.dtos.AuthEmployeeResponseDTO;
import com.pablopetr.restaurant_api.modules.employees.dtos.CreateEmployeeDTO;
import com.pablopetr.restaurant_api.modules.employees.dtos.UpdateEmployeeDTO;
import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.enums.EmployeeRole;
import com.pablopetr.restaurant_api.modules.employees.useCases.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private CreateEmployeeUseCase createEmployeeUseCase;

    @Autowired
    private AuthEmployeeUseCase authEmployeeUseCase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GetEmployeeProfileUseCase getEmployeeProfileUseCase;

    @Autowired
    private UpdateEmployeeUseCase updateEmployeeUseCase;

    @Autowired
    private DeleteEmployeeUseCase deleteEmployeeUseCase;

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

    @GetMapping("/me")
    public ResponseEntity<Object> me(HttpServletRequest request) {
        String employeeId = (String) request.getAttribute("employee_id");

        if(employeeId == null) {
            return ResponseEntity.badRequest().body("Employee ID not found in request");
        }

        try {
            Optional<EmployeeEntity> profile = this.getEmployeeProfileUseCase.execute(employeeId);

            if(profile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
            }

            return ResponseEntity.ok().body(profile);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Object> update(
            @Valid @RequestBody UpdateEmployeeDTO updateEmployeeDTO,
            HttpServletRequest request
    ) {
        try {
            var updatedEmployee = this.updateEmployeeUseCase.execute(updateEmployeeDTO);

            return ResponseEntity.ok().body(updatedEmployee);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<Object> deleteMyAccount(HttpServletRequest request) {
        try {
            UUID employeeId = UUID.fromString((String) request.getAttribute("employee_id"));

            deleteEmployeeUseCase.execute(employeeId);

            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteEmployeeById(@PathVariable String employeeId) {
        try {
            UUID employeeUUID = UUID.fromString(employeeId);

            deleteEmployeeUseCase.execute(employeeUUID);

            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
