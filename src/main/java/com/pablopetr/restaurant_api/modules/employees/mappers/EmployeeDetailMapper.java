package com.pablopetr.restaurant_api.modules.employees.mappers;

import com.pablopetr.restaurant_api.modules.employees.dtos.admin.EmployeeDTO;
import com.pablopetr.restaurant_api.modules.employees.entities.EmployeeEntity;

import java.time.format.DateTimeFormatter;

public class EmployeeDetailMapper {
    private EmployeeDetailMapper() {}

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE_TIME;

    public static EmployeeDTO toDetailDTO(EmployeeEntity e) {
        return EmployeeDTO.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .phone(e.getPhone())
                .role(e.getRole() != null ? e.getRole().name() : null)
                .createdAt(e.getCreatedAt() != null ? e.getCreatedAt().format(ISO) : null)
                .build();
    }
}
