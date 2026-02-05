<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import { useI18nStore } from '@/stores/i18nStore';

interface Props {
  modelValue?: Record<string, Record<string, string>>;
  field: string;
  placeholder?: string;
  defaultValue?: string; // The value of the default language (code from store)
  disabled?: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits(['update:modelValue', 'update:defaultValue']);

const i18nStore = useI18nStore();
const locales = computed(() => i18nStore.activeLanguages);

// Local state for all translations
const translations = ref<Record<string, string>>({});

// Sync from props
watch([() => props.modelValue, () => props.defaultValue], ([newVal, defaultVal]) => {
  locales.value.forEach(loc => {
    if (loc.code === i18nStore.defaultLanguage?.code) {
        translations.value[loc.code] = defaultVal || '';
    } else {
        const localeMap = newVal?.[loc.code] || {};
        translations.value[loc.code] = localeMap[props.field] || '';
    }
  });
}, { immediate: true, deep: true });

const updateValue = (locale: string, value: string) => {
  translations.value[locale] = value;
  
  // Clone current structure
  const updatedTranslations = JSON.parse(JSON.stringify(props.modelValue || {}));
  
  if (locale === i18nStore.defaultLanguage?.code) {
      // Create/Update default entry in translations map as well for consistency
      if (!updatedTranslations[locale]) updatedTranslations[locale] = {};
      updatedTranslations[locale][props.field] = value;
      
      // Emit primary v-model:defaultValue update
      emit('update:defaultValue', value);
  } else {
      // Update other locales
      if (!updatedTranslations[locale]) updatedTranslations[locale] = {};
      updatedTranslations[locale][props.field] = value;
  }
  
  // Emit full map update
  emit('update:modelValue', updatedTranslations);
};

</script>

<template>
  <div class="space-y-2 w-full">
    <div v-for="loc in locales" :key="loc.code" class="relative group/input flex items-center">
        <!-- Flag Badge (Absolute Left) -->
        <div class="absolute left-2 z-10 flex items-center pointer-events-none opacity-60">
            <span class="text-lg leading-none">{{ loc.flag }}</span>
            <span class="text-[0.6rem] font-bold text-gray-400 uppercase ml-1">{{ loc.code }}</span>
        </div>

        <input 
          type="text" 
          :value="translations[loc.code]"
          @input="(e: any) => updateValue(loc.code, e.target.value)"
          class="w-full pl-12 pr-3 py-1.5 text-sm rounded-lg border border-gray-200 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-300 bg-white"
          :class="{'bg-gray-50 text-gray-400': disabled}"
          :placeholder="`${placeholder || ''} (${loc.name})`"
          :disabled="disabled"
        />
    </div>
  </div>
</template>
