<template>
  <div class="bg-gray-50 min-h-screen p-6 font-sans">
    <div class="w-full h-full flex flex-col">
      <!-- Shared Header -->
      <AdminPageHeader
        :title="$t('admin.manage.products.title')"
        :subtitle="$t('admin.manage.products.subtitle')"
        :action-label="$t('admin.manage.products.add_new')"
        @action="openCreateModal"
      />

      <!-- Shared Filter Bar -->
      <AdminFilterBar
        :loading="loading"
        @filter="fetchProducts"
        @reset="resetFilters"
      >
        <!-- Search -->
        <div class="lg:col-span-1">
          <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('common.search') }}</label>
          <div class="relative">
            <input v-model="filters.search" @keyup.enter="fetchProducts" type="text" :placeholder="$t('admin.manage.products.search_placeholder')" class="w-full pl-9 pr-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
            </div>
          </div>
        </div>

        <!-- Brand -->
        <div class="lg:col-span-1">
            <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('admin.forms.product.brand') }}</label>
            <input v-model="filters.brand" type="text" :placeholder="$t('admin.manage.products.brand_placeholder')" class="w-full px-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
        </div>

        <!-- Category -->
        <div class="lg:col-span-1">
            <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('product.category') }}</label>
            <Combobox v-model="selectedCategory" nullable by="id">
                <div class="relative">
                    <div class="relative w-full text-left">
                        <ComboboxInput
                            class="w-full rounded-xl border border-gray-300 bg-white py-2 pl-3 pr-10 text-sm text-gray-900 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500 transition-all"
                            :displayValue="(category: any) => getLocalizedValue(category?.translations, category?.name, 'name')"
                            @change="query = $event.target.value"
                            :placeholder="$t('admin.manage.products.all_categories')"
                        />
                        <ComboboxButton class="absolute inset-y-0 right-0 flex items-center pr-2">
                            <svg class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                        </ComboboxButton>
                    </div>
                    <TransitionRoot
                        leave="transition ease-in duration-100"
                        leaveFrom="opacity-100"
                        leaveTo="opacity-0"
                        @after-leave="query = ''"
                    >
                        <ComboboxOptions class="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm p-1">
                            <div v-if="filteredCategories.length === 0 && query !== ''" class="relative cursor-default select-none py-2 px-4 text-gray-700 rounded-lg">
                                {{ $t('media.empty') }}
                            </div>
                            <ComboboxOption :value="null" as="template" v-slot="{ selected, active }">
                                <li class="relative cursor-pointer select-none py-2 pl-10 pr-4 rounded-lg my-0.5" :class="{ 'bg-indigo-600 text-white': active, 'text-gray-900': !active }">
                                    <span class="block truncate" :class="{ 'font-medium': selected, 'font-normal': !selected }">{{ $t('admin.manage.products.all_categories') }}</span>
                                </li>
                            </ComboboxOption>
                            <ComboboxOption
                                v-for="category in filteredCategories"
                                :key="category.id"
                                :value="category"
                                as="template"
                                v-slot="{ selected, active }"
                            >
                                <li class="relative cursor-pointer select-none py-2 pl-10 pr-4 rounded-lg my-0.5" :class="{ 'bg-indigo-600 text-white': active, 'text-gray-900': !active }">
                                    <span class="block truncate" :class="{ 'font-medium': selected, 'font-normal': !selected }">
                                        {{ getLocalizedValue(category.translations, category.name, 'name') }}
                                    </span>
                                    <span v-if="selected" class="absolute inset-y-0 left-0 flex items-center pl-3" :class="{ 'text-white': active, 'text-indigo-600': !active }">
                                        <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" /></svg>
                                    </span>
                                </li>
                            </ComboboxOption>
                        </ComboboxOptions>
                    </TransitionRoot>
                </div>
            </Combobox>
        </div>

        <!-- Status -->
        <div class="lg:col-span-1">
          <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('common.status') }}</label>
          <Listbox v-model="filters.status" v-slot="{ open }">
            <div class="relative">
              <ListboxButton class="relative w-full cursor-pointer rounded-xl bg-white py-2 pl-3 pr-10 text-left border border-gray-300 shadow-sm focus:outline-none focus:ring-0 sm:text-sm" :class="open ? 'ring-1 ring-indigo-500 border-indigo-500' : ''">
                <span class="block truncate font-medium" :class="statusColor(filters.status)">{{ getStatusLabel(filters.status) }}</span>
                <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                  <svg class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                </span>
              </ListboxButton>
              <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
                <ListboxOptions class="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm p-1">
                  <ListboxOption :value="null" as="template" v-slot="{ active, selected }">
                    <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4 rounded-lg']">
                      <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">{{ $t('admin.manage.products.all_statuses') }}</span>
                    </li>
                  </ListboxOption>
                  <ListboxOption v-for="status in ['ACTIVE', 'DRAFT', 'ARCHIVED']" :key="status" :value="status" as="template" v-slot="{ active, selected }">
                    <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4 rounded-lg']">
                      <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">{{ getStatusLabel(status) }}</span>
                      <span v-if="selected" class="absolute inset-y-0 left-0 flex items-center pl-3 text-indigo-600">
                        <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" /></svg>
                      </span>
                    </li>
                  </ListboxOption>
                </ListboxOptions>
              </transition>
            </div>
          </Listbox>
        </div>

        <!-- Price Range -->
        <div class="lg:col-span-1">
            <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('product.price_range') }}</label>
            <div class="flex items-center gap-2">
                <div class="relative flex-1">
                    <div class="absolute inset-y-0 left-0 pl-2.5 flex items-center pointer-events-none">
                        <span class="text-gray-500 text-xs">$</span>
                    </div>
                    <input v-model.number="filters.minPrice" type="number" :placeholder="$t('admin.manage.products.min_price')" class="w-full pl-5 pr-2 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
                </div>
                <span class="text-gray-400">-</span>
                <div class="relative flex-1">
                    <div class="absolute inset-y-0 left-0 pl-2.5 flex items-center pointer-events-none">
                        <span class="text-gray-500 text-xs">$</span>
                    </div>
                    <input v-model.number="filters.maxPrice" type="number" :placeholder="$t('admin.manage.products.max_price')" class="w-full pl-5 pr-2 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
                </div>
            </div>
        </div>
      </AdminFilterBar>

      <!-- Shared Data Table -->
      <AdminDataTable
        :loading="loading"
        :is-empty="products.length === 0"
        :empty-title="$t('admin.manage.products.no_results')"
        :empty-subtitle="$t('admin.manage.products.no_results_desc')"
        :current-page="currentPage"
        :total-pages="totalPages"
        :page-size="pageSize"
        :column-count="8"
        @page-change="changePage"
        @page-size-change="handlePageSizeChange"
        @clear-filters="resetFilters"
      >
        <template #header>
          <tr>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.manage.products.columns.product') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.manage.products.columns.sku') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.manage.products.columns.brand') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.manage.products.columns.category') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.manage.products.columns.price') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.manage.products.columns.stock') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.manage.products.columns.status') }}</th>
            <th scope="col" class="relative px-6 py-3 w-16">
              <span class="sr-only">{{ $t('common.actions') }}</span>
            </th>
          </tr>
        </template>

        <template #skeleton>
           <!-- Skeleton for product row -->
            <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center gap-4">
                     <div class="h-12 w-12 bg-gray-100 rounded-xl"></div>
                     <div class="space-y-2">
                        <div class="h-4 w-32 bg-gray-100 rounded"></div>
                        <div class="h-3 w-20 bg-gray-100 rounded"></div>
                     </div>
                </div>
            </td>
            <td class="px-6 py-4"><div class="h-4 w-20 bg-gray-100 rounded"></div></td>
            <td class="px-6 py-4"><div class="h-4 w-24 bg-gray-100 rounded"></div></td>
            <td class="px-6 py-4"><div class="h-4 w-16 bg-gray-100 rounded"></div></td>
            <td class="px-6 py-4"><div class="h-4 w-12 bg-gray-100 rounded"></div></td>
            <td class="px-6 py-4"><div class="h-6 w-20 bg-gray-100 rounded-full"></div></td>
            <td class="px-6 py-4"><div class="h-6 w-16 bg-gray-100 rounded-full"></div></td>
            <td class="px-6 py-4"></td>
        </template>

        <template #default>
          <tr v-for="(product, index) in products" :key="product.id" class="group hover:bg-gray-50 transition-colors cursor-pointer" @click="openEditModal(product)">
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="flex items-center">
                <div class="h-14 w-14 flex-shrink-0 relative group-hover:scale-105 transition-transform duration-300">
                  <AppImage 
                    :src="(product.images && product.images.length > 0) ? product.images[0] : ''" 
                    class="h-full w-full rounded-xl shadow-sm border border-gray-100 bg-white"
                  />
                </div>
                <div class="ml-4">
                  <div class="text-sm font-bold text-gray-900 group-hover:text-indigo-600 transition-colors line-clamp-1">{{ getLocalizedValue(product.translations, product.name, 'name') }}</div>
                  <div class="text-xs text-gray-500 hidden sm:block truncate max-w-[200px] mt-0.5">{{ getLocalizedValue(product.translations, product.description, 'description') || $t('product.no_description') }}</div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
                <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-gray-100 text-gray-800 font-mono border border-gray-200">
                    {{ product.sku || $t('common.unknown') }}
                </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
                <span class="text-sm text-gray-600">{{ product.brand || '-' }}</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                <div v-if="product.category" class="flex items-center">
                    <div class="w-2 h-2 rounded-full bg-indigo-500 mr-2"></div>
                    {{ getLocalizedValue(product.category.translations, product.category.name, 'name') }}
                </div>
                <span v-else class="text-gray-400 italic text-xs">{{ $t('common.uncategorized') }}</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-bold font-mono tracking-tight">
                <div v-if="product.salePrice && product.salePrice > 0" class="flex flex-col">
                    <span class="text-indigo-600">${{ product.salePrice.toFixed(2) }}</span>
                    <span class="text-gray-400 text-xs line-through">${{ product.basePrice?.toFixed(2) }}</span>
                </div>
                <span v-else>${{ product.basePrice?.toFixed(2) || '0.00' }}</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
                <div v-if="product.hasVariants" class="flex items-center text-xs text-purple-600 font-medium bg-purple-50 px-2.5 py-1 rounded-lg border border-purple-100 w-fit">
                    <svg class="w-3.5 h-3.5 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" /></svg>
                    {{ $t('admin.forms.product.variants') }}
                </div>
                <div v-else class="flex items-center">
                    <span class="block h-2 w-2 rounded-full mr-2" :class="[product.stockQuantity < 5 ? 'bg-red-500 animate-pulse' : (product.stockQuantity < 20 ? 'bg-yellow-500' : 'bg-green-500')]"></span>
                    <span :class="[product.stockQuantity < 5 ? 'text-red-700 font-bold' : (product.stockQuantity < 20 ? 'text-yellow-700' : 'text-gray-900'), 'text-sm font-medium']">
                        {{ product.stockQuantity }}
                    </span>
                </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <StatusBadge :status="product.status" />
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
              <ActionMenu 
                :items="getProductActions(product)" 
                :header="getLocalizedValue(product.translations, product.name, 'name')"
              />
            </td>
          </tr>
        </template>
      </AdminDataTable>

      <!-- Modal for Product Form (Headless UI) -->
      <TransitionRoot appear :show="showModal" as="template">
        <Dialog as="div" @close="closeModal" class="relative z-50">
            <TransitionChild
                as="template"
                enter="duration-300 ease-out"
                enter-from="opacity-0"
                enter-to="opacity-100"
                leave="duration-200 ease-in"
                leave-from="opacity-100"
                leave-to="opacity-0"
            >
                <div class="fixed inset-0 bg-black/40 backdrop-blur-sm" />
            </TransitionChild>

            <div class="fixed inset-0 overflow-y-auto">
                <div class="flex min-h-full items-center justify-center p-4 text-center">
                    <TransitionChild
                        as="template"
                        enter="duration-300 ease-out"
                        enter-from="opacity-0 scale-95"
                        enter-to="opacity-100 scale-100"
                        leave="duration-200 ease-in"
                        leave-from="opacity-100 scale-100"
                        leave-to="opacity-0 scale-95"
                    >
                        <DialogPanel class="w-full max-w-7xl transform overflow-hidden rounded-2xl bg-white text-left align-middle shadow-2xl transition-all">
                             <div class="max-h-[90vh] overflow-y-auto">
                                <ProductForm 
                                    :initialData="selectedProduct" 
                                    :isEdit="!!selectedProduct" 
                                    :loading="submitting"
                                    @submit="handleFormSubmit" 
                                    @cancel="closeModal" 
                                />
                             </div>
                        </DialogPanel>
                    </TransitionChild>
                </div>
            </div>
        </Dialog>
      </TransitionRoot>

      <!-- Confirmation Modal -->
      <ConfirmModal 
        :isOpen="showDeleteModal" 
        :title="$t('common.delete') + ' ' + $t('admin.products')" 
        :message="$t('media.delete_confirm', { type: $t('admin.products'), name: '' })"
        :confirmText="$t('common.delete')"
        type="danger"
        :loading="deleteLoading"
        @close="closeDeleteModal"
        @confirm="confirmDelete"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { adminProductApi, categoryApi } from '@/api/productApi';
