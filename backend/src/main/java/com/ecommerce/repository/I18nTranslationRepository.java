package com.ecommerce.repository;

import com.ecommerce.model.entity.I18nTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface I18nTranslationRepository extends JpaRepository<I18nTranslation, Long> {
    List<I18nTranslation> findAllByLanguageCode(String languageCode);

    Optional<I18nTranslation> findByTranslationKeyAndLanguageCode(String translationKey, String languageCode);

    // For bulk matrix view
    List<I18nTranslation> findAllByTranslationKeyIn(List<String> keys);

    List<I18nTranslation> findAllByTranslationKey(String translationKey);

    @org.springframework.data.jpa.repository.Query(value = "SELECT DISTINCT translation_key FROM i18n_translations WHERE (?1 IS NULL OR translation_key ILIKE CONCAT('%', ?1, '%'))", countQuery = "SELECT count(DISTINCT translation_key) FROM i18n_translations WHERE (?1 IS NULL OR translation_key ILIKE CONCAT('%', ?1, '%'))", nativeQuery = true)
    org.springframework.data.domain.Page<String> findDistinctKeys(
            String search,
            org.springframework.data.domain.Pageable pageable);

    void deleteAllByTranslationKey(String translationKey);
}
