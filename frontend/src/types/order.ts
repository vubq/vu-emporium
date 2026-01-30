export interface Order {
    id: number;
    orderNumber: string;
    userId: number;
    items: OrderItem[];
    status: OrderStatus;
    subtotal: number;
    shippingFee: number;
    tax: number;
    total: number;
    shippingName: string;
    shippingPhone: string;
    shippingAddress: string;
    notes?: string;
    paymentMethod: string;
    paymentTransactionId?: string;
    paidAt?: string;
    shippedAt?: string;
    deliveredAt?: string;
    cancelledAt?: string;
    createdAt: string;
    updatedAt: string;
}

export interface OrderItem {
    id: number;
    productId: number;
    productName: string;
    productImage?: string;
    price: number;
    quantity: number;
    subtotal: number;
}

export type OrderStatus =
    | 'PENDING'
    | 'CONFIRMED'
    | 'PROCESSING'
    | 'SHIPPED'
    | 'DELIVERED'
    | 'CANCELLED'
    | 'REFUNDED';

export interface CreateOrderRequest {
    items: {
        productId: number;
        quantity: number;
    }[];
    shippingName: string;
    shippingPhone: string;
    shippingAddress: string;
    notes?: string;
    paymentMethod: string;
}
