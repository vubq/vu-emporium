package com.ecommerce.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private String imageUrl;

    private Long parentId;

    private Integer displayOrder;

    private com.ecommerce.model.enums.CategoryStatus status;

    private String slug;

    private java.util.Map<String, java.util.Map<String, String>> translations;
}
