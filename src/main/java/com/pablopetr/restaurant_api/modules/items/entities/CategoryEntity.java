package com.pablopetr.restaurant_api.modules.items.entities;

import com.pablopetr.restaurant_api.modules.items.enums.CategoryType;
import com.pablopetr.restaurant_api.validation.ValueOfEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Entity(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String slug;
    private String description;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
