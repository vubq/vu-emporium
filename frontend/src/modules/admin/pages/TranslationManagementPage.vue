<template>
  <div class="bg-gray-50 min-h-screen p-6 font-sans">
    <div class="w-full h-full flex flex-col">
      <!-- Shared Header -->
      <AdminPageHeader
        :title="$t('admin.i18n.translations.title')"
        :subtitle="$t('admin.i18n.translations.subtitle')"
        :action-label="$t('admin.i18n.translations.add_key')"
        @action="openAddKeyModal"
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
            <input v-model="filters.search" @keyup.enter="applyFilters" type="text" :placeholder="$t('admin.i18n.translations.search_placeholder')" class="w-full pl-9 pr-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
            </div>
          </div>
        </div>

        <!-- Stats Display Slot -->
        <div class="lg:col-span-1 items-end flex pb-2">
           <span class="text-xs font-bold uppercase tracking-wider text-gray-400 bg-gray-50 px-3 py-1.5 rounded-lg border border-gray-100">
             {{ $t('admin.i18n.translations.keys_found', { count: filteredKeys.length }) }}
          </span>
        </div>
      </AdminFilterBar>

      <!-- Shared Data Table -->
      <AdminDataTable
        :loading="loading"
        :is-empty="paginatedKeys.length === 0"
        :empty-title="filters.search ? $t('common.no_results') : 'No translation keys yet.'"
        :empty-subtitle="filters.search ? $t('admin.manage.products.no_results_desc') : ''"
        :current-page="currentPage"
        :total-pages="totalPages"
        :page-size="pageSize"
        :column-count="2"
        @page-change="changePage"
        @page-size-change="handlePageSizeChange"
        @clear-filters="resetFilters"
      >
        <template #header>
          <tr>
            <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider w-full">
              {{ $t('admin.i18n.translations.columns.key') }}
            </th>
            <th scope="col" class="relative px-6 py-3 w-16">
              <span class="sr-only">{{ $t('common.actions') }}</span>
            </th>
          </tr>
        </template>

        <template #skeleton>
          <td class="px-6 py-4">
             <div class="flex flex-col gap-2">
                <div class="h-4 w-48 bg-gray-100 rounded"></div>
                <div class="flex gap-2">
                   <div class="h-3 w-6 bg-gray-100 rounded"></div>
                   <div class="h-3 w-6 bg-gray-100 rounded"></div>
                </div>
             </div>
          </td>
          <td class="px-6 py-4"></td>
        </template>

        <template #default>
          <tr v-for="(key, index) in paginatedKeys" :key="key" class="group hover:bg-gray-50 transition-colors cursor-pointer" @click="openEditKeyModal(key)">
             <td class="px-6 py-4 text-sm font-mono text-gray-600">
                <div class="flex items-center justify-between">
                     <span :title="key" class="font-bold text-gray-700 group-hover:text-indigo-600 transition-colors">{{ key }}</span>
                     <div class="flex gap-2">
                         <span v-for="lang in languages" :key="lang.code" 
                               class="inline-flex items-center px-2 py-0.5 rounded text-[10px] font-bold uppercase transition-colors border"
                               :class="matrix[key][lang.code] ? 'bg-green-50 text-green-700 border-green-100' : 'bg-gray-50 text-gray-400 border-gray-100'">
                            {{ lang.code }}
                         </span>
                     </div>
                </div>
            </td>
            <td class="px-6 py-4 text-right">
              <ActionMenu :items="getTranslationActions(key)" />
            </td>
          </tr>
        </template>
      </AdminDataTable>

      <!-- Edit/Add Modal -->
      <TransitionRoot appear :show="modal.show" as="template">
        <Dialog as="div" @close="closeModal" class="relative z-50">
          <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0" enter-to="opacity-100" leave="duration-200 ease-in" leave-from="opacity-100" leave-to="opacity-0">
            <div class="fixed inset-0 bg-black/40 backdrop-blur-sm" />
          </TransitionChild>

          <div class="fixed inset-0 overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4 text-center">
              <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0 scale-95" enter-to="opacity-100 scale-100" leave="duration-200 ease-in" leave-from="opacity-100 scale-100" leave-to="opacity-0 scale-95">
                <DialogPanel class="w-full max-w-2xl transform overflow-hidden rounded-2xl bg-white p-8 text-left align-middle shadow-2xl transition-all border border-gray-100">
                  
                  <div class="flex justify-between items-start mb-8">
                      <div>
                           <DialogTitle as="h3" class="text-xl font-bold leading-6 text-gray-900 flex items-center gap-2">
                               <span class="bg-indigo-100 text-indigo-600 p-2 rounded-lg">
                                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5h12M9 3v2m1.048 9.5A18.022 18.022 0 016.412 9m6.088 9h7M11 21l5-10 5 10M12.751 5C11.783 10.77 8.07 15.61 3 18.129" /></svg>
                               </span>
                               {{ modal.type === 'add' ? $t('admin.i18n.translations.add_key_title') : $t('admin.i18n.translations.edit_key_title') }}
                          </DialogTitle>
                           <p class="text-sm text-gray-500 mt-2 ml-11">
                              {{ modal.type === 'add' ? 'Create a new translation key and add values for all languages.' : 'Modify the key name and update translations.' }}
                           </p>
                      </div>
                     
                      <button @click="closeModal" class="text-gray-400 hover:text-gray-500 p-1 rounded-full hover:bg-gray-100 transition-colors" :disabled="submitting">
                          <span class="sr-only">Close</span>
                          <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                      </button>
                  </div>
                  
                  <div class="space-y-6">
                    <!-- Key Name Input -->
                    <div>
                      <label class="block text-sm font-semibold text-gray-700 mb-2">{{ $t('admin.i18n.translations.fields.key_name') }}</label>
                      <input 
                        v-model="modal.keyName" 
                        type="text" 
                        placeholder="e.g. common.save_changes"
                        class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400 font-mono text-sm"
                        :disabled="submitting"
                      />
                      <p class="mt-2 text-xs text-gray-500 flex items-center gap-1">
                           Use dots to group keys (e.g. <code class="bg-gray-100 px-1 py-0.5 rounded text-gray-800 mx-1 border border-gray-200">admin.dashboard.title</code>)
                      </p>
                    </div>

                    <div class="border-t border-gray-100 my-6"></div>

                    <!-- Translations Inputs -->
                    <div>
                        <h4 class="text-sm font-bold text-gray-900 mb-4">{{ $t('admin.translations') }}</h4>
                        
                        <div class="space-y-5">
                            <div v-for="lang in languages" :key="lang.code" class="relative">
                                <label class="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-2 flex items-center gap-2">
                                    <img :src="lang.flagIcon" class="w-4 h-3 rounded-sm object-cover shadow-sm border border-gray-100" v-if="lang.flagIcon" />
                                    {{ lang.name }}
                                </label>
                                <div class="relative">
                                    <textarea 
                                      v-model="modal.translations[lang.code]" 
                                      rows="2" 
                                      class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400 resize-y min-h-[80px]"
                                      :placeholder="lang.isDefault ? 'Enter translation...' : $t('admin.i18n.translations.same_as_default')"
                                      :disabled="submitting"
                                    ></textarea>
                                    <div v-if="modal.translations[lang.code] && modal.translations[lang.code] !== matrix[modal.oldKey]?.[lang.code]" class="absolute right-3 top-3 w-2 h-2 rounded-full bg-indigo-500 animate-pulse"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                  </div>

                  <div class="mt-8 flex gap-3 pt-6 border-t border-gray-100 justify-end">
                    <button type="button" class="px-6 py-2.5 rounded-xl text-gray-700 bg-white border border-gray-200 hover:bg-gray-50 hover:border-gray-300 font-medium transition-all shadow-sm" @click="closeModal" :disabled="submitting">
                      {{ $t('common.cancel') }}
                    </button>
                     <button type="button" class="px-6 py-2.5 rounded-xl bg-gray-900 text-white hover:bg-black font-medium transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 flex items-center" @click="handleModalSubmit" :disabled="submitting">
                      <svg v-if="submitting" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                      {{ modal.type === 'add' ? $t('common.create') : $t('common.save_changes') }}
                    </button>
                  </div>
                </DialogPanel>
              </TransitionChild>
            </div>
          </div>
        </Dialog>
      </TransitionRoot>

      <!-- Delete Confirm Modal -->
      <ConfirmModal 
        :isOpen="showDeleteModal" 
        :title="$t('common.delete') + ' ' + $t('admin.translations')" 
        :message="$t('admin.i18n.translations.delete_key_confirm', { key: keyToDelete })"
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
import { ref, onMounted, computed, reactive } from 'vue';
import { useI18n } from 'vue-i18n';
import { i18nApi } from '@/api/i18nApi';
import { Dialog, DialogPanel, DialogTitle, TransitionRoot, TransitionChild } from '@headlessui/vue';
import { PencilSquareIcon, TrashIcon } from '@heroicons/vue/24/outline';
import ConfirmModal from '@/components/common/ConfirmModal.vue';

