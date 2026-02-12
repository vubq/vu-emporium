export interface ApiResponse<T> {
    success: boolean;
    message?: string;
    data: T;
}

export interface Page<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number; // page index (0-based)
    first: boolean;
    last: boolean;
    empty: boolean;
}

export interface Pageable {
    page?: number;
    size?: number;
    sort?: string[];
}
