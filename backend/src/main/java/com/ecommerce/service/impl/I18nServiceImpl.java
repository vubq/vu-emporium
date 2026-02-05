package com.ecommerce.service.impl;

import com.ecommerce.model.entity.I18nTranslation;
import com.ecommerce.repository.I18nTranslationRepository;
import com.ecommerce.service.interfaces.I18nService;
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

    @Override
    public Map<String, String> getTranslationsForLanguage(String languageCode) {
        return translationRepository.findAllByLanguageCode(languageCode).stream()
                .collect(Collectors.toMap(
                        I18nTranslation::getTranslationKey,
                        I18nTranslation::getTranslationValue,
                        (existing, replacement) -> existing));
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
    public Map<String, Map<String, String>> getTranslationMatrix() {
        List<I18nTranslation> all = translationRepository.findAll();
        Map<String, Map<String, String>> matrix = new HashMap<>();

        for (I18nTranslation t : all) {
            matrix.computeIfAbsent(t.getTranslationKey(), k -> new HashMap<>())
                    .put(t.getLanguageCode(), t.getTranslationValue());
        }

        return matrix;
    }
}
