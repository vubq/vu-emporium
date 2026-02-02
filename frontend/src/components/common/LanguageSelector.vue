<template>
  <Menu as="div" class="relative inline-block text-left">
    <div>
      <MenuButton class="p-2 hover:bg-gray-100 rounded-full transition-colors duration-200 flex items-center justify-center">
        <span class="text-sm font-bold text-gray-600 uppercase">{{ locale }}</span>
      </MenuButton>
    </div>
    <transition
      enter-active-class="transition ease-out duration-100"
      enter-from-class="transform opacity-0 scale-95"
      enter-to-class="transform opacity-100 scale-100"
      leave-active-class="transition ease-in duration-75"
      leave-from-class="transform opacity-100 scale-100"
      leave-to-class="transform opacity-0 scale-95"
    >
      <MenuItems class="origin-top-right absolute right-0 mt-2 w-44 rounded-2xl shadow-xl bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-50 p-1.5 overflow-hidden border border-gray-100">
        <div class="px-3 py-2 text-xs font-bold text-gray-400 uppercase tracking-widest border-b border-gray-50 mb-1">
          {{ $t('common.select_language') }}
        </div>
        <MenuItem v-slot="{ active }">
          <button
            @click="setLocale('en')"
            :class="[
              active ? 'bg-primary-50 text-primary-700' : 'text-gray-700',
              'group flex items-center justify-between w-full px-3 py-2.5 text-sm transition-all duration-200 rounded-xl'
            ]"
          >
            <span :class="locale === 'en' ? 'font-bold text-primary-700' : ''">English</span>
            <svg v-if="locale === 'en'" class="h-4 w-4 text-primary-600" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
            </svg>
          </button>
        </MenuItem>
        <MenuItem v-slot="{ active }">
          <button
            @click="setLocale('vi')"
            :class="[
              active ? 'bg-primary-50 text-primary-700' : 'text-gray-700',
              'group flex items-center justify-between w-full px-3 py-2.5 text-sm transition-all duration-200 rounded-xl'
            ]"
          >
            <span :class="locale === 'vi' ? 'font-bold text-primary-700' : ''">Tiếng Việt</span>
            <svg v-if="locale === 'vi'" class="h-4 w-4 text-primary-600" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
            </svg>
          </button>
        </MenuItem>
      </MenuItems>
    </transition>
  </Menu>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
import { Menu, MenuButton, MenuItems, MenuItem } from '@headlessui/vue';
import { useAuthStore } from '@/stores/authStore';
import { useAdminAuthStore } from '@/stores/adminAuthStore';
import { authApi } from '@/api/authApi';
import axios from 'axios';

const { locale } = useI18n();
const authStore = useAuthStore();
const adminAuthStore = useAdminAuthStore();

async function setLocale(newLocale: string) {
  locale.value = newLocale;
  
  // Determine which localStorage key to use based on user type
  const storageKey = adminAuthStore.isAuthenticated ? 'adminLocale' : 
                     authStore.isAuthenticated ? 'customerLocale' : 
                     'locale'; // Guest users use generic 'locale'
  
  localStorage.setItem(storageKey, newLocale);

  // If user is logged in (either customer or admin), save to backend
  try {
    if (authStore.isAuthenticated) {
      // Customer is logged in
      await authApi.updateLanguage(newLocale);
    } else if (adminAuthStore.isAuthenticated) {
      // Admin is logged in
      await axios.patch('/api/admin/auth/language', 
        { language: newLocale },
        { headers: { Authorization: `Bearer ${adminAuthStore.token}` } }
      );
    }
  } catch (error) {
    console.error('Failed to update language preference:', error);
    // Language is still changed locally even if API call fails
  }
}
</script>
