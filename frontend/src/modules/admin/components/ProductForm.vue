<template>
  <div class="bg-gray-50 min-h-screen p-6 relative">
    
    <!-- Initial Loading State -->
    <div v-if="isLoadingData" class="absolute inset-0 flex items-center justify-center bg-gray-50 z-50">
         <div class="flex flex-col items-center">
            <svg class="animate-spin h-10 w-10 text-indigo-600 mb-3" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <span class="text-sm font-medium text-gray-500">Preparing product data...</span>
        </div>
    </div>

    <div v-else class="max-w-7xl mx-auto animate-fade-in relative">
        
       <!-- Submitting Overlay -->
       <div v-if="submitting" class="absolute inset-0 z-40 bg-white/50 backdrop-blur-sm flex items-center justify-center rounded-2xl">
            <div class="bg-white px-5 py-3 rounded-xl shadow-lg border border-gray-100 flex items-center gap-3">
                 <svg class="animate-spin h-5 w-5 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                <span class="text-sm font-bold text-gray-700">Saving...</span>
            </div>
       </div>

      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
           <h2 class="text-3xl font-bold text-gray-900 tracking-tight">{{ isEdit ? 'Edit Product' : 'Create New Product' }}</h2>
           <p class="text-sm text-gray-500 mt-2">Fill in the details to configure your product.</p>
        </div>
        <div class="flex space-x-4">
             <button type="button" @click="$emit('cancel')" class="px-6 py-2.5 rounded-xl text-gray-700 bg-white border border-gray-200 hover:bg-gray-50 hover:border-gray-300 font-medium transition-all shadow-sm" :disabled="submitting">
                Cancel
             </button>
             <button type="submit" @click="submitForm" class="px-6 py-2.5 rounded-xl bg-gray-900 text-white hover:bg-black font-medium transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5" :disabled="submitting">
                {{ isEdit ? 'Save Changes' : 'Publish Product' }}
             </button>
        </div>
      </div>

      <form @submit.prevent="submitForm" class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        <!-- Left Column: Main Info -->
        <div class="lg:col-span-2 space-y-8">
          
          <!-- Basic Details Card -->
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 transition-shadow hover:shadow-md">
            <h3 class="text-lg font-bold text-gray-900 mb-6 flex items-center">
                <span class="bg-indigo-100 text-indigo-600 p-2 rounded-lg mr-3">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                </span>
                General Information
            </h3>
            
            <div class="space-y-6">
                <div>
                  <label class="block text-sm font-semibold text-gray-700 mb-2">Product Name</label>
                  <input v-model="form.name" type="text" required class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400" placeholder="e.g. Premium Cotton T-Shirt" :disabled="submitting" />
                </div>
                <div>
                   <label class="block text-sm font-semibold text-gray-700 mb-2">Brand</label>
                   <input v-model="form.brand" type="text" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400" placeholder="e.g. Acme Corp" :disabled="submitting" />
                </div>
                <div>
                  <label class="block text-sm font-semibold text-gray-700 mb-2">Description</label>
                  <textarea v-model="form.description" rows="5" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400" placeholder="Describe your product..." :disabled="submitting"></textarea>
                </div>
            </div>
            
            <!-- SEO Card -->
           <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 transition-shadow hover:shadow-md mt-8">
            <h3 class="text-lg font-bold text-gray-900 mb-6 flex items-center">
                <span class="bg-green-100 text-green-600 p-2 rounded-lg mr-3">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
                </span>
                SEO
            </h3>
            
            <div class="space-y-6">
                <div>
                  <label class="block text-sm font-semibold text-gray-700 mb-2">Meta Title</label>
                  <input v-model="form.metaTitle" type="text" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" placeholder="SEO Title" :disabled="submitting" />
                </div>
                <div>
                   <label class="block text-sm font-semibold text-gray-700 mb-2">Meta Description</label>
                   <textarea v-model="form.metaDescription" rows="3" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" placeholder="SEO Description" :disabled="submitting"></textarea>
                </div>
                 <div>
                   <label class="block text-sm font-semibold text-gray-700 mb-2">Meta Keywords</label>
                   <input v-model="form.metaKeywords" type="text" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" placeholder="comma, separated, keywords" :disabled="submitting" />
                </div>
            </div>
          </div>

          </div>

          <!-- Media Card -->
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 transition-shadow hover:shadow-md">
             <h3 class="text-lg font-bold text-gray-900 mb-6 flex items-center">
                <span class="bg-pink-100 text-pink-600 p-2 rounded-lg mr-3">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                </span>
                Product Images
            </h3>
             
             <!-- Image Gallery -->
             <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
                 <!-- Existing Images -->
                 <div v-for="(img, idx) in form.images.filter(i => i)" :key="idx" 
                      class="relative aspect-square rounded-xl overflow-hidden border-2 border-gray-200 group hover:border-indigo-400 transition-colors cursor-pointer">
                    <img :src="img" class="w-full h-full object-cover" @error="handleImageError" @click="openProductPreview(idx)" />
                    <div class="absolute inset-0 bg-black/40 flex items-center justify-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                        <button type="button" @click.stop="openProductPreview(idx)" 
                                class="p-2 bg-white text-gray-700 rounded-full hover:bg-gray-100 transition-colors" :disabled="submitting">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" /></svg>
                        </button>
                        <button type="button" @click.stop="removeProductImage(idx)" 
                                class="p-2 bg-red-500 text-white rounded-full hover:bg-red-600 transition-colors" :disabled="submitting">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                        </button>
                    </div>
                    <!-- Primary Badge -->
                    <div v-if="idx === 0" class="absolute top-2 left-2 px-2 py-1 bg-indigo-600 text-white text-xs font-bold rounded-full">
                        Primary
                    </div>
                 </div>
                 
                 <!-- Add Image Button -->
                 <button type="button" @click="openMediaModal('product')" 
                         class="aspect-square rounded-xl border-2 border-dashed border-gray-300 flex flex-col items-center justify-center hover:border-indigo-500 hover:bg-indigo-50 transition-all group" :disabled="submitting">
                    <svg class="w-10 h-10 text-gray-400 group-hover:text-indigo-600 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
                    <span class="text-sm text-gray-500 group-hover:text-indigo-600 mt-2 font-medium">Add Image</span>
                 </button>
             </div>
             
             <p class="text-xs text-gray-500 mt-4">Click "Add Image" to select from media library. First image will be the primary image.</p>
          </div>

          <!-- Variants Card -->
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 transition-shadow hover:shadow-md">
            <div class="flex items-center justify-between mb-6">
                <h3 class="text-lg font-bold text-gray-900 flex items-center">
                    <span class="bg-blue-100 text-blue-600 p-2 rounded-lg mr-3">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" /></svg>
                    </span>
                    Variants & Options
                </h3>
                <SwitchGroup as="div" class="flex items-center">
                    <SwitchLabel as="span" class="mr-3 text-sm font-medium text-gray-700">Enable Variants</SwitchLabel>
                    <Switch v-model="hasVariants" :class="[hasVariants ? 'bg-blue-600' : 'bg-gray-200', 'relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500']" :disabled="submitting">
                        <span aria-hidden="true" :class="[hasVariants ? 'translate-x-5' : 'translate-x-0', 'pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 transition ease-in-out duration-200']" />
                    </Switch>
                </SwitchGroup>
            </div>

            <div v-if="hasVariants" class="animate-fade-in space-y-8">
                <!-- Option Definitions -->
                <div class="space-y-4">
                    <div v-for="(option, index) in options" :key="index" class="bg-gray-50 p-5 rounded-xl border border-gray-200 relative group transition-all hover:bg-white hover:shadow-sm hover:border-blue-200">
                         <button type="button" @click="removeOption(index)" class="absolute top-3 right-3 text-gray-400 hover:text-red-500 transition-colors p-1 rounded-full hover:bg-red-50" :disabled="submitting">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                        </button>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                            <div>
                                <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2">Option Name</label>
                                <input v-model="option.name" type="text" placeholder="e.g. Size" class="w-full px-4 py-2 text-sm rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" :disabled="submitting" />
                            </div>
                            <div class="md:col-span-2">
                                <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2">Values (comma separated)</label>
                                <input v-model="option.valuesInput" type="text" placeholder="e.g. Small, Medium, Large" class="w-full px-4 py-2 text-sm rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" @change="processOptionValues(index)" :disabled="submitting" />
                                <div class="mt-3 flex flex-wrap gap-2">
                                    <span v-for="(val, vIdx) in option.values" :key="vIdx" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 border border-blue-200">
                                        {{ val.value }}
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="flex justify-between items-center pt-2">
                     <button type="button" @click="addOption" class="text-sm font-semibold text-blue-600 hover:text-blue-800 flex items-center" :disabled="submitting">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" /></svg>
                        Add Another Option
                     </button>
                </div>

                <!-- Variant Table (Collapsed View) -->
                <div v-if="variants.length > 0" class="border rounded-xl overflow-hidden border-gray-200 shadow-sm bg-white">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Variant</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-40">SKU</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-32">Price</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-32">Stock</th>
                                    <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider w-20">Actions</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <tr v-for="(variant, index) in variants" :key="index" class="hover:bg-gray-50 transition-colors cursor-pointer" @click="openEditVariantModal(index)">
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="flex items-center">
                                            <!-- Tiny Thumbnail -->
                                            <div class="h-8 w-8 rounded-md bg-gray-100 flex-shrink-0 mr-3 border border-gray-200 overflow-hidden">
                                                 <img v-if="variant.images && variant.images[0]" :src="variant.images[0]" class="h-full w-full object-cover" />
                                                 <div v-else class="h-full w-full flex items-center justify-center text-gray-400">
                                                     <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                                                 </div>
                                            </div>
                                            <span class="text-sm font-medium text-gray-900">{{ getVariantName(variant) }}</span>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="text-sm text-gray-500 font-mono">{{ variant.sku }}</span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="flex flex-col">
                                            <span class="text-sm font-medium text-gray-900">${{ variant.basePrice?.toFixed(2) }}</span>
                                            <span v-if="variant.salePrice && variant.salePrice > 0" class="text-xs text-green-600 font-medium">Sale: ${{ variant.salePrice.toFixed(2) }}</span>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span :class="[variant.stockQuantity > 0 ? 'text-gray-900' : 'text-red-500', 'text-sm font-medium']">{{ variant.stockQuantity }}</span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                        <button type="button" class="text-indigo-600 hover:text-indigo-900 font-medium bg-indigo-50 px-3 py-1.5 rounded-lg transition-colors hover:bg-indigo-100">
                                            Edit
                                        </button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
          </div>
        </div>

        <!-- Right Column: Sidebar -->
        <div class="space-y-8">
            
            <!-- Organization Card -->
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
                <h3 class="text-md font-bold text-gray-900 mb-4">Organization</h3>
                
                <div class="space-y-5">
                    <!-- Status -->
                    <div>
                             <label class="block text-sm font-semibold text-gray-700 mb-2">Status</label>
                             <Listbox v-model="form.status" :disabled="submitting">
                                <div class="relative mt-1">
                                <ListboxButton class="relative w-full cursor-pointer rounded-xl bg-white py-3 pl-4 pr-10 text-left border border-gray-300 shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                                    <span class="block truncate font-medium" :class="{'text-green-600': form.status === 'ACTIVE', 'text-yellow-600': form.status === 'DRAFT', 'text-gray-500': form.status === 'ARCHIVED'}">{{ form.status }}</span>
                                    <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                                        <svg class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                                    </span>
                                </ListboxButton>
                            <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
                                <ListboxOptions class="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                                <ListboxOption v-for="status in ['ACTIVE', 'DRAFT', 'ARCHIVED']" :key="status" :value="status" as="template" v-slot="{ active, selected }">
                                    <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2.5 pl-10 pr-4']">
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
                         
                     <!-- Featured Toggle -->
                     <div class="flex items-center justify-between">
                         <span class="flex-grow flex flex-col">
                             <span class="text-sm font-semibold text-gray-700">Featured Product</span>
                             <span class="text-xs text-gray-500">Show this product on home page</span>
                         </span>
                         <Switch v-model="form.featured" :class="[form.featured ? 'bg-indigo-600' : 'bg-gray-200', 'relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500']" :disabled="submitting">
                             <span aria-hidden="true" :class="[form.featured ? 'translate-x-5' : 'translate-x-0', 'pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 transition ease-in-out duration-200']" />
                         </Switch>
                     </div>

                    <!-- Category -->
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Category</label>
                        <Combobox v-model="selectedCategory" nullable by="id" :disabled="submitting">
                            <div class="relative mt-1">
                                <div class="relative w-full text-left">
                                    <ComboboxInput
                                        class="w-full rounded-xl border border-gray-300 bg-white py-3 pl-4 pr-10 text-sm text-gray-900 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                        :displayValue="(category: any) => category?.name"
                                        @change="query = $event.target.value"
                                        placeholder="Search category..."
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
                                    <ComboboxOptions class="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                                        <div v-if="filteredCategories.length === 0 && query !== ''" class="relative cursor-default select-none py-2 px-4 text-gray-700">
                                            Nothing found.
                                        </div>
                                        <ComboboxOption
                                            v-for="category in filteredCategories"
                                            :key="category.id"
                                            :value="category"
                                            as="template"
                                            v-slot="{ selected, active }"
                                        >
                                            <li class="relative cursor-pointer select-none py-2.5 pl-10 pr-4" :class="{ 'bg-indigo-600 text-white': active, 'text-gray-900': !active }">
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
                </div>
            </div>

             <!-- Pricing Card -->
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
                <h3 class="text-md font-bold text-gray-900 mb-4">Pricing</h3>
                 <div class="space-y-4">
                     <div class="grid grid-cols-3 gap-4">
                         <div>
                           <label class="block text-sm font-semibold text-gray-700 mb-1">Base Price</label>
                           <div class="relative rounded-xl shadow-sm">
                             <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                               <span class="text-gray-500 sm:text-sm">$</span>
                             </div>
                             <input v-model.number="form.basePrice" type="number" step="0.01" min="0" required class="w-full pl-7 pr-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" placeholder="0.00" :disabled="submitting" />
                           </div>
                         </div>
                         <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-1">Sale Price</label>
                             <div class="relative rounded-xl shadow-sm">
                             <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                               <span class="text-gray-500 sm:text-sm">$</span>
                             </div>
                             <input v-model.number="form.salePrice" type="number" step="0.01" min="0" class="w-full pl-7 pr-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" placeholder="0.00" :disabled="submitting" />
                           </div>
                         </div>
                         <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-1">Cost Price</label>
                             <div class="relative rounded-xl shadow-sm">
                             <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                               <span class="text-gray-500 sm:text-sm">$</span>
                             </div>
                             <input v-model.number="form.costPrice" type="number" step="0.01" min="0" class="w-full pl-7 pr-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" placeholder="0.00" :disabled="submitting" />
                           </div>
                         </div>
                     </div>
                 </div>
            </div>


            <!-- Inventory Card -->
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
                <h3 class="text-md font-bold text-gray-900 mb-4">Inventory</h3>
                <div class="space-y-4">
                  <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-1">SKU</label>
                    <input v-model="form.sku" type="text" class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" :disabled="submitting" />
                  </div>
                   <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-1">Quantity</label>
                    <input v-model.number="form.stockQuantity" type="number" min="0" required class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" :disabled="submitting" />
                  </div>
                </div>
            </div>

            <!-- Stats Card (Edit Only) -->
            <div v-if="isEdit" class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
                 <h3 class="text-md font-bold text-gray-900 mb-4">Statistics</h3>
                 <div class="grid grid-cols-2 gap-4">
                     <div class="bg-gray-50 rounded-xl p-3 text-center">
                         <span class="block text-xs text-gray-500 font-bold uppercase tracking-wider">Rating</span>
                         <span class="block text-xl font-bold text-gray-900 mt-1">{{ form.averageRating ? form.averageRating.toFixed(1) : '0.0' }}</span>
                     </div>
                      <div class="bg-gray-50 rounded-xl p-3 text-center">
                         <span class="block text-xs text-gray-500 font-bold uppercase tracking-wider">Reviews</span>
                         <span class="block text-xl font-bold text-gray-900 mt-1">{{ form.reviewCount || 0 }}</span>
                     </div>
                      <div class="bg-gray-50 rounded-xl p-3 text-center">
                         <span class="block text-xs text-gray-500 font-bold uppercase tracking-wider">Sales</span>
                         <span class="block text-xl font-bold text-gray-900 mt-1">{{ form.totalSales || 0 }}</span>
                     </div>
                      <div class="bg-gray-50 rounded-xl p-3 text-center">
                         <span class="block text-xs text-gray-500 font-bold uppercase tracking-wider">Views</span>
                         <span class="block text-xl font-bold text-gray-900 mt-1">{{ initialData?.viewCount || 0 }}</span>
                     </div>
                 </div>
            </div>


            <!-- Shipping Card -->
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
                <h3 class="text-md font-bold text-gray-900 mb-4">Shipping</h3>
                <div class="space-y-4">
                  <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-1">Weight (kg)</label>
                    <input v-model.number="form.weight" type="number" step="0.01" min="0" class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" :disabled="submitting" />
                  </div>
                  <div class="grid grid-cols-3 gap-2">
                       <div>
                        <label class="block text-xs font-semibold text-gray-700 mb-1">Length (cm)</label>
                        <input v-model.number="form.length" type="number" step="0.1" min="0" class="w-full px-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" :disabled="submitting" />
                      </div>
                      <div>
                        <label class="block text-xs font-semibold text-gray-700 mb-1">Width (cm)</label>
                        <input v-model.number="form.width" type="number" step="0.1" min="0" class="w-full px-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" :disabled="submitting" />
                      </div>
                      <div>
                        <label class="block text-xs font-semibold text-gray-700 mb-1">Height (cm)</label>
                        <input v-model.number="form.height" type="number" step="0.1" min="0" class="w-full px-3 py-2 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" :disabled="submitting" />
                      </div>
                  </div>
                </div>
            </div>


        </div>
      </form>
      

      <!-- Variant Edit Modal -->
    <TransitionRoot appear :show="showEditVariantModal" as="template">
        <Dialog as="div" @close="closeEditVariantModal" class="relative z-50">
            <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0" enter-to="opacity-100" leave="duration-200 ease-in" leave-from="opacity-100" leave-to="opacity-0">
                <div class="fixed inset-0 bg-black/25 backdrop-blur-sm" />
            </TransitionChild>

            <div class="fixed inset-0 overflow-y-auto">
                <div class="flex min-h-full items-center justify-center p-4 text-center">
                    <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0 scale-95" enter-to="opacity-100 scale-100" leave="duration-200 ease-in" leave-from="opacity-100 scale-100" leave-to="opacity-0 scale-95">
                        <DialogPanel v-if="editingVariant" class="w-full max-w-2xl transform overflow-hidden rounded-2xl bg-white p-8 text-left align-middle shadow-xl transition-all border border-gray-100">
                            <DialogTitle as="h3" class="text-xl font-bold leading-6 text-gray-900 mb-8 flex justify-between items-center">
                                <div class="flex items-center">
                                     <span class="bg-indigo-100 text-indigo-600 p-2 rounded-lg mr-3">
                                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
                                    </span>
                                    <span>Edit Variant: <span class="text-indigo-600">{{ getVariantName(editingVariant) }}</span></span>
                                </div>
                                <button @click="closeEditVariantModal" class="text-gray-400 hover:text-gray-500 p-1 rounded-full hover:bg-gray-100 transition-colors">
                                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                                </button>
                            </DialogTitle>
                            
                            <div class="space-y-8">
                                <!-- SKU & Stock -->
                                <div class="grid grid-cols-2 gap-6">
                                    <div>
                                        <label class="block text-sm font-semibold text-gray-700 mb-2">SKU</label>
                                        <input v-model="editingVariant.sku" type="text" class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" />
                                    </div>
                                    <div>
                                        <label class="block text-sm font-semibold text-gray-700 mb-2">Stock Quantity</label>
                                        <input v-model.number="editingVariant.stockQuantity" type="number" min="0" class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" />
                                    </div>
                                </div>

                                <!-- Pricing -->
                                <div class="bg-gray-50 rounded-2xl p-6 border border-gray-200">
                                    <h4 class="text-xs font-bold text-gray-500 uppercase tracking-widest mb-4 flex items-center">
                                        <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                                        Pricing Configuration
                                    </h4>
                                    <div class="grid grid-cols-3 gap-5">
                                        <div>
                                            <label class="block text-xs font-bold text-gray-700 mb-2">Base Price</label>
                                            <div class="relative rounded-xl shadow-sm">
                                                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                                    <span class="text-gray-500 sm:text-sm">$</span>
                                                </div>
                                                <input v-model.number="editingVariant.basePrice" type="number" step="0.01" min="0" class="w-full pl-7 pr-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none bg-white" placeholder="0.00" />
                                            </div>
                                        </div>
                                        <div>
                                            <label class="block text-xs font-bold text-gray-700 mb-2">Sale Price</label>
                                            <div class="relative rounded-xl shadow-sm">
                                                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                                    <span class="text-gray-500 sm:text-sm">$</span>
                                                </div>
                                                 <input v-model.number="editingVariant.salePrice" type="number" step="0.01" min="0" class="w-full pl-7 pr-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none bg-white" placeholder="0.00" />
                                            </div>
                                        </div>
                                        <div>
                                            <label class="block text-xs font-bold text-gray-700 mb-2">Cost Price</label>
                                            <div class="relative rounded-xl shadow-sm">
                                                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                                    <span class="text-gray-500 sm:text-sm">$</span>
                                                </div>
                                                 <input v-model.number="editingVariant.costPrice" type="number" step="0.01" min="0" class="w-full pl-7 pr-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none bg-white" placeholder="0.00" />
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Images -->
                                <div>
                                    <div class="flex justify-between items-center mb-4">
                                        <h4 class="text-sm font-bold text-gray-900 flex items-center">
                                            <svg class="w-4 h-4 mr-2 text-pink-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                                            Variant Images
                                        </h4>
                                        <button type="button" @click="openMediaModalForEditingVariant" class="text-sm text-indigo-600 hover:text-indigo-800 font-semibold bg-indigo-50 px-3 py-1.5 rounded-lg transition-colors hover:bg-indigo-100">+ Add Images</button>
                                    </div>
                                    
                                    <div class="flex flex-wrap gap-4">
                                        <div v-for="(img, idx) in editingVariant.images" :key="idx" class="relative group w-24 h-24 rounded-xl overflow-hidden border-2 border-gray-200 shadow-sm hover:border-indigo-400 transition-all">
                                            <img :src="img" class="w-full h-full object-cover" />
                                            <div class="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                                                <button @click="removeEditingVariantImage(idx)" class="p-1.5 bg-white text-red-500 rounded-full hover:bg-red-50 transition-colors shadow-sm">
                                                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                                                </button>
                                            </div>
                                        </div>
                                        <button v-if="!editingVariant.images || editingVariant.images.length === 0" 
                                            @click="openMediaModalForEditingVariant"
                                            class="w-24 h-24 rounded-xl border-2 border-dashed border-gray-300 flex flex-col items-center justify-center text-gray-400 hover:border-indigo-400 hover:text-indigo-500 hover:bg-indigo-50 transition-all">
                                            <svg class="w-8 h-8 mb-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
                                            <span class="text-xs font-semibold">Add</span>
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="mt-8 flex justify-end space-x-3 pt-6 border-t border-gray-100">
                                <button type="button" class="px-6 py-2.5 rounded-xl border border-gray-300 text-gray-700 font-medium hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-all" @click="closeEditVariantModal">
                                    Cancel
                                </button>
                                <button type="button" class="px-6 py-2.5 rounded-xl bg-gray-900 text-white font-medium hover:bg-black shadow-lg hover:shadow-xl focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-900 transform hover:-translate-y-0.5 transition-all" @click="saveVariantChanges">
                                    Save Changes
                                </button>
                            </div>
                        </DialogPanel>
                    </TransitionChild>
                </div>
            </div>
        </Dialog>
    </TransitionRoot>

    <!-- Product Image Preview Modal -->
      <TransitionRoot appear :show="showProductPreview" as="template">
        <Dialog as="div" @close="closeProductPreview" class="relative z-50">
          <TransitionChild
            as="template"
            enter="duration-300 ease-out"
            enter-from="opacity-0"
            enter-to="opacity-100"
            leave="duration-200 ease-in"
            leave-from="opacity-100"
            leave-to="opacity-0"
          >
            <div class="fixed inset-0 bg-black/80 backdrop-blur-sm" />
          </TransitionChild>

          <div class="fixed inset-0 overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4">
              <TransitionChild
                as="template"
                enter="duration-300 ease-out"
                enter-from="opacity-0 scale-95"
                enter-to="opacity-100 scale-100"
                leave="duration-200 ease-in"
                leave-from="opacity-100 scale-100"
                leave-to="opacity-0 scale-95"
              >
                <DialogPanel class="w-full max-w-4xl transform overflow-hidden rounded-2xl bg-white shadow-xl transition-all">
                  <div class="relative">
                    <!-- Header -->
                    <div class="flex items-center justify-between p-4 border-b">
                      <h3 class="text-lg font-bold text-gray-900">Product Images</h3>
                      <button @click="closeProductPreview" class="p-2 hover:bg-gray-100 rounded-lg transition-colors">
                        <svg class="w-5 h-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                      </button>
                    </div>
                    
                    <!-- Image Display -->
                    <div class="relative bg-gray-100 aspect-video flex items-center justify-center">
                      <img v-if="previewImageIndex !== null && form.images.filter(i => i)[previewImageIndex]" 
                           :src="form.images.filter(i => i)[previewImageIndex]" 
                           class="max-h-[60vh] max-w-full object-contain" 
                           @error="handleImageError" />
                      
                      <!-- Navigation Buttons -->
                      <button v-if="form.images.filter(i => i).length > 1 && previewImageIndex > 0" 
                              @click="prevImage" 
                              class="absolute left-4 p-3 bg-white/90 hover:bg-white rounded-full shadow-lg transition-colors">
                        <svg class="w-6 h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" /></svg>
                      </button>
                      
                      <button v-if="form.images.filter(i => i).length > 1 && previewImageIndex < form.images.filter(i => i).length - 1" 
                              @click="nextImage" 
                              class="absolute right-4 p-3 bg-white/90 hover:bg-white rounded-full shadow-lg transition-colors">
                        <svg class="w-6 h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" /></svg>
                      </button>
                    </div>
                    
                    <!-- Footer with Thumbnails and Actions -->
                    <div class="p-4 border-t bg-gray-50">
                      <div class="flex items-center justify-between mb-3">
                        <span class="text-sm text-gray-600">Image {{ (previewImageIndex || 0) + 1 }} of {{ form.images.filter(i => i).length }}</span>
                        <button @click="removeProductImage(previewImageIndex || 0); closeProductPreview();" 
                                class="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg text-sm font-medium transition-colors flex items-center gap-2">
                          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                          Delete Image
                        </button>
                      </div>
                      
                      <!-- Thumbnail Strip -->
                      <div class="flex gap-2 overflow-x-auto pb-2">
                        <div v-for="(img, idx) in form.images.filter(i => i)" :key="idx"
                             @click="previewImageIndex = idx"
                             class="relative flex-shrink-0 w-20 h-20 rounded-lg overflow-hidden border-2 cursor-pointer transition-all"
                             :class="previewImageIndex === idx ? 'border-indigo-500 ring-2 ring-indigo-200' : 'border-gray-300 hover:border-indigo-300'">
                          <img :src="img" class="w-full h-full object-cover" @error="handleImageError" />
                          <div v-if="idx === 0" class="absolute top-1 left-1 px-1.5 py-0.5 bg-indigo-600 text-white text-xs font-bold rounded">
                            Primary
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </DialogPanel>
              </TransitionChild>
            </div>
          </div>
        </Dialog>
      </TransitionRoot>
      
      <!-- Variant Image Management Modal -->
      <TransitionRoot appear :show="showVariantImageModal" as="template">
        <Dialog as="div" @close="closeVariantImageModal" class="relative z-50">
          <TransitionChild
            as="template"
            enter="duration-300 ease-out"
            enter-from="opacity-0"
            enter-to="opacity-100"
            leave="duration-200 ease-in"
            leave-from="opacity-100"
            leave-to="opacity-0"
          >
            <div class="fixed inset-0 bg-black/25 backdrop-blur-sm" />
          </TransitionChild>

          <div class="fixed inset-0 overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4">
              <TransitionChild
                as="template"
                enter="duration-300 ease-out"
                enter-from="opacity-0 scale-95"
                enter-to="opacity-100 scale-100"
                leave="duration-200 ease-in"
                leave-from="opacity-100 scale-100"
                leave-to="opacity-0 scale-95"
              >
                <DialogPanel class="w-full max-w-3xl transform overflow-hidden rounded-2xl bg-white shadow-xl transition-all">
                  <div v-if="currentVariantIndex !== null && variants[currentVariantIndex]">
                    <!-- Header -->
                    <div class="flex items-center justify-between p-6 border-b">
                      <div>
                        <h3 class="text-lg font-bold text-gray-900">Manage Variant Images</h3>
                        <p class="text-sm text-gray-500 mt-1">{{ getVariantName(variants[currentVariantIndex]) }}</p>
                      </div>
                      <button @click="closeVariantImageModal" class="p-2 hover:bg-gray-100 rounded-lg transition-colors">
                        <svg class="w-5 h-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                      </button>
                    </div>
                    
                    <!-- Image Grid -->
                    <div class="p-6">
                      <div class="grid grid-cols-3 gap-4">
                        <!-- Existing Images -->
                        <div v-for="(img, imgIdx) in (variants[currentVariantIndex].images || [])" :key="imgIdx"
                             class="relative aspect-square rounded-xl overflow-hidden border-2 border-gray-200 group hover:border-indigo-400 transition-colors cursor-pointer">
                          <img :src="img" class="w-full h-full object-cover" @error="handleImageError" @click="openVariantPreview(currentVariantIndex, imgIdx)" />
                          <div class="absolute inset-0 bg-black/40 flex items-center justify-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                            <button type="button" @click.stop="openVariantPreview(currentVariantIndex, imgIdx)" 
                                    class="p-2 bg-white text-gray-700 rounded-full hover:bg-gray-100 transition-colors">
                              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" /></svg>
                            </button>
                            <button type="button" @click.stop="removeVariantImage(currentVariantIndex, imgIdx)" 
                                    class="p-2 bg-red-500 text-white rounded-full hover:bg-red-600 transition-colors">
                              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                            </button>
                          </div>
                        </div>
                        
                        <!-- Add Image Button -->
                        <button type="button" @click="openMediaModalForVariant" 
                                class="aspect-square rounded-xl border-2 border-dashed border-gray-300 flex flex-col items-center justify-center hover:border-indigo-500 hover:bg-indigo-50 transition-all group">
                          <svg class="w-10 h-10 text-gray-400 group-hover:text-indigo-600 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
                          <span class="text-sm text-gray-500 group-hover:text-indigo-600 mt-2 font-medium">Add Image</span>
                        </button>
                      </div>
                      
                      <p class="text-xs text-gray-500 mt-4">Click on an image to preview. Hover to see delete option.</p>
                    </div>
                    
                    <!-- Footer -->
                    <div class="flex justify-end gap-3 p-6 border-t bg-gray-50">
                      <button @click="closeVariantImageModal" 
                              class="px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-lg hover:bg-indigo-700 transition-colors">
                        Done
                      </button>
                    </div>
                  </div>
                </DialogPanel>
              </TransitionChild>
            </div>
          </div>
        </Dialog>
      </TransitionRoot>
      
      <!-- Variant Image Preview Modal -->
      <TransitionRoot appear :show="showVariantPreview" as="template">
        <Dialog as="div" @close="closeVariantPreview" class="relative z-50">
          <TransitionChild
            as="template"
            enter="duration-300 ease-out"
            enter-from="opacity-0"
            enter-to="opacity-100"
            leave="duration-200 ease-in"
            leave-from="opacity-100"
            leave-to="opacity-0"
          >
            <div class="fixed inset-0 bg-black/80 backdrop-blur-sm" />
          </TransitionChild>

          <div class="fixed inset-0 overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4">
              <TransitionChild
                as="template"
                enter="duration-300 ease-out"
                enter-from="opacity-0 scale-95"
                enter-to="opacity-100 scale-100"
                leave="duration-200 ease-in"
                leave-from="opacity-100 scale-100"
                leave-to="opacity-0 scale-95"
              >
                <DialogPanel class="w-full max-w-4xl transform overflow-hidden rounded-2xl bg-white shadow-xl transition-all">
                  <div v-if="previewVariantIndex !== null && variants[previewVariantIndex]" class="relative">
                    <!-- Header -->
                    <div class="flex items-center justify-between p-4 border-b">
                      <div>
                        <h3 class="text-lg font-bold text-gray-900">Variant Images</h3>
                        <p class="text-sm text-gray-500 mt-1">{{ getVariantName(variants[previewVariantIndex]) }}</p>
                      </div>
                      <button @click="closeVariantPreview" class="p-2 hover:bg-gray-100 rounded-lg transition-colors">
                        <svg class="w-5 h-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                      </button>
                    </div>
                    
                    <!-- Image Display -->
                    <div class="relative bg-gray-100 aspect-video flex items-center justify-center">
                      <img v-if="previewVariantImageIndex !== null && (variants[previewVariantIndex].images || [])[previewVariantImageIndex]" 
                           :src="(variants[previewVariantIndex].images || [])[previewVariantImageIndex]" 
                           class="max-h-[60vh] max-w-full object-contain" 
                           @error="handleImageError" />
                      
                      <!-- Navigation Buttons -->
                      <button v-if="(variants[previewVariantIndex].images || []).length > 1 && previewVariantImageIndex > 0" 
                              @click="prevVariantImage" 
                              class="absolute left-4 p-3 bg-white/90 hover:bg-white rounded-full shadow-lg transition-colors">
                        <svg class="w-6 h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" /></svg>
                      </button>
                      
                      <button v-if="(variants[previewVariantIndex].images || []).length > 1 && previewVariantImageIndex < (variants[previewVariantIndex].images || []).length - 1" 
                              @click="nextVariantImage" 
                              class="absolute right-4 p-3 bg-white/90 hover:bg-white rounded-full shadow-lg transition-colors">
                        <svg class="w-6 h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" /></svg>
                      </button>
                    </div>
                    
                    <!-- Footer with Thumbnails and Actions -->
                    <div class="p-4 border-t bg-gray-50">
                      <div class="flex items-center justify-between mb-3">
                        <span class="text-sm text-gray-600">Image {{ (previewVariantImageIndex || 0) + 1 }} of {{ (variants[previewVariantIndex].images || []).length }}</span>
                        <div class="flex gap-2">
                          <button @click="openVariantImageModal(previewVariantIndex); closeVariantPreview();" 
                                  class="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg text-sm font-medium transition-colors flex items-center gap-2">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                            Manage Images
                          </button>
                        </div>
                      </div>
                      
                      <!-- Thumbnail Strip -->
                      <div class="flex gap-2 overflow-x-auto pb-2">
                        <div v-for="(img, idx) in (variants[previewVariantIndex].images || [])" :key="idx"
                             @click="previewVariantImageIndex = idx"
                             class="relative flex-shrink-0 w-20 h-20 rounded-lg overflow-hidden border-2 cursor-pointer transition-all"
                             :class="previewVariantImageIndex === idx ? 'border-indigo-500 ring-2 ring-indigo-200' : 'border-gray-300 hover:border-indigo-300'">
                          <img :src="img" class="w-full h-full object-cover" @error="handleImageError" />
                        </div>
                      </div>
                    </div>
                  </div>
                </DialogPanel>
              </TransitionChild>
            </div>
          </div>
        </Dialog>
      </TransitionRoot>
    </div>
  </div>
      <!-- MediaManager Modal -->
      <TransitionRoot appear :show="showMediaModal" as="template">
        <Dialog as="div" @close="closeMediaModal" class="relative z-[60]">
          <TransitionChild
            as="template"
            enter="duration-300 ease-out"
            enter-from="opacity-0"
            enter-to="opacity-100"
            leave="duration-200 ease-in"
            leave-from="opacity-100"
            leave-to="opacity-0"
          >
            <div class="fixed inset-0 bg-black/25 backdrop-blur-sm" />
          </TransitionChild>

          <div class="fixed inset-0 overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4">
              <TransitionChild
                as="template"
                enter="duration-300 ease-out"
                enter-from="opacity-0 scale-95"
                enter-to="opacity-100 scale-100"
                leave="duration-200 ease-in"
                leave-from="opacity-100 scale-100"
                leave-to="opacity-0 scale-95"
              >
                <DialogPanel class="w-full max-w-5xl h-[80vh] transform overflow-hidden rounded-2xl bg-white shadow-xl transition-all">
                  <MediaManager 
                    mode="select"
                    :multi-select="true"
                    :excluded-urls="getExcludedUrls()"
                    @select="handleMediaSelect" 
                    @cancel="closeMediaModal" 
                  />
                </DialogPanel>
              </TransitionChild>
            </div>
          </div>
        </Dialog>
      </TransitionRoot>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { categoryApi } from '@/api/productApi';
