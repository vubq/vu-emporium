import { defineStore } from 'pinia';
import { ref } from 'vue';
import axios from 'axios';

interface Admin {
    id: number;
    email: string;
    fullName: string;
    enabled: boolean;
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
