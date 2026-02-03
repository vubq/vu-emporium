package com.ecommerce.repository;

import com.ecommerce.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    List<Category> findByParentIsNullAndStatus(com.ecommerce.model.enums.CategoryStatus status);

    List<Category> findByParentIdAndStatus(Long parentId, com.ecommerce.model.enums.CategoryStatus status);

    List<Category> findByStatusOrderByDisplayOrderAsc(com.ecommerce.model.enums.CategoryStatus status);
}
