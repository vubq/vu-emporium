package com.ecommerce.repository.spec;

import com.ecommerce.model.entity.Category;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.enums.ProductStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> getSpecifications(List<Long> categoryIds, String search, BigDecimal minPrice,
            BigDecimal maxPrice, String brand, ProductStatus status) {
        return (root, query, cb) -> { // Changed criteriaBuilder to cb

            List<Predicate> predicates = new java.util.ArrayList<>(); // Changed ArrayList instantiation

            // Recursive status check for ACTIVE products
            if (status == ProductStatus.ACTIVE) {
                // Join Category
                Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT); // Used imported Join and
                                                                                             // JoinType
                Join<Category, Category> parentJoin = categoryJoin.join("parent", JoinType.LEFT); // Used imported Join
                                                                                                  // and JoinType
                // Assuming max 3 levels (Grandparent -> Parent -> Child)
                Join<Category, Category> grandParentJoin = parentJoin.join("parent", JoinType.LEFT); // Used imported
                                                                                                     // Join and
                                                                                                     // JoinType

                Predicate categoryActive = cb.equal(categoryJoin.get("status"),
                        com.ecommerce.model.enums.CategoryStatus.ACTIVE);

                Predicate parentActiveOrNull = cb.or(
                        cb.isNull(parentJoin.get("id")), // Check if parentJoin itself is null by checking its ID
                        cb.equal(parentJoin.get("status"), com.ecommerce.model.enums.CategoryStatus.ACTIVE));

                Predicate grandParentActiveOrNull = cb.or(
                        cb.isNull(grandParentJoin.get("id")), // Check if grandParentJoin itself is null by checking its
                                                              // ID
                        cb.equal(grandParentJoin.get("status"), com.ecommerce.model.enums.CategoryStatus.ACTIVE));

                predicates.add(cb.and(categoryActive, parentActiveOrNull, grandParentActiveOrNull));
            } else if (status != null) {
                predicates.add(cb.equal(root.get("status"), status)); // Used cb
            }

            // Category Filter
            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categoryIds));
            }

            // Price Range Filter
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("basePrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("basePrice"), maxPrice));
            }

            // Brand Filter
            if (StringUtils.hasText(brand)) {
                predicates.add(cb.equal(cb.lower(root.get("brand")), brand.toLowerCase()));
            }

            // Search (Name or Description)
            if (StringUtils.hasText(search)) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), searchPattern);
                Predicate descPredicate = cb.like(cb.lower(root.get("description")),
                        searchPattern);
                predicates.add(cb.or(namePredicate, descPredicate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
