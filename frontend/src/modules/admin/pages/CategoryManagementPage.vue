<template>
  <div class="bg-gray-50 min-h-screen p-6 font-sans">
    <div class="w-full h-full flex flex-col">
      <!-- Shared Header -->
      <AdminPageHeader
        :title="$t('admin.manage.categories.title')"
        :subtitle="$t('admin.manage.categories.subtitle')"
        :action-label="$t('admin.manage.categories.add_new')"
        @action="openCreateModal"
      />

      <!-- Shared Filter Bar -->
      <AdminFilterBar
        :loading="loading"
        @filter="applyFilters"
        @reset="resetFilters"
      >
        <!-- Search -->
        <div class="lg:col-span-2">
          <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('common.search') }}</label>
          <div class="relative">
            <input v-model="filters.search" @keyup.enter="applyFilters" type="text" :placeholder="$t('admin.manage.categories.search_placeholder')" class="w-full pl-9 pr-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
            </div>
          </div>
        </div>

        <!-- Status Filter -->
        <div class="lg:col-span-1">
          <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('common.status') }}</label>
          <Listbox v-model="filters.status">
            <div class="relative">
              <ListboxButton class="relative w-full cursor-pointer rounded-xl bg-white py-2 pl-3 pr-10 text-left border border-gray-300 shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                <span class="block truncate font-medium" :class="!filters.status ? 'text-gray-700' : (filters.status === 'ACTIVE' ? 'text-green-600' : 'text-gray-600')">
                  {{ getStatusLabel(filters.status) }}
                </span>
                <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                  <svg class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                </span>
              </ListboxButton>
              <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
                <ListboxOptions class="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm p-1">
                  <ListboxOption :value="null" as="template" v-slot="{ active, selected }">
                    <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4 rounded-lg my-0.5']">
                      <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">{{ $t('admin.manage.products.all_statuses') }}</span>
                    </li>
                  </ListboxOption>
                  <ListboxOption v-for="status in ['ACTIVE', 'DRAFT', 'ARCHIVED']" :key="status" :value="status" as="template" v-slot="{ active, selected }">
                    <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4 rounded-lg my-0.5']">
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
      </AdminFilterBar>

      <!-- Shared Data Table -->
      <!-- Supports client-side pagination for categories -->
      <AdminDataTable
        :loading="loading"
        :is-empty="paginatedCategories.length === 0"
        :empty-title="$t('admin.manage.categories.no_results')"
        :empty-subtitle="$t('admin.manage.categories.no_results_desc')"
        :current-page="currentPage"
        :total-pages="totalPages"
        :page-size="pageSize"
        :column-count="5"
        @page-change="changePage"
        @page-size-change="handlePageSizeChange"
        @clear-filters="resetFilters"
      >
        <template #header>
          <tr>
            <th class="pl-4 pr-6 py-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider w-[40%]">{{ $t('product.category') }}</th>
            <th class="px-6 py-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider w-[30%]">{{ $t('common.description') }}</th>
            <th class="px-6 py-4 text-center text-xs font-bold text-gray-500 uppercase tracking-wider w-32 whitespace-nowrap">{{ $t('admin.forms.category.display_order') }}</th>
            <th class="px-6 py-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider w-32">{{ $t('common.status') }}</th>
            <th scope="col" class="relative px-6 py-3 w-16">
              <span class="sr-only">{{ $t('common.actions') }}</span>
            </th>
          </tr>
        </template>

        <template #skeleton>
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
        </template>

        <template #default>
          <tr v-for="(item, index) in paginatedCategories" :key="item.category.id" class="group hover:bg-gray-50 transition-colors">
            <td class="pl-4 pr-6 py-4 whitespace-nowrap">
              <div class="flex items-center relative">
                <!-- Indentation Spacer -->
                <div :style="{ width: `${item.level * 32}px` }" class="flex-shrink-0 relative h-full">
                  <!-- Vertical line for hierarchy visual if needed -->
                   <div v-if="item.level > 0" class="absolute top-1/2 -left-[16px] w-[16px] h-px bg-gray-200"></div>
                  <div v-if="item.level > 0" class="absolute -top-[100%] -left-[16px] w-px h-[150%] bg-gray-200"></div> 
                </div>

                <!-- Expand/Collapse or Spacer -->
                <div class="w-6 h-6 mr-2 flex items-center justify-center flex-shrink-0 relative z-10">
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
              <StatusBadge :status="item.category.status" />
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
              <ActionMenu 
                :items="getCategoryActions(item.category)" 
                :header="getLocalizedValue(item.category.translations, item.category.name, 'name')"
              />
            </td>
          </tr>
        </template>
      </AdminDataTable>

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
import { ref, reactive, computed, onMounted, watch } from 'vue'; // Added watch
import { useI18n } from 'vue-i18n';
import { adminCategoryApi } from '@/api/adminCategoryApi';
import type { Category } from '@/types/product';
import CategoryForm from '@/modules/admin/components/CategoryForm.vue';
import AppImage from '@/components/common/AppImage.vue';
import { 
  Dialog, DialogPanel, TransitionChild, TransitionRoot,
  Listbox, ListboxButton, ListboxOptions, ListboxOption
} from '@headlessui/vue';
import { PencilSquareIcon, TrashIcon } from '@heroicons/vue/24/outline';

