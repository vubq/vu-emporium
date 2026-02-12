<template>
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
          <span class="text-sm font-medium text-indigo-600">{{ $t('common.loading') }}</span>
        </div>
      </div>
    </Transition>

    <!-- Table Content -->
    <div class="flex-1 overflow-auto relative custom-scrollbar">
      <table class="min-w-full divide-y divide-gray-100">
        <thead class="bg-gray-50/80 backdrop-blur-md sticky top-0 z-20">
          <slot name="header" />
        </thead>
        <tbody class="bg-white divide-y divide-gray-50">
          <!-- Skeleton Loader -->
          <template v-if="loading && isEmpty">
            <tr v-for="i in skeletonRows" :key="'skeleton-' + i" class="animate-pulse">
              <slot name="skeleton">
                <td v-for="j in columnCount" :key="j" class="px-6 py-4 whitespace-nowrap">
                  <div class="h-4 bg-gray-100 rounded w-3/4"></div>
                </td>
              </slot>
            </tr>
          </template>

          <!-- Empty State -->
          <tr v-else-if="isEmpty && !loading">
            <td :colspan="columnCount" class="px-6 py-24 text-center">
              <div class="flex flex-col items-center justify-center">
                <div class="w-24 h-24 bg-gray-50 rounded-full flex items-center justify-center mb-4">
                  <svg class="w-10 h-10 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                  </svg>
                </div>
                <h3 class="text-lg font-bold text-gray-900">{{ emptyTitle || $t('common.no_results') }}</h3>
                <p v-if="emptySubtitle" class="text-sm text-gray-500 mt-1 max-w-sm mx-auto">{{ emptySubtitle }}</p>
                <button
                  v-if="showClearFilters"
                  @click="$emit('clear-filters')"
                  class="mt-4 text-indigo-600 hover:text-indigo-800 font-medium text-sm"
                >
                  {{ $t('common.reset') }}
                </button>
              </div>
            </td>
          </tr>

          <!-- Data rows -->
          <slot v-else />
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div v-if="showPagination && totalPages > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
      <!-- Mobile Pagination -->
      <div class="flex-1 flex justify-between sm:hidden">
        <button @click="handlePageChange(currentPage - 1)" :disabled="currentPage === 0" class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50">
          {{ $t('common.previous') }}
        </button>
        <button @click="handlePageChange(currentPage + 1)" :disabled="currentPage >= totalPages - 1" class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50">
          {{ $t('common.next') }}
        </button>
      </div>
      <!-- Desktop Pagination -->
      <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
        <div>
          <p class="text-sm text-gray-700">
            {{ $t('common.pagination_info', { current: currentPage + 1, total: totalPages }) }}
          </p>
        </div>
        <div class="flex items-center gap-4">
          <!-- Page Size Selector -->
          <div class="w-20">
            <Listbox :model-value="pageSize" @update:model-value="handlePageSizeUpdate">
              <div class="relative">
                <ListboxButton class="relative w-full cursor-pointer rounded-lg bg-white py-2 pl-3 pr-8 text-left border border-gray-300 shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                  <span class="block truncate">{{ pageSize }}</span>
                  <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                    <svg class="h-4 w-4 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                      <path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" />
                    </svg>
                  </span>
                </ListboxButton>
                <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
                  <ListboxOptions class="absolute bottom-full mb-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                    <ListboxOption v-for="size in pageSizes" :key="size" :value="size" as="template" v-slot="{ active, selected }">
                      <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-3 pr-4']">
                        <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">{{ size }}</span>
                      </li>
                    </ListboxOption>
                  </ListboxOptions>
                </transition>
              </div>
            </Listbox>
          </div>

          <!-- Pagination Controls -->
          <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
            <button @click="handlePageChange(0)" :disabled="currentPage === 0" class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:bg-gray-100 disabled:text-gray-300" :title="$t('common.first')">
              <span class="sr-only">{{ $t('common.first') }}</span>
              <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 19l-7-7 7-7m8 14l-7-7 7-7" /></svg>
            </button>
            <button @click="handlePageChange(currentPage - 1)" :disabled="currentPage === 0" class="relative inline-flex items-center px-2 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:bg-gray-100 disabled:text-gray-300" :title="$t('common.previous')">
              <span class="sr-only">{{ $t('common.previous') }}</span>
              <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" /></svg>
            </button>

            <span class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">
              {{ currentPage + 1 }}
            </span>

            <button @click="handlePageChange(currentPage + 1)" :disabled="currentPage >= totalPages - 1" class="relative inline-flex items-center px-2 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:bg-gray-100 disabled:text-gray-300" :title="$t('common.next')">
              <span class="sr-only">{{ $t('common.next') }}</span>
              <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" /></svg>
            </button>
            <button @click="handlePageChange(totalPages - 1)" :disabled="currentPage >= totalPages - 1" class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:bg-gray-100 disabled:text-gray-300" :title="$t('common.last')">
              <span class="sr-only">{{ $t('common.last') }}</span>
              <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 5l7 7-7 7M5 5l7 7-7 7" /></svg>
            </button>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Listbox, ListboxButton, ListboxOptions, ListboxOption } from '@headlessui/vue';

const props = withDefaults(defineProps<{
  loading?: boolean;
  isEmpty?: boolean;
  emptyTitle?: string;
  emptySubtitle?: string;
  showClearFilters?: boolean;
  showPagination?: boolean;
  currentPage?: number;
  totalPages?: number;
  pageSize?: number;
  pageSizes?: number[];
  columnCount?: number;
  skeletonRows?: number;
}>(), {
  loading: false,
  isEmpty: false,
  showClearFilters: true,
  showPagination: true,
  currentPage: 0,
  totalPages: 0,
  pageSize: 20,
  pageSizes: () => [10, 20, 50, 100],
  columnCount: 5,
  skeletonRows: 5,
});

const emit = defineEmits<{
  (e: 'page-change', page: number): void;
  (e: 'page-size-change', size: number): void;
  (e: 'clear-filters'): void;
}>();

const handlePageChange = (page: number) => {
  if (page < 0 || page >= props.totalPages) return;
  emit('page-change', page);
};

const handlePageSizeUpdate = (size: number) => {
  emit('page-size-change', size);
};
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #e2e8f0;
  border-radius: 10px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #cbd5e1;
}
</style>
