<template>
  <div class="bg-gray-50 min-h-screen p-6 font-sans">
    <div class="w-full h-full flex flex-col">
      <!-- Header -->
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
        <div>
          <h2 class="text-2xl font-bold text-gray-900 tracking-tight">{{ $t('admin.manage.categories.title') }}</h2>
          <p class="text-sm text-gray-500 mt-1">{{ $t('admin.manage.categories.subtitle') }}</p>
        </div>
        <button @click="openCreateModal" class="px-5 py-2.5 rounded-xl bg-gray-900 text-white hover:bg-black font-medium transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 flex items-center justify-center text-sm">
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
            {{ $t('admin.manage.categories.add_new') }}
        </button>
      </div>

      <!-- Filters Bar -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6">
          <div class="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
                <!-- Search -->
                <div class="md:col-span-2">
                    <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('common.search') }}</label>
                    <div class="relative">
                        <input v-model="filters.search" @keyup.enter="applyFilters" type="text" :placeholder="$t('admin.manage.categories.search_placeholder')" class="w-full pl-9 pr-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <svg class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
                        </div>
                    </div>
                </div>

                <!-- Status Filter -->
                <div class="md:col-span-1">
                        <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('common.status') }}</label>
                        <Listbox v-model="filters.status">
                        <div class="relative">
                            <ListboxButton class="relative w-full cursor-pointer rounded-xl bg-white py-2 pl-3 pr-10 text-left border border-gray-300 shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                                <span class="block truncate font-medium" :class="statusColor(filters.status)">{{ getStatusLabel(filters.status) }}</span>
                                <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                                    <svg class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                                </span>
                            </ListboxButton>
                            <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
                                <ListboxOptions class="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                                    <ListboxOption :value="null" as="template" v-slot="{ active, selected }">
                                        <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4']">
                                            <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">{{ $t('admin.manage.products.all_statuses') }}</span>
                                        </li>
                                    </ListboxOption>
                                    <ListboxOption v-for="status in ['ACTIVE', 'DRAFT', 'ARCHIVED']" :key="status" :value="status" as="template" v-slot="{ active, selected }">
                                        <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4']">
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


                <!-- Buttons -->
                <div class="md:col-span-1 flex justify-start gap-2">
                    <button @click="applyFilters" class="px-4 py-2 bg-indigo-600 text-white rounded-xl font-medium shadow-sm hover:bg-indigo-700 transition-all flex items-center justify-center text-sm h-[38px]" :disabled="loading">
                        <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                        </svg>
                        {{ $t('admin.manage.products.filter') }}
                    </button>
                     <button @click="resetFilters" class="px-4 py-2 text-indigo-600 hover:text-indigo-800 font-medium transition-all text-sm h-[38px] flex items-center">
                        {{ $t('admin.manage.products.reset') }}
                    </button>
                </div>
          </div>
      </div>

      <!-- Table -->
      <div class="flex-1 flex flex-col min-h-0 bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden relative">
        <!-- Loading Overlay -->
        <Transition
            enter-active-class="transition ease-out duration-200"
            enter-from-class="opacity-0"
            enter-to-class="opacity-100"
            leave-active-class="transition ease-in duration-150"
            leave-from-class="opacity-100"
            leave-to-class="opacity-0"
        >
            <div v-if="loading" class="absolute inset-0 z-10 bg-white/60 backdrop-blur-sm flex items-center justify-center">
                <div class="flex flex-col items-center">
                    <svg class="animate-spin h-8 w-8 text-indigo-600 mb-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    <span class="text-sm font-medium text-indigo-600">{{ $t('admin.manage.categories.loading') }}</span>
                </div>
            </div>
        </Transition>

        <div class="flex-1 overflow-auto relative custom-scrollbar">
            <table class="min-w-full divide-y divide-gray-100">
                <thead class="bg-gray-50/80 backdrop-blur-md sticky top-0 z-20">
                    <tr>
                        <th class="pl-4 pr-6 py-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider w-[40%]">{{ $t('product.category') }}</th>
                        <th class="px-6 py-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider w-[30%]">{{ $t('common.description') }}</th>
                         <th class="px-6 py-4 text-center text-xs font-bold text-gray-500 uppercase tracking-wider w-32 whitespace-nowrap">{{ $t('admin.forms.category.display_order') }}</th>
                        <th class="px-6 py-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider w-32">{{ $t('common.status') }}</th>
                         <th scope="col" class="relative px-6 py-3 w-16">
                            <span class="sr-only">{{ $t('common.actions') }}</span>
                        </th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-50">
                    <tr v-if="loading && categories.length === 0" v-for="i in 5" :key="i" class="animate-pulse">
                         <td class="pl-4 pr-6 py-4">
                            <div class="flex items-center gap-3">
                                <div class="h-10 w-10 bg-gray-100 rounded-lg"></div>
                                <div class="space-y-2">
                                    <div class="h-4 w-32 bg-gray-100 rounded"></div>
                                    <div class="h-3 w-20 bg-gray-100 rounded"></div>
                                </div>
                            </div>
                        </td>
                        <td class="px-6 py-4"><div class="h-4 w-48 bg-gray-100 rounded"></div></td>
                        <td class="px-6 py-4"><div class="h-4 w-12 bg-gray-100 rounded mx-auto"></div></td>
                        <td class="px-6 py-4"><div class="h-6 w-16 bg-gray-100 rounded-full"></div></td>
                        <td class="px-6 py-4"></td>
                    </tr>
                    <tr v-else-if="flattenedCategories.length === 0">
                        <td colspan="5" class="px-6 py-24 text-center">
                             <div class="flex flex-col items-center justify-center">
                                <div class="w-24 h-24 bg-gray-50 rounded-full flex items-center justify-center mb-4">
                                    <svg class="w-10 h-10 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" /></svg>
                                </div>
                                <h3 class="text-lg font-bold text-gray-900">{{ $t('admin.manage.categories.no_results') }}</h3>
                                <p class="text-sm text-gray-500 mt-1">{{ $t('admin.manage.categories.no_results_desc') }}</p>
                                <button @click="resetFilters" class="mt-4 text-indigo-600 hover:text-indigo-800 font-medium text-sm">{{ $t('admin.manage.products.reset') }}</button>
                            </div>
                        </td>
                    </tr>
                    <tr v-else v-for="(item, index) in flattenedCategories" :key="item.category.id" class="group hover:bg-gray-50 transition-colors">
                        <td class="pl-4 pr-6 py-4 whitespace-nowrap">
                            <div class="flex items-center relative">
                                <!-- Indentation Spacer -->
                                <div :style="{ width: `${item.level * 32}px` }" class="flex-shrink-0 relative h-full">
                                    <!-- Optional: Visual tree lines could go here if needed, but simple whitespace is cleaner for now -->
                                     <div v-if="item.level > 0" class="absolute top-1/2 -left-[16px] w-[16px] h-px bg-gray-200"></div>
                                     <div v-if="item.level > 0" class="absolute -top-1/2 -left-[16px] w-px h-full bg-gray-200"></div> 
                                </div>

                                <!-- Expand/Collapse or Spacer -->
                                <div class="w-6 h-6 mr-2 flex items-center justify-center flex-shrink-0">
                                    <button v-if="item.hasChildren" @click.stop="toggleExpand(item.category.id)" class="p-0.5 rounded-md hover:bg-gray-200 text-gray-400 hover:text-indigo-600 transition-colors focus:outline-none bg-gray-50 border border-gray-200">
                                        <svg class="w-3 h-3 transition-transform duration-200" :class="{ 'transform rotate-90': expandedIds.has(item.category.id) }" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" /></svg>
                                    </button>
                                     <div v-else class="w-2 h-2 rounded-full bg-gray-200 mx-auto"></div>
                                </div>

                                <!-- Image & Name Combined -->
                                <div class="flex items-center gap-3">
                                    <div class="h-10 w-10 flex-shrink-0 relative group-hover:scale-105 transition-transform duration-300">
                                        <AppImage 
                                            :src="item.category.imageUrl" 
                                            :alt="item.category.name"
                                            class="h-full w-full rounded-lg shadow-sm border border-gray-100 bg-white object-cover"
                                        />
                                    </div>
                                    <div class="flex flex-col">
                                        <span class="text-sm font-bold text-gray-900 group-hover:text-indigo-600 transition-colors">{{ getLocalizedValue(item.category.translations, item.category.name, 'name') }}</span>
                                        <span class="text-xs text-gray-400 font-mono">{{ item.category.slug }}</span>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td class="px-6 py-4">
                            <div class="text-sm text-gray-500 line-clamp-2 max-w-xs">{{ getLocalizedValue(item.category.translations, item.category.description, 'description') || '-' }}</div>
                        </td>
                         <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600 font-mono text-center">
                            {{ item.category.displayOrder }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium border"
                                :class="{
                                'bg-green-50 text-green-700 border-green-200': item.category.status === 'ACTIVE',
                                'bg-yellow-50 text-yellow-700 border-yellow-200': item.category.status === 'DRAFT',
                                'bg-gray-50 text-gray-600 border-gray-200': item.category.status === 'ARCHIVED'
                                }">
                                <span class="h-1.5 w-1.5 rounded-full mr-1.5" :class="{
                                    'bg-green-500': item.category.status === 'ACTIVE',
                                    'bg-yellow-500': item.category.status === 'DRAFT',
                                    'bg-gray-400': item.category.status === 'ARCHIVED'
                                }"></span>
                                {{ getStatusLabel(item.category.status) }}
                            </span>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                            <Menu as="div" class="relative inline-block text-left">
                                <div>
                                    <MenuButton class="flex items-center justify-center h-8 w-8 rounded-full text-gray-400 hover:text-indigo-600 hover:bg-indigo-50 focus:outline-none transition-all">
                                        <span class="sr-only">Open options</span>
                                        <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                                            <path d="M10 3a1.5 1.5 0 110 3 1.5 1.5 0 010-3zM10 8.5a1.5 1.5 0 110 3 1.5 1.5 0 010-3zM11.5 15.5a1.5 1.5 0 10-3 0 1.5 1.5 0 003 0z" />
                                        </svg>
                                    </MenuButton>
                                    <transition enter-active-class="transition ease-out duration-100" enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100" leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100" leave-to-class="transform opacity-0 scale-95">
                                        <MenuItems 
                                            class="absolute right-0 w-48 rounded-xl shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-50 divide-y divide-gray-100"
                                            :class="[
                                                (Number(index) >= flattenedCategories.length - 3 && flattenedCategories.length > 4) ? 'bottom-full mb-2 origin-bottom-right' : 'mt-2 origin-top-right'
                                            ]"
                                        >
                                            <div class="py-1">
                                                    <button @click="openEditModal(item.category)" class="group flex items-center px-4 py-3 text-sm w-full text-left text-gray-700 hover:bg-indigo-50 hover:text-indigo-700 transition-colors">
                                                        <svg class="mr-3 h-4 w-4 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
                                                        {{ $t('admin.forms.category.edit_title') }}
                                                    </button>
                                            </div>
                                            <div class="py-1">
                                                <MenuItem v-slot="{ active }">
                                                    <button 
                                                        @click="item.category.status === 'DRAFT' ? confirmDelete(item.category) : null" 
                                                        :disabled="item.category.status !== 'DRAFT'"
                                                        :class="[
                                                            active && item.category.status === 'DRAFT' ? 'bg-red-50' : '',
                                                            item.category.status !== 'DRAFT' ? 'opacity-50 cursor-not-allowed' : 'group hover:bg-red-50',
                                                            'flex items-center px-4 py-3 text-sm w-full text-left text-red-600 transition-colors'
                                                        ]"
                                                    >
                                                        <svg class="mr-3 h-4 w-4 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                                                        {{ $t('common.delete') }} {{ $t('product.category') }}
                                                    </button>
                                                </MenuItem>
                                            </div>
                                        </MenuItems>
                                    </transition>
                                </div>
                            </Menu>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
      </div>

        <!-- Create/Edit Modal -->
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
                            <DialogPanel class="w-full max-w-5xl transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                                <CategoryForm 
                                    :initial-data="editingCategory"
                                    :categories="categories"
                                    :loading="submitting"
                                    @submit="handleSubmit"
                                    @cancel="closeModal"
                                />
                            </DialogPanel>
                        </TransitionChild>
                    </div>
                </div>
            </Dialog>
        </TransitionRoot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { adminCategoryApi } from '@/api/adminCategoryApi';