// Shared Components
import AdminPageHeader from '@/components/admin/AdminPageHeader.vue';
import AdminFilterBar from '@/components/admin/AdminFilterBar.vue';
import AdminDataTable from '@/components/admin/AdminDataTable.vue';
import StatusBadge from '@/components/admin/StatusBadge.vue';
import ActionMenu, { type ActionMenuItem } from '@/components/admin/ActionMenu.vue';

// Types for List Item (Simplified from Tree)
interface CategoryListItem {
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

// Applied Filters (for API)
const appliedFilters = reactive({
  search: '',
  status: null as string | null
});

// Pagination State (Server-side)
const currentPage = ref(0);
const pageSize = ref(20);
const totalElements = ref(0);
const totalPages = ref(0);

const showModal = ref(false);
const editingCategory = ref<Category | null>(null);
// expandedIds removed as we are doing flat list for server pagination

// Computed: Display List (Transform to flat structure wrapping)
const paginatedCategories = computed(() => {
  return categories.value.map(c => ({
    category: c,
    level: 0, // Flat list for now
    hasChildren: false // No children in flat view
  }));
});

// Methods
const fetchCategories = async () => {
  loading.value = true;
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      search: appliedFilters.search || undefined, // Send undefined if empty
      status: appliedFilters.status || undefined
    };
    
    const res = await adminCategoryApi.getAllCategories(params);
    categories.value = res.data.data.content;
    totalPages.value = res.data.data.totalPages;
    totalElements.value = res.data.data.totalElements;
  } catch (error) {
    console.error('Failed to fetch categories', error);
    categories.value = [];
    totalPages.value = 0;
  } finally {
    loading.value = false;
  }
};

const applyFilters = () => {
  appliedFilters.search = filters.search;
  appliedFilters.status = filters.status;
  currentPage.value = 0; // Reset page on filter
  fetchCategories();
};

const resetFilters = () => {
  filters.search = '';
  filters.status = null;
  applyFilters();
};

const changePage = (page: number) => {
  currentPage.value = page;
  window.scrollTo({ top: 0, behavior: 'smooth' });
  fetchCategories();
};

const handlePageSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 0;
  fetchCategories();
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
    } catch (error: any) {
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

const getLocalizedValue = (translations: any, defaultValue: string, field: string) => {
  if (!translations) return defaultValue;
  const currentLang = locale.value;
  return translations[currentLang]?.[field] || defaultValue;
};

const getCategoryActions = (category: Category): ActionMenuItem[] => {
  return [
    {
      label: t('common.edit'),
      icon: PencilSquareIcon,
      onClick: () => openEditModal(category),
    },
    {
      label: t('common.delete'),
      type: 'danger',
      icon: TrashIcon,
      disabled: category.status !== 'DRAFT',
      onClick: () => confirmDelete(category),
    }
  ];
};

// Lifecycle
onMounted(() => {
  fetchCategories();
});
</script>
