package com.pablopetr.restaurant_api.modules.employees.useCases;

import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateEmployeeUseCase {
    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeEntity execute(EmployeeEntity employeeEntity) {
        return employeeRepository.save(employeeEntity);
    }
}