import type { Product, Category } from '@/types/product';
import ProductForm from '../components/ProductForm.vue';
import ConfirmModal from '@/components/common/ConfirmModal.vue';
import AppImage from '@/components/common/AppImage.vue';
import { 
    Dialog, DialogPanel, TransitionChild, TransitionRoot,
    Listbox, ListboxButton, ListboxOptions, ListboxOption,
    Combobox, ComboboxInput, ComboboxButton, ComboboxOptions, ComboboxOption
} from '@headlessui/vue';
import { PencilSquareIcon, TrashIcon } from '@heroicons/vue/24/outline';

// Shared Components
import AdminPageHeader from '@/components/admin/AdminPageHeader.vue';
import AdminFilterBar from '@/components/admin/AdminFilterBar.vue';
import AdminDataTable from '@/components/admin/AdminDataTable.vue';
import StatusBadge from '@/components/admin/StatusBadge.vue';
import ActionMenu, { type ActionMenuItem } from '@/components/admin/ActionMenu.vue';

const { t, locale } = useI18n();
const products = ref<Product[]>([]);
const loading = ref(false);
const currentPage = ref(0);
const pageSize = ref(20);
const totalPages = ref(0);

// Filters
const filters = reactive({
    search: '',
    minPrice: null as number | null,
    maxPrice: null as number | null,
    status: null as string | null,
    brand: ''
});
const categories = ref<Category[]>([]);
const selectedCategory = ref<Category | null>(null);
const query = ref('');

