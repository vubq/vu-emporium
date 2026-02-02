package com.ecommerce.service.interfaces;

import com.ecommerce.model.dto.response.ProductDTO;
import com.ecommerce.model.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import java.math.BigDecimal;

public interface ProductService {
    Page<ProductDTO> getAllProducts(Long categoryId, String search, Pageable pageable);

    Page<ProductDTO> getAllProducts(Long categoryId, String search, BigDecimal minPrice, BigDecimal maxPrice,
            String brand, ProductStatus status, Pageable pageable);

    ProductDTO getProductById(Long id);

    ProductDTO getProductBySlug(String slug);

    List<ProductDTO> getFeaturedProducts();

    ProductDTO createProduct(com.ecommerce.model.dto.request.ProductRequest request);

    ProductDTO updateProduct(Long id, com.ecommerce.model.dto.request.ProductRequest request);

    void deleteProduct(Long id);
}
