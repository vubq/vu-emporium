<template>
  <div class="bg-gray-50 min-h-screen p-6">
    <div class="max-w-7xl mx-auto">
      <!-- Header -->
      <div class="flex items-center justify-between mb-8">
        <div>
           <h2 class="text-3xl font-bold text-gray-900 tracking-tight">{{ isEdit ? 'Edit Product' : 'Create New Product' }}</h2>
           <p class="text-sm text-gray-500 mt-2">Fill in the details to configure your product.</p>
        </div>
        <div class="flex space-x-4">
             <button type="button" @click="$emit('cancel')" class="px-6 py-2.5 rounded-xl text-gray-700 bg-white border border-gray-200 hover:bg-gray-50 hover:border-gray-300 font-medium transition-all shadow-sm">
                Cancel
             </button>
             <button type="submit" @click="submitForm" class="px-6 py-2.5 rounded-xl bg-gray-900 text-white hover:bg-black font-medium transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5">
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
                  <input v-model="form.name" type="text" required class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400" placeholder="e.g. Premium Cotton T-Shirt" />
                </div>
                <div>
                  <label class="block text-sm font-semibold text-gray-700 mb-2">Description</label>
                  <textarea v-model="form.description" rows="5" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400" placeholder="Describe your product..."></textarea>
                </div>
            </div>
          </div>

          <!-- Media Card -->
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 transition-shadow hover:shadow-md">
             <h3 class="text-lg font-bold text-gray-900 mb-6 flex items-center">
                <span class="bg-pink-100 text-pink-600 p-2 rounded-lg mr-3">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                </span>
                Media
            </h3>
             <div class="space-y-4">
                 <label class="block text-sm font-semibold text-gray-700 mb-2">Main Image</label>
                 
                 <!-- Image Preview (if available) -->
                 <div v-if="form.images[0]" class="relative w-full h-64 bg-gray-50 rounded-xl overflow-hidden mb-4 border-2 border-dashed border-gray-200 group">
                    <img :src="form.images[0]" class="w-full h-full object-contain" alt="Product Preview" @error="handleImageError"/>
                    <div class="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                        <button type="button" @click="form.images[0] = ''" class="text-white bg-red-500 p-2 rounded-full hover:bg-red-600">
                             <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                        </button>
                    </div>
                 </div>
                 <div v-else class="w-full h-32 bg-gray-50 rounded-xl border-2 border-dashed border-gray-300 flex flex-col items-center justify-center text-gray-400">
                    <svg class="w-10 h-10 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                    <span class="text-sm">No image selected</span>
                 </div>

                 <input v-model="form.images[0]" type="text" class="w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400" placeholder="https://example.com/image.jpg" />
                 <p class="text-xs text-gray-500">Enter a direct URL to an image.</p>
             </div>
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
                    <Switch v-model="hasVariants" :class="[hasVariants ? 'bg-blue-600' : 'bg-gray-200', 'relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500']">
                        <span aria-hidden="true" :class="[hasVariants ? 'translate-x-5' : 'translate-x-0', 'pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 transition ease-in-out duration-200']" />
                    </Switch>
                </SwitchGroup>
            </div>

            <div v-if="hasVariants" class="animate-fade-in space-y-8">
                <!-- Option Definitions -->
                <div class="space-y-4">
                    <div v-for="(option, index) in options" :key="index" class="bg-gray-50 p-5 rounded-xl border border-gray-200 relative group transition-all hover:bg-white hover:shadow-sm hover:border-blue-200">
                         <button type="button" @click="removeOption(index)" class="absolute top-3 right-3 text-gray-400 hover:text-red-500 transition-colors p-1 rounded-full hover:bg-red-50">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                        </button>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                            <div>
                                <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2">Option Name</label>
                                <input v-model="option.name" type="text" placeholder="e.g. Size" class="w-full px-4 py-2 text-sm rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" />
                            </div>
                            <div class="md:col-span-2">
                                <label class="block text-xs font-bold text-gray-500 uppercase tracking-widest mb-2">Values (comma separated)</label>
                                <input v-model="option.valuesInput" type="text" placeholder="e.g. Small, Medium, Large" class="w-full px-4 py-2 text-sm rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" @change="processOptionValues(index)" />
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
                     <button type="button" @click="addOption" class="text-sm font-semibold text-blue-600 hover:text-blue-800 flex items-center">
                        <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" /></svg>
                        Add Another Option
                     </button>
                </div>

                <div v-if="variants.length > 0" class="border rounded-xl overflow-hidden border-gray-200 shadow-sm bg-white">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Variant</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-40">SKU (Auto)</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-40">Price</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-40">Stock</th>
                                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Image URL</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <tr v-for="(variant, index) in variants" :key="index" class="hover:bg-gray-50 transition-colors">
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                        {{ getVariantName(variant) }}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <input v-model="variant.sku" type="text" readonly class="block w-full text-sm rounded-xl border border-gray-300 px-3 py-2 shadow-sm bg-gray-100 text-gray-500 cursor-not-allowed focus:outline-none transition-all" />
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <input v-model.number="variant.price" type="number" step="0.01" class="block w-full text-sm rounded-xl border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" />
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <input v-model.number="variant.stockQuantity" type="number" class="block w-full text-sm rounded-xl border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" />
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <input v-model="variant.image" type="text" placeholder="https://..." class="block w-full text-sm rounded-xl border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all" />
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
                             <Listbox v-model="form.status">
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

                    <!-- Category -->
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Category</label>
                        <Combobox v-model="selectedCategory" nullable by="id">
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
                     <div>
                      <label class="block text-sm font-semibold text-gray-700 mb-1">Price</label>
                      <div class="relative rounded-xl shadow-sm">
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                          <span class="text-gray-500 sm:text-sm">$</span>
                        </div>
                        <input v-model.number="form.price" type="number" step="0.01" min="0" required class="w-full pl-7 pr-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" placeholder="0.00" />
                      </div>
                    </div>
                    <div>
                       <label class="block text-sm font-semibold text-gray-700 mb-1">Compare at Price</label>
                        <div class="relative rounded-xl shadow-sm">
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                          <span class="text-gray-500 sm:text-sm">$</span>
                        </div>
                        <input v-model.number="form.compareAtPrice" type="number" step="0.01" min="0" class="w-full pl-7 pr-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" placeholder="0.00" />
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
                    <input v-model="form.sku" type="text" class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" />
                  </div>
                   <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-1">Quantity</label>
                    <input v-model.number="form.stockQuantity" type="number" min="0" required class="w-full px-4 py-2.5 rounded-xl border border-gray-300 shadow-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 focus:outline-none" />
                  </div>
                </div>
            </div>

        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { categoryApi } from '@/api/productApi';
