import { defineStore } from 'pinia';
import { ref } from 'vue';
import axios from 'axios';
import { useI18nStore } from './i18nStore';

interface Admin {
    id: number;
    email: string;
    fullName: string;
    enabled: boolean;
    preferredLanguage?: string;
}

interface AuthResponse {
    accessToken: string;
    refreshToken: string;
    tokenType: string;
    expiresIn: number;
}

export const useAdminAuthStore = defineStore('adminAuth', () => {
    const storedAdmin = localStorage.getItem('admin');
    const admin = ref<Admin | null>(storedAdmin ? JSON.parse(storedAdmin) : null);
    const token = ref<string | null>(localStorage.getItem('adminToken'));
    const isAuthenticated = ref<boolean>(!!token.value);
    const i18nStore = useI18nStore();

    async function login(email: string, password: string) {
        try {
            const response = await axios.post<{ data: AuthResponse }>('/api/admin/auth/login', {
                email,
                password,
            });

            const { accessToken, refreshToken } = response.data.data;

            // Set admin tokens (keep customer tokens if they exist)
            token.value = accessToken;
            localStorage.setItem('adminToken', accessToken);
            localStorage.setItem('adminRefreshToken', refreshToken);
            isAuthenticated.value = true;

            // Fetch admin info
            await fetchAdminInfo();

            return true;
        } catch (error) {
            console.error('Admin login failed:', error);
            throw error;
        }
    }

    async function fetchAdminInfo() {
        try {
            const response = await axios.get<{ data: Admin }>('/api/admin/auth/me', {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                },
            });
            admin.value = response.data.data;
            localStorage.setItem('admin', JSON.stringify(admin.value));

            // Save admin's preferred language to localStorage with admin-specific key
            // The i18n plugin will automatically load it on next page load
            if (admin.value.preferredLanguage) {
                localStorage.setItem('adminLocale', admin.value.preferredLanguage);
                i18nStore.changeLanguage(admin.value.preferredLanguage);
            }
        } catch (error) {
            console.error('Failed to fetch admin info:', error);
            logout();
        }
    }

    function logout() {
        admin.value = null;
        token.value = null;
        isAuthenticated.value = false;

        // Only clear admin tokens
        localStorage.removeItem('adminToken');
        localStorage.removeItem('adminRefreshToken');
        localStorage.removeItem('admin');
    }

    return {
        admin,
        token,
        isAuthenticated,
        login,
        logout,
        fetchAdminInfo,
    };
});
