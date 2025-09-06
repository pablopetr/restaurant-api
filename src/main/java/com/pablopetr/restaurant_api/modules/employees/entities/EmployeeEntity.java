package com.pablopetr.restaurant_api.modules.employees.entities;

import com.pablopetr.restaurant_api.modules.employees.enums.EmployeeRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="employees")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String phone;

    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    @Column(name = "token_version", nullable = false)
    private Integer tokenVersion = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
