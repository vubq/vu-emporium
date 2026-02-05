package com.ecommerce.controller.api.admin;

import com.ecommerce.model.entity.Language;
import com.ecommerce.service.interfaces.I18nService;
import com.ecommerce.service.interfaces.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/i18n")
@RequiredArgsConstructor
public class AdminI18nController {

    private final LanguageService languageService;
    private final I18nService i18nService;

    // Language Management
    @GetMapping("/languages")
    public ResponseEntity<List<Language>> getAllLanguages() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }

    @PostMapping("/languages")
    public ResponseEntity<Language> createLanguage(@RequestBody Language language) {
        return ResponseEntity.ok(languageService.createLanguage(language));
    }

    @PutMapping("/languages/{code}")
    public ResponseEntity<Language> updateLanguage(@PathVariable String code, @RequestBody Language language) {
        return ResponseEntity.ok(languageService.updateLanguage(code, language));
    }

    @DeleteMapping("/languages/{code}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable String code) {
        languageService.deleteLanguage(code);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/languages/{code}/default")
    public ResponseEntity<Void> setDefault(@PathVariable String code) {
        languageService.setDefaultLanguage(code);
        return ResponseEntity.ok().build();
    }

    // Translation Management
    @GetMapping("/matrix")
    public ResponseEntity<Map<String, Map<String, String>>> getTranslationMatrix() {
        return ResponseEntity.ok(i18nService.getTranslationMatrix());
    }

    @PostMapping("/translations/{lang}")
    public ResponseEntity<Void> updateTranslations(@PathVariable String lang,
            @RequestBody Map<String, String> translations) {
        i18nService.updateTranslations(lang, translations);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/keys")
    public ResponseEntity<Void> addKey(@RequestParam String key) {
        i18nService.addTranslationKey(key, "default");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/keys")
    public ResponseEntity<Void> renameKey(@RequestParam String oldKey, @RequestParam String newKey) {
        i18nService.renameTranslationKey(oldKey, newKey);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/keys")
    public ResponseEntity<Void> deleteKey(@RequestParam String key) {
        i18nService.deleteTranslationKey(key);
        return ResponseEntity.noContent().build();
    }
}
