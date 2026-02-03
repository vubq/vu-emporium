package com.ecommerce.service.impl;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.dto.request.CategoryRequest;
import com.ecommerce.model.entity.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Override
    @Transactional
    public Category createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        // Default to DRAFT if not provided
        category.setStatus(
                request.getStatus() != null ? request.getStatus() : com.ecommerce.model.enums.CategoryStatus.DRAFT);

        category.setSlug(generateSlug(request.getName()));

        if (request.getParentId() != null) {
            Category parent = getCategoryById(request.getParentId());
            category.setParent(parent);
        }

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, CategoryRequest request) {
        Category category = getCategoryById(id);

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setDisplayOrder(request.getDisplayOrder());

        if (request.getStatus() != null) {
            // Validate functionality: If already ACTIVE or ARCHIVED, cannot switch to DRAFT
            if (request.getStatus() == com.ecommerce.model.enums.CategoryStatus.DRAFT) {
                if (category.getStatus() == com.ecommerce.model.enums.CategoryStatus.ACTIVE ||
                        category.getStatus() == com.ecommerce.model.enums.CategoryStatus.ARCHIVED) {
                    throw new com.ecommerce.exception.BadRequestException(
                            "Cannot change status from " + category.getStatus() + " to DRAFT");
                }
            }
            category.setStatus(request.getStatus());
        }

        // Update slug if name changes
        if (!category.getName().equals(request.getName())) {
            category.setSlug(generateSlug(request.getName()));
        }

        if (request.getParentId() != null) {
            Category parent = getCategoryById(request.getParentId());
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);

        // Only allow deleting DRAFT categories (or ARCHIVED if policy allows)
        if (category.getStatus() == com.ecommerce.model.enums.CategoryStatus.ACTIVE) {
            throw new com.ecommerce.exception.BadRequestException(
                    "Cannot delete ACTIVE category. Please archive it first or change status to DRAFT.");
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getActiveCategories() {
        return categoryRepository.findByStatusOrderByDisplayOrderAsc(com.ecommerce.model.enums.CategoryStatus.ACTIVE);
    }

    private String generateSlug(String input) {
        if (input == null)
            return "";
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
