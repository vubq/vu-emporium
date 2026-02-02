import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { authApi } from '@/api/authApi';
import type { User, LoginRequest, RegisterRequest, AuthResponse } from '@/types/auth';

export const useAuthStore = defineStore('auth', () => {
    const customer = ref<User | null>(null);
    const customerToken = ref<string | null>(localStorage.getItem('customerToken'));
    const customerRefreshToken = ref<string | null>(localStorage.getItem('customerRefreshToken'));

    const isAuthenticated = computed(() => !!customerToken.value);
    const isAdmin = computed(() => customer.value?.roles.includes('ROLE_ADMIN') ?? false);

    function setAuthData(data: AuthResponse) {
        // Set customer data (keep admin tokens if they exist)
        customer.value = {
            id: data.user.id,
            email: data.user.email,
            fullName: data.user.fullName,
            roles: data.user.roles,
            preferredLanguage: data.user.preferredLanguage,
        };
        customerToken.value = data.accessToken;
        customerRefreshToken.value = data.refreshToken;

        localStorage.setItem('customerToken', data.accessToken);
        localStorage.setItem('customerRefreshToken', data.refreshToken);
        localStorage.setItem('customer', JSON.stringify(customer.value));

        // Save user's preferred language to localStorage with customer-specific key
        // The i18n plugin will automatically load it on next page load
        if (data.user.preferredLanguage) {
            localStorage.setItem('customerLocale', data.user.preferredLanguage);
        }
    }

    function clearAuthData() {
        customer.value = null;
        customerToken.value = null;
        customerRefreshToken.value = null;

        localStorage.removeItem('customerToken');
        localStorage.removeItem('customerRefreshToken');
        localStorage.removeItem('customer');
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
            if (customerRefreshToken.value) {
                await authApi.logout(customerRefreshToken.value);
            }
        } finally {
            clearAuthData();
        }
    }

    function initializeAuth() {
        const storedCustomer = localStorage.getItem('customer');
        if (storedCustomer) {
            try {
                customer.value = JSON.parse(storedCustomer);
            } catch (e) {
                clearAuthData();
            }
        }
    }

    return {
        customer,
        customerToken,
        customerRefreshToken,
        isAuthenticated,
        isAdmin,
        register,
        login,
        logout,
        initializeAuth,
    };
});
