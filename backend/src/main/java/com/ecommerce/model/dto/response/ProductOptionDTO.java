package com.ecommerce.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDTO {
    private Long id;
    private String name; // Derived from translations
    private List<ProductOptionValueDTO> values;
    private java.util.Map<String, java.util.Map<String, String>> translations;
}
