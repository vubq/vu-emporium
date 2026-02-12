<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6">
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-4 items-end">
      <!-- Dynamic filter slots -->
      <slot />

      <!-- Filter & Reset Buttons -->
      <div class="lg:col-span-1 flex gap-2">
        <button
          @click="$emit('filter')"
          class="flex-1 py-2 bg-indigo-600 text-white rounded-xl font-medium shadow-sm hover:bg-indigo-700 transition-all flex items-center justify-center text-sm"
          :disabled="loading"
        >
          <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          {{ $t('common.filter') }}
        </button>
        <button
          @click="$emit('reset')"
          class="px-4 py-2 text-indigo-600 hover:text-indigo-800 font-medium transition-all text-sm"
          :disabled="loading"
        >
          {{ $t('common.reset') }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  loading?: boolean;
}>();

defineEmits<{
  (e: 'filter'): void;
  (e: 'reset'): void;
}>();
</script>
