<template>
  <div class="bg-gray-50 min-h-screen p-6 font-sans">
    <div class="w-full h-full flex flex-col">
      <!-- Shared Header -->
      <AdminPageHeader
        :title="$t('admin.i18n.languages.title')"
        :subtitle="$t('admin.i18n.languages.subtitle')"
        :action-label="$t('admin.i18n.languages.add_new')"
        @action="openCreateModal"
      />

      <!-- Shared Filter Bar -->
      <AdminFilterBar
        :loading="loading"
        @filter="applyFilters"
        @reset="resetFilters"
      >
        <!-- Search -->
        <div class="lg:col-span-1">
          <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">{{ $t('common.search') }}</label>
          <div class="relative">
            <input v-model="filters.search" @keyup.enter="applyFilters" type="text" :placeholder="$t('admin.i18n.languages.search_placeholder')" class="w-full pl-9 pr-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
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
                <span class="block truncate font-medium" :class="filters.status === null ? 'text-gray-700' : (filters.status ? 'text-green-600' : 'text-gray-500')">
                  {{ filters.status === null ? $t('common.all') : (filters.status ? $t('common.active') : $t('common.inactive')) }}
                </span>
                <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                  <svg class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                </span>
              </ListboxButton>
              <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
                <ListboxOptions class="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                  <ListboxOption :value="null" as="template" v-slot="{ active, selected }">
                    <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4']">
                      <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">{{ $t('common.all') }}</span>
                    </li>
                  </ListboxOption>
                  <ListboxOption :value="true" as="template" v-slot="{ active, selected }">
                    <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4']">
                      <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate text-green-600']">{{ $t('common.active') }}</span>
                      <span v-if="selected" class="absolute inset-y-0 left-0 flex items-center pl-3 text-indigo-600">
                        <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" /></svg>
                      </span>
                    </li>
                  </ListboxOption>
                  <ListboxOption :value="false" as="template" v-slot="{ active, selected }">
                    <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4']">
                      <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate text-gray-500']">{{ $t('common.inactive') }}</span>
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
      <AdminDataTable
        :loading="loading"
        :is-empty="languages.length === 0"
        :empty-title="$t('admin.manage.products.no_results')"
        :empty-subtitle="$t('admin.manage.products.no_results_desc')"
        :current-page="currentPage"
        :total-pages="totalPages"
        :page-size="pageSize"
        :column-count="7"
        @page-change="changePage"
        @page-size-change="handlePageSizeChange"
        @clear-filters="resetFilters"
      >
        <template #header>
          <tr>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.i18n.languages.columns.flag') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.i18n.languages.columns.name') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.i18n.languages.columns.code') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.i18n.languages.columns.status') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.i18n.languages.columns.default') }}</th>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{{ $t('admin.i18n.languages.columns.order') }}</th>
            <th scope="col" class="relative px-6 py-3">
              <span class="sr-only">{{ $t('common.actions') }}</span>
            </th>
          </tr>
        </template>
        
        <template #skeleton>
          <td class="px-6 py-4 whitespace-nowrap"><div class="h-7 w-10 bg-gray-100 rounded"></div></td>
          <td class="px-6 py-4 whitespace-nowrap"><div class="h-4 w-32 bg-gray-100 rounded"></div></td>
          <td class="px-6 py-4 whitespace-nowrap"><div class="h-5 w-10 bg-gray-100 rounded"></div></td>
          <td class="px-6 py-4 whitespace-nowrap"><div class="h-6 w-16 bg-gray-100 rounded-full"></div></td>
          <td class="px-6 py-4 whitespace-nowrap"><div class="h-6 w-20 bg-gray-100 rounded"></div></td>
          <td class="px-6 py-4 whitespace-nowrap"><div class="h-4 w-8 bg-gray-100 rounded"></div></td>
          <td class="px-6 py-4 whitespace-nowrap"></td>
        </template>

        <template #default>
          <tr v-for="lang in languages" :key="lang.code" class="group hover:bg-gray-50 transition-colors">
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="w-10 h-7 overflow-hidden rounded shadow-sm border border-gray-100 group-hover:scale-105 transition-transform duration-300">
                <img :src="lang.flagIcon" :alt="lang.name" class="w-full h-full object-cover" />
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <span class="text-sm font-bold text-gray-900 group-hover:text-indigo-600 transition-colors">{{ lang.name }}</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-gray-100 text-gray-700 font-mono border border-gray-200">
                {{ lang.code }}
              </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium border"
                    :class="{
                      'bg-green-50 text-green-700 border-green-200': lang.isActive,
                      'bg-gray-50 text-gray-600 border-gray-200': !lang.isActive
                    }">
                <span class="h-1.5 w-1.5 rounded-full mr-1.5" :class="{
                  'bg-green-500': lang.isActive,
                  'bg-gray-400': !lang.isActive
                }"></span>
                {{ lang.isActive ? $t('common.active') : $t('common.inactive') }}
              </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div v-if="lang.isDefault" class="flex items-center text-xs text-green-600 font-bold bg-green-50 px-2.5 py-1 rounded-lg border border-green-100 w-fit">
                <svg class="w-3.5 h-3.5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" /></svg>
                {{ $t('common.default') }}
              </div>
              <button v-else @click="makeDefault(lang)" class="text-xs text-indigo-600 hover:text-indigo-800 font-medium hover:underline opacity-0 group-hover:opacity-100 transition-opacity">
                {{ $t('admin.i18n.languages.make_default') }}
              </button>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <span class="text-sm text-gray-500 font-mono font-medium bg-gray-50 px-2 py-1 rounded">{{ lang.displayOrder }}</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
              <ActionMenu 
                :items="getLanguageActions(lang)" 
                :header="lang.name"
              />
            </td>
          </tr>
        </template>
      </AdminDataTable>

      <!-- Modal for Language Form -->
      <TransitionRoot appear :show="showModal" as="template">
        <Dialog as="div" @close="closeModal" class="relative z-50">
          <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0" enter-to="opacity-100" leave="duration-200 ease-in" leave-from="opacity-100" leave-to="opacity-0">
            <div class="fixed inset-0 bg-black/40 backdrop-blur-sm" />
          </TransitionChild>

          <div class="fixed inset-0 overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4">
              <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0 scale-95" enter-to="opacity-100 scale-100" leave="duration-200 ease-in" leave-from="opacity-100 scale-100" leave-to="opacity-0 scale-95">
                <DialogPanel class="w-full max-w-2xl transform overflow-hidden rounded-2xl bg-white p-8 shadow-2xl transition-all text-left align-middle border border-gray-100">
                  
                  <!-- Header -->
                  <DialogTitle as="h3" class="text-xl font-bold leading-6 text-gray-900 mb-8 flex justify-between items-center">
                    <div class="flex items-center">
                      <span class="bg-indigo-100 text-indigo-600 p-2 rounded-lg mr-3">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5h12M9 3v2m1.048 9.5A18.022 18.022 0 016.412 9m6.088 9h7M11 21l5-10 5 10M12.751 5C11.783 10.77 8.07 15.61 3 18.129" /></svg>
                      </span>
                      {{ isEdit ? $t('admin.i18n.languages.edit_title') : $t('admin.i18n.languages.add_title') }}
                    </div>
                    <button @click="closeModal" class="text-gray-400 hover:text-gray-500 p-1 rounded-full hover:bg-gray-100 transition-colors">
                      <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                    </button>
                  </DialogTitle>
                  
                  <form @submit.prevent="handleSave" class="space-y-6">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">{{ $t('admin.i18n.languages.fields.code') }}</label>
                        <input v-model="form.code" :disabled="isEdit" type="text" :placeholder="$t('admin.i18n.languages.fields.code_placeholder')" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400 bg-white disabled:bg-gray-50 disabled:text-gray-500 font-mono" required />
                        <p class="text-xs text-gray-500 mt-1">ISO 639-1 code (e.g. en, vi, fr)</p>
                      </div>
                      <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">{{ $t('admin.i18n.languages.fields.order') }}</label>
                        <input v-model.number="form.displayOrder" type="number" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400 bg-white" />
                      </div>
                    </div>
                    
                    <div>
                      <label class="block text-sm font-semibold text-gray-700 mb-2">{{ $t('admin.i18n.languages.fields.name') }}</label>
                      <input v-model="form.name" type="text" :placeholder="$t('admin.i18n.languages.fields.name_placeholder')" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400 bg-white" required />
                    </div>

                    <div>
                      <label class="block text-sm font-semibold text-gray-700 mb-2">{{ $t('admin.i18n.languages.fields.flag') }}</label>
                      <div class="flex gap-4">
                        <div class="flex-grow">
                          <input v-model="form.flagIcon" type="text" :placeholder="$t('admin.i18n.languages.fields.flag_placeholder')" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400 bg-white" />
                        </div>
                        <div class="w-14 h-12 rounded-xl border border-gray-200 bg-gray-50 flex items-center justify-center overflow-hidden shrink-0 shadow-sm">
                          <img v-if="form.flagIcon" :src="form.flagIcon" class="w-full h-full object-cover" />
                          <span v-else class="text-xs text-gray-400 font-medium">Flag</span>
                        </div>
                      </div>
                      <p class="text-xs text-gray-500 mt-1">URL to flag image (e.g. https://flagcdn.com/w80/us.png)</p>
                    </div>

                    <!-- Footer Buttons -->
                    <div class="mt-8 flex justify-end space-x-3 pt-6 border-t border-gray-100">
                      <button type="button" @click="closeModal" class="px-6 py-2.5 rounded-xl text-gray-700 bg-white border border-gray-300 hover:bg-gray-50 font-medium transition-all shadow-sm">
                        {{ $t('common.cancel') }}
                      </button>
                      <button type="submit" class="px-6 py-2.5 rounded-xl bg-gray-900 text-white font-medium hover:bg-black shadow-lg hover:shadow-xl focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-900 transform hover:-translate-y-0.5 transition-all" :disabled="loading">
                        <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white inline-block" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                        {{ loading ? $t('common.saving') : $t('common.save_changes') }}
                      </button>
                    </div>
                  </form>
                </DialogPanel>
              </TransitionChild>
            </div>
          </div>
        </Dialog>
      </TransitionRoot>

      <!-- Confirm Delete Modal -->
      <ConfirmModal 
        :isOpen="showDeleteModal" 
        :title="$t('common.delete') + ' ' + $t('admin.i18n.languages.title')" 
        :message="$t('admin.i18n.languages.delete_confirm', { name: languageToDelete?.name })"
        :confirmText="$t('common.delete')"
        type="danger"
        :loading="deleteLoading"
        @close="closeDeleteModal"
        @confirm="handleConfirmDelete"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { i18nApi } from '@/api/i18nApi';
import ConfirmModal from '@/components/common/ConfirmModal.vue';
import { 
  Dialog, DialogPanel, DialogTitle, TransitionRoot, TransitionChild,
  Listbox, ListboxButton, ListboxOptions, ListboxOption
} from '@headlessui/vue';
import { PencilSquareIcon, TrashIcon, PowerIcon, CheckCircleIcon } from '@heroicons/vue/24/outline'; // Install @heroicons/vue if missing

// Shared Components
import AdminPageHeader from '@/components/admin/AdminPageHeader.vue';
import AdminFilterBar from '@/components/admin/AdminFilterBar.vue';
import AdminDataTable from '@/components/admin/AdminDataTable.vue';
import ActionMenu, { type ActionMenuItem } from '@/components/admin/ActionMenu.vue';

const { t } = useI18n();
const languages = ref<any[]>([]);
const loading = ref(false);
const showModal = ref(false);
const isEdit = ref(false);

// Filters
const filters = reactive({
  search: '',
  status: null as boolean | null
});

// Delete Modal State
const showDeleteModal = ref(false);
const languageToDelete = ref<any | null>(null);
const deleteLoading = ref(false);

const form = reactive({
  code: '',
  name: '',
  flagIcon: '',
  isActive: true,
  displayOrder: 0
});

// Pagination State
const currentPage = ref(0);
const pageSize = ref(20);
const totalPages = ref(0);
const totalElements = ref(0);

const applyFilters = () => {
  currentPage.value = 0;
  fetchLanguages();
};

const resetFilters = () => {
  filters.search = '';
  filters.status = null;
  applyFilters();
};

const changePage = (page: number) => {
  currentPage.value = page;
  window.scrollTo({ top: 0, behavior: 'smooth' });
  fetchLanguages();
};

const handlePageSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 0;
  fetchLanguages();
};

