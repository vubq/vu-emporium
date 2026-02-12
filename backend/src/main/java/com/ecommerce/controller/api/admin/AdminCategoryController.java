package com.ecommerce.controller.api.admin;

import com.ecommerce.model.dto.request.CategoryRequest;
import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.model.dto.response.CategoryDTO;
import com.ecommerce.model.entity.Category;
import com.ecommerce.service.interfaces.CategoryService;
import com.ecommerce.util.TranslationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<org.springframework.data.domain.Page<CategoryDTO>>> getAllCategories(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) com.ecommerce.model.enums.CategoryStatus status,
            org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.domain.Page<Category> categories = categoryService.getCategories(search, status,
                pageable);
        org.springframework.data.domain.Page<CategoryDTO> categoryDTOs = categories
                .map(cat -> convertToDTO(cat, false));
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
        String viName = "";
        String viDesc = "";
        if (category.getTranslations() != null) {
            var viTrans = category.getTranslations().stream()
                    .filter(t -> "vi".equals(t.getLanguageCode()))
                    .findFirst();
            if (viTrans.isPresent()) {
                viName = viTrans.get().getName();
                viDesc = viTrans.get().getDescription();
            }
        }

        CategoryDTO dto = CategoryDTO.builder()
                .id(category.getId())
                .name(viName)
                .slug(category.getSlug())
                .description(viDesc)
                .imageUrl(category.getImageUrl())
                .status(category.getStatus())
                .displayOrder(category.getDisplayOrder())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .translations(TranslationMapper.toCategoryMap(category.getTranslations()))
                .build();

        if (includeChildren && category.getChildren() != null) {
            dto.setChildren(category.getChildren().stream()
                    .map(child -> convertToDTO(child, true))
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
