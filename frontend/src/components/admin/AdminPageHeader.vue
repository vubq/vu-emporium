<template>
  <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
    <div>
      <h2 class="text-2xl font-bold text-gray-900 tracking-tight">{{ title }}</h2>
      <p v-if="subtitle" class="text-sm text-gray-500 mt-1">{{ subtitle }}</p>
    </div>
    <button
      v-if="actionLabel"
      @click="$emit('action')"
      class="px-5 py-2.5 rounded-xl bg-gray-900 text-white hover:bg-black font-medium transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 flex items-center justify-center text-sm"
    >
      <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
      </svg>
      {{ actionLabel }}
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string;
  subtitle?: string;
  actionLabel?: string;
}>();

defineEmits<{
  (e: 'action'): void;
}>();
</script>
