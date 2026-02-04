package com.ecommerce.repository;

import com.ecommerce.model.entity.Product;
import com.ecommerce.model.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

        Optional<Product> findBySlug(String slug);

        Page<Product> findByStatus(ProductStatus status, Pageable pageable);

        Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

        Page<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable);

        List<Product> findByFeaturedTrueAndStatus(ProductStatus status);

        @Query("SELECT DISTINCT p FROM Product p JOIN p.translations t WHERE " +
                        "(LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "p.status = :status")
        Page<Product> searchProducts(@Param("keyword") String keyword,
                        @Param("status") ProductStatus status,
                        Pageable pageable);
}