// Shared Components
import AdminPageHeader from '@/components/admin/AdminPageHeader.vue';
import AdminFilterBar from '@/components/admin/AdminFilterBar.vue';
import AdminDataTable from '@/components/admin/AdminDataTable.vue';
import ActionMenu, { type ActionMenuItem } from '@/components/admin/ActionMenu.vue';

const { t } = useI18n();
const languages = ref<any[]>([]);
const matrix = ref<Record<string, Record<string, string>>>({});
const keys = ref<string[]>([]);
const loading = ref(false);
const submitting = ref(false);

const modal = reactive({
  show: false,
  type: 'add' as 'add' | 'edit',
  oldKey: '',
  keyName: '',
  translations: {} as Record<string, string>
});

// Delete Modal State
const showDeleteModal = ref(false);
const keyToDelete = ref<string | null>(null);
const deleteLoading = ref(false);

// Filters
const filters = reactive({
  search: '',
});

// Applied Filters for client-side functionality
const appliedFilters = reactive({
  search: '',
});

// Pagination State
const currentPage = ref(0);
const pageSize = ref(20);

// Filtered List
const filteredKeys = computed(() => {
  if (!appliedFilters.search) return keys.value;
  const q = appliedFilters.search.toLowerCase();
  
  return keys.value.filter(key => {
    if (key.toLowerCase().includes(q)) return true;
    const row = matrix.value[key];
    if (!row) return false;
    return Object.values(row).some(val => val && (val as string).toLowerCase().includes(q));
  });
});

