package com.ecommerce.service.interfaces;

import com.ecommerce.model.entity.Language;
import java.util.List;

public interface LanguageService {
    List<Language> getAllLanguages();

    org.springframework.data.domain.Page<Language> getAllLanguages(org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<Language> getLanguages(String search, Boolean isActive,
            org.springframework.data.domain.Pageable pageable);

    List<Language> getActiveLanguages();
    // ... rest of file

    Language getLanguageByCode(String code);

    Language createLanguage(Language language);

    Language updateLanguage(String code, Language language);

    void deleteLanguage(String code);

    Language getDefaultLanguage();

    void setDefaultLanguage(String code);
}