import type { Category } from '@/types/product';
import { 
    Listbox, ListboxButton, ListboxOptions, ListboxOption,
    Switch, SwitchGroup, SwitchLabel,
    Combobox, ComboboxInput, ComboboxButton, ComboboxOptions, ComboboxOption, TransitionRoot,
    Dialog, DialogPanel, TransitionChild, DialogTitle
} from '@headlessui/vue';
import MediaManager from './MediaManager.vue';

const props = defineProps<{
  initialData?: any;
  isEdit?: boolean;
}>();

const emit = defineEmits(['submit', 'cancel']);

const categories = ref<Category[]>([]);
const hasVariants = ref(false);
const query = ref('');
const selectedCategory = ref<Category | null>(null);

interface OptionInput {
  name: string;
  valuesInput: string;
  values: { value: string }[];
}

const options = ref<OptionInput[]>([]);
const variants = ref<any[]>([]);

// MediaManager Modal State
const showMediaModal = ref(false);
const mediaTarget = ref<{ type: 'product' | 'variant', index?: number } | null>(null);

// Product Preview Modal State
const showProductPreview = ref(false);
const previewImageIndex = ref<number | null>(null);

// Variant Image Modal State
const showVariantImageModal = ref(false);
const currentVariantIndex = ref<number | null>(null);

