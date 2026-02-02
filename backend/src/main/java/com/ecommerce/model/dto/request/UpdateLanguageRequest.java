package com.ecommerce.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLanguageRequest {

    @NotBlank(message = "Language is required")
    @Pattern(regexp = "^(en|vi)$", message = "Language must be either 'en' or 'vi'")
    private String language;
}
