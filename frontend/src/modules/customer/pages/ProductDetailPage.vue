<template>
  <div class="min-h-screen bg-gray-50 font-sans text-gray-900">
    <ProductDetailSkeleton v-if="loading" />
    <div v-else-if="!product" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 text-center">
        <h2 class="text-2xl font-bold text-gray-900">{{ $t('product.not_found') }}</h2>
        <router-link to="/products" class="mt-4 inline-block text-primary-600 hover:text-primary-800">{{ $t('product.return_to_shop') }}</router-link>
    </div>

    <div v-else class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Breadcrumb -->
      <nav class="flex mb-8 animate-fade-in" aria-label="Breadcrumb">
        <ol class="inline-flex items-center space-x-2">
          <li><router-link to="/" class="text-gray-500 hover:text-primary-600 transition-colors">{{ $t('common.home') }}</router-link></li>
          <li><span class="text-gray-400">/</span></li>
          <li><router-link to="/products" class="text-gray-500 hover:text-primary-600 transition-colors">{{ $t('common.products') }}</router-link></li>
          <li><span class="text-gray-400">/</span></li>
          <li class="text-gray-900 font-medium truncate max-w-xs">{{ getLocalized(product, 'name') }}</li>
        </ol>
      </nav>

      <div class="lg:grid lg:grid-cols-2 lg:gap-x-12 lg:items-start animate-slide-up">
        <!-- Image Gallery -->
        <div class="flex flex-col-reverse">
          <!-- Image Selector -->
          <div class="hidden mt-6 w-full max-w-2xl mx-auto sm:block lg:max-w-none">
            <div class="grid grid-cols-4 gap-6" aria-orientation="horizontal" role="tablist">
              <button 
                v-for="(image, index) in allImages" 
                :key="index"
                @click="selectedImageIndex = index"
                class="relative h-24 bg-white rounded-lg flex items-center justify-center text-sm font-medium uppercase text-gray-900 cursor-pointer hover:bg-gray-50 focus:outline-none focus:ring focus:ring-opacity-50 focus:ring-primary-500 overflow-hidden shadow-sm transition-all duration-200"
                :class="{'ring-2 ring-primary-600 ring-offset-2': selectedImageIndex === index, 'opacity-70': selectedImageIndex !== index}"
              >
                <span class="sr-only">Image {{ index + 1 }}</span>
                <span class="absolute inset-0 overflow-hidden rounded-lg">
                <AppImage 
                  :src="image || ''" 
                  class="w-full h-full object-cover" 
                />
              </span>
            </button>
          </div>
        </div>

        <!-- Main Image -->
        <div class="w-full aspect-w-1 aspect-h-1 bg-white rounded-2xl overflow-hidden shadow-card border border-gray-100">
           <transition
              enter-active-class="transition duration-300 ease-out"
              enter-from-class="opacity-0 scale-95"
              enter-to-class="opacity-100 scale-100"
              leave-active-class="transition duration-200 ease-in"
              leave-from-class="opacity-100 scale-100"
              leave-to-class="opacity-0 scale-95"
              mode="out-in"
           >
              <AppImage 
                  :key="currentDisplayImage"
                  :src="currentDisplayImage || ''" 
                  :alt="product.name" 
                  class="w-full h-full hover:scale-105 transition-transform duration-500 cursor-zoom-in"
                  object-fit="cover"
              />
           </transition>
        </div>
        </div>

        <!-- Product Info -->
        <div class="mt-10 px-4 sm:px-0 sm:mt-16 lg:mt-0">
          <div class="flex justify-between">
              <h1 class="text-3xl font-display font-extrabold tracking-tight text-gray-900 sm:text-4xl">{{ getLocalized(product, 'name') }}</h1>
              <span v-if="currentStrikethroughPrice" class="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-red-100 text-red-800 self-start mt-2">
                 {{ $t('product.save') }} {{ calculateDiscount(currentDisplayPrice, currentStrikethroughPrice) }}%
              </span>
          </div>
          
          <div class="mt-3 flex items-end space-x-4">
            <h2 class="sr-only">Product information</h2>
            <p class="text-3xl text-gray-900 font-bold gradient-text from-primary-600 to-accent-600">${{ currentDisplayPrice.toFixed(2) }}</p>
            <p v-if="currentStrikethroughPrice" class="text-lg text-gray-500 line-through mb-1">${{ currentStrikethroughPrice.toFixed(2) }}</p>
          </div>

          <!-- Ratings (Static for now) -->
          <div class="mt-3">
            <div class="flex items-center">
              <div class="flex items-center">
                <svg v-for="rating in [0, 1, 2, 3, 4]" :key="rating" :class="[4 > rating ? 'text-yellow-400' : 'text-gray-300', 'h-5 w-5 flex-shrink-0']" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                </svg>
              </div>
              <p class="sr-only">4 out of 5 stars</p>
              <a href="#" class="ml-3 text-sm font-medium text-primary-600 hover:text-primary-500">117 reviews</a>
            </div>
          </div>

          <div class="mt-6">
            <h3 class="sr-only">Description</h3>
            <div class="text-base text-gray-700 space-y-6" v-html="getLocalized(product, 'description')"></div>
          </div>

          <div class="mt-8 border-t border-gray-200 pt-8">
            <!-- Variants Selection (RadioGroup) -->
            <div v-if="product.hasVariants && product.options && product.options.length > 0" class="space-y-6">
                 <div v-for="option in product.options" :key="option.id">
                     <RadioGroup v-model="selectedOptions[option.name]" class="mt-2">
                        <RadioGroupLabel class="text-sm font-medium text-gray-900 block mb-2">{{ getLocalized(option, 'name') }}</RadioGroupLabel>
                        <div class="flex items-center space-x-3">
                            <RadioGroupOption 
                                as="template" 
                                v-for="value in option.values" 
                                :key="value.id" 
                                :value="value.value" 
                                v-slot="{ active, checked }"
                            >
                                <div 
                                    :class="[
                                        'group relative border rounded-md py-2 px-4 flex items-center justify-center text-sm font-medium uppercase hover:bg-gray-50 focus:outline-none sm:flex-1 cursor-pointer transition-all duration-200',
                                        active ? 'ring-2 ring-primary-600 ring-offset-2' : '',
                                        checked ? 'bg-primary-600 border-transparent text-white hover:bg-primary-700' : 'bg-white border-gray-200 text-gray-900',
                                    ]"
                                >
                                    <RadioGroupLabel as="span">{{ getLocalized(value, 'value') }}</RadioGroupLabel>
                                </div>
                            </RadioGroupOption>
                        </div>
                     </RadioGroup>
                 </div>
            </div>

            <!-- Add to Cart Section -->
            <div class="mt-8">
                 <!-- Discontinued Banner -->
                <div v-if="isArchived" class="mb-6 bg-red-50 border-l-4 border-red-500 p-4">
                    <div class="flex">
                        <div class="flex-shrink-0">
                            <svg class="h-5 w-5 text-red-500" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                            </svg>
                        </div>
                        <div class="ml-3">
                            <p class="text-sm text-red-700">
                                {{ $t('product.discontinued') }}
                            </p>
                        </div>
                    </div>
                </div>

                <div class="flex items-center space-x-4">
                    <!-- Quantity -->
                    <div class="flex items-center border border-gray-300 rounded-lg" :class="{'opacity-50 pointer-events-none': isArchived}">
                        <button @click="quantity > 1 ? quantity-- : null" class="p-2 text-gray-600 hover:text-primary-600 focus:outline-none" :disabled="isArchived">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4" /></svg>
                        </button>
                        <input type="number" v-model.number="quantity" class="w-12 text-center border-none focus:ring-0 text-gray-900 font-medium p-0" min="1" :disabled="isArchived" />
                        <button @click="quantity++" class="p-2 text-gray-600 hover:text-primary-600 focus:outline-none" :disabled="isArchived">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
                        </button>
                    </div>

                    <!-- Add to Cart Button -->
                    <button 
                        @click="addToCart" 
                        :disabled="isOutOfStock || loadingCart || isArchived"
                        class="flex-1 bg-gradient-to-r from-primary-600 to-indigo-600 border border-transparent rounded-lg py-3 px-8 flex items-center justify-center text-base font-medium text-white hover:from-primary-700 hover:to-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50 disabled:cursor-not-allowed shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 transition-all duration-200"
                    >
                        <svg v-if="loadingCart" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                        </svg>
                        <svg v-else-if="!isArchived" class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" /></svg>
                        {{ isArchived ? $t('product.discontinued') : (isOutOfStock ? $t('product.out_of_stock') : (loadingCart ? $t('product.adding') : $t('product.add_to_cart'))) }}
                    </button>
                </div>
                
                <p v-if="!isArchived && currentVariant && currentVariant.stockQuantity < 5 && currentVariant.stockQuantity > 0" class="mt-2 text-sm text-red-600 font-medium">
                    {{ $t('product.only_left', { count: currentVariant.stockQuantity }) }}
                </p>
            </div>
          
           </div>

           <!-- Product Features Icons -->
           <div class="mt-8 flex justify-between border-t border-gray-100 pt-6">
                <div class="flex flex-col items-center text-center group">
                    <div class="w-12 h-12 bg-blue-50 rounded-full flex items-center justify-center text-blue-600 mb-2 group-hover:scale-110 transition-transform">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                    </div>
                    <span class="text-xs font-medium text-gray-500">{{ $t('home.benefits.quality_title').split(' ')[0] }}<br>{{ $t('home.benefits.quality_title').split(' ')[1] }}</span>
                </div>
                <div class="flex flex-col items-center text-center group">
                    <div class="w-12 h-12 bg-green-50 rounded-full flex items-center justify-center text-green-600 mb-2 group-hover:scale-110 transition-transform">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" /></svg>
                    </div>
                    <span class="text-xs font-medium text-gray-500">{{ $t('home.benefits.delivery_title').split(' ')[0] }}<br>{{ $t('home.benefits.delivery_title').split(' ')[1] }}</span>
                </div>
                <div class="flex flex-col items-center text-center group">
                    <div class="w-12 h-12 bg-purple-50 rounded-full flex items-center justify-center text-purple-600 mb-2 group-hover:scale-110 transition-transform">
                         <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" /></svg>
                    </div>
                    <span class="text-xs font-medium text-gray-500">{{ $t('product.benefits.return_title').split(' ')[0] }}<br>{{ $t('product.benefits.return_title').split(' ')[1] }}</span>
                </div>
            </div>
        </div>
      </div>

       <!-- Product Tabs (Headless UI) -->
       <div class="mt-16 sm:mt-24">
            <TabGroup>
                <TabList class="flex space-x-1 rounded-xl bg-gray-100 p-1 max-w-md mx-auto">
                    <Tab 
                        v-for="tab in ['description', 'specifications', 'reviews']" 
                        as="template" 
                        :key="tab" 
                        v-slot="{ selected }"
                    >
                        <button
                            :class="[
                                'w-full rounded-lg py-2.5 text-sm font-medium leading-5',
                                'ring-white ring-opacity-60 ring-offset-2 ring-offset-blue-400 focus:outline-none focus:ring-2',
                                selected
                                    ? 'bg-white text-primary-700 shadow'
                                    : 'text-gray-600 hover:bg-white/[0.12] hover:text-primary-600',
                            ]"
                        >
                            {{ $t(`product.tabs.${tab}`) }}
                        </button>
                    </Tab>
                </TabList>

                <TabPanels class="mt-8">
                    <TabPanel class="rounded-xl bg-white p-6 shadow-sm ring-white ring-opacity-60 ring-offset-2 ring-offset-blue-400 focus:outline-none animate-fade-in text-gray-500">
                         <div v-html="product.description" class="prose max-w-none text-gray-500"></div>
                         <p v-if="!product.description" class="text-center italic">{{ $t('product.no_description') }}</p>
                    </TabPanel>
                     <TabPanel class="rounded-xl bg-white p-6 shadow-sm ring-white ring-opacity-60 ring-offset-2 ring-offset-blue-400 focus:outline-none animate-fade-in">
                        <dl class="grid grid-cols-1 gap-x-4 gap-y-8 sm:grid-cols-2">
                            <div class="sm:col-span-1 border-b border-gray-100 pb-4">
                                <dt class="text-sm font-medium text-gray-500">{{ $t('product.material') }}</dt>
                                <dd class="mt-1 text-sm text-gray-900">Premium blend</dd>
                            </div>
                             <div class="sm:col-span-1 border-b border-gray-100 pb-4">
                                <dt class="text-sm font-medium text-gray-500">{{ $t('product.care') }}</dt>
                                <dd class="mt-1 text-sm text-gray-900">Machine wash cold</dd>
                            </div>
                            <div class="sm:col-span-1">
                                <dt class="text-sm font-medium text-gray-500">{{ $t('product.category') }}</dt>
                                <dd class="mt-1 text-sm text-gray-900">{{ currentVariant ? getVariantName(currentVariant) : $t('common.all') }}</dd>
                            </div>
                        </dl>
                    </TabPanel>
                    <TabPanel class="rounded-xl bg-white p-6 shadow-sm ring-white ring-opacity-60 ring-offset-2 ring-offset-blue-400 focus:outline-none animate-fade-in text-center">
                        <div class="py-12">
                             <svg class="mx-auto h-12 w-12 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-3.582 8-8 8a8.013 8.013 0 01-5.45-2.125L3 19l1.45-4.55A8 8 0 0112 4c4.418 0 8 3.582 8 8z" /></svg>
                             <h3 class="mt-2 text-sm font-medium text-gray-900">{{ $t('product.no_reviews') }}</h3>
                             <p class="mt-1 text-sm text-gray-500">{{ $t('product.be_first_review') }}</p>
                             <div class="mt-6">
                                <button type="button" class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500">
                                {{ $t('product.write_review') }}
                                </button>
                             </div>
                        </div>
                    </TabPanel>
                </TabPanels>
            </TabGroup>
       </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute } from 'vue-router';
