package com.pablopetr.restaurant_api.modules.employees.dtos;

import com.pablopetr.restaurant_api.modules.employees.enums.EmployeeRole;
import com.pablopetr.restaurant_api.modules.employees.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEmployeeDTO (
    @NotBlank  String firstName,
    @NotBlank  String lastName,
    @NotBlank  String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank  String phone,

    @NotBlank
    @ValueOfEnum(enumClass = EmployeeRole.class, message = "Role must be WAITER, MANAGEMENT or ADMIN")
    String role
) {}
