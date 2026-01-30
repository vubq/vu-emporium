export interface ProductOptionValue {
    id: number;
    value: string;
}

export interface ProductOption {
    id: number;
    name: string;
    values: ProductOptionValue[];
}

export interface ProductVariant {
    id: number;
    sku: string;
    price: number;
    stockQuantity: number;
    image: string;
    optionValues: ProductOptionValue[];
}

export interface Product {
    id: number;
    name: string;
    slug: string;
    description?: string;
    price: number;
    compareAtPrice?: number;
    stockQuantity: number;
    sku?: string;
    category?: Category;
    categoryId?: number;
    images: string[];
    status: 'ACTIVE' | 'DRAFT' | 'ARCHIVED';
    featured: boolean;
    viewCount: number;
    soldCount: number;
    createdAt: string;
    updatedAt: string;
    hasVariants: boolean;
    options: ProductOption[];
    variants: ProductVariant[];
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
