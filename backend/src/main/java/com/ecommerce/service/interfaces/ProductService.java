package com.ecommerce.service.interfaces;

import com.ecommerce.model.dto.response.ProductDTO;
import com.ecommerce.model.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductDTO> getAllProducts(Long categoryId, String search, Pageable pageable);

    ProductDTO getProductById(Long id);

    ProductDTO getProductBySlug(String slug);

    List<ProductDTO> getFeaturedProducts();
}
