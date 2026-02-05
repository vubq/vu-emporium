package com.ecommerce.service.impl;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.dto.request.AdminLoginRequest;
import com.ecommerce.model.dto.response.AdminDTO;
import com.ecommerce.model.dto.response.AuthResponse;
import com.ecommerce.model.entity.Admin;
import com.ecommerce.repository.AdminRepository;
import com.ecommerce.security.JwtService;
import com.ecommerce.service.interfaces.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

        private final AdminRepository adminRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;

        @Override
        public AuthResponse login(AdminLoginRequest request) {
                // Load admin user details
                Admin admin = adminRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new ResourceNotFoundException("Admin", "email", request.getEmail()));

                // Verify password manually
                if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                        throw new org.springframework.security.authentication.BadCredentialsException(
                                        "Invalid email or password");
                }

                // Check if admin is enabled
                if (!admin.getEnabled()) {
                        throw new org.springframework.security.authentication.DisabledException(
                                        "Admin account is disabled");
                }

                // Generate admin tokens
                String accessToken = jwtService.generateAdminToken(request.getEmail());
                String refreshToken = jwtService.generateAdminRefreshToken(request.getEmail());

                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .tokenType("Bearer")
                                .expiresIn(jwtService.getExpirationTime())
                                .user(AuthResponse.UserInfo.builder()
                                                .id(admin.getId())
                                                .email(admin.getEmail())
                                                .fullName(admin.getFullName())
                                                .roles(new String[] { "ROLE_ADMIN" })
                                                .preferredLanguage(admin.getPreferredLanguage())
                                                .build())
                                .build();
        }

        @Override
        public AdminDTO getCurrentAdmin(String email) {
                Admin admin = adminRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Admin", "email", email));

                return AdminDTO.builder()
                                .id(admin.getId())
                                .email(admin.getEmail())
                                .fullName(admin.getFullName())
                                .enabled(admin.getEnabled())
                                .preferredLanguage(admin.getPreferredLanguage())
                                .build();
        }

        @Override
        public void updateLanguage(String email, String language) {
                Admin admin = adminRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Admin", "email", email));

                admin.setPreferredLanguage(language);
                adminRepository.save(admin);
        }
}
