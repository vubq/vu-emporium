<template>
  <Menu as="div" class="relative inline-block text-left">
    <div>
      <MenuButton class="p-2 hover:bg-gray-100 rounded-xl transition-all duration-200 flex items-center gap-2 group border border-transparent hover:border-gray-200 shadow-sm hover:shadow-md bg-white">
        <img v-if="currentLanguage?.flagIcon" :src="currentLanguage.flagIcon" class="w-5 h-3.5 rounded-sm object-cover shadow-sm" />
        <span class="text-sm font-bold text-gray-700 uppercase">{{ locale }}</span>
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
      <MenuItems class="origin-top-right absolute right-0 mt-2 w-48 rounded-2xl shadow-xl bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-50 p-1.5 overflow-hidden border border-gray-100">
        <div class="px-3 py-2 text-xs font-bold text-gray-400 uppercase tracking-widest border-b border-gray-50 mb-1">
          {{ $t('common.select_language') }}
        </div>
        
        <div v-if="i18nStore.isLoading && i18nStore.activeLanguages.length === 0" class="px-3 py-4 text-center">
          <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-indigo-600 mx-auto"></div>
        </div>

        <MenuItem v-for="lang in i18nStore.activeLanguages" :key="lang.code" v-slot="{ active }">
          <button
            @click="handleSetLocale(lang.code)"
            :class="[
              active ? 'bg-indigo-50 text-indigo-700' : 'text-gray-700',
              'group flex items-center justify-between w-full px-3 py-2.5 text-sm transition-all duration-200 rounded-xl'
            ]"
          >
            <div class="flex items-center gap-2.5">
              <img :src="lang.flagIcon" class="w-4 h-3 rounded-sm object-cover shadow-sm" v-if="lang.flagIcon" />
              <span :class="locale === lang.code ? 'font-bold text-indigo-700' : ''">{{ lang.name }}</span>
            </div>
            <svg v-if="locale === lang.code" class="h-4 w-4 text-indigo-600" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
            </svg>
          </button>
        </MenuItem>
      </MenuItems>
    </transition>
  </Menu>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { Menu, MenuButton, MenuItems, MenuItem } from '@headlessui/vue';
import { useI18nStore } from '@/stores/i18nStore';
import { useAdminAuthStore } from '@/stores/adminAuthStore';
import { useAuthStore } from '@/stores/authStore';

const { locale } = useI18n();
const i18nStore = useI18nStore();
const adminAuthStore = useAdminAuthStore();
const authStore = useAuthStore();

const currentLanguage = computed(() => {
  return i18nStore.activeLanguages.find(l => l.code === locale.value);
});

async function handleSetLocale(newLocale: string) {
  // Only save to profile if authenticated as a customer
  const shouldSaveToProfile = authStore.isAuthenticated;
  
  // Use the store to handle language change (persistence, loading messages, etc.)
  await i18nStore.changeLanguage(newLocale, shouldSaveToProfile);
  
  // Persist specific admin locale if needed
  if (adminAuthStore.isAuthenticated) {
     localStorage.setItem('adminLocale', newLocale);
  }
}

onMounted(() => {
  if (i18nStore.activeLanguages.length === 0) {
    i18nStore.fetchLanguages();
  }
});
</script>