// Variant Preview Modal State
const showVariantPreview = ref(false);
const previewVariantIndex = ref<number | null>(null);
const previewVariantImageIndex = ref<number | null>(null);

// Variant Edit Modal State
const showEditVariantModal = ref(false);
const editingVariantIndex = ref<number | null>(null);
const editingVariant = ref<any>(null); // Temporary copy for editing


// Loading States
const isLoadingData = ref(true);
const submitting = ref(false);

const form = reactive({
  name: '',
  description: '',
  stockQuantity: 0,
  sku: '',
  categoryId: null as number | null,
  status: 'ACTIVE',
  featured: false,
  images: [''],
  // New Fields
  basePrice: 0,
  salePrice: 0,
  costPrice: 0,
  brand: '',
  weight: 0,
  length: 0,
  width: 0,
  height: 0,
  metaTitle: '',
  metaDescription: '',
  metaKeywords: ''
});

// Update form when editing
watch(() => props.initialData, (newVal) => {
    if (newVal) {
        Object.assign(form, {
            ...newVal,
            // Ensure zeros if null/undefined for numeric fields
            basePrice: newVal.basePrice || 0,
            salePrice: newVal.salePrice || 0,
            costPrice: newVal.costPrice || 0,
            weight: newVal.weight || 0,
            length: newVal.length || 0,
            width: newVal.width || 0,
            height: newVal.height || 0,
            images: newVal.images && newVal.images.length > 0 ? newVal.images : ['']
        });
        hasVariants.value = newVal.hasVariants;
        if (newVal.options) {
             options.value = newVal.options.map((opt: any) => ({
                 name: opt.name,
                 valuesInput: opt.values.map((v: any) => v.value).join(', '),
                 values: opt.values
             }));
        }
        if (newVal.variants) {
            variants.value = JSON.parse(JSON.stringify(newVal.variants));
        }
        if (newVal.category) {
            selectedCategory.value = newVal.category;
            query.value = newVal.category.name;
        }
    } else {
        // Reset form
        Object.assign(form, {
             name: '',
             description: '',
             stockQuantity: 0,
             sku: '',
             categoryId: null,
             status: 'ACTIVE',
             featured: false,
             images: [''],
             basePrice: 0,
             salePrice: 0,
             costPrice: 0,
             brand: '',
             weight: 0,
             length: 0,
             width: 0,
             height: 0,
             metaTitle: '',
             metaDescription: '',
             metaKeywords: ''
        });
        hasVariants.value = false;
        options.value = [];
        variants.value = [];
        selectedCategory.value = null;
        query.value = '';
    }
}, { immediate: true });


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