import type { Category } from '@/types/product';
import { 
    Listbox, ListboxButton, ListboxOptions, ListboxOption,
    Switch, SwitchGroup, SwitchLabel,
    Combobox, ComboboxInput, ComboboxButton, ComboboxOptions, ComboboxOption, TransitionRoot
} from '@headlessui/vue';

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

const form = reactive({
  name: '',
  description: '',
  price: 0,
  compareAtPrice: 0,
  stockQuantity: 0,
  sku: '',
  categoryId: null as number | null,
  status: 'ACTIVE',
  images: ['']
});

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

onMounted(async () => {
    try {
        const res = await categoryApi.getCategories();
        categories.value = res.data.data.content || res.data.data;
    } catch (e) {
        console.error("Failed to load categories", e);
    }

    if (props.initialData) {
        Object.assign(form, props.initialData);
        // Fix for numbers that might come as strings or undefined
        if(form.price === undefined) form.price = 0;
        if(form.compareAtPrice === undefined) form.compareAtPrice = 0;
        
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
                    imagesInput: v.images ? v.images.join(', ') : ''
                }));
            }
        }
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

    // Run normalization once to ensure initial data is sorted and consistent
    if (hasVariants.value) {
        updateVariantsMatrix();
    }
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

// Watchers moved to onMounted


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
                price: form.price,
                stockQuantity: form.stockQuantity, 
                image: '', 
                images: [],
                imagesInput: '',
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
    const payload = {
        ...form,
        hasVariants: hasVariants.value,
        options: hasVariants.value ? options.value.map(o => ({
            name: o.name,
            values: o.values.map(v => v.value)
        })) : [],
        variants: hasVariants.value ? variants.value.map(v => ({
            ...v,
            optionValues: v.optionValues.map((ov: any) => ov.value),
            images: v.imagesInput ? v.imagesInput.split(',').map((s: string) => s.trim()).filter((s: string) => s) : []
        })) : []
    };
    emit('submit', payload);
}
</script>
