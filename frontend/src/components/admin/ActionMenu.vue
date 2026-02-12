<template>
  <Menu as="div" class="relative inline-block text-left">
    <div>
      <MenuButton class="flex items-center justify-center h-8 w-8 rounded-full text-gray-400 hover:text-indigo-600 hover:bg-indigo-50 focus:outline-none transition-all">
        <span class="sr-only">Open options</span>
        <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
          <path d="M10 3a1.5 1.5 0 110 3 1.5 1.5 0 010-3zM10 8.5a1.5 1.5 0 110 3 1.5 1.5 0 010-3zM11.5 15.5a1.5 1.5 0 10-3 0 1.5 1.5 0 003 0z" />
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
      <MenuItems
        class="absolute right-0 w-48 rounded-xl shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-50 divide-y divide-gray-100"
        :class="dropDirection === 'up' ? 'bottom-full mb-2 origin-bottom-right' : 'mt-2 origin-top-right'"
      >
        <div v-for="(group, gIdx) in groupedItems" :key="gIdx" class="py-1">
          <MenuItem v-for="item in group" :key="item.label" v-slot="{ active }">
            <button
              @click.stop="item.onClick?.()"
              :disabled="item.disabled"
              :class="[
                item.type === 'danger'
                  ? (active ? 'bg-red-50 text-red-700' : 'text-red-600')
                  : (active ? 'bg-indigo-50 text-indigo-700' : 'text-gray-700'),
                item.disabled ? 'opacity-50 cursor-not-allowed' : '',
                'group flex items-center px-4 py-3 text-sm w-full text-left transition-colors'
              ]"
            >
              <component
                v-if="item.icon"
                :is="item.icon"
                class="mr-3 h-4 w-4"
                :class="item.type === 'danger' ? 'text-red-500' : 'text-indigo-500'"
              />
              {{ item.label }}
            </button>
          </MenuItem>
        </div>
      </MenuItems>
    </transition>
  </Menu>
</template>

<script setup lang="ts">
import { computed, type Component } from 'vue';
import { Menu, MenuButton, MenuItems, MenuItem } from '@headlessui/vue';

export interface ActionMenuItem {
  label: string;
  icon?: Component;
  type?: 'default' | 'danger';
  disabled?: boolean;
  onClick?: () => void;
  group?: number;
}

const props = withDefaults(defineProps<{
  items: ActionMenuItem[];
  dropDirection?: 'up' | 'down' | 'auto';
  index?: number;
  totalItems?: number;
}>(), {
  dropDirection: 'auto',
  index: 0,
  totalItems: 10,
});

// Group items by the `group` property (default = 0)
const groupedItems = computed(() => {
  const groups = new Map<number, ActionMenuItem[]>();
  props.items.forEach((item, idx) => {
    const g = item.group ?? (item.type === 'danger' ? 1 : 0);
    if (!groups.has(g)) groups.set(g, []);
    groups.get(g)!.push(item);
  });
  return Array.from(groups.values());
});
</script>
