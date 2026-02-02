<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-pulse">
    <!-- Breadcrumb Skeleton -->
    <div class="flex space-x-2 mb-8">
      <Skeleton width="60px" />
      <Skeleton width="10px" />
      <Skeleton width="80px" />
      <Skeleton width="10px" />
      <Skeleton width="120px" />
    </div>

    <div class="lg:grid lg:grid-cols-2 lg:gap-x-12 lg:items-start">
      <!-- Image Gallery Skeleton -->
      <div class="flex flex-col-reverse">
        <div class="hidden mt-6 w-full max-w-2xl mx-auto sm:block lg:max-w-none">
          <div class="grid grid-cols-4 gap-6">
            <Skeleton v-for="i in 4" :key="i" height="6rem" customClass="rounded-lg" />
          </div>
        </div>
        <Skeleton height="35rem" customClass="rounded-2xl" />
      </div>

      <!-- Info Skeleton -->
      <div class="mt-10 px-4 sm:px-0 sm:mt-16 lg:mt-0 space-y-6">
        <Skeleton width="70%" height="3rem" />
        <div class="flex space-x-4 items-end">
          <Skeleton width="30%" height="2.5rem" />
          <Skeleton width="20%" height="1.5rem" />
        </div>
        
        <Skeleton width="40%" height="1.5rem" />
        
        <div class="space-y-2 pt-4">
          <Skeleton width="100%" />
          <Skeleton width="100%" />
          <Skeleton width="90%" />
          <Skeleton width="40%" />
        </div>

        <div class="pt-8 space-y-4">
          <Skeleton width="30%" height="1rem" />
          <div class="flex space-x-2">
            <Skeleton v-for="i in 3" :key="i" width="60px" height="40px" />
          </div>
        </div>

        <div class="pt-8 flex space-x-4">
          <Skeleton width="100px" height="3rem" customClass="rounded-lg" />
          <Skeleton width="100%" height="3rem" customClass="rounded-lg" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Skeleton from './Skeleton.vue';
</script>
