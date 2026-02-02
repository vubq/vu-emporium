<template>
  <Teleport to="body">
    <Transition
      enter-active-class="transition ease-out duration-200"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition ease-in duration-150"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div
        v-if="modelValue"
        class="fixed inset-0 z-50 overflow-y-auto"
        @click.self="handleClose"
      >
        <!-- Backdrop -->
        <div class="fixed inset-0 bg-black/50 backdrop-blur-sm"></div>

        <!-- Modal Container -->
        <div class="flex min-h-full items-center justify-center p-4">
          <Transition
            enter-active-class="transition ease-out duration-200"
            enter-from-class="opacity-0 scale-95"
            enter-to-class="opacity-100 scale-100"
            leave-active-class="transition ease-in duration-150"
            leave-from-class="opacity-100 scale-100"
            leave-to-class="opacity-0 scale-95"
          >
            <div
              v-if="modelValue"
              class="relative bg-white rounded-xl shadow-2xl max-w-md w-full p-6"
              @click.stop
            >
              <!-- Icon -->
              <div
                class="mx-auto flex h-12 w-12 items-center justify-center rounded-full mb-4"
                :class="{
                  'bg-green-100': variant === 'success',
                  'bg-red-100': variant === 'error',
                  'bg-yellow-100': variant === 'warning',
                  'bg-blue-100': variant === 'info'
                }"
              >
                <!-- Success Icon -->
                <svg
                  v-if="variant === 'success'"
                  class="h-6 w-6 text-green-600"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M5 13l4 4L19 7"
                  />
                </svg>
                <!-- Error Icon -->
                <svg
                  v-else-if="variant === 'error'"
                  class="h-6 w-6 text-red-600"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
                <!-- Warning Icon -->
                <svg
                  v-else-if="variant === 'warning'"
                  class="h-6 w-6 text-yellow-600"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
                  />
                </svg>
                <!-- Info Icon -->
                <svg
                  v-else
                  class="h-6 w-6 text-blue-600"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                  />
                </svg>
              </div>

              <!-- Title -->
              <h3 class="text-lg font-semibold text-gray-900 text-center mb-2">
                {{ title }}
              </h3>

              <!-- Message -->
              <p class="text-sm text-gray-600 text-center mb-6">
                {{ message }}
              </p>

              <!-- Action Button -->
              <button
                type="button"
                @click="handleClose"
                class="w-full px-4 py-2.5 text-sm font-medium text-white rounded-lg focus:outline-none focus:ring-2 focus:ring-offset-2 transition-colors"
                :class="{
                  'bg-green-600 hover:bg-green-700 focus:ring-green-500': variant === 'success',
                  'bg-red-600 hover:bg-red-700 focus:ring-red-500': variant === 'error',
                  'bg-yellow-600 hover:bg-yellow-700 focus:ring-yellow-500': variant === 'warning',
                  'bg-blue-600 hover:bg-blue-700 focus:ring-blue-500': variant === 'info'
                }"
              >
                {{ buttonText }}
              </button>
            </div>
          </Transition>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, watch } from 'vue';

interface Props {
  modelValue: boolean;
  title: string;
  message: string;
  buttonText?: string;
  variant?: 'success' | 'error' | 'warning' | 'info';
  autoClose?: number; // Auto-close after X milliseconds
}

const props = withDefaults(defineProps<Props>(), {
  buttonText: 'OK',
  variant: 'info',
  autoClose: 0
});

const emit = defineEmits<{
  'update:modelValue': [value: boolean];
  close: [];
}>();

let autoCloseTimer: ReturnType<typeof setTimeout> | null = null;

const handleClose = () => {
  if (autoCloseTimer) {
    clearTimeout(autoCloseTimer);
    autoCloseTimer = null;
  }
  emit('close');
  emit('update:modelValue', false);
};

// Handle ESC key
const handleEscape = (e: KeyboardEvent) => {
  if (e.key === 'Escape' && props.modelValue) {
    handleClose();
  }
};

// Auto-close functionality
watch(() => props.modelValue, (newValue) => {
  if (newValue && props.autoClose > 0) {
    autoCloseTimer = setTimeout(() => {
      handleClose();
    }, props.autoClose);
  }
});

onMounted(() => {
  document.addEventListener('keydown', handleEscape);
});

onUnmounted(() => {
  document.removeEventListener('keydown', handleEscape);
  if (autoCloseTimer) {
    clearTimeout(autoCloseTimer);
  }
});
</script>
