<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Page Header -->
    <div class="bg-gradient-to-r from-primary-600 to-primary-800 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-4xl md:text-5xl font-display font-bold mb-4 animate-fade-in">Our Products</h1>
        <p class="text-xl text-white/90 animate-slide-up">Discover our curated collection of quality items</p>
      </div>
    </div>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <!-- Loading State -->
      <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <div v-for="i in 8" :key="i" class="animate-pulse">
          <div class="bg-white rounded-2xl shadow-card overflow-hidden">
            <div class="aspect-square bg-gray-200"></div>
            <div class="p-5 space-y-3">
              <div class="h-4 bg-gray-200 rounded w-3/4"></div>
              <div class="h-6 bg-gray-200 rounded w-1/2"></div>
              <div class="h-10 bg-gray-200 rounded"></div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Products Grid -->
      <div v-else-if="products.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <div 
          v-for="(product, index) in products" 
          :key="product.id" 
          class="group cursor-pointer animate-fade-in"
          :style="{ animationDelay: `${index * 50}ms` }"
          @click="goToProduct(product.slug)"
        >
          <div class="bg-white rounded-2xl shadow-card hover:shadow-card-hover transition-all duration-300 overflow-hidden group-hover:-translate-y-2">
            <!-- Image -->
            <div class="relative overflow-hidden aspect-square bg-gray-100">
                <AppImage 
                  :src="product.images[0] || ''" 
                  class="w-full h-full group-hover:scale-110 transition-transform duration-500"
                />
              
              <!-- Gradient Overlay -->
              <div class="absolute inset-0 bg-gradient-to-t from-black/50 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
              
              <!-- Stock Badge -->
              <div v-if="product.stockQuantity > 0" class="absolute top-4 right-4">
                <span class="badge badge-success shadow-lg">In Stock</span>
              </div>
              <div v-else class="absolute top-4 right-4">
                <span class="badge bg-red-100 text-red-700 shadow-lg">Out of Stock</span>
              </div>
            </div>
            
            <!-- Product Info -->
            <div class="p-5">
              <h3 class="font-display font-bold text-lg mb-2 text-gray-900 group-hover:text-primary-600 transition-colors line-clamp-2">
                {{ product.name }}
              </h3>
              <p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ product.description }}</p>
              <div class="flex items-center space-x-2">
                <div class="flex items-center space-x-2 mb-4">
                  <p class="text-2xl font-bold gradient-text">
                    ${{ (product.salePrice && product.salePrice > 0 ? product.salePrice : product.basePrice || 0).toFixed(2) }}
                  </p>
                  <p v-if="product.salePrice && product.salePrice > 0 && product.salePrice < product.basePrice" class="text-sm text-gray-500 line-through">
                    ${{ product.basePrice?.toFixed(2) }}
                  </p>
                </div>
              </div>
              <button 
                @click.stop="addToCart(product)" 
                :disabled="product.stockQuantity === 0"
                class="btn btn-primary w-full group-hover:shadow-glow disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
                {{ product.stockQuantity > 0 ? 'Add to Cart' : 'Out of Stock' }}
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Empty State -->
      <div v-else class="text-center py-16">
        <svg class="w-24 h-24 mx-auto text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
        </svg>
        <p class="text-gray-600 text-lg">No products found</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { productApi } from '@/api/productApi';
import { useCartStore } from '@/stores/cartStore';
import type { Product } from '@/types/product';
import AppImage from '@/components/common/AppImage.vue';

const router = useRouter();
const cartStore = useCartStore();
const products = ref<Product[]>([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const response = await productApi.getProducts();
    products.value = response.data.data.content;
  } catch (error) {
    console.error('Failed to load products:', error);
  } finally {
    loading.value = false;
  }
});

function goToProduct(slug: string) {
  router.push(`/products/${slug}`);
}

function addToCart(product: Product) {
  if (product.stockQuantity > 0) {
    cartStore.addToCart(product, 1);
  }
}
</script>
