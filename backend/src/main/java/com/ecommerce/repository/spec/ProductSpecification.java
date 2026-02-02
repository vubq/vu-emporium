package com.ecommerce.repository.spec;

import com.ecommerce.model.entity.Product;
import com.ecommerce.model.enums.ProductStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> getSpecifications(List<Long> categoryIds, String search, BigDecimal minPrice,
            BigDecimal maxPrice, String brand, ProductStatus status) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Status Filter
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // Category Filter
            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categoryIds));
            }

            // Price Range Filter
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("basePrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("basePrice"), maxPrice));
            }

            // Brand Filter
            if (StringUtils.hasText(brand)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.toLowerCase()));
            }

            // Search (Name or Description)
            if (StringUtils.hasText(search)) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                        searchPattern);
                predicates.add(criteriaBuilder.or(namePredicate, descPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
