package com.ecommerce.service.impl;

import com.ecommerce.model.entity.Language;
import com.ecommerce.repository.LanguageRepository;
import com.ecommerce.service.interfaces.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Override
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    public org.springframework.data.domain.Page<Language> getAllLanguages(
            org.springframework.data.domain.Pageable pageable) {
        return languageRepository.findAll(pageable);
    }

    @Override
    public org.springframework.data.domain.Page<Language> getLanguages(String search, Boolean isActive,
            org.springframework.data.domain.Pageable pageable) {
        if (search != null && !search.isEmpty() && Boolean.TRUE.equals(isActive)) {
            return languageRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(search, pageable);
        } else if (search != null && !search.isEmpty()) {
            return languageRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (Boolean.TRUE.equals(isActive)) {
            return languageRepository.findByIsActiveTrue(pageable);
        } else {
            return languageRepository.findAll(pageable);
        }
    }

    @Override
    public List<Language> getActiveLanguages() {
        return languageRepository.findAllByIsActiveTrueOrderByDisplayOrderAsc();
    }

    @Override
    public Language getLanguageByCode(String code) {
        return languageRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Language not found with code: " + code));
    }

    @Override
    @Transactional
    public Language createLanguage(Language language) {
        if (languageRepository.existsById(language.getCode())) {
            throw new RuntimeException("Language code already exists: " + language.getCode());
        }
        if (Boolean.TRUE.equals(language.getIsDefault())) {
            resetDefaultLanguage();
        }
        return languageRepository.save(language);
    }

    @Override
    @Transactional
    public Language updateLanguage(String code, Language language) {
        Language existing = getLanguageByCode(code);
        existing.setName(language.getName());
        existing.setFlagIcon(language.getFlagIcon());
        existing.setIsActive(language.getIsActive());
        existing.setDisplayOrder(language.getDisplayOrder());

        if (Boolean.TRUE.equals(language.getIsDefault()) && !Boolean.TRUE.equals(existing.getIsDefault())) {
            resetDefaultLanguage();
            existing.setIsDefault(true);
        }

        return languageRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteLanguage(String code) {
        Language language = getLanguageByCode(code);
        if (Boolean.TRUE.equals(language.getIsDefault())) {
            throw new RuntimeException("Cannot delete the default language");
        }
        languageRepository.delete(language);
    }

    @Override
    public Language getDefaultLanguage() {
        return languageRepository.findByIsDefaultTrue()
                .orElseGet(() -> languageRepository.findAll().stream().findFirst()
                        .orElse(null));
    }

    @Override
    @Transactional
    public void setDefaultLanguage(String code) {
        resetDefaultLanguage();
        Language language = getLanguageByCode(code);
        language.setIsDefault(true);
        language.setIsActive(true);
        languageRepository.save(language);
    }

    private void resetDefaultLanguage() {
        languageRepository.findByIsDefaultTrue().ifPresent(l -> {
            l.setIsDefault(false);
            languageRepository.save(l);
        });
    }
}
