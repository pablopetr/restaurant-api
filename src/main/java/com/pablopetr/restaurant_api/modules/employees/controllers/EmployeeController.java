package com.pablopetr.restaurant_api.modules.employees.controllers;

import com.pablopetr.restaurant_api.modules.employees.dtos.CreateEmployeeDTO;
import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.enums.EmployeeRole;
import com.pablopetr.restaurant_api.modules.employees.useCases.CreateEmployeeUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private CreateEmployeeUseCase createEmployeeUseCase;

    @PostMapping("")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateEmployeeDTO createEmployeeDTO) {
        try {
            var role = EmployeeRole.valueOf(createEmployeeDTO.role().trim().toUpperCase());

            var employeeEntity = EmployeeEntity.builder()
                    .firstName(createEmployeeDTO.firstName())
                    .lastName(createEmployeeDTO.lastName())
                    .email(createEmployeeDTO.email())
                    .password(createEmployeeDTO.password())
                    .phone(createEmployeeDTO.phone())
                    .role(role)
                    .build();

            var result = this.createEmployeeUseCase.execute(employeeEntity);

            return ResponseEntity.ok().body(result);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
