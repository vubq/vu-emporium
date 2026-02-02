<template>
  <div class="bg-gray-50 min-h-screen p-6 font-sans">
    <div class="w-full h-full flex flex-col">
      <!-- Header -->
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
        <div>
          <h2 class="text-2xl font-bold text-gray-900 tracking-tight">Product Management</h2>
          <p class="text-sm text-gray-500 mt-1">Manage your catalog, inventory, and pricing.</p>
        </div>
        <button @click="openCreateModal" class="px-5 py-2.5 rounded-xl bg-gray-900 text-white hover:bg-black font-medium transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 flex items-center justify-center text-sm">
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
            Add New Product
        </button>
      </div>

      <!-- Filters Bar -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-4 mb-6">
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4 items-end">
                <!-- Search -->
                <div class="lg:col-span-1">
                    <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">Search</label>
                    <div class="relative">
                        <input v-model="filters.search" @keyup.enter="fetchProducts" type="text" placeholder="Product name, SKU..." class="w-full pl-9 pr-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <svg class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
                        </div>
                    </div>
                </div>

                <!-- Brand -->
                <div class="lg:col-span-1">
                    <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">Brand</label>
                    <input v-model="filters.brand" type="text" placeholder="Filter by brand..." class="w-full px-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
                </div>

                <!-- Category -->
                <div class="lg:col-span-1">
                    <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">Category</label>
                    <Combobox v-model="selectedCategory" nullable by="id">
                        <div class="relative">
                            <div class="relative w-full text-left">
                                <ComboboxInput
                                    class="w-full rounded-xl border border-gray-300 bg-white py-2 pl-3 pr-10 text-sm text-gray-900 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500 transition-all"
                                    :displayValue="(category: any) => category?.name"
                                    @change="query = $event.target.value"
                                    placeholder="All Categories"
                                />
                                <ComboboxButton class="absolute inset-y-0 right-0 flex items-center pr-2">
                                    <svg class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                                </ComboboxButton>
                            </div>
                            <TransitionRoot
                                leave="transition ease-in duration-100"
                                leaveFrom="opacity-100"
                                leaveTo="opacity-0"
                                @after-leave="query = ''"
                            >
                                <ComboboxOptions class="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                                    <div v-if="filteredCategories.length === 0 && query !== ''" class="relative cursor-default select-none py-2 px-4 text-gray-700">
                                        Nothing found.
                                    </div>
                                    <ComboboxOption :value="null" as="template" v-slot="{ selected, active }">
                                        <li class="relative cursor-pointer select-none py-2 pl-10 pr-4" :class="{ 'bg-indigo-600 text-white': active, 'text-gray-900': !active }">
                                            <span class="block truncate" :class="{ 'font-medium': selected, 'font-normal': !selected }">All Categories</span>
                                        </li>
                                    </ComboboxOption>
                                    <ComboboxOption
                                        v-for="category in filteredCategories"
                                        :key="category.id"
                                        :value="category"
                                        as="template"
                                        v-slot="{ selected, active }"
                                    >
                                        <li class="relative cursor-pointer select-none py-2 pl-10 pr-4" :class="{ 'bg-indigo-600 text-white': active, 'text-gray-900': !active }">
                                            <span class="block truncate" :class="{ 'font-medium': selected, 'font-normal': !selected }">
                                                {{ category.name }}
                                            </span>
                                            <span v-if="selected" class="absolute inset-y-0 left-0 flex items-center pl-3" :class="{ 'text-white': active, 'text-indigo-600': !active }">
                                                <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" /></svg>
                                            </span>
                                        </li>
                                    </ComboboxOption>
                                </ComboboxOptions>
                            </TransitionRoot>
                        </div>
                    </Combobox>
                </div>

                <!-- Status -->
                <div class="lg:col-span-1">
                        <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">Status</label>
                        <Listbox v-model="filters.status">
                        <div class="relative">
                            <ListboxButton class="relative w-full cursor-pointer rounded-xl bg-white py-2 pl-3 pr-10 text-left border border-gray-300 shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                                <span class="block truncate font-medium" :class="statusColor(filters.status)">{{ filters.status || 'All Statuses' }}</span>
                                <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                                    <svg class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                                </span>
                            </ListboxButton>
                            <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
                                <ListboxOptions class="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                                    <ListboxOption :value="null" as="template" v-slot="{ active, selected }">
                                        <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4']">
                                            <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">All Statuses</span>
                                        </li>
                                    </ListboxOption>
                                    <ListboxOption v-for="status in ['ACTIVE', 'DRAFT', 'ARCHIVED']" :key="status" :value="status" as="template" v-slot="{ active, selected }">
                                        <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-10 pr-4']">
                                            <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">{{ status }}</span>
                                            <span v-if="selected" class="absolute inset-y-0 left-0 flex items-center pl-3 text-indigo-600">
                                                <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" /></svg>
                                            </span>
                                        </li>
                                    </ListboxOption>
                                </ListboxOptions>
                            </transition>
                        </div>
                        </Listbox>
                </div>

                <!-- Price Range -->
                <div class="lg:col-span-1">
                        <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-1.5">Price Range</label>
                        <div class="flex items-center gap-2">
                            <div class="relative flex-1">
                                <div class="absolute inset-y-0 left-0 pl-2.5 flex items-center pointer-events-none">
                                    <span class="text-gray-500 text-xs">$</span>
                                </div>
                                <input v-model.number="filters.minPrice" type="number" placeholder="Min" class="w-full pl-5 pr-2 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
                            </div>
                            <span class="text-gray-400">-</span>
                            <div class="relative flex-1">
                                <div class="absolute inset-y-0 left-0 pl-2.5 flex items-center pointer-events-none">
                                    <span class="text-gray-500 text-xs">$</span>
                                </div>
                                <input v-model.number="filters.maxPrice" type="number" placeholder="Max" class="w-full pl-5 pr-2 py-2 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all text-sm" />
                            </div>
                        </div>
                </div>

                <!-- Buttons -->
                <div class="lg:col-span-1 flex gap-2">
                    <button @click="fetchProducts" class="flex-1 py-2 bg-indigo-600 text-white rounded-xl font-medium shadow-sm hover:bg-indigo-700 transition-all flex items-center justify-center text-sm" :disabled="loading">
                        <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                        </svg>
                        Filter
                    </button>
                     <button @click="resetFilters" class="px-4 py-2 text-indigo-600 hover:text-indigo-800 font-medium transition-all text-sm" :disabled="loading">
                        Reset
                    </button>
                </div>
          </div>
      </div>

      <!-- Product List -->
      <div class="flex-1 flex flex-col min-h-0 bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden relative">
        <!-- Loading Overlay -->
        <Transition
            enter-active-class="transition ease-out duration-200"
            enter-from-class="opacity-0"
            enter-to-class="opacity-100"
            leave-active-class="transition ease-in duration-150"
            leave-from-class="opacity-100"
            leave-to-class="opacity-0"
        >
            <div v-if="loading" class="absolute inset-0 z-10 bg-white/60 backdrop-blur-sm flex items-center justify-center">
                <div class="flex flex-col items-center">
                    <svg class="animate-spin h-8 w-8 text-indigo-600 mb-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    <span class="text-sm font-medium text-indigo-600">Loading products...</span>
                </div>
            </div>
        </Transition>

        <div class="flex-1 overflow-auto relative custom-scrollbar">
                <table class="min-w-full divide-y divide-gray-100">
                <thead class="bg-gray-50/80 backdrop-blur-md sticky top-0 z-20">
                <tr>
                    <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Product</th>
                    <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">SKU</th>
                    <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Brand</th>
                    <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Category</th>
                    <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Price</th>
                    <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Stock</th>
                    <th scope="col" class="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
                    <th scope="col" class="relative px-6 py-3">
                    <span class="sr-only">Actions</span>
                    </th>
                </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-50">
                <!-- Skeleton Loader -->
                <tr v-if="loading && products.length === 0" v-for="i in 5" :key="i" class="animate-pulse">
                    <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex items-center">
                            <div class="h-12 w-12 bg-gray-100 rounded-xl"></div>
                            <div class="ml-4 space-y-2">
                                <div class="h-4 w-32 bg-gray-100 rounded"></div>
                                <div class="h-3 w-20 bg-gray-100 rounded"></div>
                            </div>
                        </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap"><div class="h-4 w-20 bg-gray-100 rounded"></div></td>
                    <td class="px-6 py-4 whitespace-nowrap"><div class="h-4 w-24 bg-gray-100 rounded"></div></td>
                    <td class="px-6 py-4 whitespace-nowrap"><div class="h-4 w-16 bg-gray-100 rounded"></div></td>
                    <td class="px-6 py-4 whitespace-nowrap"><div class="h-4 w-12 bg-gray-100 rounded"></div></td>
                    <td class="px-6 py-4 whitespace-nowrap"><div class="h-6 w-20 bg-gray-100 rounded-full"></div></td>
                    <td class="px-6 py-4 whitespace-nowrap"></td>
                </tr>
                
                <!-- Empty State -->
                <tr v-else-if="products.length === 0">
                    <td colspan="7" class="px-6 py-24 text-center">
                        <div class="flex flex-col items-center justify-center">
                            <div class="w-24 h-24 bg-gray-50 rounded-full flex items-center justify-center mb-4">
                                <svg class="w-10 h-10 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" /></svg>
                            </div>
                            <h3 class="text-lg font-bold text-gray-900">No products found</h3>
                            <p class="text-sm text-gray-500 mt-1 max-w-sm mx-auto">We couldn't find any products matching your filters. Try adjusting your search query or create a new product.</p>
                            <button @click="resetFilters" class="mt-4 text-indigo-600 hover:text-indigo-800 font-medium text-sm">Clear all filters</button>
                        </div>
                    </td>
                </tr>

                <!-- Product Rows -->
                <tr v-else v-for="(product, index) in products" :key="product.id" class="group hover:bg-gray-50 transition-colors">
                    <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                        <div class="h-14 w-14 flex-shrink-0 relative group-hover:scale-105 transition-transform duration-300">
                             <AppImage 
                                :src="(product.images && product.images.length > 0) ? product.images[0] : ''" 
                                class="h-full w-full rounded-xl shadow-sm border border-gray-100 bg-white"
                             />
                        </div>
                        <div class="ml-4">
                        <div class="text-sm font-bold text-gray-900 group-hover:text-indigo-600 transition-colors line-clamp-1">{{ product.name }}</div>
                        <div class="text-xs text-gray-500 hidden sm:block truncate max-w-[200px] mt-0.5">{{ product.description || 'No description' }}</div>
                        </div>
                    </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-gray-100 text-gray-800 font-mono border border-gray-200">
                            {{ product.sku || 'NOSKU' }}
                        </span>
                    </td>
                     <td class="px-6 py-4 whitespace-nowrap">
                        <span class="text-sm text-gray-600">{{ product.brand || '-' }}</span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                        <div v-if="product.category" class="flex items-center">
                             <div class="w-2 h-2 rounded-full bg-indigo-500 mr-2"></div>
                            {{ product.category.name }}
                        </div>
                        <span v-else class="text-gray-400 italic text-xs">Uncategorized</span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-bold font-mono tracking-tight">
                        <div v-if="product.salePrice && product.salePrice > 0" class="flex flex-col">
                            <span class="text-indigo-600">${{ product.salePrice.toFixed(2) }}</span>
                            <span class="text-gray-400 text-xs line-through">${{ product.basePrice?.toFixed(2) }}</span>
                        </div>
                        <span v-else>${{ product.basePrice?.toFixed(2) || '0.00' }}</span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <div v-if="product.hasVariants" class="flex items-center text-xs text-purple-600 font-medium bg-purple-50 px-2.5 py-1 rounded-lg border border-purple-100 w-fit">
                            <svg class="w-3.5 h-3.5 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" /></svg>
                            Variants
                        </div>
                        <div v-else class="flex items-center">
                            <span class="block h-2 w-2 rounded-full mr-2" :class="[product.stockQuantity < 5 ? 'bg-red-500 animate-pulse' : (product.stockQuantity < 20 ? 'bg-yellow-500' : 'bg-green-500')]"></span>
                            <span :class="[product.stockQuantity < 5 ? 'text-red-700 font-bold' : (product.stockQuantity < 20 ? 'text-yellow-700' : 'text-gray-900'), 'text-sm font-medium']">
                                {{ product.stockQuantity }}
                            </span>
                        </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                    <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium border"
                            :class="{
                            'bg-green-50 text-green-700 border-green-200': product.status === 'ACTIVE',
                            'bg-yellow-50 text-yellow-700 border-yellow-200': product.status === 'DRAFT',
                            'bg-gray-50 text-gray-600 border-gray-200': product.status === 'ARCHIVED'
                            }">
                        <span class="h-1.5 w-1.5 rounded-full mr-1.5" :class="{
                            'bg-green-500': product.status === 'ACTIVE',
                            'bg-yellow-500': product.status === 'DRAFT',
                            'bg-gray-400': product.status === 'ARCHIVED'
                        }"></span>
                        {{ product.status }}
                    </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <Menu as="div" class="relative inline-block text-left">
                        <div>
                            <MenuButton class="flex items-center justify-center h-8 w-8 rounded-full text-gray-400 hover:text-indigo-600 hover:bg-indigo-50 focus:outline-none transition-all">
                                <span class="sr-only">Open options</span>
                                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                                    <path d="M10 3a1.5 1.5 0 110 3 1.5 1.5 0 010-3zM10 8.5a1.5 1.5 0 110 3 1.5 1.5 0 010-3zM11.5 15.5a1.5 1.5 0 10-3 0 1.5 1.5 0 003 0z" />
                                </svg>
                            </MenuButton>
                        </div>

                        <transition enter-active-class="transition ease-out duration-100" enter-from-class="transform opacity-0 scale-95" enter-to-class="transform opacity-100 scale-100" leave-active-class="transition ease-in duration-75" leave-from-class="transform opacity-100 scale-100" leave-to-class="transform opacity-0 scale-95">
                            <MenuItems 
                                class="absolute right-0 w-48 rounded-xl shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-50 divide-y divide-gray-100"
                                :class="[
                                    (Number(index) >= products.length - 3 && products.length > 4) ? 'bottom-full mb-2 origin-bottom-right' : 'mt-2 origin-top-right'
                                ]"
                            >
                                <div class="py-1">
                                    <MenuItem v-slot="{ active }">
                                        <button @click="openEditModal(product)" :class="[active ? 'bg-indigo-50 text-indigo-700' : 'text-gray-700', 'group flex items-center px-4 py-3 text-sm w-full text-left transition-colors']">
                                            <svg class="mr-3 h-4 w-4 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
                                            Edit Product
                                        </button>
                                    </MenuItem>
                                </div>
                                <div class="py-1">
                                    <MenuItem v-slot="{ active }">
                                        <button @click="deleteProduct(product)" 
                                            :disabled="product.status === 'ACTIVE' || product.status === 'ARCHIVED'"
                                            :class="[
                                                active ? 'bg-red-50 text-red-700' : 'text-red-600',
                                                (product.status === 'ACTIVE' || product.status === 'ARCHIVED') ? 'opacity-50 cursor-not-allowed' : 'hover:bg-red-50',
                                                'group flex items-center px-4 py-3 text-sm w-full text-left transition-colors'
                                            ]">
                                                <svg class="mr-3 h-4 w-4 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                                            Delete Product
                                        </button>
                                    </MenuItem>
                                </div>
                            </MenuItems>
                        </transition>
                    </Menu>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

            <!-- Pagination -->
            <!-- Pagination -->
            <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
                <!-- Mobile Pagination -->
                <div class="flex-1 flex justify-between sm:hidden">
                    <button @click="changePage(currentPage - 1)" :disabled="currentPage === 0" class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50">Previous</button>
                    <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages - 1" class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50">Next</button>
                </div>
                <!-- Desktop Pagination -->
                <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
                    <div>
                        <p class="text-sm text-gray-700">
                            Showing page <span class="font-bold">{{ currentPage + 1 }}</span> of <span class="font-medium">{{ totalPages }}</span>
                        </p>
                    </div>

                    <div class="flex items-center gap-4">
                        <!-- Page Size Selector (Headless UI Listbox) -->
                        <div class="w-20">
                             <Listbox v-model="pageSize" @update:modelValue="handlePageSizeChange">
                                <div class="relative">
                                    <ListboxButton class="relative w-full cursor-pointer rounded-lg bg-white py-2 pl-3 pr-8 text-left border border-gray-300 shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                                        <span class="block truncate">{{ pageSize }}</span>
                                        <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                                            <svg class="h-4 w-4 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                                                <path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" />
                                            </svg>
                                        </span>
                                    </ListboxButton>
                                    <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
                                        <ListboxOptions class="absolute bottom-full mb-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                                            <ListboxOption v-for="size in [10, 20, 50, 100]" :key="size" :value="size" as="template" v-slot="{ active, selected }">
                                                <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-3 pr-4']">
                                                    <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">{{ size }}</span>
                                                </li>
                                            </ListboxOption>
                                        </ListboxOptions>
                                    </transition>
                                </div>
                            </Listbox>
                        </div>

                        <!-- Pagination Controls -->
                        <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                            <button @click="changePage(0)" :disabled="currentPage === 0" class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:bg-gray-100 disabled:text-gray-300" title="First Page">
                                <span class="sr-only">First</span>
                                <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 19l-7-7 7-7m8 14l-7-7 7-7" /></svg>
                            </button>
                            <button @click="changePage(currentPage - 1)" :disabled="currentPage === 0" class="relative inline-flex items-center px-2 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:bg-gray-100 disabled:text-gray-300" title="Previous">
                                <span class="sr-only">Previous</span>
                                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" /></svg>
                            </button>
                            
                            <span class="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">
                                {{ currentPage + 1 }}
                            </span>

                            <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages - 1" class="relative inline-flex items-center px-2 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:bg-gray-100 disabled:text-gray-300" title="Next">
                                <span class="sr-only">Next</span>
                                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" /></svg>
                            </button>
                                <button @click="changePage(totalPages - 1)" :disabled="currentPage >= totalPages - 1" class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:bg-gray-100 disabled:text-gray-300" title="Last Page">
                                <span class="sr-only">Last</span>
                                <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 5l7 7-7 7M5 5l7 7-7 7" /></svg>
                            </button>
                        </nav>
                    </div>
                </div>
            </div>
    </div>

      <!-- Modal for Product Form (Headless UI) -->
      <TransitionRoot appear :show="showModal" as="template">
        <Dialog as="div" @close="closeModal" class="relative z-50">
            <TransitionChild
                as="template"
                enter="duration-300 ease-out"
                enter-from="opacity-0"
                enter-to="opacity-100"
                leave="duration-200 ease-in"
                leave-from="opacity-100"
                leave-to="opacity-0"
            >
                <div class="fixed inset-0 bg-black/40 backdrop-blur-sm" />
            </TransitionChild>

            <div class="fixed inset-0 overflow-y-auto">
                <div class="flex min-h-full items-center justify-center p-4 text-center">
                    <TransitionChild
                        as="template"
                        enter="duration-300 ease-out"
                        enter-from="opacity-0 scale-95"
                        enter-to="opacity-100 scale-100"
                        leave="duration-200 ease-in"
                        leave-from="opacity-100 scale-100"
                        leave-to="opacity-0 scale-95"
                    >
                        <DialogPanel class="w-full max-w-7xl transform overflow-hidden rounded-2xl bg-white text-left align-middle shadow-2xl transition-all">
                             <div class="max-h-[90vh] overflow-y-auto">
                                <ProductForm :initialData="selectedProduct" :isEdit="!!selectedProduct" @submit="handleFormSubmit" @cancel="closeModal" />
                             </div>
                        </DialogPanel>
                    </TransitionChild>
                </div>
            </div>
        </Dialog>
      </TransitionRoot>

      <!-- Confirmation Modal -->
      <ConfirmModal 
        :isOpen="showDeleteModal" 
        title="Delete Product" 
        message="Are you sure you want to delete this product? This action cannot be undone."
        confirmText="Delete"
        type="danger"
        :loading="deleteLoading"
        @close="closeDeleteModal"
        @confirm="confirmDelete"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue';
