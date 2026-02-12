package com.ecommerce.repository;

import com.ecommerce.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
    List<Language> findAllByIsActiveTrueOrderByDisplayOrderAsc();

    Optional<Language> findByIsDefaultTrue();

    org.springframework.data.domain.Page<Language> findByNameContainingIgnoreCase(String name,
            org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<Language> findByIsActiveTrue(
            org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<Language> findByNameContainingIgnoreCaseAndIsActiveTrue(String name,
            org.springframework.data.domain.Pageable pageable);
}
