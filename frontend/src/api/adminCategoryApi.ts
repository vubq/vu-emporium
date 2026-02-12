import apiClient from './axiosClient';
import type { ApiResponse, Page } from '@/types/common';
import type { Category } from '@/types/product';

export const adminCategoryApi = {
    getAllCategories(params?: { page?: number; size?: number; search?: string; status?: string }) {
        return apiClient.get<ApiResponse<Page<Category>>>('/admin/categories', { params });
    },

    createCategory(data: any) {
        return apiClient.post<ApiResponse<Category>>('/admin/categories', data);
    },

    updateCategory(id: number, data: any) {
        return apiClient.put<ApiResponse<Category>>(`/admin/categories/${id}`, data);
    },

    deleteCategory(id: number) {
        return apiClient.delete<ApiResponse<void>>(`/admin/categories/${id}`);
    }
};