import AppImage from "@/components/common/AppImage.vue";
import { productApi } from '@/api/productApi';
import type { Product } from '@/types/product';
import { useCartStore } from '@/stores/cartStore';
import { RadioGroup, RadioGroupLabel, RadioGroupOption, TabGroup, TabList, Tab, TabPanels, TabPanel } from '@headlessui/vue';
import ProductDetailSkeleton from '@/components/skeleton/ProductDetailSkeleton.vue';
import { useLocalized } from '@/composables/useLocalized';

const { getLocalized } = useLocalized();
const route = useRoute();
const cartStore = useCartStore();


const product = ref<Product | null>(null);
const loading = ref(true);
const quantity = ref(1);
const selectedImageIndex = ref(0);
const loadingCart = ref(false);

const selectedOptions = ref<Record<string, string>>({});

onMounted(async () => {
  try {
    const slug = route.params.slug as string;
    const res = await productApi.getProductBySlug(slug);
    product.value = res.data.data;
    
    // Initialize selected options with first available values
    if (product.value.hasVariants && product.value.options) {
        product.value.options.forEach(opt => {
            if (opt.values && opt.values.length > 0) {
                selectedOptions.value[opt.name] = opt.values[0].value;
            }
        });
    }
  } catch (error) {
    console.error("Failed to fetch product:", error);
  } finally {
    loading.value = false;
  }
});

