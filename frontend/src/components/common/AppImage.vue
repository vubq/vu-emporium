<template>
  <div class="relative overflow-hidden bg-gray-50 flex items-center justify-center">
    <!-- Fallback Icon -->
    <div class="absolute inset-0 flex items-center justify-center text-gray-300">
      <svg class="w-1/2 h-1/2 opacity-50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
    </div>

    <img 
      v-if="src && !hasError"
      :src="src" 
      :alt="alt"
      class="relative z-10 w-full h-full transition-opacity duration-300"
      :class="objectFitClass"
      @error="handleError"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';

const props = defineProps({
  src: {
    type: String,
    default: ''
  },
  alt: {
    type: String,
    default: ''
  },
  objectFit: {
    type: String,
    default: 'cover', // cover, contain, fill
    validator: (value: string) => ['cover', 'contain', 'fill', 'none', 'scale-down'].includes(value)
  }
});

const hasError = ref(false);

const objectFitClass = computed(() => `object-${props.objectFit}`);

const handleError = () => {
  hasError.value = true;
};

watch(() => props.src, () => {
    hasError.value = false;
});
</script>