watch(selectedCategory, (newVal) => {
    form.categoryId = newVal ? newVal.id : null;
});

const handleImageError = (e: Event) => {
    (e.target as HTMLImageElement).style.display = 'none';
};

// MediaManager Functions
function openMediaModal(type: 'product' | 'variant', index?: number) {
    mediaTarget.value = { type, index };
    showMediaModal.value = true;
}

function closeMediaModal() {
    showMediaModal.value = false;
    mediaTarget.value = null;
}


function removeProductImage(index: number) {
    form.images.splice(index, 1);
    // Ensure at least one empty string if all removed
    if (form.images.length === 0) {
        form.images = [''];
    }
}

function removeVariantImage(variantIndex: number, imageIndex: number) {
    if (variants.value[variantIndex] && variants.value[variantIndex].images) {
        variants.value[variantIndex].images.splice(imageIndex, 1);
    }
}


// Product Preview Functions
function openProductPreview(index: number) {
    previewImageIndex.value = index;
    showProductPreview.value = true;
}

function closeProductPreview() {
    showProductPreview.value = false;
    previewImageIndex.value = null;
}

function nextImage() {
    const images = form.images.filter(i => i);
    if (previewImageIndex.value !== null && previewImageIndex.value < images.length - 1) {
        previewImageIndex.value++;
    }
}

