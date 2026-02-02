<template>
  <div class="min-h-screen bg-gray-50 flex">
    <LoadingBar />
    <!-- Sidebar -->
    <aside class="w-72 bg-slate-900 text-slate-300 flex flex-col border-r border-slate-800 shadow-2xl z-20 transition-all duration-300">
      <!-- Logo -->
      <div class="h-20 flex items-center px-8 border-b border-slate-800/50 bg-slate-900/50 backdrop-blur-sm">
        <div class="flex items-center gap-4 group cursor-pointer">
          <div class="relative w-10 h-10 flex items-center justify-center rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 shadow-lg shadow-blue-500/20 group-hover:shadow-blue-500/40 transition-all duration-300 transform group-hover:scale-105">
            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"></path>
            </svg>
            <div class="absolute inset-0 rounded-xl ring-1 ring-white/20"></div>
          </div>
          <div class="flex flex-col">
            <h2 class="text-lg font-bold text-white tracking-wide leading-none">Admin Panel</h2>
            <p class="text-xs text-blue-400 font-medium mt-1 group-hover:text-blue-300 transition-colors">Vu Emporium</p>
          </div>
        </div>
      </div>

      <!-- Navigation -->
      <nav class="flex-1 px-4 py-6 space-y-2 overflow-y-auto custom-scrollbar">
        <div class="mb-2 px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider">
            Menu
        </div>
        <template v-for="(item, index) in menuItems" :key="index">
          <!-- Single Link -->
          <router-link
            v-if="!item.children"
            :to="item.path"
            class="flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-300 group relative overflow-hidden"
            :class="isActive(item.path) 
                ? 'bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-lg shadow-blue-500/25' 
                : 'text-slate-400 hover:text-white hover:bg-slate-800/50'"
          >
            <component 
                :is="item.icon" 
                class="w-5 h-5 flex-shrink-0 transition-transform duration-300"
                :class="isActive(item.path) ? 'scale-110' : 'group-hover:scale-110'"
            />
            <span class="font-medium relative z-10">{{ item.label }}</span>
            <div v-if="isActive(item.path)" class="absolute inset-0 bg-white/10 opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
          </router-link>

          <!-- Group (Disclosure) -->
          <Disclosure v-else as="div" v-slot="{ open }" :defaultOpen="isGroupActive(item)">
             <DisclosureButton
                class="w-full flex items-center justify-between px-4 py-3 rounded-xl text-left transition-all duration-300 group"
                :class="isGroupActive(item) 
                    ? 'bg-slate-800 text-white' 
                    : 'text-slate-400 hover:text-white hover:bg-slate-800/50'"
             >
                <div class="flex items-center gap-3">
                    <component 
                        :is="item.icon" 
                        class="w-5 h-5 flex-shrink-0 transition-colors duration-300"
                        :class="isGroupActive(item) ? 'text-blue-400' : 'group-hover:text-blue-400'"
                    />
                    <span class="font-medium">{{ item.label }}</span>
                </div>
                <svg
                    :class="open ? 'rotate-180 text-blue-400' : 'text-slate-500 group-hover:text-slate-400'"
                    class="h-4 w-4 transition-all duration-300"
                    fill="none" viewBox="0 0 24 24" stroke="currentColor"
                >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
             </DisclosureButton>
             <transition
                enter-active-class="transition-all duration-300 ease-in-out"
                enter-from-class="max-h-0 opacity-0 transform -translate-y-2"
                enter-to-class="max-h-96 opacity-100 transform translate-y-0"
                leave-active-class="transition-all duration-200 ease-in-out"
                leave-from-class="max-h-96 opacity-100 transform translate-y-0"
                leave-to-class="max-h-0 opacity-0 transform -translate-y-2"
             >
                <DisclosurePanel class="ml-4 pl-4 border-l-2 border-slate-700/50 my-1 space-y-1 overflow-hidden">
                    <router-link
                        v-for="child in item.children"
                        :key="child.path"
                        :to="child.path"
                        class="flex items-center gap-2 px-4 py-2.5 rounded-lg text-sm font-medium transition-all duration-200"
                        :class="route.path === child.path 
                            ? 'text-white bg-blue-500/10 border border-blue-500/20' 
                            : 'text-slate-500 hover:text-slate-200 hover:bg-slate-800/30'"
                    >
                        <span class="w-1.5 h-1.5 rounded-full transition-colors duration-300" :class="route.path === child.path ? 'bg-blue-400' : 'bg-slate-600 group-hover:bg-slate-500'"></span>
                        {{ child.label }}
                    </router-link>
                </DisclosurePanel>
             </transition>
          </Disclosure>
        </template>
      </nav>

      <!-- User Section -->
      <div class="p-4 border-t border-slate-800 bg-slate-900/50">
        <div class="flex items-center gap-3 p-3 rounded-xl bg-slate-800/50 border border-slate-700/50 hover:bg-slate-800 transition-all duration-300 group">
            <div class="relative">
                <div class="w-10 h-10 bg-gradient-to-tr from-purple-500 to-pink-500 rounded-full flex items-center justify-center shadow-lg ring-2 ring-slate-900">
                    <span class="text-sm font-bold text-white">{{ getInitials() }}</span>
                </div>
                <div class="absolute bottom-0 right-0 w-3 h-3 bg-green-500 border-2 border-slate-900 rounded-full"></div>
            </div>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-semibold text-white truncate">{{ admin?.fullName || 'Admin' }}</p>
              <p class="text-xs text-slate-400 truncate group-hover:text-blue-400 transition-colors">{{ admin?.email || '' }}</p>
            </div>
            <button
                @click="handleLogout"
                class="p-2 text-slate-400 hover:text-red-400 hover:bg-red-500/10 rounded-lg transition-all"
                title="Logout"
            >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
                </svg>
            </button>
        </div>
      </div>
    </aside>
    
    <!-- Main Content -->
    <div class="flex-1 flex flex-col min-w-0 transition-all bg-gray-50">
      <!-- Header -->
      <header class="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-30">
        <div class="px-8 py-4 flex justify-between items-center">
          <h1 class="text-2xl font-bold text-gray-900">{{ pageTitle }}</h1>
          <!-- Add notification bell or other header items here -->
           <button class="p-2 text-gray-400 hover:text-gray-600 rounded-full hover:bg-gray-100 transition-colors">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" /></svg>
           </button>
        </div>
      </header>
      
      <!-- Page Content -->
      <main class="flex-1 p-8 overflow-auto bg-gray-50/50">
        <RouterView v-slot="{ Component }">
             <transition
                enter-active-class="transition ease-out duration-200"
                enter-from-class="opacity-0 translate-y-2"
                enter-to-class="opacity-100 translate-y-0"
                mode="out-in"
             >
                <component :is="Component" />
             </transition>
        </RouterView>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAdminAuthStore } from '@/stores/adminAuthStore';
