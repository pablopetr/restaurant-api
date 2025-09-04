package com.pablopetr.restaurant_api.modules.employees.useCases;

import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateEmployeeUseCase {
    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeEntity execute(EmployeeEntity employeeEntity) {
        var existentEmployee = this.employeeRepository.findByEmail(employeeEntity.getEmail());

        if(existentEmployee.isPresent()) {
            throw new IllegalArgumentException("Employee with email already exists!");
        }

        return employeeRepository.save(employeeEntity);
    }
}