import type { Category } from '@/types/product';
import CategoryForm from '@/modules/admin/components/CategoryForm.vue';
import AppImage from '@/components/common/AppImage.vue';
import { 
    Dialog, DialogPanel, TransitionChild, TransitionRoot,
    Menu, MenuButton, MenuItems, MenuItem,
    Listbox, ListboxButton, ListboxOptions, ListboxOption
} from '@headlessui/vue';

// Types for Tree
interface FlattenedCategory {
    category: Category;
    level: number;
    hasChildren: boolean;
}

// State
const { t, locale } = useI18n();
const categories = ref<Category[]>([]);
const loading = ref(true);
const submitting = ref(false);

// Filters State
const filters = reactive({
    search: '',
    status: null as string | null
});

// Applied Filters (Snapshot for filtering)
const appliedFilters = reactive({
    search: '',
    status: null as string | null
});

const showModal = ref(false);
const editingCategory = ref<Category | null>(null);
const expandedIds = ref<Set<number>>(new Set());

// Tree Logic
const buildTree = (cats: Category[], parentId: number | null = null): any[] => {
    return cats
        .filter(c => c.parentId === parentId)
        .map(c => ({
            ...c,
            children: buildTree(cats, c.id)
        }))
        .sort((a, b) => a.displayOrder - b.displayOrder);
};

