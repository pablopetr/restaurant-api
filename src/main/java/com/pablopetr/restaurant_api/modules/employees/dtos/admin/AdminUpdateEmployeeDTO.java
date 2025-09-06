package com.pablopetr.restaurant_api.modules.employees.dtos.admin;

import com.pablopetr.restaurant_api.modules.employees.enums.EmployeeRole;
import com.pablopetr.restaurant_api.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AdminUpdateEmployeeDTO(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String phone,

    @NotBlank
    @ValueOfEnum(enumClass = EmployeeRole.class, message = "Role must be WAITER, MANAGEMENT or ADMIN")
    String role
) {}
