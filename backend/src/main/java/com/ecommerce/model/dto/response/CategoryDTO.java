package com.ecommerce.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private com.ecommerce.model.enums.CategoryStatus status;
    private Integer displayOrder;
    private Long parentId;
    private java.util.List<CategoryDTO> children;
    private java.util.Map<String, java.util.Map<String, String>> translations;
}