async function fetchLanguages() {
  loading.value = true;
  try {
    const params: any = {
      page: currentPage.value,
      size: pageSize.value,
    };
    if (filters.search) {
      params.search = filters.search;
    }
    if (filters.status !== null) {
      params.status = filters.status;
    }

    const res = await i18nApi.admin.getAllLanguages(params);
    
    if (res.data && Array.isArray(res.data.content)) {
      languages.value = res.data.content;
      totalPages.value = res.data.totalPages;
      totalElements.value = res.data.totalElements;
    } else if (Array.isArray(res.data)) {
        languages.value = res.data;
        totalPages.value = 1;
        totalElements.value = res.data.length;
    } else {
       languages.value = [];
       totalPages.value = 0;
       totalElements.value = 0;
    }

  } catch (error) {
    console.error('Failed to fetch languages', error);
  } finally {
    loading.value = false;
  }
}

function openCreateModal() {
  isEdit.value = false;
  form.code = '';
  form.name = '';
  form.flagIcon = '';
  form.isActive = true;
  form.displayOrder = languages.value.length + 1;
  showModal.value = true;
}

function openEditModal(lang: any) {
  isEdit.value = true;
  form.code = lang.code;
  form.name = lang.name;
  form.flagIcon = lang.flagIcon;
  form.isActive = lang.isActive;
  form.displayOrder = lang.displayOrder;
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}

