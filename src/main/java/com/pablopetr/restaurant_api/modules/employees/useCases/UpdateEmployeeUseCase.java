package com.pablopetr.restaurant_api.modules.employees.useCases;

import com.pablopetr.restaurant_api.modules.employees.dtos.UpdateEmployeeDTO;
import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.enums.EmployeeRole;
import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateEmployeeUseCase {
    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeEntity execute(UpdateEmployeeDTO updateEmployeeDTO) {
        var employee = this.employeeRepository.findById(UUID.fromString(updateEmployeeDTO.employeeId()))
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        var existentEmployeeByEmail = this.employeeRepository.findByEmail(updateEmployeeDTO.email());

        if(existentEmployeeByEmail.isPresent() && !existentEmployeeByEmail.get().getId().equals(employee.getId())) {
            throw new IllegalArgumentException("Employee with email already exists!");
        }

        employee.setId(UUID.fromString(updateEmployeeDTO.employeeId()));
        employee.setFirstName(updateEmployeeDTO.firstName());
        employee.setLastName(updateEmployeeDTO.lastName());
        employee.setEmail(updateEmployeeDTO.email());
        employee.setPhone(updateEmployeeDTO.phone());
        employee.setRole(EmployeeRole.valueOf(updateEmployeeDTO.role()));

        return this.employeeRepository.save(employee);
    }
}
