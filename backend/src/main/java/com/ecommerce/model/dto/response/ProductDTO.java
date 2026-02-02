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
    private Integer stockQuantity;
    private String sku;
    private CategoryDTO category;
    private List<String> images;
    private ProductStatus status;
    private Boolean featured;
    private Integer viewCount;
    private Integer soldCount;

    // New Fields
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
    private Double averageRating;
    private Integer reviewCount;
    private Integer totalSales;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // New fields for variants
    private Boolean hasVariants;
    private List<ProductOptionDTO> options;
    private List<ProductVariantDTO> variants;
}