import { adminProductApi, categoryApi } from '@/api/productApi';
import type { Product, Category } from '@/types/product';
import ProductForm from '../components/ProductForm.vue';
import ConfirmModal from '@/components/common/ConfirmModal.vue';
import AppImage from '@/components/common/AppImage.vue';
import { 
    Menu, MenuButton, MenuItems, MenuItem,
    Dialog, DialogPanel, TransitionRoot, TransitionChild,
    Listbox, ListboxButton, ListboxOptions, ListboxOption,
    Combobox, ComboboxInput, ComboboxButton, ComboboxOptions, ComboboxOption
} from '@headlessui/vue';

const products = ref<Product[]>([]);
const loading = ref(false);
const currentPage = ref(0);
const pageSize = ref(20);
const totalPages = ref(0);

// Filters
const filters = reactive({
    search: '',
    minPrice: null as number | null,
    maxPrice: null as number | null,
    status: null as string | null,
    brand: ''
});
const categories = ref<Category[]>([]);
const selectedCategory = ref<Category | null>(null);
const query = ref('');

const showModal = ref(false);
const selectedProduct = ref<Product | null>(null);

// Delete Modal State
const showDeleteModal = ref(false);
const productToDelete = ref<number | null>(null);
const deleteLoading = ref(false);

const filteredCategories = computed(() =>
  query.value === ''
    ? categories.value
    : categories.value.filter((category) =>
        category.name
          .toLowerCase()
          .replace(/\s+/g, '')
          .includes(query.value.toLowerCase().replace(/\s+/g, ''))
      )
);

