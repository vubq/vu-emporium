<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Page Header -->
    <div class="bg-gradient-to-r from-primary-600 to-primary-800 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-4xl md:text-5xl font-display font-bold mb-4 animate-fade-in">{{ $t('product.listing.title') }}</h1>
        <p class="text-xl text-white/90 animate-slide-up">{{ $t('product.listing.subtitle') }}</p>
      </div>
    </div>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="flex flex-col lg:flex-row gap-8">
        <!-- Sidebar: Categories -->
        <aside class="w-full lg:w-64 flex-shrink-0">
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 sticky top-24">
            <h2 class="text-lg font-bold text-gray-900 mb-6 flex items-center">
              <svg class="w-5 h-5 mr-2 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7" /></svg>
              {{ $t('common.categories') }}
            </h2>
            
            <div class="space-y-1">
              <button 
                @click="filterByCategory(null)"
                :class="[!selectedCategoryId ? 'bg-primary-50 text-primary-700' : 'text-gray-600 hover:bg-gray-50', 'w-full text-left px-4 py-2.5 rounded-xl text-sm font-medium transition-all flex items-center justify-between group']"
              >
                {{ $t('nav.all_products') }}
                <span v-if="!selectedCategoryId" class="w-1.5 h-1.5 rounded-full bg-primary-600"></span>
              </button>

              <!-- Recursive Category Item -->
              <div v-for="category in categories" :key="category.id">
                <button 
                  @click="filterByCategory(category.id)"
                  :class="[selectedCategoryId === category.id ? 'bg-primary-50 text-primary-700' : 'text-gray-600 hover:bg-gray-50', 'w-full text-left px-4 py-2.5 rounded-xl text-sm font-bold transition-all flex items-center justify-between group']"
                >
                  {{ getLocalized(category, 'name') }}
                  <span v-if="selectedCategoryId === category.id" class="w-1.5 h-1.5 rounded-full bg-primary-600"></span>
                </button>
                
                <!-- Children -->
                <div v-if="category.children && category.children.length > 0" class="ml-4 mt-1 border-l border-gray-100 pl-2 space-y-1">
                  <button 
                    v-for="child in category.children" 
                    :key="child.id"
                    @click="filterByCategory(child.id)"
                    :class="[selectedCategoryId === child.id ? 'bg-primary-50 text-primary-700' : 'text-gray-500 hover:bg-gray-50', 'w-full text-left px-3 py-1.5 rounded-lg text-xs font-medium transition-all flex items-center justify-between group']"
                  >
                    {{ getLocalized(child, 'name') }}
                    <span v-if="selectedCategoryId === child.id" class="w-1 h-1 rounded-full bg-primary-600"></span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </aside>

        <!-- Product List Area -->
        <div class="flex-1">
          <!-- Loading State -->
          <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            <ProductCardSkeleton v-for="i in 9" :key="i" />
          </div>
      
          <!-- Products Grid -->
          <div v-else-if="products.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        <div 
          v-for="(product, index) in products" 
          :key="product.id" 
          class="group cursor-pointer animate-fade-in"
          :style="{ animationDelay: (index as number) * 50 + 'ms' }"
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
                <span class="badge badge-success shadow-lg">{{ $t('product.in_stock') }}</span>
              </div>
              <div v-else class="absolute top-4 right-4">
                <span class="badge bg-red-100 text-red-700 shadow-lg">{{ $t('product.out_of_stock') }}</span>
              </div>
            </div>
            
            <!-- Product Info -->
            <div class="p-5">
              <h3 class="font-display font-bold text-lg mb-2 text-gray-900 group-hover:text-primary-600 transition-colors line-clamp-2">
                {{ getLocalized(product, 'name') }}
              </h3>
              <p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ getLocalized(product, 'description') }}</p>
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
                {{ product.stockQuantity > 0 ? $t('product.add_to_cart') : $t('product.out_of_stock') }}
              </button>
            </div>
          </div>
        </div>
      </div>
      
          <!-- Empty State -->
          <div v-else class="text-center py-24 bg-white rounded-3xl border border-dashed border-gray-200">
            <svg class="w-20 h-20 mx-auto text-gray-200 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
            </svg>
            <p class="text-gray-500 text-lg font-medium">{{ $t('product.listing.no_results') }}</p>
            <button @click="filterByCategory(null)" class="mt-4 text-primary-600 hover:text-primary-700 font-bold">{{ $t('product.listing.view_all') }}</button>
          </div>
      </div>
    </div>
  </div>
</div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { productApi, categoryApi } from '@/api/productApi';
import { useCartStore } from '@/stores/cartStore';
import type { Product, Category } from '@/types/product';
import AppImage from '@/components/common/AppImage.vue';
import ProductCardSkeleton from '@/components/skeleton/ProductCardSkeleton.vue';

import { useLocalized } from '@/composables/useLocalized';

const router = useRouter();
const route = useRoute();
const cartStore = useCartStore();
const { getLocalized } = useLocalized();
const products = ref<Product[]>([]);
const categories = ref<Category[]>([]);
const loading = ref(true);
const selectedCategoryId = ref<number | null>(null);

const fetchProducts = async () => {
  loading.value = true;
  try {
    const response = await productApi.getProducts({
      categoryId: selectedCategoryId.value || undefined
    });
    products.value = response.data.data.content;
  } catch (error) {
    console.error('Failed to load products:', error);
  } finally {
    loading.value = false;
  }
};

const fetchCategories = async () => {
    try {
        const response = await categoryApi.getRootCategories();
        categories.value = response.data.data;
    } catch (error) {
        console.error('Failed to load categories:', error);
    }
};

onMounted(async () => {
  if (route.query.categoryId) {
    selectedCategoryId.value = Number(route.query.categoryId);
  }
  await Promise.all([fetchCategories(), fetchProducts()]);
});

watch(() => route.query.categoryId, (newId) => {
    selectedCategoryId.value = newId ? Number(newId) : null;
    fetchProducts();
});

function filterByCategory(id: number | null) {
  router.push({
    path: '/products',
    query: { ...route.query, categoryId: id || undefined }
  });
}

function goToProduct(slug: string) {
  router.push(`/products/${slug}`);
}

function addToCart(product: Product) {
  if (product.stockQuantity > 0) {
    cartStore.addToCart(product, 1);
  }
}
</script>