function prevImage() {
    if (previewImageIndex.value !== null && previewImageIndex.value > 0) {
        previewImageIndex.value--;
    }
}

// Variant Image Modal Functions
function openVariantImageModal(index: number) {
    currentVariantIndex.value = index;
    showVariantImageModal.value = true;
}

function closeVariantImageModal() {
    showVariantImageModal.value = false;
    currentVariantIndex.value = null;
}

function openMediaModalForVariant() {
    if (currentVariantIndex.value !== null) {
        mediaTarget.value = { type: 'variant', index: currentVariantIndex.value };
        showMediaModal.value = true;
    }
}

// Variant Preview Functions
function openVariantPreview(variantIndex: number, imageIndex: number) {
    previewVariantIndex.value = variantIndex;
    previewVariantImageIndex.value = imageIndex;
    showVariantPreview.value = true;
}

function closeVariantPreview() {
    showVariantPreview.value = false;
    previewVariantIndex.value = null;
    previewVariantImageIndex.value = null;
}

function nextVariantImage() {
    if (previewVariantIndex.value !== null && previewVariantImageIndex.value !== null) {
        const images = variants.value[previewVariantIndex.value].images || [];
        if (previewVariantImageIndex.value < images.length - 1) {
            previewVariantImageIndex.value++;
        }
    }
}

