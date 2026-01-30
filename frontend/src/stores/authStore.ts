import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { authApi } from '@/api/authApi';
import type { User, LoginRequest, RegisterRequest, AuthResponse } from '@/types/auth';

export const useAuthStore = defineStore('auth', () => {
    const user = ref<User | null>(null);
    const accessToken = ref<string | null>(localStorage.getItem('accessToken'));
    const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'));

    const isAuthenticated = computed(() => !!accessToken.value);
    const isAdmin = computed(() => user.value?.roles.includes('ROLE_ADMIN') ?? false);

    function setAuthData(data: AuthResponse) {
        // Set customer data (keep admin tokens if they exist)
        user.value = {
            id: data.user.id,
            email: data.user.email,
            fullName: data.user.fullName,
            roles: data.user.roles,
        };
        accessToken.value = data.accessToken;
        refreshToken.value = data.refreshToken;

        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);
        localStorage.setItem('user', JSON.stringify(user.value));
    }

    function clearAuthData() {
        user.value = null;
        accessToken.value = null;
        refreshToken.value = null;

        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
    }

    async function register(data: RegisterRequest) {
        try {
            const response = await authApi.register(data);
            setAuthData(response.data.data);
            return response.data;
        } catch (error) {
            throw error;
        }
    }

    async function login(data: LoginRequest) {
        try {
            const response = await authApi.login(data);
            setAuthData(response.data.data);
            return response.data;
        } catch (error) {
            throw error;
        }
    }

    async function logout() {
        try {
            if (refreshToken.value) {
                await authApi.logout(refreshToken.value);
            }
        } finally {
            clearAuthData();
        }
    }

    function initializeAuth() {
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            try {
                user.value = JSON.parse(storedUser);
            } catch (e) {
                clearAuthData();
            }
        }
    }

    return {
        user,
        accessToken,
        refreshToken,
        isAuthenticated,
        isAdmin,
        register,
        login,
        logout,
        initializeAuth,
    };
});
