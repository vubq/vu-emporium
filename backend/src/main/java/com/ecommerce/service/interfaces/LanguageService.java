package com.ecommerce.service.interfaces;

import com.ecommerce.model.entity.Language;
import java.util.List;

public interface LanguageService {
    List<Language> getAllLanguages();

    List<Language> getActiveLanguages();

    Language getLanguageByCode(String code);

    Language createLanguage(Language language);

    Language updateLanguage(String code, Language language);

    void deleteLanguage(String code);

    Language getDefaultLanguage();

    void setDefaultLanguage(String code);
}
