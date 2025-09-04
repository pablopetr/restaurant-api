package com.pablopetr.restaurant_api.modules.employees.repositories;

import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
    Optional<EmployeeEntity> findByEmail(String email);
}