const flattenTree = (nodes: any[], level: number = 0, result: FlattenedCategory[] = []) => {
    for (const node of nodes) {
        const hasChildren = node.children && node.children.length > 0;
        
        result.push({
            category: node,
            level,
            hasChildren
        });

        if (hasChildren && expandedIds.value.has(node.id)) {
            flattenTree(node.children, level + 1, result);
        }
    }
    return result;
};

// Computed
const flattenedCategories = computed(() => {
    // 1. Basic Filtering first
    let filtered = categories.value;
    
    // Use APPLIED filters, not the live inputs
    const isFiltering = appliedFilters.search || appliedFilters.status;

    if (isFiltering) {
        if (appliedFilters.search) {
            const query = appliedFilters.search.toLowerCase();
            filtered = filtered.filter(cat => 
                cat.name.toLowerCase().includes(query) || 
                cat.slug.toLowerCase().includes(query)
            );
        }

        if (appliedFilters.status) {
            filtered = filtered.filter(cat => cat.status === appliedFilters.status);
        }
        
        // Return as flat list with level 0
        return filtered.map(c => ({ category: c, level: 0, hasChildren: false }));
    } else {
        // Build Tree
        const tree = buildTree(categories.value, null);
        const result: FlattenedCategory[] = [];
        flattenTree(tree, 0, result);
        return result;
    }
});

