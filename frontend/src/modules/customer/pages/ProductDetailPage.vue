<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Breadcrumb -->
      <nav class="flex mb-8 animate-fade-in" aria-label="Breadcrumb">
        <ol class="inline-flex items-center space-x-2">
          <li>
            <router-link to="/" class="text-gray-500 hover:text-primary-600 transition-colors">Home</router-link>
          </li>
          <li><span class="text-gray-400">/</span></li>
          <li>
            <router-link to="/products" class="text-gray-500 hover:text-primary-600 transition-colors">Products</router-link>
          </li>
          <li><span class="text-gray-400">/</span></li>
          <li class="text-gray-900 font-medium">{{ product?.name || 'Product' }}</li>
        </ol>
      </nav>

      <!-- Loading State -->
      <div v-if="loading" class="animate-pulse">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
          <div class="aspect-square bg-gray-200 rounded-2xl"></div>
          <div class="space-y-6">
            <div class="h-10 bg-gray-200 rounded w-3/4"></div>
            <div class="h-8 bg-gray-200 rounded w-1/2"></div>
            <div class="h-24 bg-gray-200 rounded"></div>
            <div class="h-12 bg-gray-200 rounded w-full"></div>
          </div>
        </div>
      </div>
      
      <!-- Product Detail -->
      <div v-else-if="product" class="grid grid-cols-1 lg:grid-cols-2 gap-12 animate-slide-up">
        <!-- Image Gallery -->
        <div class="space-y-4">
          <div class="relative overflow-hidden rounded-2xl shadow-card bg-white">
            <img 
              v-if="product.images[0]" 
              :src="product.images[0]" 
              :alt="product.name" 
              class="w-full aspect-square object-cover"
            />
            <div v-else class="w-full aspect-square bg-gray-200 flex items-center justify-center">
              <span class="text-gray-400 text-lg">No image available</span>
            </div>
          </div>
          
          <!-- Thumbnail Gallery (if multiple images) -->
          <div v-if="product.images.length > 1" class="grid grid-cols-4 gap-4">
            <div 
              v-for="(image, index) in product.images.slice(0, 4)" 
              :key="index"
              class="aspect-square rounded-lg overflow-hidden border-2 border-gray-200 hover:border-primary-500 cursor-pointer transition-colors"
            >
              <img :src="image" :alt="`${product.name} ${index + 1}`" class="w-full h-full object-cover" />
            </div>
          </div>
        </div>
        
        <!-- Product Info -->
        <div class="space-y-6">
          <div>
            <h1 class="text-4xl font-display font-bold text-gray-900 mb-4">{{ product.name }}</h1>
            <div class="flex items-center gap-4 mb-6">
              <p class="text-4xl font-bold gradient-text">${{ product.price.toFixed(2) }}</p>
              <span v-if="product.stockQuantity > 0" class="badge badge-success">In Stock ({{ product.stockQuantity }})</span>
              <span v-else class="badge bg-red-100 text-red-700">Out of Stock</span>
            </div>
          </div>
          
          <!-- Description -->
          <div class="prose max-w-none">
            <h3 class="text-lg font-display font-bold text-gray-900 mb-2">Description</h3>
            <p class="text-gray-700 leading-relaxed">{{ product.description || 'No description available' }}</p>
          </div>
          
          <!-- Product Details -->
          <div class="bg-gray-50 rounded-xl p-6 space-y-3">
            <div class="flex justify-between">
              <span class="text-gray-600">SKU:</span>
              <span class="font-semibold">{{ product.sku || 'N/A' }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Category:</span>
              <span class="font-semibold">{{ product.category?.name || 'Uncategorized' }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Availability:</span>
              <span class="font-semibold" :class="product.stockQuantity > 0 ? 'text-green-600' : 'text-red-600'">
                {{ product.stockQuantity > 0 ? 'In Stock' : 'Out of Stock' }}
              </span>
            </div>
          </div>
          
          <!-- Quantity Selector & Add to Cart -->
          <div class="space-y-4">
            <div class="flex items-center gap-4">
              <label class="text-gray-700 font-medium">Quantity:</label>
              <div class="flex items-center border-2 border-gray-300 rounded-lg overflow-hidden">
                <button 
                  @click="decrementQuantity" 
                  class="px-4 py-2 bg-gray-100 hover:bg-gray-200 transition-colors"
                  :disabled="quantity <= 1"
                >
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4" />
                  </svg>
                </button>
                <input 
                  v-model.number="quantity" 
                  type="number" 
                  min="1" 
                  :max="product.stockQuantity"
                  class="w-20 text-center py-2 border-x-2 border-gray-300 focus:outline-none"
                />
                <button 
                  @click="incrementQuantity" 
                  class="px-4 py-2 bg-gray-100 hover:bg-gray-200 transition-colors"
                  :disabled="quantity >= product.stockQuantity"
                >
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                  </svg>
                </button>
              </div>
            </div>
            
            <button 
              @click="addToCart" 
              :disabled="product.stockQuantity === 0"
              class="btn btn-primary w-full text-lg py-4 shadow-glow disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <svg class="w-6 h-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
              {{ product.stockQuantity > 0 ? 'Add to Cart' : 'Out of Stock' }}
            </button>
          </div>
          
          <!-- Features -->
          <div class="grid grid-cols-3 gap-4 pt-6 border-t">
            <div class="text-center">
              <div class="w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-2">
                <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <p class="text-sm text-gray-600">Quality Guaranteed</p>
            </div>
            <div class="text-center">
              <div class="w-12 h-12 bg-accent-100 rounded-full flex items-center justify-center mx-auto mb-2">
                <svg class="w-6 h-6 text-accent-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
              </div>
              <p class="text-sm text-gray-600">Fast Shipping</p>
            </div>
            <div class="text-center">
              <div class="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-2">
                <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                </svg>
              </div>
              <p class="text-sm text-gray-600">Easy Returns</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Error State -->
      <div v-else class="text-center py-16">
        <svg class="w-24 h-24 mx-auto text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <p class="text-gray-600 text-lg mb-4">Product not found</p>
        <router-link to="/products" class="btn btn-primary">Back to Products</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { productApi } from '@/api/productApi';
import { useCartStore } from '@/stores/cartStore';
import type { Product } from '@/types/product';

const route = useRoute();
const cartStore = useCartStore();
const product = ref<Product | null>(null);
const loading = ref(true);
const quantity = ref(1);

onMounted(async () => {
  try {
    const slug = route.params.slug as string;
    const response = await productApi.getProductBySlug(slug);
    product.value = response.data.data;
  } catch (error) {
    console.error('Failed to load product:', error);
  } finally {
    loading.value = false;
  }
});

function incrementQuantity() {
  if (product.value && quantity.value < product.value.stockQuantity) {
    quantity.value++;
  }
}

function decrementQuantity() {
  if (quantity.value > 1) {
    quantity.value--;
  }
}

function addToCart() {
  if (product.value && product.value.stockQuantity > 0) {
    cartStore.addToCart(product.value, quantity.value);
  }
}
</script>
