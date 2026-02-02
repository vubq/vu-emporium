package com.ecommerce.service.interfaces;

import com.ecommerce.model.dto.request.AdminLoginRequest;
import com.ecommerce.model.dto.response.AdminDTO;
import com.ecommerce.model.dto.response.AuthResponse;

public interface AdminAuthService {
    AuthResponse login(AdminLoginRequest request);

    AdminDTO getCurrentAdmin(String email);

    void updateLanguage(String email, String language);
}
