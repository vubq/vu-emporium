package com.ecommerce.controller.api;

import com.ecommerce.model.entity.Language;
import com.ecommerce.service.interfaces.I18nService;
import com.ecommerce.service.interfaces.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/i18n")
@RequiredArgsConstructor
public class PublicI18nController {

    private final LanguageService languageService;
    private final I18nService i18nService;

    @GetMapping("/languages")
    public ResponseEntity<List<Language>> getActiveLanguages() {
        return ResponseEntity.ok(languageService.getActiveLanguages());
    }

    @GetMapping("/messages")
    public ResponseEntity<Map<String, String>> getMessages(@RequestParam String lang) {
        return ResponseEntity.ok(i18nService.getTranslationsForLanguage(lang));
    }
}