const currentVariant = computed(() => {
    if (!product.value || !product.value.hasVariants || !product.value.variants) return null;
    
    // Find variant that matches all selected options
    // IMPROVED LOGIC: Match by Index/Order instead of just value inclusion
    return product.value.variants.find(v => {
        // Basic length check
        if (v.optionValues.length !== product.value!.options.length) return false;

        // Check each position strictly
        return product.value!.options.every((opt, index) => {
             const selectedVal = selectedOptions.value[opt.name];
             const variantVal = v.optionValues[index].value;
             return variantVal === selectedVal;
        });
    });
});

// Reset image index when variant changes
watch(currentVariant, () => {
    selectedImageIndex.value = 0;
});

const currentDisplayPrice = computed(() => {
    const p = currentVariant.value || product.value;
    if (!p) return 0;
    return (p.salePrice && p.salePrice > 0) ? p.salePrice : (p.basePrice || 0);
});

const currentStrikethroughPrice = computed(() => {
    const p = currentVariant.value || product.value;
    if (!p || !p.salePrice || p.salePrice <= 0 || !p.basePrice || p.salePrice >= p.basePrice) {
        return null;
    }
    return p.basePrice;
});

const currentDisplayImage = computed(() => {
    // Priority: variant images > product images
    const images = allImages.value;
    if (images.length > 0) return images[selectedImageIndex.value] || images[0];
    return "https://via.placeholder.com/400?text=No+Image";
});