// Methods
const toggleExpand = (id: number) => {
    if (expandedIds.value.has(id)) {
        expandedIds.value.delete(id);
    } else {
        expandedIds.value.add(id);
    }
};

const fetchCategories = async () => {
    loading.value = true;
    try {
        const res = await adminCategoryApi.getAllCategories();
        categories.value = res.data.data;
    } catch (error) {
        console.error('Failed to fetch categories', error);
    } finally {
        loading.value = false;
    }
};

const applyFilters = () => {
    appliedFilters.search = filters.search;
    appliedFilters.status = filters.status;
};

const resetFilters = () => {
    filters.search = '';
    filters.status = null;
    applyFilters();
};

const openCreateModal = () => {
    editingCategory.value = null;
    showModal.value = true;
};

const openEditModal = (category: Category) => {
    editingCategory.value = category;
    showModal.value = true;
};

const closeModal = () => {
    showModal.value = false;
    editingCategory.value = null;
};

const handleSubmit = async (formData: any) => {
    submitting.value = true;
    try {
        if (editingCategory.value) {
            await adminCategoryApi.updateCategory(editingCategory.value.id, formData);
        } else {
            await adminCategoryApi.createCategory(formData);
        }
        await fetchCategories();
        closeModal();
    } catch (error) {
        console.error('Failed to save category', error);
        alert(t('common.error'));
    } finally {
        submitting.value = false;
    }
};

const confirmDelete = async (category: Category) => {
    if (confirm(t('media.delete_confirm', { type: t('product.category'), name: category.name }))) {
        try {
            await adminCategoryApi.deleteCategory(category.id);
            await fetchCategories();
        } catch (error) {
            console.error('Failed to delete category', error);
            alert(error.response?.data?.message || t('common.error'));
        }
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

const statusColor = (status: string | null) => {
    if (!status) return 'text-gray-700';
    return status === 'ACTIVE' ? 'text-green-600' : (status === 'DRAFT' ? 'text-yellow-600' : 'text-gray-600');
};

const getLocalizedValue = (translations: any, defaultValue: string, field: string) => {
    if (!translations) return defaultValue;
    const currentLang = locale.value;
    return translations[currentLang]?.[field] || defaultValue;
};

// Lifecycle
onMounted(() => {
    fetchCategories();
});
</script>
