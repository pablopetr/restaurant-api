package com.pablopetr.restaurant_api.modules.employees.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthEmployeeResponseDTO {
    private String access_token;
    private Long expires_in;
}
