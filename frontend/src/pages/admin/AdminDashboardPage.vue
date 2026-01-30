<template>
  <div class="space-y-6">
    <!-- Welcome Header -->
    <div class="bg-gradient-to-r from-blue-600 to-blue-700 rounded-xl p-6 text-white">
      <h2 class="text-2xl font-bold mb-2">Welcome back, {{ admin?.fullName || 'Admin' }}!</h2>
      <p class="text-blue-100">Here's what's happening with your store today.</p>
    </div>

    <!-- Stats Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <!-- Total Revenue -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <div class="flex items-center justify-between mb-4">
          <div class="p-3 bg-green-100 rounded-lg">
            <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
          </div>
        </div>
        <h3 class="text-gray-600 text-sm font-medium mb-1">Total Revenue</h3>
        <p class="text-2xl font-bold text-gray-900">${{ formatNumber(stats?.totalRevenue || 0) }}</p>
      </div>

      <!-- Total Orders -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <div class="flex items-center justify-between mb-4">
          <div class="p-3 bg-blue-100 rounded-lg">
            <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"></path>
            </svg>
          </div>
        </div>
        <h3 class="text-gray-600 text-sm font-medium mb-1">Total Orders</h3>
        <p class="text-2xl font-bold text-gray-900">{{ stats?.totalOrders || 0 }}</p>
        <p class="text-sm text-gray-500 mt-1">{{ stats?.pendingOrders || 0 }} pending</p>
      </div>

      <!-- Total Products -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <div class="flex items-center justify-between mb-4">
          <div class="p-3 bg-purple-100 rounded-lg">
            <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
            </svg>
          </div>
        </div>
        <h3 class="text-gray-600 text-sm font-medium mb-1">Total Products</h3>
        <p class="text-2xl font-bold text-gray-900">{{ stats?.totalProducts || 0 }}</p>
        <p class="text-sm text-gray-500 mt-1">{{ stats?.activeProducts || 0 }} active</p>
      </div>

      <!-- Total Customers -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <div class="flex items-center justify-between mb-4">
          <div class="p-3 bg-orange-100 rounded-lg">
            <svg class="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
            </svg>
          </div>
        </div>
        <h3 class="text-gray-600 text-sm font-medium mb-1">Total Customers</h3>
        <p class="text-2xl font-bold text-gray-900">{{ stats?.totalCustomers || 0 }}</p>
      </div>
    </div>

    <!-- Quick Actions -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <router-link to="/admin/products" class="bg-white rounded-xl shadow-sm p-6 border border-gray-200 hover:shadow-md transition group">
        <div class="flex items-center justify-between">
          <div>
            <h3 class="text-lg font-semibold text-gray-900 mb-1">Manage Products</h3>
            <p class="text-sm text-gray-500">Add, edit, or remove products</p>
          </div>
          <svg class="w-6 h-6 text-gray-400 group-hover:text-blue-600 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
          </svg>
        </div>
      </router-link>

      <router-link to="/admin/orders" class="bg-white rounded-xl shadow-sm p-6 border border-gray-200 hover:shadow-md transition group">
        <div class="flex items-center justify-between">
          <div>
            <h3 class="text-lg font-semibold text-gray-900 mb-1">View Orders</h3>
            <p class="text-sm text-gray-500">Process and track orders</p>
          </div>
          <svg class="w-6 h-6 text-gray-400 group-hover:text-blue-600 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
          </svg>
        </div>
      </router-link>

      <router-link to="/admin/users" class="bg-white rounded-xl shadow-sm p-6 border border-gray-200 hover:shadow-md transition group">
        <div class="flex items-center justify-between">
          <div>
            <h3 class="text-lg font-semibold text-gray-900 mb-1">Manage Users</h3>
            <p class="text-sm text-gray-500">View and manage customers</p>
          </div>
          <svg class="w-6 h-6 text-gray-400 group-hover:text-blue-600 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
          </svg>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useAdminAuthStore } from '@/stores/adminAuthStore';
import axios from 'axios';

interface DashboardStats {
  totalRevenue: number;
  totalOrders: number;
  pendingOrders: number;
  completedOrders: number;
  totalProducts: number;
  totalCustomers: number;
  activeProducts: number;
  outOfStockProducts: number;
}

const adminAuthStore = useAdminAuthStore();
const admin = adminAuthStore.admin;
const stats = ref<DashboardStats | null>(null);

onMounted(async () => {
  await fetchDashboardStats();
});

async function fetchDashboardStats() {
  try {
    const response = await axios.get<{ data: DashboardStats }>('/api/admin/dashboard/stats', {
      headers: {
        Authorization: `Bearer ${adminAuthStore.token}`,
      },
    });
    stats.value = response.data.data;
  } catch (error) {
    console.error('Failed to fetch dashboard stats:', error);
  }
}

function formatNumber(num: number): string {
  return new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(num);
}
</script>
