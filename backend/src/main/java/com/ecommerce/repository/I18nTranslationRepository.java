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

    void deleteAllByTranslationKey(String translationKey);
}
