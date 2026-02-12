package com.ecommerce.service.interfaces;

import com.ecommerce.model.dto.request.CategoryRequest;
import com.ecommerce.model.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest request);

    Category updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    org.springframework.data.domain.Page<Category> getAllCategories(org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<Category> getCategories(String search,
            com.ecommerce.model.enums.CategoryStatus status, org.springframework.data.domain.Pageable pageable);

    List<Category> getActiveCategories();
}
