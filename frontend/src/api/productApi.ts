import apiClient from './axiosClient';
import type { Product, Category } from '@/types/product';

export const productApi = {
    getProducts(params?: {
        page?: number;
        size?: number;
        categoryId?: number;
        search?: string;
        sortBy?: string;
        sortDir?: string;
    }) {
        return apiClient.get<{ data: { content: Product[]; totalPages: number; totalElements: number } }>('/products', { params });
    },

    getProductById(id: number) {
        return apiClient.get<{ data: Product }>(`/products/${id}`);
    },

    getProductBySlug(slug: string) {
        return apiClient.get<{ data: Product }>(`/products/slug/${slug}`);
    },

    getFeaturedProducts() {
        return apiClient.get<{ data: Product[] }>('/products/featured');
    },
};

export const categoryApi = {
    getCategories() {
        return apiClient.get<{ data: Category[] }>('/categories');
    },

    getCategoryById(id: number) {
        return apiClient.get<{ data: Category }>(`/categories/${id}`);
    },

    getCategoryBySlug(slug: string) {
        return apiClient.get<{ data: Category }>(`/categories/slug/${slug}`);
    },

    getRootCategories() {
        return apiClient.get<{ data: Category[] }>('/categories/root');
    },
};
