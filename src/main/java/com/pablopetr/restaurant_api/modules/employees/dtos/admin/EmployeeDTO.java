package com.pablopetr.restaurant_api.modules.employees.dtos.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private String createdAt;
}