const showModal = ref(false);
const selectedProduct = ref<Product | null>(null);

// Delete Modal State
const showDeleteModal = ref(false);
const productToDelete = ref<number | null>(null);
const deleteLoading = ref(false);

const filteredCategories = computed(() =>
  query.value === ''
    ? categories.value
    : categories.value.filter((category) =>
        getLocalizedValue(category.translations, category.name, 'name')
          .toLowerCase()
          .replace(/\s+/g, '')
          .includes(query.value.toLowerCase().replace(/\s+/g, ''))
      )
);

const fetchCategories = async () => {
    try {
        const res = await categoryApi.getCategories();
        categories.value = res.data.data.content || res.data.data;
    } catch (e) {
        console.error("Failed to load categories", e);
    }
};

const fetchProducts = async () => {
    loading.value = true;
    try {
        const res = await adminProductApi.getAllProducts({
            page: currentPage.value,
            size: pageSize.value,
            search: filters.search,
            minPrice: filters.minPrice,
            maxPrice: filters.maxPrice,
            status: filters.status,
            brand: filters.brand,
            categoryId: selectedCategory.value?.id,
            sortBy: 'id',
            sortDir: 'DESC'
        });
        products.value = res.data.data.content;
        totalPages.value = res.data.data.totalPages;
    } catch (error) {
        console.error("Failed to fetch products:", error);
    } finally {
        loading.value = false;
    }
};

