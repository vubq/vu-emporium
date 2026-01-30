export interface Product {
    id: number;
    name: string;
    slug: string;
    description?: string;
    price: number;
    compareAtPrice?: number;
    stockQuantity: number;
    sku?: string;
    categoryId?: number;
    images: string[];
    status: 'ACTIVE' | 'INACTIVE' | 'OUT_OF_STOCK' | 'DISCONTINUED';
    featured: boolean;
    viewCount: number;
    soldCount: number;
    createdAt: string;
    updatedAt: string;
}

export interface Category {
    id: number;
    name: string;
    slug: string;
    description?: string;
    imageUrl?: string;
    parentId?: number;
    active: boolean;
    displayOrder: number;
}
