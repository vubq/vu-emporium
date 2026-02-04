package com.ecommerce.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionValueDTO {
    private Long id;
    private String value; // Derived from translations
    private java.util.Map<String, java.util.Map<String, String>> translations;
}