const resetFilters = () => {
    filters.search = '';
    filters.minPrice = null;
    filters.maxPrice = null;
    filters.status = null;
    filters.brand = '';
    selectedCategory.value = null;
    currentPage.value = 0;
    fetchProducts();
};

const handlePageSizeChange = (size: number) => {
    pageSize.value = size;
    currentPage.value = 0;
    fetchProducts();
};

const changePage = (page: number) => {
    if (page < 0 || page >= totalPages.value) return;
    currentPage.value = page;
    fetchProducts();
    // Scroll to top of table or window
    window.scrollTo({ top: 0, behavior: 'smooth' });
};

const getLocalizedValue = (translations: any, defaultValue: string, field: string) => {
    if (!translations) return defaultValue;
    const currentLang = locale.value;
    return translations[currentLang]?.[field] || defaultValue;
};

const openCreateModal = () => {
    selectedProduct.value = null;
    showModal.value = true;
};

const openEditModal = (product: Product) => {
    selectedProduct.value = { ...product };
    showModal.value = true;
};

const closeModal = () => {
    showModal.value = false;
    selectedProduct.value = null;
};

const submitting = ref(false);

const handleFormSubmit = async (payload: any) => {
    submitting.value = true;
    try {
        const productId = selectedProduct.value?.id;
        
        if (productId) {
            await adminProductApi.updateProduct(productId, payload);
        } else {
            await adminProductApi.createProduct(payload);
        }
        closeModal();
        fetchProducts();
    } catch (error) {
        console.error("Failed to save product:", error);
        alert("Failed to save product. Check console for details.");
    } finally {
        submitting.value = false;
    }
};

