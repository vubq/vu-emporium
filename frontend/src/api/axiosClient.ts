import axios, { AxiosInstance, AxiosError, InternalAxiosRequestConfig } from 'axios';

const apiClient: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor to add auth token
apiClient.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        // Check if this is an admin route
        const isAdminRoute = config.url?.startsWith('/admin') || config.url?.includes('/api/admin');

        // Get appropriate token based on route (no fallback to prevent conflicts)
        let token = null;
        if (isAdminRoute) {
            // For admin routes, only use adminToken
            token = localStorage.getItem('adminToken');
        } else {
            // For customer routes, only use accessToken
            token = localStorage.getItem('accessToken');
        }

        if (token && config.headers) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error: AxiosError) => {
        return Promise.reject(error);
    }
);

// Response interceptor to handle errors
apiClient.interceptors.response.use(
    (response) => response,
    async (error: AxiosError) => {
        const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

        // If 401 and not already retried, try to refresh token
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            // Check if this is an admin route
            const isAdminRoute = originalRequest.url?.startsWith('/admin') || originalRequest.url?.includes('/api/admin');

            if (isAdminRoute) {
                // Handle admin token refresh
                const adminRefreshToken = localStorage.getItem('adminRefreshToken');
                if (adminRefreshToken) {
                    try {
                        const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL || '/api'}/admin/auth/refresh`, {
                            refreshToken: adminRefreshToken,
                        });

                        const { accessToken, refreshToken: newRefreshToken } = response.data.data;
                        localStorage.setItem('adminToken', accessToken);
                        localStorage.setItem('adminRefreshToken', newRefreshToken);

                        if (originalRequest.headers) {
                            originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                        }

                        return apiClient(originalRequest);
                    } catch (refreshError) {
                        // Refresh failed, clear admin tokens and redirect to admin login
                        localStorage.removeItem('adminToken');
                        localStorage.removeItem('adminRefreshToken');
                        window.location.href = '/admin/login';
                        return Promise.reject(refreshError);
                    }
                }
            } else {
                // Handle customer token refresh
                const refreshToken = localStorage.getItem('refreshToken');
                if (refreshToken) {
                    try {
                        const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL || '/api'}/auth/refresh`, {
                            refreshToken,
                        });

                        const { accessToken, refreshToken: newRefreshToken } = response.data.data;
                        localStorage.setItem('accessToken', accessToken);
                        localStorage.setItem('refreshToken', newRefreshToken);

                        if (originalRequest.headers) {
                            originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                        }

                        return apiClient(originalRequest);
                    } catch (refreshError) {
                        // Refresh failed, clear tokens and redirect to login
                        localStorage.removeItem('accessToken');
                        localStorage.removeItem('refreshToken');
                        window.location.href = '/login';
                        return Promise.reject(refreshError);
                    }
                }
            }
        }

        return Promise.reject(error);
    }
);

export default apiClient;
