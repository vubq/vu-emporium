<script setup lang="ts">
import { ref, watch } from 'vue';
import { TabGroup, TabList, Tab, TabPanels, TabPanel } from '@headlessui/vue';

interface Props {
  modelValue: Record<string, Record<string, string>>;
  field: string;
  label?: string;
  required?: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits(['update:modelValue']);

const locales = [
  { code: 'vi', name: 'Tiếng Việt', flag: '🇻🇳' },
  { code: 'en', name: 'English', flag: '🇺🇸' }
];

// Local state for the specific field across languages
const localTranslations = ref<Record<string, string>>({});

// Initialize and sync
watch(() => props.modelValue, (newVal) => {
  locales.forEach(loc => {
    const localeMap = newVal?.[loc.code] || {};
    localTranslations.value[loc.code] = localeMap[props.field] || '';
  });
}, { immediate: true, deep: true });

const updateValue = (locale: string, value: string) => {
  localTranslations.value[locale] = value;
  
  // Clone the whole translations object to emit
  const updatedTranslations = JSON.parse(JSON.stringify(props.modelValue || {}));
  
  if (!updatedTranslations[locale]) {
    updatedTranslations[locale] = {};
  }
  
  updatedTranslations[locale][props.field] = value;
  
  emit('update:modelValue', updatedTranslations);
};
</script>

<template>
  <div class="space-y-2 group">
    <TabGroup>
      <!-- Optimized Header: Label and Tabs on the same line -->
      <div class="flex items-center justify-between mb-1.5">
        <label v-if="label" class="text-sm font-semibold text-gray-700 flex items-center">
            {{ label }}
            <span v-if="required" class="text-red-500 ml-1">*</span>
        </label>
        
        <TabList class="flex p-0.5 space-x-0.5 bg-gray-100/80 rounded-lg border border-gray-200/50">
          <Tab
            v-for="loc in locales"
            :key="loc.code"
            as="template"
            v-slot="{ selected }"
          >
            <button
              type="button"
              :class="[
                'px-2.5 py-1 text-[11px] font-bold uppercase tracking-tight rounded-md transition-all duration-200 flex items-center gap-1.5 outline-none ring-0 border-0 focus:outline-none focus:ring-0 focus:border-0 focus-visible:outline-none focus-visible:ring-0 focus-visible:border-0',
                selected
                  ? 'bg-white text-indigo-600 shadow-sm'
                  : 'text-gray-400 hover:text-gray-600 hover:bg-white/50'
              ]"
            >
              <span>{{ loc.flag }}</span>
              {{ loc.code }}
              <!-- Status Indicator Dot -->
              <span 
                v-if="localTranslations[loc.code]"
                class="w-1.5 h-1.5 rounded-full bg-green-500 animate-pulse"
              ></span>
            </button>
          </Tab>
        </TabList>
      </div>

      <TabPanels>
        <TabPanel
          v-for="loc in locales"
          :key="loc.code"
          as="div"
          class="focus:outline-none animate-in fade-in slide-in-from-top-1 duration-200"
        >
          <slot 
            :locale="loc.code" 
            :value="localTranslations[loc.code]" 
            :updateValue="(val: string) => updateValue(loc.code, val)"
          >
            <!-- Default Input if no slot is provided -->
            <input 
              type="text" 
              :value="localTranslations[loc.code]"
              @input="(e: any) => updateValue(loc.code, e.target.value)"
              class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400 bg-white"
              :placeholder="`${$t('common.enter')} ${label || field}...`"
            />
          </slot>
        </TabPanel>
      </TabPanels>
    </TabGroup>
  </div>
</template>

<style scoped>
.animate-in {
  animation-fill-mode: forwards;
}
</style>
