package com.ecommerce.service.impl;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.dto.response.CategoryDTO;
import com.ecommerce.model.dto.response.ProductDTO;
import com.ecommerce.model.entity.Category;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.enums.ProductStatus;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import com.ecommerce.repository.spec.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final com.ecommerce.repository.CategoryRepository categoryRepository;

    @Override
    public Page<ProductDTO> getAllProducts(Long categoryId, String search, Pageable pageable) {
        // Delegate to the more specific method with default behavior (Active status)
        return getAllProducts(categoryId, search, null, null, ProductStatus.ACTIVE, pageable);
    }

    @Override
    public Page<ProductDTO> getAllProducts(Long categoryId, String search, BigDecimal minPrice, BigDecimal maxPrice,
            ProductStatus status, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.getSpecifications(categoryId, search, minPrice, maxPrice,
                status);
        Page<Product> products = productRepository.findAll(spec, pageable);
        return products.map(this::convertToDTO);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return convertToDTO(product);
    }

    @Override
    public ProductDTO getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
        return convertToDTO(product);
    }

    @Override
    public List<ProductDTO> getFeaturedProducts() {
        List<Product> products = productRepository.findByFeaturedTrueAndStatus(ProductStatus.ACTIVE);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDTO createProduct(com.ecommerce.model.dto.request.ProductRequest request) {
        Product product = new Product();
        updateProductFromRequest(product, request);

        // Handle Options and Variants
        handleProductOptionsAndVariants(product, request);

        product = productRepository.save(product);
        return convertToDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, com.ecommerce.model.dto.request.ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        updateProductFromRequest(product, request);

        // Smart merge of Options and Variants logic
        handleProductOptionsAndVariants(product, request);

        product = productRepository.save(product);
        return convertToDTO(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }

    private void updateProductFromRequest(Product product, com.ecommerce.model.dto.request.ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCompareAtPrice(request.getCompareAtPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setSku(request.getSku());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getImages() != null) {
            product.setImages(new java.util.ArrayList<>(request.getImages()));
        }
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }
        if (request.getFeatured() != null) {
            product.setFeatured(request.getFeatured());
        }

        // Slug generation (simplistic)
        if (product.getSlug() == null || !product.getName().equals(request.getName())) {
            String baseSlug = request.getName().toLowerCase().replaceAll("[^a-z0-9]", "-").replaceAll("-+", "-");
            // Add randomness or check unique in real app
            product.setSlug(baseSlug + "-" + System.currentTimeMillis());
        }
    }

    private void handleProductOptionsAndVariants(Product product,
            com.ecommerce.model.dto.request.ProductRequest request) {
        if (request.getOptions() == null || request.getOptions().isEmpty()) {
            product.setHasVariants(false);
            // If clearing options, archive all variants
            product.getVariants().forEach(v -> {
                v.setStatus(ProductStatus.ARCHIVED);
                v.setStockQuantity(0);
            });
            // We can choose to clear options or keep them orphaned. Clearing is simpler for
            // structure.
            product.getOptions().clear();
            return;
        }

        product.setHasVariants(true);

        // 1. Merge Options - Mark all as ARCHIVED first, then reactivate
        product.getOptions().forEach(o -> {
            o.setStatus(ProductStatus.ARCHIVED);
            o.getValues().forEach(v -> v.setStatus(ProductStatus.ARCHIVED));
        });

        for (com.ecommerce.model.dto.request.ProductOptionRequest optionReq : request.getOptions()) {
            // Find existing option by name
            com.ecommerce.model.entity.ProductOption option = product.getOptions().stream()
                    .filter(o -> o.getName().equalsIgnoreCase(optionReq.getName()))
                    .findFirst()
                    .orElseGet(() -> {
                        com.ecommerce.model.entity.ProductOption newOpt = new com.ecommerce.model.entity.ProductOption();
                        newOpt.setName(optionReq.getName());
                        newOpt.setProduct(product);
                        product.getOptions().add(newOpt);
                        return newOpt;
                    });

            // Reactivate option
            option.setStatus(ProductStatus.ACTIVE);

            // Merge Values for this option
            if (optionReq.getValues() != null) {
                for (String valStr : optionReq.getValues()) {
                    com.ecommerce.model.entity.ProductOptionValue val = option.getValues().stream()
                            .filter(v -> v.getValue().equalsIgnoreCase(valStr))
                            .findFirst()
                            .orElseGet(() -> {
                                com.ecommerce.model.entity.ProductOptionValue newVal = new com.ecommerce.model.entity.ProductOptionValue();
                                newVal.setValue(valStr);
                                newVal.setProductOption(option);
                                option.getValues().add(newVal);
                                return newVal;
                            });

                    // Reactivate value
                    val.setStatus(ProductStatus.ACTIVE);
                }
            }
        }

        // 2. Merge Variants (Match by SKU, Soft Delete others)
        // First, mark all as ARCHIVED
        product.getVariants().forEach(v -> v.setStatus(ProductStatus.ARCHIVED));

        if (request.getVariants() != null) {
            for (com.ecommerce.model.dto.request.ProductVariantRequest variantReq : request.getVariants()) {
                // Find existing variant by SKU
                java.util.Optional<com.ecommerce.model.entity.ProductVariant> existingVariant = product.getVariants()
                        .stream()
                        .filter(v -> v.getSku() != null && v.getSku().equalsIgnoreCase(variantReq.getSku()))
                        .findFirst();

                com.ecommerce.model.entity.ProductVariant variant;
                if (existingVariant.isPresent()) {
                    variant = existingVariant.get();
                } else {
                    variant = new com.ecommerce.model.entity.ProductVariant();
                    variant.setProduct(product);
                    product.getVariants().add(variant);
                }

                // Update fields
                variant.setSku(variantReq.getSku());
                variant.setPrice(variantReq.getPrice());
                variant.setStockQuantity(variantReq.getStockQuantity());
                if (variantReq.getImages() != null) {
                    variant.setImages(new java.util.ArrayList<>(variantReq.getImages()));
                }
                variant.setStatus(ProductStatus.ACTIVE);

                // Update Option Values
                variant.getOptionValues().clear();
                if (variantReq.getOptionValues() != null) {
                    for (String valStr : variantReq.getOptionValues()) {
                        findOptionValue(product.getOptions(), valStr)
                                .ifPresent(ov -> variant.getOptionValues().add(ov));
                    }
                }
            }
        }
    }

    private java.util.Optional<com.ecommerce.model.entity.ProductOptionValue> findOptionValue(
            List<com.ecommerce.model.entity.ProductOption> options, String value) {
        for (com.ecommerce.model.entity.ProductOption option : options) {
            for (com.ecommerce.model.entity.ProductOptionValue ov : option.getValues()) {
                if (ov.getValue().equals(value)) {
                    return java.util.Optional.of(ov);
                }
            }
        }
        return java.util.Optional.empty();
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO.ProductDTOBuilder builder = ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .price(product.getPrice())
                .compareAtPrice(product.getCompareAtPrice())
                .stockQuantity(product.getStockQuantity())
                .sku(product.getSku())
                .category(convertCategoryToDTO(product.getCategory()))
                .images(product.getImages() != null ? new java.util.ArrayList<>(product.getImages())
                        : new java.util.ArrayList<>())
                .status(product.getStatus())
                .featured(product.getFeatured())
                .viewCount(product.getViewCount())
                .soldCount(product.getSoldCount())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .hasVariants(product.getHasVariants());

        if (Boolean.TRUE.equals(product.getHasVariants())) {
            // Filter only ACTIVE options and variants
            builder.options(product.getOptions().stream()
                    .filter(o -> o.getStatus() == ProductStatus.ACTIVE)
                    .map(this::convertOptionToDTO)
                    .collect(Collectors.toList()));
            builder.variants(product.getVariants().stream()
                    .filter(v -> v.getStatus() == ProductStatus.ACTIVE)
                    .map(this::convertVariantToDTO)
                    .collect(Collectors.toList()));
        }

        return builder.build();
    }

    private com.ecommerce.model.dto.response.ProductOptionDTO convertOptionToDTO(
            com.ecommerce.model.entity.ProductOption option) {
        return com.ecommerce.model.dto.response.ProductOptionDTO.builder()
                .id(option.getId())
                .name(option.getName())
                // Filter only ACTIVE values
                .values(option.getValues().stream()
                        .filter(v -> v.getStatus() == ProductStatus.ACTIVE)
                        .map(v -> com.ecommerce.model.dto.response.ProductOptionValueDTO.builder()
                                .id(v.getId())
                                .value(v.getValue())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private com.ecommerce.model.dto.response.ProductVariantDTO convertVariantToDTO(
            com.ecommerce.model.entity.ProductVariant variant) {
        return com.ecommerce.model.dto.response.ProductVariantDTO.builder()
                .id(variant.getId())
                .sku(variant.getSku())
                .price(variant.getPrice())
                .stockQuantity(variant.getStockQuantity())
                .images(variant.getImages() != null ? new java.util.ArrayList<>(variant.getImages())
                        : new java.util.ArrayList<>())
                .optionValues(variant.getOptionValues().stream()
                        .map(v -> com.ecommerce.model.dto.response.ProductOptionValueDTO.builder()
                                .id(v.getId())
                                .value(v.getValue())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private CategoryDTO convertCategoryToDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .active(category.getActive())
                .displayOrder(category.getDisplayOrder())
                .build();
    }
}