const deleteProduct = (product: Product) => {
    if (product.status === 'ACTIVE' || product.status === 'ARCHIVED') {
        alert("Cannot delete ACTIVE or ARCHIVED products. Only DRAFT products can be deleted.");
        return;
    }
    productToDelete.value = product.id;
    showDeleteModal.value = true;
};

const closeDeleteModal = () => {
    showDeleteModal.value = false;
    productToDelete.value = null;
    deleteLoading.value = false;
};

const confirmDelete = async () => {
    if (!productToDelete.value) return;
    
    deleteLoading.value = true;
    try {
        await adminProductApi.deleteProduct(productToDelete.value);
        fetchProducts();
        closeDeleteModal();
    } catch (error) {
            console.error("Failed to delete product:", error);
            // Could add an error toast/state here if needed
            alert("Failed to delete product."); // Fallback for now, could be improved
    } finally {
        deleteLoading.value = false;
    }
};

const getStatusLabel = (status: string | null) => {
  if (!status) return t('admin.manage.products.all_statuses');
  switch (status) {
    case 'ACTIVE': return t('common.active');
    case 'DRAFT': return t('common.draft');
    case 'ARCHIVED': return t('common.archived');
    default: return status;
  }
};

// Simplified Action Menu Items
const getProductActions = (product: Product): ActionMenuItem[] => {
  return [
    {
      label: t('common.edit'),
      icon: PencilSquareIcon,
      onClick: () => openEditModal(product),
    },
    {
      label: t('common.delete'),
      type: 'danger',
      icon: TrashIcon,
      disabled: product.status === 'ACTIVE' || product.status === 'ARCHIVED',
      onClick: () => deleteProduct(product),
    }
  ];
};

const statusColor = (status: string | null) => {
    if (!status) return 'text-gray-700';
    switch (status) {
        case 'ACTIVE': return 'text-green-600';
        case 'DRAFT': return 'text-yellow-600';
        case 'ARCHIVED': return 'text-gray-600';
        default: return 'text-gray-700';
    }
};

onMounted(() => {
    fetchCategories();
    fetchProducts();
});

watch(selectedCategory, () => {
    currentPage.value = 0; // Reset page when category changes
});
</script>
