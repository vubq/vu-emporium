<template>
  <div class="h-full flex flex-col bg-white">
    <!-- Toolbar -->
    <div class="p-3 border-b flex items-center justify-between gap-3">
        <div class="flex items-center gap-2 overflow-x-auto text-sm">
            <button @click="navigate('/')" class="hover:bg-gray-100 p-1.5 rounded-lg text-gray-500">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" /></svg>
            </button>
            <span class="text-gray-300">/</span>
            <template v-for="(part, index) in pathParts" :key="index">
                <button @click="navigatePart(index)" class="font-medium text-gray-700 hover:text-indigo-600 hover:underline px-1 whitespace-nowrap">
                    {{ part }}
                </button>
                <span class="text-gray-300">/</span>
            </template>
        </div>
        <div class="flex items-center gap-2">
            <button @click="showNewFolderInput = true" v-if="!showNewFolderInput" class="p-2 text-gray-600 hover:bg-gray-100 rounded-lg flex items-center gap-1 text-sm font-medium">
                <svg class="w-5 h-5 text-yellow-500" fill="currentColor" viewBox="0 0 20 20"><path d="M2 6a2 2 0 012-2h5l2 2h5a2 2 0 012 2v6a2 2 0 01-2 2H4a2 2 0 01-2-2V6z"/></svg>
                New Folder
            </button>
            <div v-else class="flex items-center gap-1">
                <input v-model="newFolderName" ref="newFolderRef" @keyup.enter="createFolder" @keyup.esc="showNewFolderInput = false" type="text" class="border rounded px-2 py-1 text-sm w-32 focus:outline-none focus:border-indigo-500" placeholder="Name..." />
                <button @click="createFolder" class="p-1 text-green-600 hover:bg-green-50 rounded"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" /></svg></button>
                <button @click="showNewFolderInput = false" class="p-1 text-red-600 hover:bg-red-50 rounded"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg></button>
            </div>

            <label class="cursor-pointer p-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg flex items-center gap-2 text-sm font-medium transition-colors">
                <svg v-if="uploading" class="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12" /></svg>
                Upload
                <input type="file" @change="handleUpload" class="hidden" multiple />
            </label>
        </div>
    </div>

    <!-- Content -->
    <div class="flex-1 overflow-auto p-4 relative" @dragover.prevent @drop.prevent="handleDrop">
        <div v-if="loading" class="absolute inset-0 z-10 flex items-center justify-center bg-white/60">
             <svg class="animate-spin h-8 w-8 text-indigo-600" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
        </div>

        <div v-if="items.length === 0 && !loading" class="h-full flex flex-col items-center justify-center text-gray-400">
            <svg class="w-16 h-16 mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 19a2 2 0 01-2-2V7a2 2 0 012-2h4l2 2h4a2 2 0 012 2v5.5M15 21h5a2 2 0 002-2v-5M9 21h6" /></svg>
            <p>Folder is empty</p>
        </div>

        <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4">
            <div v-for="item in items" :key="item.path" 
                class="group relative border rounded-xl p-3 flex flex-col gap-2 hover:shadow-md hover:border-indigo-300 transition-all cursor-pointer bg-white"
                :class="{'ring-2 ring-indigo-500': selectedItem?.path === item.path}"
                @click="item.type === 'file' ? selectItem(item) : enterFolder(item)"
            >
                <!-- Thumbnail -->
                <div class="aspect-square rounded-lg bg-gray-50 flex items-center justify-center overflow-hidden">
                    <svg v-if="item.type === 'folder'" class="w-12 h-12 text-yellow-400" fill="currentColor" viewBox="0 0 20 20"><path d="M2 6a2 2 0 012-2h5l2 2h5a2 2 0 012 2v6a2 2 0 01-2 2H4a2 2 0 01-2-2V6z"/></svg>
                    <img v-else :src="item.url" class="w-full h-full object-cover" />
                </div>
                
                <!-- Info -->
                <div class="text-xs truncate text-center font-medium text-gray-700 select-none">{{ item.name }}</div>

                <!-- Actions (Hover) -->
                <button v-if="isManageMode || item.type === 'folder'" @click.stop="handleDelete(item)" class="absolute top-1 right-1 p-1 bg-white rounded-full text-red-500 opacity-0 group-hover:opacity-100 shadow-sm hover:bg-red-50 transition-opacity">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                </button>
            </div>
        </div>
    </div>
    
    <!-- Footer (Select Mode) -->
    <div v-if="!isManageMode" class="p-3 border-t bg-gray-50 flex justify-between items-center">
        <div class="text-sm text-gray-500" v-if="selectedItem">
            Selected: <span class="font-medium text-gray-900">{{ selectedItem.name }}</span>
        </div>
        <div v-else class="text-sm text-gray-500">Select an image</div>
        
        <div class="flex gap-2">
            <button @click="$emit('cancel')" class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50">Cancel</button>
            <button @click="$emit('select', selectedItem)" :disabled="!selectedItem" class="px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-lg hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed">
                Select
            </button>
        </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue';