async function handleSave() {
  loading.value = true;
  try {
    if (isEdit.value) {
      await i18nApi.admin.updateLanguage(form.code, { ...form });
    } else {
      await i18nApi.admin.createLanguage({ ...form });
    }
    await fetchLanguages();
    closeModal();
  } catch (error) {
    console.error('Failed to save language', error);
  } finally {
    loading.value = false;
  }
}

async function toggleActive(lang: any) {
  try {
    await i18nApi.admin.updateLanguage(lang.code, { ...lang, isActive: !lang.isActive });
    await fetchLanguages();
  } catch (error) {
    console.error('Failed to toggle language status', error);
  }
}

async function makeDefault(lang: any) {
  try {
    await i18nApi.admin.setDefault(lang.code);
    await fetchLanguages();
  } catch (error) {
    console.error('Failed to set default language', error);
  }
}

function confirmDelete(lang: any) {
  languageToDelete.value = lang;
  showDeleteModal.value = true;
}

function closeDeleteModal() {
  showDeleteModal.value = false;
  languageToDelete.value = null;
}

async function handleConfirmDelete() {
  if (!languageToDelete.value) return;
  
  deleteLoading.value = true;
  try {
    await i18nApi.admin.deleteLanguage(languageToDelete.value.code);
    await fetchLanguages();
    closeDeleteModal();
  } catch (error) {
    console.error('Failed to delete language', error);
  } finally {
    deleteLoading.value = false;
  }
}

const getLanguageActions = (lang: any): ActionMenuItem[] => {
  return [
    {
      label: t('common.edit'),
      icon: PencilSquareIcon,
      onClick: () => openEditModal(lang),
    },
    {
      label: lang.isActive ? t('common.deactivate') : t('common.activate'),
      icon: lang.isActive ? PowerIcon : CheckCircleIcon,
      onClick: () => toggleActive(lang),
    },
    {
      label: t('common.delete'),
      type: 'danger',
      icon: TrashIcon,
      disabled: lang.isDefault,
      onClick: () => confirmDelete(lang),
    }
  ];
};

onMounted(fetchLanguages);
</script>
