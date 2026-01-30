<template>
  <div class="min-h-screen bg-gray-50 flex">
    <!-- Sidebar -->
    <aside class="w-64 bg-gray-900 text-white flex flex-col transition-all duration-300">
      <!-- Logo -->
      <div class="p-6 border-b border-gray-800">
        <div class="flex items-center space-x-3">
          <div class="w-10 h-10 bg-blue-600 rounded-lg flex items-center justify-center shadow-lg transform hover:rotate-3 transition-transform">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"></path>
            </svg>
          </div>
          <div>
            <h2 class="text-lg font-bold tracking-wide">Admin Panel</h2>
            <p class="text-xs text-gray-400">Vu Emporium</p>
          </div>
        </div>
      </div>

      <!-- Navigation -->
      <nav class="flex-1 p-4 space-y-1 overflow-y-auto">
        <router-link
          to="/admin"
          class="flex items-center space-x-3 px-4 py-3 rounded-lg transition-all duration-200"
          :class="isActive('/admin') ? 'bg-blue-600 text-white shadow-md' : 'text-gray-300 hover:bg-gray-800 hover:text-white'"
        >
          <svg class="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
          </svg>
          <span class="font-medium">Dashboard</span>
        </router-link>

        <!-- Products Disclosure -->
        <Disclosure as="div" v-slot="{ open }" :defaultOpen="isActive('/admin/products')">
             <DisclosureButton
                class="flex w-full items-center justify-between rounded-lg px-4 py-3 text-left transition-all duration-200"
                :class="isActive('/admin/products') ? 'bg-gray-800 text-white' : 'text-gray-300 hover:bg-gray-800 hover:text-white'"
             >
                <div class="flex items-center space-x-3">
                    <svg class="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
                    </svg>
                    <span class="font-medium">Products</span>
                </div>
                <svg
                    :class="open ? 'rotate-180 transform' : ''"
                    class="h-4 w-4 text-gray-400 transition-transform duration-200"
                    fill="none" viewBox="0 0 24 24" stroke="currentColor"
                >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
             </DisclosureButton>
             <transition
                enter-active-class="transition duration-100 ease-out"
                enter-from-class="transform scale-95 opacity-0"
                enter-to-class="transform scale-100 opacity-100"
                leave-active-class="transition duration-75 ease-out"
                leave-from-class="transform scale-100 opacity-100"
                leave-to-class="transform scale-95 opacity-0"
             >
                <DisclosurePanel class="pl-12 pr-4 py-2 space-y-1">
                    <router-link
                        to="/admin/products"
                        class="block rounded-md px-3 py-2 text-sm font-medium transition-colors"
                        :class="route.path === '/admin/products' ? 'text-blue-400 bg-gray-800' : 'text-gray-400 hover:text-white hover:bg-gray-800'"
                    >
                        All Products
                    </router-link>
                     <a href="#" class="block rounded-md px-3 py-2 text-sm font-medium text-gray-400 hover:text-white hover:bg-gray-800 transition-colors opacity-50 cursor-not-allowed">
                        Categories (Soon)
                     </a>
                     <a href="#" class="block rounded-md px-3 py-2 text-sm font-medium text-gray-400 hover:text-white hover:bg-gray-800 transition-colors opacity-50 cursor-not-allowed">
                        Inventory (Soon)
                     </a>
                </DisclosurePanel>
             </transition>
        </Disclosure>

        <router-link
          to="/admin/orders"
          class="flex items-center space-x-3 px-4 py-3 rounded-lg transition-all duration-200"
          :class="isActive('/admin/orders') ? 'bg-blue-600 text-white shadow-md' : 'text-gray-300 hover:bg-gray-800 hover:text-white'"
        >
          <svg class="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"></path>
          </svg>
          <span class="font-medium">Orders</span>
        </router-link>

        <router-link
          to="/admin/users"
          class="flex items-center space-x-3 px-4 py-3 rounded-lg transition-all duration-200"
          :class="isActive('/admin/users') ? 'bg-blue-600 text-white shadow-md' : 'text-gray-300 hover:bg-gray-800 hover:text-white'"
        >
          <svg class="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>
          </svg>
          <span class="font-medium">Customers</span>
        </router-link>
      </nav>

      <!-- User Section -->
      <div class="p-4 border-t border-gray-800">
        <div class="flex items-center justify-between mb-3">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-full flex items-center justify-center shadow-inner">
              <span class="text-sm font-bold text-white">{{ getInitials() }}</span>
            </div>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-medium truncate text-white">{{ admin?.fullName || 'Admin' }}</p>
              <p class="text-xs text-gray-400 truncate">{{ admin?.email || '' }}</p>
            </div>
          </div>
        </div>
        <button
          @click="handleLogout"
          class="w-full flex items-center justify-center space-x-2 px-4 py-2 bg-gray-800 hover:bg-red-900/50 rounded-lg transition-colors text-sm text-gray-300 hover:text-white border border-gray-700 hover:border-red-800"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
          </svg>
          <span>Logout</span>
        </button>
      </div>
    </aside>
    
    <!-- Main Content -->
    <div class="flex-1 flex flex-col min-w-0 transition-all">
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

const route = useRoute();
const router = useRouter();
const adminAuthStore = useAdminAuthStore();
const admin = adminAuthStore.admin;

const pageTitle = computed(() => {
  const titles: Record<string, string> = {
    'admin-dashboard': 'Dashboard',
    'admin-products': 'Product Management',
    'admin-orders': 'Order Management',
    'admin-users': 'Customer Management',
  };
  return titles[route.name as string] || 'Admin';
});

function isActive(path: string): boolean {
  if (path === '/admin') {
    return route.path === '/admin';
  }
  return route.path.startsWith(path);
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
