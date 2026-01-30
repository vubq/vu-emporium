package com.ecommerce.model.dto.response;

import com.ecommerce.model.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private Integer stockQuantity;
    private String sku;
    private CategoryDTO category;
    private List<String> images;
    private ProductStatus status;
    private Boolean featured;
    private Integer viewCount;
    private Integer soldCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // New fields for variants
    private Boolean hasVariants;
    private List<ProductOptionDTO> options;
    private List<ProductVariantDTO> variants;
}