// Paginated Keys
const paginatedKeys = computed(() => {
  const start = currentPage.value * pageSize.value;
  const end = start + pageSize.value;
  return filteredKeys.value.slice(start, end);
});

const totalPages = computed(() => {
  return Math.ceil(filteredKeys.value.length / pageSize.value);
});

async function fetchData() {
  loading.value = true;
  try {
    const [langsRes, matrixRes] = await Promise.all([
      i18nApi.admin.getAllLanguages(),
      i18nApi.admin.getMatrix()
    ]);
    
    languages.value = langsRes.data;
    matrix.value = matrixRes.data;
    keys.value = Object.keys(matrix.value).sort();
    
    // Ensure every key has every language entry
    keys.value.forEach(key => {
      languages.value.forEach(lang => {
        if (matrix.value[key][lang.code] === undefined) {
          matrix.value[key][lang.code] = '';
        }
      });
    });
  } catch (error) {
    console.error('Failed to fetch translations matrix', error);
  } finally {
    loading.value = false;
  }
}

const applyFilters = () => {
  appliedFilters.search = filters.search;
  currentPage.value = 0;
};

const resetFilters = () => {
  filters.search = '';
  applyFilters();
};

const changePage = (page: number) => {
  currentPage.value = page;
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handlePageSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 0;
};

// Modal Logic
function closeModal() {
  modal.show = false;
  modal.keyName = '';
  modal.translations = {};
}

function openAddKeyModal() {
  modal.type = 'add';
  modal.oldKey = '';
  modal.keyName = '';
  // Initialize empty translations
  modal.translations = {};
  languages.value.forEach(l => modal.translations[l.code] = '');
  modal.show = true;
}

function openEditKeyModal(key: string) {
  modal.type = 'edit';
  modal.oldKey = key;
  modal.keyName = key;
  // Load current values
  modal.translations = { ...matrix.value[key] };
  modal.show = true;
}

async function handleModalSubmit() {
  if (!modal.keyName.trim()) return;
  submitting.value = true;

  try {
    // 1. Rename Key if changed (Edit Mode Only)
    if (modal.type === 'edit' && modal.keyName !== modal.oldKey) {
        await i18nApi.admin.renameKey(modal.oldKey, modal.keyName);
    } 
    // 1. Add Key (Add Mode Only)
    else if (modal.type === 'add') {
        await i18nApi.admin.addKey(modal.keyName);
    }

    // 2. Update Translations
    const finalKey = modal.keyName;
    const updatesByLang: Record<string, Record<string, string>> = {};

    languages.value.forEach(lang => {
        const newVal = modal.translations[lang.code] || '';
        const oldVal = modal.type === 'edit' ? matrix.value[modal.oldKey]?.[lang.code] : '';
        
        // Only update if changed or new
        if (newVal !== oldVal || modal.type === 'add') {
            if (!updatesByLang[lang.code]) updatesByLang[lang.code] = {};
            updatesByLang[lang.code][finalKey] = newVal;
        }
    });

    const updatePromises = Object.entries(updatesByLang).map(([langCode, data]) => {
        return i18nApi.admin.updateTranslations(langCode, data);
    });

    await Promise.all(updatePromises);

    await fetchData();
    closeModal();
  } catch (error) {
    console.error('Action failed', error);
  } finally {
    submitting.value = false;
  }
}

function confirmDeleteKey(key: string) {
  keyToDelete.value = key;
  showDeleteModal.value = true;
}

function closeDeleteModal() {
  showDeleteModal.value = false;
  keyToDelete.value = null;
}

async function handleConfirmDelete() {
  if (!keyToDelete.value) return;
  
  deleteLoading.value = true;
  try {
    await i18nApi.admin.deleteKey(keyToDelete.value);
    await fetchData();
    closeDeleteModal();
  } catch (error) {
    console.error('Delete failed', error);
  } finally {
    deleteLoading.value = false;
  }
}

const getTranslationActions = (key: string): ActionMenuItem[] => {
  return [
    {
      label: `${t('common.edit')} ${t('admin.translations')}`,
      icon: PencilSquareIcon,
      onClick: () => openEditKeyModal(key),
    },
    {
      label: `${t('common.delete')} ${t('admin.translations')}`,
      type: 'danger',
      icon: TrashIcon,
      onClick: () => confirmDeleteKey(key),
    }
  ];
};

onMounted(fetchData);
</script>