function prevVariantImage() {
    if (previewVariantImageIndex.value !== null && previewVariantImageIndex.value > 0) {
        previewVariantImageIndex.value--;
    }
}

// Variant Edit Functions
function openEditVariantModal(index: number) {
    editingVariantIndex.value = index;
    // Create a deep copy to avoid direct mutation until saved
    editingVariant.value = JSON.parse(JSON.stringify(variants.value[index]));
    showEditVariantModal.value = true;
}

function closeEditVariantModal() {
    // Guard: Do not close if Media Modal is open (prevents accidental closing due to event bubbling)
    if (showMediaModal.value) return;
    
    showEditVariantModal.value = false;
    editingVariantIndex.value = null;
    editingVariant.value = null;
}

function saveVariantChanges() {
    if (editingVariantIndex.value !== null && editingVariant.value) {
        variants.value[editingVariantIndex.value] = { ...editingVariant.value };
        closeEditVariantModal();
    }
}

// Media Manager Helper for Edit Modal
function openMediaModalForEditingVariant() {
    // We used to open via index, but now we are editing a temporary object 'editingVariant'
    // The MediaManager needs to know where to put the results.
    // Solution: We can temporarily hijack the mediaTarget to point to a special 'editing' state
    // OR easier: modify handleMediaSelect to handle 'editing-variant'
    
    mediaTarget.value = { type: 'editing-variant' };
    showMediaModal.value = true;
}

