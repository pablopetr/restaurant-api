package com.pablopetr.restaurant_api.modules.employees.useCases.admin;

import com.pablopetr.restaurant_api.modules.employees.dtos.admin.EmployeeDTO;
import com.pablopetr.restaurant_api.modules.employees.mappers.EmployeeDetailMapper;
import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListEmployeesUseCase {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Page<EmployeeDTO> execute(Pageable pageable) {
        return this.employeeRepository.findAll(pageable)
                .map(EmployeeDetailMapper::toDetailDTO);
    }
}