const fetchCategories = async () => {
    try {
        const res = await categoryApi.getCategories();
        categories.value = res.data.data.content || res.data.data;
    } catch (e) {
        console.error("Failed to load categories", e);
    }
};

const fetchProducts = async () => {
    loading.value = true;
    try {
        const res = await adminProductApi.getAllProducts({
            page: currentPage.value,
            size: pageSize.value,
            search: filters.search,
            minPrice: filters.minPrice,
            maxPrice: filters.maxPrice,
            status: filters.status,
            brand: filters.brand,
            categoryId: selectedCategory.value?.id,
            sortBy: 'id',
            sortDir: 'DESC'
        });
        products.value = res.data.data.content;
        totalPages.value = res.data.data.totalPages;
    } catch (error) {
        console.error("Failed to fetch products:", error);
    } finally {
        loading.value = false;
    }
};

const resetFilters = () => {
    filters.search = '';
    filters.minPrice = null;
    filters.maxPrice = null;
    filters.status = null;
    filters.brand = '';
    selectedCategory.value = null;
    currentPage.value = 0;
    fetchProducts();
};

const handlePageSizeChange = () => {
    currentPage.value = 0;
    fetchProducts();
};

const changePage = (page: number) => {
    if (page < 0 || page >= totalPages.value) return;
    currentPage.value = page;
    fetchProducts();
    // Scroll to top of table or window
    window.scrollTo({ top: 0, behavior: 'smooth' });
};