function removeEditingVariantImage(imageIndex: number) {
    if (editingVariant.value && editingVariant.value.images) {
        editingVariant.value.images.splice(imageIndex, 1);
    }
}


function handleMediaSelect(items: any) {
    // Handle both single item (legacy) and array (multi-select)
    const itemsArray = Array.isArray(items) ? items : [items];
    
    if (!itemsArray || itemsArray.length === 0 || !mediaTarget.value) return;
    
    const { type, index } = mediaTarget.value;
    
    if (type === 'product') {
        const cleanImages = form.images.filter(img => img);
        const newUrls = itemsArray.map(item => item.url).filter(url => !cleanImages.includes(url));
        form.images = [...cleanImages, ...newUrls];
    } else if (type === 'variant' && index !== undefined) {
        if (!variants.value[index].images) {
            variants.value[index].images = [];
        }
        const existingUrls = variants.value[index].images;
        const newUrls = itemsArray.map(item => item.url).filter(url => !existingUrls.includes(url));
        variants.value[index].images.push(...newUrls);
    } else if (type === 'editing-variant') {
         if (!editingVariant.value.images) {
            editingVariant.value.images = [];
        }
        const existingUrls = editingVariant.value.images;
        const newUrls = itemsArray.map(item => item.url).filter(url => !existingUrls.includes(url));
        editingVariant.value.images.push(...newUrls);
    }
    
    closeMediaModal();
}

function getExcludedUrls(): string[] {
    if (!mediaTarget.value) return [];
    
    if (mediaTarget.value.type === 'product') {
        return form.images.filter(i => i);
    } else if (mediaTarget.value.type === 'variant' && mediaTarget.value.index !== undefined) {
        return variants.value[mediaTarget.value.index].images || [];
    } else if (mediaTarget.value.type === 'editing-variant' && editingVariant.value) {
        return editingVariant.value.images || [];
    }
    return [];
}

onMounted(async () => {
    isLoadingData.value = true;
    try {
        // 1. Fetch Categories
        const res = await categoryApi.getCategories();
        categories.value = res.data.data.content || res.data.data;
        
        // 2. Initialize Form Data
        if (props.initialData) {
            Object.assign(form, props.initialData);
            // Fix for numbers that might come as strings or undefined
            
            if (props.initialData.category) {
                form.categoryId = props.initialData.category.id;
                selectedCategory.value = props.initialData.category;
            } else if (form.categoryId) {
                const found = categories.value.find(c => c.id === form.categoryId);
                if (found) selectedCategory.value = found;
            }
            
            if (!form.images || form.images.length === 0) {
                form.images = [''];
            }

            if (props.initialData.hasVariants) {
                hasVariants.value = true;
                if (props.initialData.options) {
                    options.value = props.initialData.options.map((opt: any) => ({
                        name: opt.name,
                        values: Array.isArray(opt.values) && typeof opt.values[0] === 'string' 
                            ? opt.values.map((v: string) => ({ value: v }))
                            : opt.values,
                        valuesInput: Array.isArray(opt.values) 
                            ? opt.values.map((v: any) => typeof v === 'string' ? v : v.value).join(', ') 
                            : ''
                    }));
                }
                if (props.initialData.variants) {
                    variants.value = props.initialData.variants.map((v: any) => ({
                       ...v,
                        optionValues: Array.isArray(v.optionValues) && typeof v.optionValues[0] === 'string'
                            ? v.optionValues.map((val: string) => ({ value: val }))
                            : v.optionValues,
                        // Ensure images is always an array
                        images: Array.isArray(v.images) ? v.images : (v.image ? [v.image] : [])
                    }));
                }
            }
            
            // Run normalization once to ensure initial data is sorted and consistent
            if (hasVariants.value) {
                updateVariantsMatrix();
            }
        }
    } catch (e) {
        console.error("Failed to load initial data", e);
    } finally {
        // Enforce a minimum loading time for smoother UX (optional, but prevents flicker)
        setTimeout(() => {
            isLoadingData.value = false;
        }, 500);
    }

    // Register watchers AFTER loading initial data
    watch(options, debounce(() => {
        if (hasVariants.value) {
            updateVariantsMatrix();
        }
    }, 500), { deep: true });

    watch(hasVariants, (newVal) => {
        if (newVal) {
            updateVariantsMatrix();
        }
    });

    // Watch for SKU changes to update variant SKUs
    watch(() => form.sku, debounce(() => {
        if (hasVariants.value) {
            updateVariantsMatrix();
        }
    }, 500));
});

