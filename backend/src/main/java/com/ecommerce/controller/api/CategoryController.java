package com.ecommerce.controller.api;

import com.ecommerce.model.dto.response.CategoryDTO;
import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.model.entity.Category;
import com.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<Category> categories = categoryRepository.findByActiveTrueOrderByDisplayOrderAsc();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(categoryDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new com.ecommerce.exception.ResourceNotFoundException("Category", "id", id));
        return ResponseEntity.ok(ApiResponse.success(convertToDTO(category)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryBySlug(@PathVariable String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new com.ecommerce.exception.ResourceNotFoundException("Category", "slug", slug));
        return ResponseEntity.ok(ApiResponse.success(convertToDTO(category)));
    }

    @GetMapping("/root")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getRootCategories() {
        List<Category> categories = categoryRepository.findByParentIsNullAndActiveTrue();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(categoryDTOs));
    }

    private CategoryDTO convertToDTO(Category category) {
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
