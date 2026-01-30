<template>
  <div class="min-h-screen bg-gray-50 flex flex-col font-sans">
    <!-- Modern Header with Gradient -->
    <header class="sticky top-0 z-50 bg-white/80 backdrop-blur-lg border-b border-gray-200 shadow-sm">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <!-- Logo -->
          <div class="flex items-center">
            <router-link to="/" class="flex items-center space-x-2 group">
              <div class="w-10 h-10 bg-gradient-to-br from-primary-600 to-primary-800 rounded-lg flex items-center justify-center transform group-hover:scale-110 transition-transform duration-200 shadow-md">
                <span class="text-white font-bold text-xl">V</span>
              </div>
              <span class="text-2xl font-display font-bold gradient-text hidden sm:block tracking-tight">Vu Emporium</span>
            </router-link>
          </div>
          
          <!-- Desktop Navigation -->
          <nav class="hidden md:flex space-x-8">
            <router-link 
              to="/" 
              class="text-gray-600 hover:text-primary-600 font-medium transition-colors duration-200 relative group py-2"
            >
              Home
              <span class="absolute bottom-0 left-0 w-0 h-0.5 bg-gradient-to-r from-primary-600 to-primary-800 group-hover:w-full transition-all duration-300"></span>
            </router-link>
            <router-link 
              to="/products" 
              class="text-gray-600 hover:text-primary-600 font-medium transition-colors duration-200 relative group py-2"
            >
              Products
              <span class="absolute bottom-0 left-0 w-0 h-0.5 bg-gradient-to-r from-primary-600 to-primary-800 group-hover:w-full transition-all duration-300"></span>
            </router-link>
          </nav>
          
          <!-- Right Actions -->
          <div class="flex items-center space-x-4">
            <!-- Cart Icon with Badge -->
            <router-link to="/cart" class="relative group">
              <div class="p-2 hover:bg-primary-50 rounded-full transition-colors duration-200">
                <svg class="w-6 h-6 text-gray-600 group-hover:text-primary-600 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
                </svg>
              </div>
              <span 
                v-if="cartStore.totalItems > 0" 
                class="absolute -top-1 -right-1 bg-gradient-to-r from-accent-500 to-accent-600 text-white text-xs font-bold rounded-full w-5 h-5 flex items-center justify-center animate-scale-in shadow-sm ring-2 ring-white"
              >
                {{ cartStore.totalItems }}
              </span>
            </router-link>
            
            <!-- User Menu (Headless UI) -->
            <Menu as="div" class="relative inline-block text-left" v-if="authStore.isAuthenticated">
                <div>
                     <MenuButton class="inline-flex justify-center w-full rounded-full border border-gray-200 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-100 focus:ring-primary-500 transition-all">
                        My Account
                        <svg class="-mr-1 ml-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
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
                    <MenuItems class="origin-top-right absolute right-0 mt-2 w-56 rounded-xl shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none divide-y divide-gray-100 z-50">
                        <div class="px-4 py-3">
                            <p class="text-sm">Signed in as</p>
                            <p class="text-sm font-medium text-gray-900 truncate">{{ authStore.user?.email || 'User' }}</p>
                        </div>
                        <div class="py-1">
                            <MenuItem v-slot="{ active }">
                                <router-link to="/orders" :class="[active ? 'bg-gray-50 text-gray-900' : 'text-gray-700', 'group flex items-center px-4 py-2 text-sm']">
                                    <svg class="mr-3 h-5 w-5 text-gray-400 group-hover:text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" /></svg>
                                    My Orders
                                </router-link>
                            </MenuItem>
                            <MenuItem v-slot="{ active }">
                                <router-link to="/profile" :class="[active ? 'bg-gray-50 text-gray-900' : 'text-gray-700', 'group flex items-center px-4 py-2 text-sm']">
                                    <svg class="mr-3 h-5 w-5 text-gray-400 group-hover:text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
                                    Profile
                                </router-link>
                            </MenuItem>
                        </div>
                        <div class="py-1">
                             <MenuItem v-slot="{ active }">
                                <button @click="handleLogout" :class="[active ? 'bg-gray-50 text-gray-900' : 'text-gray-700', 'group flex w-full items-center px-4 py-2 text-sm text-left']">
                                    <svg class="mr-3 h-5 w-5 text-gray-400 group-hover:text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" /></svg>
                                    Sign out
                                </button>
                            </MenuItem>
                        </div>
                    </MenuItems>
                </transition>
            </Menu>

            <template v-else>
               <div class="hidden md:flex space-x-2">
                  <router-link to="/login" class="px-4 py-2 text-sm font-medium text-gray-700 hover:text-primary-600 transition-colors">
                    Log in
                  </router-link>
                  <router-link to="/register" class="px-4 py-2 text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 rounded-full shadow-sm hover:shadow transition-all">
                    Sign up
                  </router-link>
               </div>
            </template>
            
            <!-- Mobile Menu Button (Hamburger) - Simplified -->
            <div class="md:hidden flex items-center">
                 <button class="text-gray-500 hover:text-gray-900 focus:outline-none">
                    <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                    </svg>
                 </button>
            </div>
          </div>
        </div>
      </div>
    </header>
    
    <!-- Main Content -->
    <main class="flex-1 animate-fade-in">
      <RouterView />
    </main>
    
    <!-- Modern Footer -->
    <footer class="bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 text-white mt-auto">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
          <!-- Brand -->
          <div class="col-span-1 md:col-span-2">
            <div class="flex items-center space-x-2 mb-4">
              <div class="w-10 h-10 bg-gradient-to-br from-primary-600 to-primary-800 rounded-lg flex items-center justify-center">
                <span class="text-white font-bold text-xl">V</span>
              </div>
              <span class="text-2xl font-display font-bold">Vu Emporium</span>
            </div>
            <p class="text-gray-400 mb-4 max-w-md text-sm leading-relaxed">
              Your premier destination for quality products at amazing prices. Shop with confidence, style, and peace of mind.
            </p>
            <div class="flex space-x-4">
               <!-- Social Icons (Placeholder) -->
               <div class="flex space-x-3">
                   <a href="#" class="w-8 h-8 rounded-full bg-white/10 flex items-center justify-center hover:bg-white/20 transition-colors">
                        <span class="sr-only">Facebook</span>
                        <svg class="h-4 w-4" fill="currentColor" viewBox="0 0 24 24"><path fill-rule="evenodd" d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.988C18.343 21.128 22 16.991 22 12z" clip-rule="evenodd" /></svg>
                   </a>
                   <a href="#" class="w-8 h-8 rounded-full bg-white/10 flex items-center justify-center hover:bg-white/20 transition-colors">
                        <span class="sr-only">Twitter</span>
                        <svg class="h-4 w-4" fill="currentColor" viewBox="0 0 24 24"><path d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84" /></svg>
                   </a>
               </div>
            </div>
          </div>
          
          <!-- Quick Links -->
          <div>
            <h3 class="font-display font-bold mb-4 text-gray-200">Quick Links</h3>
            <ul class="space-y-2 text-sm">
              <li><router-link to="/" class="text-gray-400 hover:text-white transition-colors">Home</router-link></li>
              <li><router-link to="/products" class="text-gray-400 hover:text-white transition-colors">Shop All</router-link></li>
              <li><router-link to="/cart" class="text-gray-400 hover:text-white transition-colors">My Cart</router-link></li>
            </ul>
          </div>
          
          <!-- Customer Service -->
          <div>
            <h3 class="font-display font-bold mb-4 text-gray-200">Help</h3>
            <ul class="space-y-2 text-sm">
              <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Shipping & Returns</a></li>
              <li><a href="#" class="text-gray-400 hover:text-white transition-colors">FAQ</a></li>
              <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Contact Support</a></li>
            </ul>
          </div>
        </div>
        
        <!-- Bottom Bar -->
        <div class="border-t border-gray-700 pt-8 flex flex-col md:flex-row justify-between items-center text-xs text-gray-500">
          <p>&copy; 2024 Vu Emporium. All rights reserved.</p>
          <div class="flex space-x-6 mt-4 md:mt-0">
            <a href="#" class="hover:text-white transition-colors">Privacy Policy</a>
            <a href="#" class="hover:text-white transition-colors">Terms of Service</a>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore';
import { useCartStore } from '@/stores/cartStore';
import { useRouter } from 'vue-router';
import { Menu, MenuButton, MenuItems, MenuItem } from '@headlessui/vue';

const authStore = useAuthStore();
const cartStore = useCartStore();
const router = useRouter();

async function handleLogout() {
  await authStore.logout();
  router.push('/');
}
</script>
