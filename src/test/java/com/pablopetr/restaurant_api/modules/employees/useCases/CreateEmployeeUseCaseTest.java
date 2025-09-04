package com.pablopetr.restaurant_api.modules.employees.useCases;

import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateEmployeeUseCaseTest {
    @InjectMocks
    private CreateEmployeeUseCase createEmployeeUseCase;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Should be able to create an employee")
    public void should_be_able_to_create_an_employee() {
        var employeeId = UUID.randomUUID();

        var employeeEntity = EmployeeEntity.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password")
                .phone("55999999999")
                .role("waiter")
                .build();

        when(this.employeeRepository.save(employeeEntity)).thenReturn(employeeEntity);

        var result = this.createEmployeeUseCase.execute(employeeEntity);

        assert(result.getId().equals(employeeId));
    }
}
