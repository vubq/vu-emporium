package com.ecommerce.service.impl;

import com.ecommerce.model.entity.I18nTranslation;
import com.ecommerce.repository.I18nTranslationRepository;
import com.ecommerce.model.entity.Language;
import com.ecommerce.service.interfaces.I18nService;
import com.ecommerce.service.interfaces.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class I18nServiceImpl implements I18nService {

    private final I18nTranslationRepository translationRepository;
    private final LanguageService languageService;

    @Override
    public Map<String, String> getTranslationsForLanguage(String languageCode) {
        // Start with default language translations as fallback base
        Language defaultLang = languageService.getDefaultLanguage();
        Map<String, String> result = new HashMap<>();

        if (defaultLang != null && !defaultLang.getCode().equals(languageCode)) {
            // Load default language translations first as base
            for (I18nTranslation t : translationRepository.findAllByLanguageCode(defaultLang.getCode())) {
                result.put(t.getTranslationKey(), t.getTranslationValue());
            }
            // Override with requested language translations
            for (I18nTranslation t : translationRepository.findAllByLanguageCode(languageCode)) {
                result.put(t.getTranslationKey(), t.getTranslationValue());
            }
        } else {
            // Requested language IS the default, just return its translations
            for (I18nTranslation t : translationRepository.findAllByLanguageCode(languageCode)) {
                result.put(t.getTranslationKey(), t.getTranslationValue());
            }
        }

        return result;
    }

    @Override
    @Transactional
    public void updateTranslations(String languageCode, Map<String, String> translations) {
        translations.forEach((key, value) -> {
            I18nTranslation translation = translationRepository
                    .findByTranslationKeyAndLanguageCode(key, languageCode)
                    .orElse(I18nTranslation.builder()
                            .translationKey(key)
                            .languageCode(languageCode)
                            .build());
            translation.setTranslationValue(value);
            translationRepository.save(translation);
        });
    }

    @Override
    @Transactional
    public void addTranslationKey(String key, String groupName) {
        // Just a placeholder for now, usually we might want to insert empty values for
        // all languages
    }

    @Override
    @Transactional
    public void renameTranslationKey(String oldKey, String newKey) {
        List<I18nTranslation> translations = translationRepository.findAllByTranslationKey(oldKey);
        for (I18nTranslation t : translations) {
            t.setTranslationKey(newKey);
        }
        translationRepository.saveAll(translations);
    }

    @Override
    @Transactional
    public void deleteTranslationKey(String key) {
        translationRepository.deleteAllByTranslationKey(key);
    }

    @Override
    public List<String> getAllTranslationKeys() {
        return translationRepository.findAll().stream()
                .map(I18nTranslation::getTranslationKey)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<com.ecommerce.model.dto.response.TranslationMatrixDTO> getTranslationMatrix(
            org.springframework.data.domain.Pageable pageable, String search) {
        // 1. Get paginated keys
        org.springframework.data.domain.Page<String> keysPage = translationRepository.findDistinctKeys(search,
                pageable);
        List<String> keys = keysPage.getContent();

        if (keys.isEmpty()) {
            return org.springframework.data.domain.Page.empty(pageable);
        }

        // 2. Fetch all translations for these keys
        List<I18nTranslation> translations = translationRepository.findAllByTranslationKeyIn(keys);

        // 3. Group by key
        Map<String, Map<String, String>> matrixMap = new HashMap<>();
        for (I18nTranslation t : translations) {
            matrixMap.computeIfAbsent(t.getTranslationKey(), k -> new HashMap<>())
                    .put(t.getLanguageCode(), t.getTranslationValue());
        }

        // 4. Convert to DTOs (preserving order of keys from the page)
        List<com.ecommerce.model.dto.response.TranslationMatrixDTO> dtos = keys.stream()
                .map(key -> com.ecommerce.model.dto.response.TranslationMatrixDTO.builder()
                        .key(key)
                        .translations(matrixMap.getOrDefault(key, new HashMap<>()))
                        .build())
                .collect(Collectors.toList());

        return new org.springframework.data.domain.PageImpl<>(dtos, pageable, keysPage.getTotalElements());
    }

}
