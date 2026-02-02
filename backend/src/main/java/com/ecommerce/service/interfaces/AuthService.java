package com.ecommerce.service.interfaces;

import com.ecommerce.model.dto.request.LoginRequest;
import com.ecommerce.model.dto.request.RegisterRequest;
import com.ecommerce.model.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

    void updateLanguage(String email, String language);
}
