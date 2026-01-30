package com.ecommerce.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class ProductVariantRequest {

    private String sku;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @Min(value = 0, message = "Stock quantity must be non-negative")
    private Integer stockQuantity;

    private List<String> images;

    private List<String> optionValues;
}
