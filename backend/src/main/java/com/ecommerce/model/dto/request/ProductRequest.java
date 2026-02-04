package com.ecommerce.model.dto.request;

import com.ecommerce.model.enums.ProductStatus;
import jakarta.validation.constraints.*;
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
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String name;

    private String slug;

    private String description;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be non-negative")
    private Integer stockQuantity;

    @Size(max = 100, message = "SKU must not exceed 100 characters")
    private String sku;

    private Long categoryId;

    private List<String> images;

    private ProductStatus status;

    private Boolean featured;

    // New Fields
    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private BigDecimal basePrice;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private String brand;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;

    private List<ProductOptionRequest> options;

    private List<ProductVariantRequest> variants;

    private java.util.Map<String, java.util.Map<String, String>> translations;
}
