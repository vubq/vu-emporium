package com.ecommerce.util;

import com.ecommerce.model.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslationMapper {

    public static Map<String, Map<String, String>> toCategoryMap(List<CategoryTranslation> translations) {
        if (translations == null)
            return new HashMap<>();
        Map<String, Map<String, String>> map = new HashMap<>();
        for (CategoryTranslation t : translations) {
            Map<String, String> fields = new HashMap<>();
            fields.put("name", t.getName());
            fields.put("description", t.getDescription());
            map.put(t.getLanguageCode(), fields);
        }
        return map;
    }

    public static void mapCategoryTranslations(Category category, Map<String, Map<String, String>> translationMap) {
        if (translationMap == null)
            return;

        // 1. Update existing translations
        for (CategoryTranslation translation : category.getTranslations()) {
            if (translationMap.containsKey(translation.getLanguageCode())) {
                Map<String, String> fields = translationMap.get(translation.getLanguageCode());
                translation.setName(fields.get("name"));
                translation.setDescription(fields.get("description"));
            }
        }

        // 2. Add new translations
        for (Map.Entry<String, Map<String, String>> entry : translationMap.entrySet()) {
            String langCode = entry.getKey();
            boolean exists = category.getTranslations().stream()
                    .anyMatch(t -> t.getLanguageCode().equals(langCode));

            if (!exists) {
                Map<String, String> fields = entry.getValue();
                CategoryTranslation translation = CategoryTranslation.builder()
                        .category(category)
                        .languageCode(langCode)
                        .name(fields.get("name"))
                        .description(fields.get("description"))
                        .build();
                category.getTranslations().add(translation);
            }
        }

        // 3. Remove orphaned translations (not in the map)
        category.getTranslations().removeIf(t -> !translationMap.containsKey(t.getLanguageCode()));
    }

    public static Map<String, Map<String, String>> toProductMap(List<ProductTranslation> translations) {
        if (translations == null)
            return new HashMap<>();
        Map<String, Map<String, String>> map = new HashMap<>();
        for (ProductTranslation t : translations) {
            Map<String, String> fields = new HashMap<>();
            fields.put("name", t.getName());
            fields.put("description", t.getDescription());
            fields.put("metaTitle", t.getMetaTitle());
            fields.put("metaDescription", t.getMetaDescription());
            fields.put("metaKeywords", t.getMetaKeywords());
            map.put(t.getLanguageCode(), fields);
        }
        return map;
    }

    public static void mapProductTranslations(Product product, Map<String, Map<String, String>> translationMap) {
        if (translationMap == null)
            return;

        // 1. Update existing translations
        for (ProductTranslation translation : product.getTranslations()) {
            if (translationMap.containsKey(translation.getLanguageCode())) {
                Map<String, String> fields = translationMap.get(translation.getLanguageCode());
                translation.setName(fields.get("name"));
                translation.setDescription(fields.get("description"));
                translation.setMetaTitle(fields.get("metaTitle"));
                translation.setMetaDescription(fields.get("metaDescription"));
                translation.setMetaKeywords(fields.get("metaKeywords"));
            }
        }

        // 2. Add new translations
        for (Map.Entry<String, Map<String, String>> entry : translationMap.entrySet()) {
            String langCode = entry.getKey();
            boolean exists = product.getTranslations().stream()
                    .anyMatch(t -> t.getLanguageCode().equals(langCode));

            if (!exists) {
                Map<String, String> fields = entry.getValue();
                ProductTranslation translation = ProductTranslation.builder()
                        .product(product)
                        .languageCode(langCode)
                        .name(fields.get("name"))
                        .description(fields.get("description"))
                        .metaTitle(fields.get("metaTitle"))
                        .metaDescription(fields.get("metaDescription"))
                        .metaKeywords(fields.get("metaKeywords"))
                        .build();
                product.getTranslations().add(translation);
            }
        }

        // 3. Remove orphaned translations
        product.getTranslations().removeIf(t -> !translationMap.containsKey(t.getLanguageCode()));
    }

    public static Map<String, Map<String, String>> toProductOptionMap(List<ProductOptionTranslation> translations) {
        if (translations == null)
            return new HashMap<>();
        Map<String, Map<String, String>> map = new HashMap<>();
        for (ProductOptionTranslation t : translations) {
            Map<String, String> fields = new HashMap<>();
            fields.put("name", t.getName());
            map.put(t.getLanguageCode(), fields);
        }
        return map;
    }

    public static void mapProductOptionTranslations(ProductOption option,
            Map<String, Map<String, String>> translationMap) {
        if (translationMap == null)
            return;

        for (ProductOptionTranslation translation : option.getTranslations()) {
            if (translationMap.containsKey(translation.getLanguageCode())) {
                translation.setName(translationMap.get(translation.getLanguageCode()).get("name"));
            }
        }

        for (Map.Entry<String, Map<String, String>> entry : translationMap.entrySet()) {
            String langCode = entry.getKey();
            if (option.getTranslations().stream().noneMatch(t -> t.getLanguageCode().equals(langCode))) {
                option.getTranslations().add(ProductOptionTranslation.builder()
                        .productOption(option)
                        .languageCode(langCode)
                        .name(entry.getValue().get("name"))
                        .build());
            }
        }

        option.getTranslations().removeIf(t -> !translationMap.containsKey(t.getLanguageCode()));
    }

    public static Map<String, Map<String, String>> toProductOptionValueMap(
            List<ProductOptionValueTranslation> translations) {
        if (translations == null)
            return new HashMap<>();
        Map<String, Map<String, String>> map = new HashMap<>();
        for (ProductOptionValueTranslation t : translations) {
            Map<String, String> fields = new HashMap<>();
            fields.put("value", t.getValue());
            map.put(t.getLanguageCode(), fields);
        }
        return map;
    }

    public static void mapProductOptionValueTranslations(ProductOptionValue value,
            Map<String, Map<String, String>> translationMap) {
        if (translationMap == null)
            return;

        for (ProductOptionValueTranslation translation : value.getTranslations()) {
            if (translationMap.containsKey(translation.getLanguageCode())) {
                translation.setValue(translationMap.get(translation.getLanguageCode()).get("value"));
            }
        }

        for (Map.Entry<String, Map<String, String>> entry : translationMap.entrySet()) {
            String langCode = entry.getKey();
            if (value.getTranslations().stream().noneMatch(t -> t.getLanguageCode().equals(langCode))) {
                value.getTranslations().add(ProductOptionValueTranslation.builder()
                        .productOptionValue(value)
                        .languageCode(langCode)
                        .value(entry.getValue().get("value"))
                        .build());
            }
        }

        value.getTranslations().removeIf(t -> !translationMap.containsKey(t.getLanguageCode()));
    }
}