import { Disclosure, DisclosureButton, DisclosurePanel } from '@headlessui/vue';
import { 
    HomeIcon, 
    CubeIcon, 
    ShoppingBagIcon, 
    UsersIcon, 
    PhotoIcon,
    TagIcon
} from '@heroicons/vue/24/outline';
import LoadingBar from '@/components/common/LoadingBar.vue';

const route = useRoute();
const router = useRouter();
const adminAuthStore = useAdminAuthStore();
const admin = adminAuthStore.admin;

// Map icon strings to components
const iconMap: Record<string, any> = {
    'HomeIcon': HomeIcon,
    'CubeIcon': CubeIcon,
    'ShoppingBagIcon': ShoppingBagIcon,
    'UsersIcon': UsersIcon,
    'PhotoIcon': PhotoIcon,
    'TagIcon': TagIcon
};

interface MenuItem {
    label: string;
    path: string;
    icon?: any;
    children?: MenuItem[];
    model?: string;
}

const menuItems = computed(() => {
    const adminRoute = router.options.routes.find(r => r.path === '/admin');
    if (!adminRoute || !adminRoute.children) return [];

    const items: MenuItem[] = [];
    const groups: Record<string, MenuItem> = {};

    adminRoute.children.forEach(child => {
        if (!child.meta?.showInSidebar) return;

        const path = child.path ? `/admin/${child.path}` : '/admin';
        const label = (child.meta.label as string) || (child.meta.title as string);
        const iconName = child.meta.icon as string;
        const icon = iconMap[iconName];
        const groupName = child.meta.model as string;

        if (groupName) {
            if (!groups[groupName]) {
                groups[groupName] = {
                    label: groupName,
                    path: '', // path for group is strictly for key purposes, actual navigation is handled by children
                    icon: icon,
                    children: [],
                    model: groupName
                };
                items.push(groups[groupName]);
            }
            groups[groupName].children?.push({
                label: label,
                path: path
            });
        } else {
            items.push({
                label: label,
                path: path,
                icon: icon
            });
        }
    });

    return items;
});

const pageTitle = computed(() => {
    return route.meta.title as string || 'Admin Panel';
});

function isActive(path: string): boolean {
  if (path === '/admin') {
    return route.path === '/admin';
  }
  return route.path.startsWith(path);
}

function isGroupActive(item: MenuItem): boolean {
    if (!item.children) return false;
    return item.children.some(child => isActive(child.path));
}

function getInitials(): string {
  const name = admin?.fullName || 'Admin';
  return name
    .split(' ')
    .map(n => n[0])
    .join('')
    .toUpperCase()
    .substring(0, 2);
}

async function handleLogout() {
  adminAuthStore.logout();
  router.push('/admin/login');
}
</script>
