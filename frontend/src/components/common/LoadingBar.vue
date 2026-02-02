<template>
  <div v-if="visible" class="fixed top-0 left-0 right-0 z-[100] h-1 overflow-hidden bg-transparent">
    <div 
      class="h-full bg-gradient-to-r from-primary-400 via-accent-500 to-primary-600 transition-all duration-300 ease-out"
      :style="{ width: progress + '%' }"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';

const visible = ref(false);
const progress = ref(0);
const router = useRouter();
let timer: any = null;

const start = () => {
  visible.value = true;
  progress.value = 0;
  if (timer) clearInterval(timer);
  
  timer = setInterval(() => {
    if (progress.value < 90) {
      progress.value += Math.random() * 5;
    }
  }, 200);
};

const finish = () => {
  progress.value = 100;
  setTimeout(() => {
    visible.value = false;
    progress.value = 0;
  }, 300);
  if (timer) clearInterval(timer);
};

// Global hooks for axios or router
onMounted(() => {
  router.beforeEach((to, from, next) => {
    start();
    next();
  });
  
  router.afterEach(() => {
    finish();
  });
});

onUnmounted(() => {
  if (timer) clearInterval(timer);
});
</script>