function addOption() {
    options.value.push({ name: '', valuesInput: '', values: [] });
}

function removeOption(index: number) {
    options.value.splice(index, 1);
}

function processOptionValues(index: number) {
    const opt = options.value[index];
    if (opt.valuesInput) {
        opt.values = opt.valuesInput.split(',').map(s => ({ value: s.trim() })).filter(v => v.value);
    }
}

// Debounce utility
function debounce(fn: Function, delay: number) {
    let timeoutId: ReturnType<typeof setTimeout>;
    return function (...args: any[]) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => fn.apply(null, args), delay);
    };
}


function updateVariantsMatrix() {
    // 1. Prepare valid options
    options.value.forEach((_, idx) => processOptionValues(idx));
    const validOptions = options.value.filter(o => o.values && o.values.length > 0);
    
    if (validOptions.length === 0) {
        if (variants.value.length > 0 && options.value.every(o => !o.values || o.values.length === 0)) {
             variants.value = [];
        }
        return;
    }

    // 2. Generate all possible combinations (Cartesian Product)
    const combinations = cartesian(validOptions.map(o => o.values));

    // 3. Smart Merge: Map combinations to variants
    // Strategy: Try to find the best matching existing variant to copy data from.
    // 1. Exact Match
    // 2. Superset/Subset Match (for added/removed options)

    const newVariants: any[] = [];
    
    // Create a pool of current variants to search from
    const currentPool = [...variants.value];
    
    // Track used IDs to ensure we don't duplicate the same ID across multiple new variants (e.g. when splitting 1 variant into 2)
    const usedIds = new Set<number>();

    combinations.forEach(combo => {
        // Combo values as a Set for easy comparison
        const comboValuesSet = new Set(combo.map((c: any) => c.value));
        
        let bestMatch: any = null;
        let bestMatchScore = -1;

        // Find best match in current pool
        for (const existing of currentPool) {
            if (!existing.optionValues) continue;
            
            const existingValues = existing.optionValues.map((v: any) => v.value);
            const existingSet = new Set(existingValues);
            
            // Calculate Intersection Size
            let intersectionSize = 0;
            existingValues.forEach((val: string) => {
                if (comboValuesSet.has(val)) intersectionSize++;
            });

            // "Subtitle" Match Logic debug
            // console.log(`Checking existing: ${getVariantName(existing)}`, existingValues);
            
            // STRATEGY update: Use Index Matching + Set Matching
            // Index matching is safer for "Expansion" (Appending options)
            
            let indexMatchScore = 0;
            const len = Math.min(existingValues.length, combo.length);
            let matchCount = 0;
            for(let k=0; k<len; k++) {
                if (existingValues[k] === combo[k].value) {
                    matchCount++;
                    indexMatchScore += 10;
                } else {
                    // Stop at first mismatch for strict prefix (optional, but good for "parent" logic)
                    // But maybe user reordered options? Let's punish mismatch.
                    indexMatchScore -= 5;
                }
            }
            
            // Set Matching (Fallback/Supplement)
            const minSize = Math.min(comboValuesSet.size, existingSet.size);
            const isSubset = intersectionSize === minSize;

            // Combine Scores
            let score = -100;
            
            // Condition 1: Index Prefix Match (Strongest)
            // If existing is a full prefix of combo (e.g. S/Red -> S/Red/D)
            if (matchCount === existingValues.length) {
                score = 100 + (matchCount * 10);
            } 
            // Condition 2: Subset Match (Existing is subset of New, OR New is subset of Existing)
            else if (isSubset) {
                score = intersectionSize * 10;
            }
            // Condition 3: Partial Index Match (Weak)
            else {
                score = indexMatchScore;
            }

            // Penalty for size difference
            const sizeDiff = Math.abs(combo.length - existingValues.length);
            score -= sizeDiff;

            // Tie-breaker: Prefer matching ID
            if (existing.id && !usedIds.has(existing.id)) {
                 score += 0.5;
            }

            if (score > bestMatchScore) {
                bestMatchScore = score;
                bestMatch = existing;
            }
        }

        if (bestMatch) {
            // Found a match to copy data from
            
            const variantData = {
                ...bestMatch,
                // IMPORTANT: Overwrite optionValues with the NEW correct structure
                optionValues: combo.map((c: any) => ({ value: c.value })),
                // ALWAYS regenerate SKU to ensure it matches current options (remove stale segments like deleted colors)
                sku: `${form.sku || 'SKU'}-${combo.map((c: any) => c.value).join('-')}`
            };

            // Handle ID uniqeness
            // If we split one variant into multiple (e.g. Add Size: Red -> Red/S, Red/M),
            // We can only give the original ID to ONE of them. The others must be new.
            if (bestMatch.id) {
                if (!usedIds.has(bestMatch.id)) {
                    usedIds.add(bestMatch.id);
                    // Keep the ID
                } else {
                    // ID already used by another sibling, this one must be new
                    delete variantData.id;
                }
            }
            
            // SKU Logic: If we are creating a NEW variant (no ID or ID removed), potentially append/modify SKU to avoid dupes?
            // For now, user requested "keep data", so we keep SKU. 
            // Warning: This means multiple Red/S and Red/M might have SAME SKU initially. User must edit.
            
            newVariants.push(variantData);

        } else {
            // New Variant
              newVariants.push({
                sku: `${form.sku || 'SKU'}-${combo.map((c: any) => c.value).join('-')}`,
                basePrice: form.basePrice,
                salePrice: form.salePrice,
                costPrice: form.costPrice,
                stockQuantity: form.stockQuantity, 
                images: [],
                optionValues: combo.map((c: any) => ({ value: c.value }))
            });
        }
    });

    variants.value = newVariants;
}

function cartesian(args: any[]): any[] {
    const r: any[] = [];
    const max = args.length - 1;
    function helper(arr: any[], i: number) {
        for (let j = 0; j < args[i].length; j++) {
            const a = arr.slice(0);
            a.push(args[i][j]);
            if (i == max) r.push(a);
            else helper(a, i + 1);
        }
    }
    helper([], 0);
    return r;
}

function getVariantName(variant: any) {
    if (!variant.optionValues) return 'Variant';
    return variant.optionValues.map((v: any) => v.value).join(' / ');
}

function submitForm() {
    submitting.value = true;
    try {
        const payload = {
            ...form,
            hasVariants: hasVariants.value,
            // Filter empty strings from product images
            images: form.images.filter(img => img),
            options: hasVariants.value ? options.value.map(o => ({
                name: o.name,
                values: o.values.map(v => v.value)
            })) : [],
            variants: hasVariants.value ? variants.value.map(v => ({
                ...v,
                optionValues: v.optionValues.map((ov: any) => ov.value),
                // Ensure images array is clean
                images: Array.isArray(v.images) ? v.images.filter((img: string) => img) : []
            })) : []
        };
        emit('submit', payload);
        
        // Auto-reset submitting if parent doesn't close modal immediately (fallback)
        setTimeout(() => {
             submitting.value = false; 
        }, 5000);
    } catch (e) {
        console.error("Submission Error", e);
        submitting.value = false;
    }
}
</script>
