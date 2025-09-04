package com.pablopetr.restaurant_api.modules.employees.useCases;

import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import com.pablopetr.restaurant_api.modules.employees.repositories.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    @DisplayName("Should not be able to create an employee when the same email already exists")
    public void should_not_be_able_to_create_an_employee_when_the_same_email_already_exists() {
        var employeeId = UUID.randomUUID();
        var newEmployeeId = UUID.randomUUID();

        var employeeEntity = EmployeeEntity.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password")
                .phone("55999999999")
                .role("waiter")
                .build();

        when(employeeRepository.findByEmail("johndoe@example.com"))
                .thenReturn(Optional.of(employeeEntity));

        var newEmployeeEntity = EmployeeEntity.builder()
                .id(newEmployeeId)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password")
                .phone("55999999999")
                .role("waiter")
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> createEmployeeUseCase.execute(newEmployeeEntity)
        );
    }
}
