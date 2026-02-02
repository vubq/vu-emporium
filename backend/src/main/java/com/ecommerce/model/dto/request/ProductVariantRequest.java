package com.ecommerce.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
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

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", message = "Base price must be positive")
    private BigDecimal basePrice;

    @DecimalMin(value = "0.0", message = "Sale price must be positive")
    private BigDecimal salePrice;

    @DecimalMin(value = "0.0", message = "Cost price must be positive")
    private BigDecimal costPrice;

    @Min(value = 0, message = "Stock quantity must be non-negative")
    private Integer stockQuantity;

    private List<String> images;

    private List<String> optionValues;
}