import { mediaApi, type FileItem } from '@/api/mediaApi';

const props = defineProps<{
    mode?: 'manage' | 'select'; // Default: manage
}>();

const emit = defineEmits(['select', 'cancel']);

const isManageMode = computed(() => props.mode !== 'select');

const items = ref<FileItem[]>([]);
const currentPath = ref('');
const loading = ref(false);
const uploading = ref(false);

const selectedItem = ref<FileItem | null>(null);

const showNewFolderInput = ref(false);
const newFolderName = ref('');
const newFolderRef = ref<HTMLInputElement | null>(null);

const pathParts = computed(() => {
    return currentPath.value ? currentPath.value.split('/').filter(p => p) : [];
});

const loadFiles = async () => {
    loading.value = true;
    try {
        const res = await mediaApi.listFiles(currentPath.value);
        items.value = res.data.data;
        selectedItem.value = null;
    } catch (e) {
        console.error(e);
        alert('Failed to load files');
    } finally {
        loading.value = false;
    }
};

const navigate = (path: string) => {
    currentPath.value = path;
    loadFiles();
};

const navigatePart = (index: number) => {
    const parts = pathParts.value.slice(0, index + 1);
    navigate(parts.join('/'));
};

const enterFolder = (item: FileItem) => {
    navigate(item.path);
};

const selectItem = (item: FileItem) => {
    selectedItem.value = item;
    // If in manage mode, maybe show details? For now just select.
    if (isManageMode.value) {
        // Could enable a visual selection state
    }
};

const createFolder = async () => {
    if (!newFolderName.value) return;
    try {
        await mediaApi.createFolder(currentPath.value, newFolderName.value);
        newFolderName.value = '';
        showNewFolderInput.value = false;
        loadFiles();
    } catch (e) {
        alert('Failed to create folder');
    }
};

watch(showNewFolderInput, (val) => {
    if (val) nextTick(() => newFolderRef.value?.focus());
});

import { watch } from 'vue';

const handleUpload = async (event: Event) => {
    const input = event.target as HTMLInputElement;
    if (input.files) {
        await uploadFiles(input.files);
        input.value = '';
    }
};

const handleDrop = async (event: DragEvent) => {
    if (event.dataTransfer?.files) {
        await uploadFiles(event.dataTransfer.files);
    }
};

const uploadFiles = async (fileList: FileList) => {
    uploading.value = true;
    try {
        for (let i = 0; i < fileList.length; i++) {
            const file = fileList[i];
            // Check for overwrite? For simplicity, we assume no overwrite or backend logic handles error
             try {
                // If it exists, simple overwrite? Or ask?
                // User requirement: "có thể update đè (có xác nhận)"
                // For "Drag & Drop" bulk upload, prompting for each is annoying. 
                // Let's rely on backend returning 409 if exists.
                
                await mediaApi.uploadFile(file, currentPath.value, false);
            } catch (error: any) {
                if (error.response && error.response.status === 409) {
                     if (confirm(`File "${file.name}" already exists. Overwrite?`)) {
                         await mediaApi.uploadFile(file, currentPath.value, true);
                     }
                } else {
                    console.error("Upload error", error);
                    alert(`Failed to upload ${file.name}`);
                }
            }
        }
        loadFiles();
    } finally {
        uploading.value = false;
    }
};

const handleDelete = async (item: FileItem) => {
    if (!confirm(`Delete ${item.type} "${item.name}"?`)) return;
    try {
        await mediaApi.deleteItem(item.path);
        loadFiles();
    } catch (e: any) {
        alert(e.response?.data?.message || 'Delete failed');
    }
};

onMounted(() => {
    loadFiles();
});
</script>
