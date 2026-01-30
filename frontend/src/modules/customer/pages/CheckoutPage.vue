<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <h1 class="text-3xl font-bold text-gray-900 mb-8">Checkout</h1>
      <div class="bg-white rounded-lg shadow-md p-6">
        <p class="text-gray-600">Checkout functionality will be implemented soon.</p>
        <p class="text-gray-600 mt-4">Total: ${{ cartStore.totalPrice.toFixed(2) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useCartStore } from '@/stores/cartStore';
const cartStore = useCartStore();
</script>
