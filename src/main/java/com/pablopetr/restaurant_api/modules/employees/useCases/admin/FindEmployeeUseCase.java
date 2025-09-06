package com.pablopetr.restaurant_api.modules.employees.useCases.admin;

import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FindEmployeeUseCase {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Optional<EmployeeEntity> execute(UUID employeeId) {
        return this.employeeRepository.findById(employeeId);
    }
}
