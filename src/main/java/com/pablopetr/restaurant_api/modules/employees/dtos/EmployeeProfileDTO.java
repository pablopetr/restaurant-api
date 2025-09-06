package com.pablopetr.restaurant_api.modules.employees.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
}
