package com.ecommerce.controller.api;

import com.ecommerce.model.dto.response.CategoryDTO;
import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.model.entity.Category;
import com.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

        private final CategoryRepository categoryRepository;

        @GetMapping
        @Transactional(readOnly = true)
        public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
                List<Category> categories = categoryRepository
                                .findByStatusOrderByDisplayOrderAsc(com.ecommerce.model.enums.CategoryStatus.ACTIVE);
                List<CategoryDTO> categoryDTOs = categories.stream()
                                .map(cat -> convertToDTO(cat, false))
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success(categoryDTOs));
        }

        @GetMapping("/{id}")
        @Transactional(readOnly = true)
        public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id) {
                Category category = categoryRepository.findById(id)
                                .orElseThrow(() -> new com.ecommerce.exception.ResourceNotFoundException("Category",
                                                "id", id));
                return ResponseEntity.ok(ApiResponse.success(convertToDTO(category, true)));
        }

        @GetMapping("/slug/{slug}")
        @Transactional(readOnly = true)
        public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryBySlug(@PathVariable String slug) {
                Category category = categoryRepository.findBySlug(slug)
                                .orElseThrow(() -> new com.ecommerce.exception.ResourceNotFoundException("Category",
                                                "slug", slug));
                return ResponseEntity.ok(ApiResponse.success(convertToDTO(category, true)));
        }

        @GetMapping("/root")
        @Transactional(readOnly = true)
        public ResponseEntity<ApiResponse<List<CategoryDTO>>> getRootCategories() {
                List<Category> categories = categoryRepository
                                .findByParentIsNullAndStatus(com.ecommerce.model.enums.CategoryStatus.ACTIVE);
                List<CategoryDTO> categoryDTOs = categories.stream()
                                .map(cat -> convertToDTO(cat, true))
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success(categoryDTOs));
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
                                .translations(com.ecommerce.util.TranslationMapper
                                                .toCategoryMap(category.getTranslations()))
                                .build();

                if (includeChildren && category.getChildren() != null) {
                        dto.setChildren(category.getChildren().stream()
                                        .filter(c -> c.getStatus() == com.ecommerce.model.enums.CategoryStatus.ACTIVE)
                                        .map(child -> convertToDTO(child, true))
                                        .collect(Collectors.toList()));
                }

                return dto;
        }
}
