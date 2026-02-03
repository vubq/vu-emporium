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
    basePrice: number;
    salePrice?: number;
    costPrice?: number;
    stockQuantity: number;
    images: string[]; // Changed from single image to array
    optionValues: ProductOptionValue[];
}

export interface Product {
    id: number;
    name: string;
    slug: string;
    description?: string;
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

    // New Fields
    basePrice?: number;
    salePrice?: number;
    costPrice?: number;
    brand?: string;
    weight?: number;
    length?: number;
    width?: number;
    height?: number;
    metaTitle?: string;
    metaDescription?: string;
    metaKeywords?: string;
    averageRating?: number;
    reviewCount?: number;
    totalSales?: number;

    isDiscontinued?: boolean;
}

export interface Category {
    id: number;
    name: string;
    slug: string;
    description?: string;
    imageUrl?: string;
    parentId?: number;
    status: 'ACTIVE' | 'DRAFT' | 'ARCHIVED';
    displayOrder: number;
    children?: Category[];
}
