<template>
  <Menu as="div" class="relative inline-block text-left" v-slot="{ open }">
    <div ref="triggerRef" @click.stop="updatePosition">
      <MenuButton class="flex items-center justify-center h-8 w-8 rounded-full text-gray-400 hover:text-indigo-600 hover:bg-indigo-50 focus:outline-none transition-all">
        <span class="sr-only">Open options</span>
        <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
          <path d="M10 3a1.5 1.5 0 110 3 1.5 1.5 0 010-3zM10 8.5a1.5 1.5 0 110 3 1.5 1.5 0 010-3zM11.5 15.5a1.5 1.5 0 10-3 0 1.5 1.5 0 003 0z" />
        </svg>
      </MenuButton>
    </div>

    <Teleport to="body">
      <transition
        enter-active-class="transition ease-out duration-100"
        enter-from-class="transform opacity-0 scale-95"
        enter-to-class="transform opacity-100 scale-100"
        leave-active-class="transition ease-in duration-75"
        leave-from-class="transform opacity-100 scale-100"
        leave-to-class="transform opacity-0 scale-95"
      >
        <MenuItems
          v-if="open"
          class="fixed z-[9999] w-48 rounded-xl shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none divide-y divide-gray-100"
          :style="menuStyles"
        >
          <!-- Optional Header -->
          <div v-if="header" class="px-4 py-2 text-xs font-semibold text-gray-500 bg-gray-50 border-b border-gray-100 truncate rounded-t-xl">
            {{ header }}
          </div>

          <div v-for="(group, gIdx) in groupedItems" :key="gIdx" class="py-1">
            <MenuItem v-for="item in group" :key="item.label" v-slot="{ active }">
              <button
                @click="item.onClick?.()"
                :disabled="item.disabled"
                :class="[
                  item.type === 'danger'
                    ? (active ? 'bg-red-50 text-red-700' : 'text-red-600')
                    : (active ? 'bg-indigo-50 text-indigo-700' : 'text-gray-700'),
                  item.disabled ? 'opacity-50 cursor-not-allowed' : '',
                  'group flex items-center px-3 py-2 text-sm w-[calc(100%-8px)] mx-1 my-0.5 rounded-lg text-left transition-colors first:mt-1 last:mb-1'
                ]"
              >
                <span class="flex items-center justify-center mr-2 w-5 h-5">
                  <component
                    v-if="item.icon"
                    :is="item.icon"
                    class="h-4 w-4"
                    :class="item.type === 'danger' ? 'text-red-500' : 'text-indigo-500'"
                  />
                </span>
                {{ item.label }}
              </button>
            </MenuItem>
          </div>
        </MenuItems>
      </transition>
    </Teleport>
  </Menu>
</template>

<script setup lang="ts">
import { computed, ref, reactive, nextTick, type Component } from 'vue';
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
  header?: string;
}>(), {});

const triggerRef = ref<HTMLElement | null>(null);
const menuStyles = reactive<any>({});

const updatePosition = async () => {
  await nextTick();
  if (!triggerRef.value) return;

  const rect = triggerRef.value.getBoundingClientRect();
  const windowHeight = window.innerHeight;
  const spaceBelow = windowHeight - rect.bottom;
  
  // Logic: Default down, but if less than 220px below, go up
  const side = spaceBelow < 220 ? 'up' : 'down';
  
  if (side === 'down') {
    menuStyles.top = `${rect.bottom + 8}px`; // mt-2 equivalent
    menuStyles.left = `${rect.right - 192}px`; // Align right edge (192px = w-48)
    menuStyles.transformOrigin = 'top right';
  } else {
    menuStyles.top = `${rect.top - 8 - 10}px`; // Move up. NOTE: We use top with transform translateY(-100%) for simplicity or calculate actual top.
    // Easier: set top to rect.top - 8, and use transform translateY(-100%) via class?
    // Let's just calculate pixels to be safe
    // If we assume menu height is variable, translateY(-100%) is best.
    
    // Changing strategy for UP:
    // we can use `bottom` if we want, but top + transform is more robust if we don't know height.
    // Actually, let's just use a fixed max height assumption or use pure layout styles.
    // Let's use `bottom` approach relative to viewport? No, `top` is safer.
    
    menuStyles.top = 'auto'; // Reset top
    menuStyles.bottom = `${windowHeight - rect.top + 8}px`;
    menuStyles.left = `${rect.right - 192}px`;
    menuStyles.transformOrigin = 'bottom right';
  }
};

const groupedItems = computed(() => {
  const groups = new Map<number, ActionMenuItem[]>();
  props.items.forEach((item) => {
    const g = item.group ?? (item.type === 'danger' ? 99 : 0);
    if (!groups.has(g)) groups.set(g, []);
    groups.get(g)!.push(item);
  });
  return Array.from(groups.entries())
    .sort((a, b) => a[0] - b[0])
    .map(entry => entry[1]);
});
</script>
