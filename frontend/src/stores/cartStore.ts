import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { Product } from '@/types/product';

export interface CartItem {
    productId: number;
    variantId?: number | null;
    name: string;
    price: number;
    image: string;
    quantity: number;
    slug: string;
    variantName?: string;
}

export const useCartStore = defineStore('cart', () => {
    const items = ref<CartItem[]>([]);

    const totalItems = computed(() => {
        return items.value.reduce((sum, item) => sum + item.quantity, 0);
    });

    const totalPrice = computed(() => {
        return items.value.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    });

    // Accept a partial item or full structure. 
    // We expect the caller to provide current price/image/variant info.
    function addItem(newItem: CartItem) {
        const existingItem = items.value.find(item =>
            item.productId === newItem.productId &&
            item.variantId === newItem.variantId
        );

        if (existingItem) {
            existingItem.quantity += newItem.quantity;
        } else {
            items.value.push({ ...newItem });
        }

        saveCart();
    }

    function removeFromCart(productId: number, variantId: number | null = null) {
        items.value = items.value.filter(item =>
            !(item.productId === productId && item.variantId === variantId)
        );
        saveCart();
    }

    function updateQuantity(productId: number, variantId: number | null, quantity: number) {
        const item = items.value.find(item =>
            item.productId === productId && item.variantId === variantId
        );

        if (item) {
            item.quantity = quantity;
            if (item.quantity <= 0) {
                removeFromCart(productId, variantId);
            } else {
                saveCart();
            }
        }
    }

    function clearCart() {
        items.value = [];
        saveCart();
    }

    function saveCart() {
        localStorage.setItem('cart', JSON.stringify(items.value));
    }

    function loadCart() {
        const savedCart = localStorage.getItem('cart');
        if (savedCart) {
            try {
                items.value = JSON.parse(savedCart);
            } catch (e) {
                items.value = [];
            }
        }
    }

    return {
        items,
        totalItems,
        totalPrice,
        addItem,
        removeFromCart,
        updateQuantity,
        clearCart,
        loadCart,
    };
});
