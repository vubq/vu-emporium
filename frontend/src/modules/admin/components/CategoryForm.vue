<template>
    <div>
        <!-- Submitting Overlay -->
        <div v-if="loading" class="absolute inset-0 z-40 bg-white/50 backdrop-blur-sm flex items-center justify-center rounded-2xl">
             <div class="bg-white px-5 py-3 rounded-xl shadow-lg border border-gray-100 flex items-center gap-3">
                  <svg class="animate-spin h-5 w-5 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                     <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                     <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                 </svg>
                 <span class="text-sm font-bold text-gray-700">Saving...</span>
             </div>
        </div>
 
        <!-- Header -->
       <div class="flex items-center justify-between mb-6">
         <div>
            <h2 class="text-2xl font-bold text-gray-900 tracking-tight">{{ isEdit ? 'Edit Category' : 'Create New Category' }}</h2>
            <p class="text-sm text-gray-500 mt-1">Configure category details and settings.</p>
         </div>
         <div class="flex space-x-3">
              <button type="button" @click="$emit('cancel')" class="px-4 py-2 rounded-xl text-gray-700 bg-white border border-gray-200 hover:bg-gray-50 hover:border-gray-300 font-medium transition-all shadow-sm text-sm" :disabled="loading">
                 Cancel
              </button>
              <button type="button" @click="submitForm" class="px-4 py-2 rounded-xl bg-gray-900 text-white hover:bg-black font-medium transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 text-sm" :disabled="loading">
                 {{ isEdit ? 'Save Changes' : 'Create Category' }}
              </button>
         </div>
       </div>
 
       <form @submit.prevent="submitForm" class="grid grid-cols-1 lg:grid-cols-3 gap-6">
         <!-- Left Column: Main Info -->
         <div class="lg:col-span-2 space-y-6">
           <!-- Basic Details Card -->
           <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 transition-shadow hover:shadow-md">
             <h3 class="text-lg font-bold text-gray-900 mb-4 flex items-center">
                 <span class="bg-indigo-100 text-indigo-600 p-2 rounded-lg mr-3">
                     <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                 </span>
                 General Information
             </h3>
             <div class="space-y-4">
                 <div>
                   <label class="block text-sm font-semibold text-gray-700 mb-1">Category Name</label>
                   <input v-model="formData.name" type="text" required class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400" placeholder="e.g. Electronics" :disabled="loading" />
                 </div>
                 <div>
                   <label class="block text-sm font-semibold text-gray-700 mb-1">Description</label>
                   <textarea v-model="formData.description" rows="4" class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400" placeholder="Description of the category..." :disabled="loading"></textarea>
                 </div>
             </div>
           </div>
 
           <!-- Media Card -->
           <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 transition-shadow hover:shadow-md">
              <h3 class="text-lg font-bold text-gray-900 mb-4 flex items-center">
                 <span class="bg-pink-100 text-pink-600 p-2 rounded-lg mr-3">
                     <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                 </span>
                 Category Image
             </h3>
             <div class="flex items-start gap-6">
                 <div v-if="formData.imageUrl" class="relative group w-32 h-32 rounded-xl overflow-hidden border-2 border-gray-200">
                     <AppImage :src="formData.imageUrl" class="w-full h-full object-cover" />
                     <div class="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                         <button type="button" @click="formData.imageUrl = ''" class="p-2 bg-red-500 text-white rounded-full hover:bg-red-600 transition-colors">
                             <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                         </button>
                     </div>
                 </div>
                 <button v-else type="button" @click="openMediaManager" class="w-32 h-32 rounded-xl border-2 border-dashed border-gray-300 flex flex-col items-center justify-center hover:border-indigo-500 hover:bg-indigo-50 transition-all group" :disabled="loading">
                     <svg class="w-8 h-8 text-gray-400 group-hover:text-indigo-600 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
                     <span class="text-xs text-gray-500 group-hover:text-indigo-600 mt-2 font-medium">Add Image</span>
                 </button>
                 <div class="flex-1 text-sm text-gray-500">
                     <p>Upload a representative image for this category.</p>
                     <p class="mt-1">Recommended size: 500x500px.</p>
                     <button v-if="formData.imageUrl" type="button" @click="openMediaManager" class="text-indigo-600 font-medium hover:text-indigo-800 mt-2">Change Image</button>
                 </div>
             </div>
           </div>
         </div>
 
         <!-- Right Column: Sidebar -->
         <div class="space-y-6">
             <!-- Organization Card -->
             <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
                 <h3 class="text-md font-bold text-gray-900 mb-4">Organization</h3>
                 <div class="space-y-5">
                      <!-- Active Status -->
                      <div class="flex items-center justify-between">
                          <span class="flex-grow flex flex-col">
                              <span class="text-sm font-semibold text-gray-700">Active Status</span>
                              <span class="text-xs text-gray-500">Enable or disable category</span>
                          </span>
                           <Switch v-model="formData.active" :class="[formData.active ? 'bg-green-500' : 'bg-gray-200', 'relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500']" :disabled="loading">
                              <span aria-hidden="true" :class="[formData.active ? 'translate-x-5' : 'translate-x-0', 'pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 transition ease-in-out duration-200']" />
                          </Switch>
                      </div>
 
                      <!-- Parent Category -->
                     <div>
                         <label class="block text-sm font-semibold text-gray-700 mb-1">Parent Category</label>
                         <Listbox v-model="formData.parentId" :disabled="loading">
                             <div class="relative mt-1">
                                 <ListboxButton class="relative w-full cursor-pointer rounded-xl bg-white py-2.5 pl-4 pr-10 text-left border border-gray-300 shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                                     <span class="block truncate">{{ getParentName(formData.parentId) }}</span>
                                     <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                                         <svg class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                                     </span>
                                 </ListboxButton>
                                 <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
                                     <ListboxOptions class="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-xl bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                                         <ListboxOption :value="null" as="template" v-slot="{ active, selected }">
                                             <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-4 pr-4']">
                                                 <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">None (Root)</span>
                                             </li>
                                         </ListboxOption>
                                         <ListboxOption v-for="cat in parentCategories" :key="cat.id" :value="cat.id" as="template" v-slot="{ active, selected }">
                                             <li :class="[active ? 'bg-indigo-50 text-indigo-900' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-4 pr-4']">
                                                 <span :class="[selected ? 'font-medium' : 'font-normal', 'block truncate']">{{ cat.name }}</span>
                                             </li>
                                         </ListboxOption>
                                     </ListboxOptions>
                                 </transition>
                             </div>
                         </Listbox>
                     </div>
 
                     <!-- Display Order -->
                     <div>
                         <label class="block text-sm font-semibold text-gray-700 mb-1">Display Order</label>
                         <input v-model.number="formData.displayOrder" type="number" class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" :disabled="loading" />
                     </div>
                 </div>
             </div>
         </div>
       </form>
 
        <MediaManager 
            v-if="showMediaManager"
            :show="showMediaManager"
            @close="showMediaManager = false"
            @select="handleImageSelect"
        />
    </div>
 </template>
 
 <script setup lang="ts">
 import { ref, watch, computed } from 'vue';
 import type { Category } from '@/types/product';
 import MediaManager from './MediaManager.vue'; 
 import AppImage from '@/components/common/AppImage.vue';
 import { 
     Switch, SwitchGroup, SwitchLabel,
     Listbox, ListboxButton, ListboxOptions, ListboxOption
 } from '@headlessui/vue';
 
 const props = defineProps<{
     initialData?: Category | null;
     categories: Category[];
     loading?: boolean;
 }>();
 
 const emit = defineEmits<{
     (e: 'submit', data: any): void;
     (e: 'cancel'): void;
 }>();
 
 const showMediaManager = ref(false);
 const formData = ref({
     name: '',
     description: '',
     imageUrl: '',
     parentId: null as number | null,
     displayOrder: 0,
     active: true
 });
 
 const isEdit = computed(() => !!props.initialData);
 
 const parentCategories = computed(() => {
     if (!isEdit.value || !props.initialData) return props.categories;
     return props.categories.filter(c => c.id !== props.initialData?.id);
 });
 
 const getParentName = (id: number | null) => {
     if (!id) return 'None (Root)';
     const parent = props.categories.find(c => c.id === id);
     return parent ? parent.name : 'Unknown';
 };
 
 watch(() => props.initialData, (newVal) => {
     if (newVal) {
         formData.value = {
             name: newVal.name,
             description: newVal.description || '',
             imageUrl: newVal.imageUrl || '',
             parentId: null, // Still assuming null for now as explained before
             displayOrder: newVal.displayOrder || 0,
             active: newVal.active !== undefined ? newVal.active : true
         };
         // If traversing parents is possible, logic to set parentId can go here.
     } else {
         formData.value = {
             name: '',
             description: '',
             imageUrl: '',
             parentId: null,
             displayOrder: 0,
             active: true
         };
     }
 }, { immediate: true });
 
 const openMediaManager = () => {
     showMediaManager.value = true;
 };
 
 const handleImageSelect = (url: string) => {
     formData.value.imageUrl = url;
     showMediaManager.value = false;
 };
 
 const submitForm = () => {
     emit('submit', formData.value);
 };
 </script>
