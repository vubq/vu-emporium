<template>
  <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium border"
        :class="badgeClasses">
    <span class="h-1.5 w-1.5 rounded-full mr-1.5" :class="dotClasses"></span>
    {{ label || statusLabel }}
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';

const props = defineProps<{
  status: string;
  label?: string;
}>();

const { t } = useI18n();

// Status color mapping
const colorMap: Record<string, { badge: string; dot: string }> = {
  ACTIVE: { badge: 'bg-green-50 text-green-700 border-green-200', dot: 'bg-green-500' },
  DRAFT: { badge: 'bg-yellow-50 text-yellow-700 border-yellow-200', dot: 'bg-yellow-500' },
  ARCHIVED: { badge: 'bg-gray-50 text-gray-600 border-gray-200', dot: 'bg-gray-400' },
  INACTIVE: { badge: 'bg-gray-50 text-gray-600 border-gray-200', dot: 'bg-gray-400' },
};

const colors = computed(() => colorMap[props.status] || colorMap['ARCHIVED']);
const badgeClasses = computed(() => colors.value.badge);
const dotClasses = computed(() => colors.value.dot);

// Auto-translate status label
const statusLabel = computed(() => {
  const key = `common.${props.status.toLowerCase()}`;
  const translated = t(key);
  return translated !== key ? translated : props.status;
});
</script>
