package com.ecommerce.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {
    private Long id;
    private String sku;
    private BigDecimal price;
    private Integer stockQuantity;
    private List<String> images;
    private List<ProductOptionValueDTO> optionValues;
}
