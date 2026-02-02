<template>
  <div class="bg-white rounded-2xl shadow-card overflow-hidden">
    <!-- Image Skeleton -->
    <div class="aspect-square">
      <Skeleton height="100%" customClass="rounded-none" />
    </div>
    
    <!-- Content Skeleton -->
    <div class="p-5 space-y-4">
      <!-- Title -->
      <div class="space-y-2">
        <Skeleton width="80%" height="1.25rem" />
        <Skeleton width="40%" height="1.25rem" />
      </div>
      
      <!-- Description -->
      <div class="space-y-2">
        <Skeleton width="100%" height="0.75rem" />
        <Skeleton width="90%" height="0.75rem" />
      </div>
      
      <!-- Price and Button -->
      <div class="pt-2 space-y-4">
        <Skeleton width="50%" height="2rem" />
        <Skeleton width="100%" height="2.75rem" customClass="rounded-xl" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Skeleton from './Skeleton.vue';
</script>