const allImages = computed(() => {
    // If variant is selected and has images, show variant images
    if (currentVariant.value && currentVariant.value.images && currentVariant.value.images.length > 0) {
        return currentVariant.value.images;
    }
    // Otherwise show product images
    return product.value?.images || [];
});

const isArchived = computed(() => {
    if (!product.value) return false;
    // Use backend-calculated flag which handles recursive category checks
    return !!product.value.discontinued;
});

const isOutOfStock = computed(() => {
    if (isArchived.value) return true; // Treat archived as out of stock for button state
    if (currentVariant.value) return currentVariant.value.stockQuantity <= 0;
    return (product.value?.stockQuantity || 0) <= 0;
});

function calculateDiscount(price: number, compareAt: number) {
    if (!compareAt || compareAt <= price) return 0;
    return Math.round(((compareAt - price) / compareAt) * 100);
}

function getVariantName(variant: any) {
    return variant.optionValues.map((v: any) => v.value).join(' / ');
}

async function addToCart() {
  if (!product.value || isArchived.value) return;
  
  loadingCart.value = true;
  await new Promise(resolve => setTimeout(resolve, 600)); // Fake network delay for effect

  // Construct cart item
  let variantId = null;
  if (product.value.hasVariants && currentVariant.value) {
      variantId = currentVariant.value.id; // Ensure variant has ID from backend
  }

  cartStore.addItem({
    productId: product.value.id,
    variantId: variantId, 
    name: product.value.name,
    price: currentDisplayPrice.value,
    image: allImages.value[0] || currentDisplayImage.value, // Use first image from gallery
    quantity: quantity.value,
    slug: product.value.slug,
    variantName: currentVariant.value ? getVariantName(currentVariant.value) : undefined
  });
  
  loadingCart.value = false;
  // Could add a toast notification here
// ... (existing code)
  
  loadingCart.value = false;
  // Could add a toast notification here
}

const handleImageError = (e: Event) => {
  const target = e.target as HTMLImageElement;
  target.src = 'https://via.placeholder.com/300x300?text=No+Image';
};
</script>
