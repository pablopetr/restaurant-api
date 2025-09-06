package com.pablopetr.restaurant_api.modules.employees.useCases;

import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvalidateEmployeeTokenUseCase {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void execute(UUID employeeId) {
        var employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setTokenVersion(employee.getTokenVersion() + 1);

        employeeRepository.save(employee);
    }
}
