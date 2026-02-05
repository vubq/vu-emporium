package com.ecommerce.service.interfaces;

import java.util.List;
import java.util.Map;

public interface I18nService {
    Map<String, String> getTranslationsForLanguage(String languageCode);

    void updateTranslations(String languageCode, Map<String, String> translations);

    void addTranslationKey(String key, String groupName);

    void renameTranslationKey(String oldKey, String newKey);

    void deleteTranslationKey(String key);

    List<String> getAllTranslationKeys();

    Map<String, Map<String, String>> getTranslationMatrix(); // key -> (lang -> value)
}
