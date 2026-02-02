<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Page Header -->
    <div class="bg-gradient-to-r from-primary-600 to-primary-800 text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-4xl md:text-5xl font-display font-bold mb-4 animate-fade-in">{{ $t('cart.title') }}</h1>
        <p class="text-xl text-white/90 animate-slide-up">{{ $t('cart.subtitle') }}</p>
      </div>
    </div>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <!-- Cart Items -->
      <div v-if="cartStore.items.length > 0" class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Items List -->
        <div class="lg:col-span-2 space-y-4">
          <div 
            v-for="(item, index) in cartStore.items" 
            :key="`${item.productId}-${item.variantId}`"
            class="bg-white rounded-2xl shadow-card p-6 animate-fade-in hover:shadow-card-hover transition-all duration-300"
            :style="{ animationDelay: `${index * 50}ms` }"
          >
            <div class="flex gap-6">
              <!-- Product Image -->
              <div class="flex-shrink-0">
                <div class="w-24 h-24 rounded-lg overflow-hidden bg-gray-100">
                  <AppImage 
                    :src="item.image || ''" 
                    :alt="item.name" 
                    class="w-full h-full object-cover"
                  />
                </div>
              </div>
              
              <!-- Product Info -->
              <div class="flex-1 min-w-0">
                <h3 class="font-display font-bold text-lg text-gray-900 mb-1 line-clamp-1">
                  {{ item.name }}
                </h3>
                
                <!-- Variant Info display -->
                <p v-if="item.variantName" class="text-sm text-gray-500 mb-2 font-medium">
                    {{ item.variantName }}
                </p>

                <p class="text-2xl font-bold gradient-text mb-4">
                  ${{ item.price.toFixed(2) }}
                </p>
                
                <!-- Quantity Controls -->
                <div class="flex items-center gap-4">
                  <div class="flex items-center border-2 border-gray-300 rounded-lg overflow-hidden">
                    <button 
                      @click="cartStore.updateQuantity(item.productId, item.variantId || null, item.quantity - 1)" 
                      class="px-3 py-2 bg-gray-100 hover:bg-gray-200 transition-colors"
                      :disabled="item.quantity <= 1"
                    >
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4" />
                      </svg>
                    </button>
                    <span class="px-4 py-2 font-semibold min-w-[3rem] text-center">{{ item.quantity }}</span>
                    <button 
                      @click="cartStore.updateQuantity(item.productId, item.variantId || null, item.quantity + 1)" 
                      class="px-3 py-2 bg-gray-100 hover:bg-gray-200 transition-colors"
                    >
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                      </svg>
                    </button>
                  </div>
                  
                  <!-- Remove Button -->
                  <button 
                    @click="cartStore.removeFromCart(item.productId, item.variantId || null)" 
                    class="text-red-600 hover:text-red-700 font-medium transition-colors flex items-center gap-2 group"
                  >
                    <svg class="w-5 h-5 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                    {{ $t('cart.remove') }}
                  </button>
                </div>
              </div>
              
              <!-- Item Subtotal -->
              <div class="text-right">
                <p class="text-sm text-gray-600 mb-1">{{ $t('cart.subtotal') }}</p>
                <p class="text-2xl font-bold gradient-text">
                  ${{ (item.price * item.quantity).toFixed(2) }}
                </p>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Order Summary -->
        <div class="lg:col-span-1">
          <div class="bg-gradient-to-br from-gray-900 to-gray-800 rounded-2xl shadow-glow-lg p-8 text-white sticky top-24">
            <h2 class="text-2xl font-display font-bold mb-6">{{ $t('cart.order_summary') }}</h2>
            
            <div class="space-y-4 mb-6">
              <div class="flex justify-between text-white/80">
                <span>{{ $t('common.products') }} ({{ cartStore.totalItems }})</span>
                <span>${{ cartStore.totalPrice.toFixed(2) }}</span>
              </div>
              <div class="flex justify-between text-white/80">
                <span>{{ $t('cart.shipping') }}</span>
                <span>{{ $t('cart.free') }}</span>
              </div>
              <div class="border-t border-white/20 pt-4">
                <div class="flex justify-between text-xl font-bold">
                  <span>{{ $t('common.total') }}</span>
                  <span class="text-accent-400">${{ cartStore.totalPrice.toFixed(2) }}</span>
                </div>
              </div>
            </div>
            
            <router-link 
              to="/checkout" 
              class="btn btn-accent w-full text-lg py-4 shadow-xl hover:shadow-2xl"
            >
              {{ $t('cart.checkout') }}
            </router-link>
            
            <router-link 
              to="/products" 
              class="block text-center text-white/80 hover:text-white mt-4 transition-colors"
            >
              ← {{ $t('cart.continue_shopping') }}
            </router-link>
          </div>
        </div>
      </div>
      
      <!-- Empty Cart State -->
      <div v-else class="text-center py-16 animate-fade-in">
        <div class="max-w-md mx-auto">
          <div class="w-32 h-32 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg class="w-16 h-16 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
          </div>
          <h2 class="text-3xl font-display font-bold text-gray-900 mb-4">{{ $t('cart.empty') }}</h2>
          <p class="text-gray-600 mb-8">{{ $t('cart.empty_desc') }}</p>
          <router-link to="/products" class="btn btn-primary text-lg px-8 py-4">
            {{ $t('cart.start_shopping') }}
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import { useCartStore } from '@/stores/cartStore';
import AppImage from '@/components/common/AppImage.vue';

const cartStore = useCartStore();
</script>