const statusColor = (status: string | null) => {
    if (!status) return 'text-gray-700';
    switch (status) {
        case 'ACTIVE': return 'text-green-600';
        case 'DRAFT': return 'text-yellow-600';
        case 'ARCHIVED': return 'text-gray-600';
        default: return 'text-gray-700';
    }
};

const openCreateModal = () => {
    selectedProduct.value = null;
    showModal.value = true;
};

const openEditModal = (product: Product) => {
    selectedProduct.value = { ...product };
    showModal.value = true;
};

const closeModal = () => {
    showModal.value = false;
    selectedProduct.value = null;
};

const handleFormSubmit = async (payload: any) => {
    try {
        const productId = selectedProduct.value?.id;
        
        if (productId) {
            await adminProductApi.updateProduct(productId, payload);
        } else {
            await adminProductApi.createProduct(payload);
        }
        closeModal();
        fetchProducts();
    } catch (error) {
        console.error("Failed to save product:", error);
        alert("Failed to save product. Check console for details.");
    }
};

const deleteProduct = (product: Product) => {
    if (product.status === 'ACTIVE' || product.status === 'ARCHIVED') {
        alert("Cannot delete ACTIVE or ARCHIVED products. Only DRAFT products can be deleted.");
        return;
    }
    productToDelete.value = product.id;
    showDeleteModal.value = true;
};

const closeDeleteModal = () => {
    showDeleteModal.value = false;
    productToDelete.value = null;
    deleteLoading.value = false;
};

const confirmDelete = async () => {
    if (!productToDelete.value) return;
    
    deleteLoading.value = true;
    try {
        await adminProductApi.deleteProduct(productToDelete.value);
        fetchProducts();
        closeDeleteModal();
    } catch (error) {
            console.error("Failed to delete product:", error);
            // Could add an error toast/state here if needed
            alert("Failed to delete product."); // Fallback for now, could be improved
    } finally {
        deleteLoading.value = false;
    }
};

onMounted(() => {
    fetchCategories();
    fetchProducts();
});

// ... (existing code)
watch(selectedCategory, () => {
    currentPage.value = 0; // Reset page when category changes
});


</script>
