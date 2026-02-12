<template>
  <div class="min-h-screen">
    <!-- Hero Section with Gradient -->
    <section class="relative bg-gradient-hero overflow-hidden">
      <!-- Background Pattern -->
      <div class="absolute inset-0 opacity-10">
        <div class="absolute inset-0" style="background-image: radial-gradient(circle at 1px 1px, white 1px, transparent 0); background-size: 40px 40px;"></div>
      </div>
      
      <div class="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24 md:py-32">
        <div class="text-center">
          <h1 class="text-5xl md:text-7xl font-display font-bold text-white mb-6 animate-fade-in">
            {{ $t('home.welcome') }} <span class="block mt-2">{{ $t('home.hero_title') }}</span>
          </h1>
          <p class="text-xl md:text-2xl text-white/90 mb-8 max-w-3xl mx-auto animate-slide-up">
            {{ $t('home.hero_subtitle') }}
          </p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center animate-slide-up animate-delay-100">
            <router-link to="/products" class="btn btn-accent text-lg px-8 py-4 shadow-glow-lg">
              {{ $t('home.shop_now') }}
            </router-link>
            <a href="#featured" class="btn btn-secondary bg-white/20 backdrop-blur-sm text-white border-white/30 hover:bg-white/30 text-lg px-8 py-4">
              {{ $t('home.explore_featured') }}
            </a>
          </div>
        </div>
      </div>
      
      <!-- Wave Divider -->
      <div class="absolute bottom-0 left-0 right-0">
        <svg viewBox="0 0 1440 120" fill="none" xmlns="http://www.w3.org/2000/svg" class="w-full h-auto">
          <path d="M0 120L60 105C120 90 240 60 360 45C480 30 600 30 720 37.5C840 45 960 60 1080 67.5C1200 75 1320 75 1380 75L1440 75V120H1380C1320 120 1200 120 1080 120C960 120 840 120 720 120C600 120 480 120 360 120C240 120 120 120 60 120H0Z" fill="#F9FAFB"/>
        </svg>
      </div>
    </section>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- Benefits Section -->
      <section class="py-16 -mt-8">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="text-center p-6 bg-white rounded-2xl shadow-card hover:shadow-card-hover transition-all duration-300 group">
            <div class="w-16 h-16 bg-gradient-to-br from-primary-500 to-primary-700 rounded-2xl flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
              </svg>
            </div>
            <h3 class="font-display font-bold text-lg mb-2">{{ $t('home.benefits.quality_title') }}</h3>
            <p class="text-gray-600">{{ $t('home.benefits.quality_desc') }}</p>
          </div>
          
          <div class="text-center p-6 bg-white rounded-2xl shadow-card hover:shadow-card-hover transition-all duration-300 group">
            <div class="w-16 h-16 bg-gradient-to-br from-accent-500 to-accent-700 rounded-2xl flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
            </div>
            <h3 class="font-display font-bold text-lg mb-2">{{ $t('home.benefits.delivery_title') }}</h3>
            <p class="text-gray-600">{{ $t('home.benefits.delivery_desc') }}</p>
          </div>
          
          <div class="text-center p-6 bg-white rounded-2xl shadow-card hover:shadow-card-hover transition-all duration-300 group">
            <div class="w-16 h-16 bg-gradient-to-br from-green-500 to-green-700 rounded-2xl flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="font-display font-bold text-lg mb-2">{{ $t('home.benefits.price_title') }}</h3>
            <p class="text-gray-600">{{ $t('home.benefits.price_desc') }}</p>
          </div>
        </div>
      </section>
      
      <!-- Featured Products -->
      <section id="featured" class="py-16">
        <div class="text-center mb-12">
          <h2 class="text-4xl font-display font-bold gradient-text mb-4">{{ $t('home.featured_products') }}</h2>
          <p class="text-gray-600 text-lg">{{ $t('home.featured_subtitle') }}</p>
        </div>
        
        <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <ProductCardSkeleton v-for="i in 4" :key="i" />
        </div>
        
        <div v-else-if="featuredProducts.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <div 
            v-for="(product, index) in featuredProducts" 
            :key="product.id" 
            class="group cursor-pointer animate-fade-in"
            :style="{ animationDelay: `${index * 100}ms` }"
            @click="goToProduct(product.slug)"
          >
            <!-- Product Card -->
            <div class="bg-white rounded-2xl shadow-card hover:shadow-card-hover transition-all duration-300 overflow-hidden group-hover:-translate-y-2">
              <!-- Image Container -->
              <div class="relative overflow-hidden aspect-square bg-gray-100">
                <AppImage 
                  :src="product.images[0] || ''" 
                  :alt="product.name" 
                  class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
                />
                
                <!-- Gradient Overlay on Hover -->
                <div class="absolute inset-0 bg-gradient-to-t from-black/50 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
                
                <!-- Quick View Badge -->
                <div class="absolute top-4 right-4 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                  <span class="badge badge-primary shadow-lg">Featured</span>
                </div>
              </div>
              
              <!-- Product Info -->
              <div class="p-5">
                <h3 class="font-display font-bold text-lg mb-2 text-gray-900 group-hover:text-primary-600 transition-colors line-clamp-1">
                  {{ getLocalized(product, 'name') }}
                </h3>
                <p class="text-2xl font-bold gradient-text mb-4">
                  ${{ (product.salePrice && product.salePrice > 0 ? product.salePrice : (product.basePrice || 0)).toFixed(2) }}
                  <span v-if="product.salePrice && product.salePrice > 0 && product.salePrice < (product.basePrice || 0)" class="text-sm text-gray-500 line-through ml-2">
                    ${{ (product.basePrice || 0).toFixed(2) }}
                  </span>
                </p>
                <button 
                  @click.stop="addToCart(product)" 
                  class="btn btn-primary w-full group-hover:shadow-glow"
                >
                  <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                  </svg>
                  {{ $t('product.add_to_cart') }}
                </button>
              </div>
            </div>
          </div>
        </div>
        
        <div v-else class="text-center py-12">
          <p class="text-gray-500">No featured products available</p>
        </div>
      </section>
      
      <!-- CTA Section -->
      <section class="py-16">
        <div class="bg-gradient-primary rounded-3xl p-12 text-center shadow-glow-lg">
          <h2 class="text-4xl font-display font-bold text-white mb-4">
            {{ $t('home.cta_title') }}
          </h2>
          <p class="text-white/90 text-lg mb-8 max-w-2xl mx-auto">
            {{ $t('home.cta_desc') }}
          </p>
          <router-link to="/products" class="btn btn-accent text-lg px-8 py-4 shadow-xl">
            {{ $t('home.browse_all') }}
          </router-link>
        </div>
      </section>
    </div>
  </div>
</template>


<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { productApi } from '@/api/productApi';
import { useCartStore } from '@/stores/cartStore';
import type { Product } from '@/types/product';
import AppImage from "@/components/common/AppImage.vue";
import ProductCardSkeleton from '@/components/skeleton/ProductCardSkeleton.vue';
import { useLocalized } from '@/composables/useLocalized';

const router = useRouter();
const cartStore = useCartStore();
const { getLocalized } = useLocalized();
const featuredProducts = ref<Product[]>([]);
const loading = ref(true);

onMounted(async () => {
  loading.value = true;
  try {
    const response = await productApi.getFeaturedProducts();
    featuredProducts.value = response.data.data;
  } catch (error) {
    console.error('Failed to load featured products:', error);
  } finally {
    loading.value = false;
  }
});

function goToProduct(slug: string) {
  router.push(`/products/${slug}`);
}

function addToCart(product: Product) {
  cartStore.addToCart(product, 1);
}
</script>
