package com.ecommerce.controller.api.admin;

import com.ecommerce.model.dto.request.AdminLoginRequest;
import com.ecommerce.model.dto.response.AdminDTO;
import com.ecommerce.model.dto.response.ApiResponse;
import com.ecommerce.model.dto.response.AuthResponse;
import com.ecommerce.service.interfaces.AdminAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AdminLoginRequest request) {
        AuthResponse response = adminAuthService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AdminDTO>> getCurrentAdmin(Authentication authentication) {
        String email = authentication.getName();
        AdminDTO admin = adminAuthService.getCurrentAdmin(email);
        return ResponseEntity.ok(ApiResponse.success(admin));
    }
}
