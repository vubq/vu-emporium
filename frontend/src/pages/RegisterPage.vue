<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center px-4">
    <div class="max-w-md w-full bg-white rounded-lg shadow-md p-8">
      <h1 class="text-3xl font-bold text-center mb-8">Sign Up</h1>
      
      <form @submit.prevent="handleSubmit">
        <div class="mb-4">
          <label class="block text-gray-700 mb-2">Full Name</label>
          <input v-model="form.fullName" type="text" required class="input" placeholder="John Doe" />
        </div>
        
        <div class="mb-4">
          <label class="block text-gray-700 mb-2">Email</label>
          <input v-model="form.email" type="email" required class="input" placeholder="your@email.com" />
        </div>
        
        <div class="mb-4">
          <label class="block text-gray-700 mb-2">Password</label>
          <input v-model="form.password" type="password" required minlength="6" class="input" placeholder="••••••••" />
        </div>
        
        <div class="mb-4">
          <label class="block text-gray-700 mb-2">Phone (Optional)</label>
          <input v-model="form.phone" type="tel" class="input" placeholder="+84 123 456 789" />
        </div>
        
        <div class="mb-6">
          <label class="block text-gray-700 mb-2">Address (Optional)</label>
          <textarea v-model="form.address" class="input" rows="3" placeholder="Your address"></textarea>
        </div>
        
        <div v-if="error" class="mb-4 p-3 bg-red-100 text-red-700 rounded">
          {{ error }}
        </div>
        
        <button type="submit" :disabled="loading" class="btn btn-primary w-full">
          {{ loading ? 'Creating account...' : 'Sign Up' }}
        </button>
      </form>
      
      <p class="text-center mt-4 text-gray-600">
        Already have an account?
        <router-link to="/login" class="text-primary-600 hover:underline">Login</router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const router = useRouter();
const authStore = useAuthStore();

const form = ref({
  email: '',
  password: '',
  fullName: '',
  phone: '',
  address: '',
});

const loading = ref(false);
const error = ref('');

async function handleSubmit() {
  loading.value = true;
  error.value = '';
  
  try {
    await authStore.register(form.value);
    router.push('/');
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Registration failed';
  } finally {
    loading.value = false;
  }
}
</script>
