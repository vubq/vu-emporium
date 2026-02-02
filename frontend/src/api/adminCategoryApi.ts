import apiClient from './axiosClient';
import type { ApiResponse } from '@/types/common';
import type { Category } from '@/types/product';

export const adminCategoryApi = {
    getAllCategories() {
        return apiClient.get<ApiResponse<Category[]>>('/admin/categories');
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
