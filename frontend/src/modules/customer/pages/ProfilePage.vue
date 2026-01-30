<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <h1 class="text-3xl font-bold text-gray-900 mb-8">Profile</h1>
      <div class="bg-white rounded-lg shadow-md p-6">
        <div v-if="authStore.user">
          <p class="mb-2"><strong>Name:</strong> {{ authStore.user.fullName }}</p>
          <p class="mb-2"><strong>Email:</strong> {{ authStore.user.email }}</p>
          <p class="mb-2"><strong>Roles:</strong> {{ authStore.user.roles.join(', ') }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore';
const authStore = useAuthStore();
</script>
