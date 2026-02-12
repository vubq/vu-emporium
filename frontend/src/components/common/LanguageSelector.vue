<template>
  <Menu as="div" class="relative inline-block text-left" v-slot="{ open }">
    <div>
      <MenuButton 
        class="flex items-center gap-2 px-3 py-1.5 bg-white border border-gray-200 rounded-full shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-0 focus-visible:outline-none focus-visible:ring-0 transition-colors cursor-pointer select-none" 
        :class="open ? 'ring-1 ring-indigo-500 border-indigo-500' : ''"
        style="outline: none !important;"
      >
        <img v-if="currentFlag" :src="currentFlag" class="w-5 h-3.5 rounded-[2px] object-cover shadow-sm" alt="" />
        <span class="text-sm font-bold text-gray-700 uppercase" v-if="currentLanguage">{{ currentLanguage.code }}</span>
        <span class="text-sm font-bold text-gray-700 uppercase" v-else>{{ locale }}</span>
        <svg class="h-4 w-4 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
          <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
        </svg>
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
      <MenuItems class="absolute right-0 z-50 mt-2 w-48 origin-top-right rounded-xl bg-white p-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
        <div class="px-3 py-2 text-xs font-bold text-gray-400 uppercase tracking-widest border-b border-gray-50 mb-1 mx-1">
          {{ $t('common.select_language') }}
        </div>
        
        <div v-for="lang in i18nStore.activeLanguages" :key="lang.code">
          <MenuItem v-slot="{ active }">
            <button
              @click="handleSetLocale(lang.code)"
              :class="[
                active ? 'bg-indigo-50 text-indigo-700' : 'text-gray-700',
                'group flex w-full items-center justify-between rounded-lg px-3 py-2 text-sm transition-colors my-0.5 mx-1 w-[calc(100%-8px)]'
              ]"
            >
              <div class="flex items-center gap-3">
                <img :src="lang.flagIcon" class="w-5 h-3.5 rounded-[2px] object-cover shadow-sm" v-if="lang.flagIcon" alt="" />
                <span :class="locale === lang.code ? 'font-bold' : 'font-normal'">{{ lang.name }}</span>
              </div>
              <svg v-if="locale === lang.code" class="h-4 w-4 text-indigo-600" viewBox="0 0 20 20" fill="currentColor">
                 <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
              </svg>
            </button>
          </MenuItem>
        </div>
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

const currentFlag = computed(() => {
  return currentLanguage.value?.flagIcon || i18nStore.activeLanguages.find(l => l.code === 'en')?.flagIcon;
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
