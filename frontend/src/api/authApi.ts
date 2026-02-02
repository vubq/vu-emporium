import apiClient from './axiosClient';
import type { LoginRequest, RegisterRequest, AuthResponse } from '@/types/auth';

export const authApi = {
    register(data: RegisterRequest) {
        return apiClient.post<{ data: AuthResponse }>('/auth/register', data);
    },

    login(data: LoginRequest) {
        return apiClient.post<{ data: AuthResponse }>('/auth/login', data);
    },

    refreshToken(refreshToken: string) {
        return apiClient.post<{ data: AuthResponse }>('/auth/refresh', { refreshToken });
    },

    logout(refreshToken: string) {
        return apiClient.post('/auth/logout', { refreshToken });
    },

    updateLanguage(language: string) {
        return apiClient.patch('/auth/language', { language });
    },
};
