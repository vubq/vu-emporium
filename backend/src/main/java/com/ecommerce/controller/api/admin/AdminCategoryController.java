package com.ecommerce.controller.api.admin;

import com.ecommerce.model.dto.request.CategoryRequest;
import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.model.dto.response.CategoryDTO;
import com.ecommerce.model.entity.Category;
import com.ecommerce.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(cat -> convertToDTO(cat, false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(categoryDTOs));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(convertToDTO(category, true)));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long id,
            @RequestBody CategoryRequest request) {
        Category category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(convertToDTO(category, true)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private CategoryDTO convertToDTO(Category category, boolean includeChildren) {
        CategoryDTO dto = CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .active(category.getActive())
                .displayOrder(category.getDisplayOrder())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();

        if (includeChildren && category.getChildren() != null) {
            dto.setChildren(category.getChildren().stream()
                    .map(child -> convertToDTO(child, true))
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
