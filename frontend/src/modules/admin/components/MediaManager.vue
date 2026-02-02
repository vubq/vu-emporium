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
                {{ $t('admin.manage.media.new_folder') }}
            </button>
            <div v-else class="flex items-center gap-1">
                <input v-model="newFolderName" ref="newFolderRef" @keyup.enter="createFolder" @keyup.esc="showNewFolderInput = false" type="text" class="border rounded px-2 py-1 text-sm w-32 focus:outline-none focus:border-indigo-500" :placeholder="$t('admin.manage.media.folder_name_placeholder')" />
                <button @click="createFolder" class="p-1 text-green-600 hover:bg-green-50 rounded"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" /></svg></button>
                <button @click="showNewFolderInput = false" class="p-1 text-red-600 hover:bg-red-50 rounded"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg></button>
            </div>

            <label class="cursor-pointer p-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg flex items-center gap-2 text-sm font-medium transition-colors">
                <svg v-if="uploading" class="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12" /></svg>
                {{ $t('admin.manage.media.upload') }}
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
            <p>{{ $t('admin.manage.media.empty') }}</p>
        </div>

        <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4">
            <div v-for="item in items" :key="item.path" 
                class="group relative border rounded-xl p-3 flex flex-col gap-2 transition-all bg-white"
                :class="{
                    'ring-2 ring-indigo-500': isItemSelected(item),
                    'opacity-50 cursor-not-allowed': item.type === 'file' && isItemExcluded(item),
                    'hover:shadow-md hover:border-indigo-300 cursor-pointer': item.type === 'folder' || (item.type === 'file' && !isItemExcluded(item))
                }"
                @click="item.type === 'file' ? (isItemExcluded(item) ? null : selectItem(item)) : enterFolder(item)"
            >
                <!-- Checkbox for multi-select -->
                <div v-if="props.multiSelect && item.type === 'file' && !isItemExcluded(item)" 
                     class="absolute top-2 left-2 z-10">
                    <div class="w-5 h-5 rounded border-2 flex items-center justify-center transition-all"
                         :class="isItemSelected(item) ? 'bg-indigo-600 border-indigo-600' : 'bg-white border-gray-300'">
                        <svg v-if="isItemSelected(item)" class="w-3 h-3 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" /></svg>
                    </div>
                </div>
                
                <!-- Excluded badge -->
                <div v-if="item.type === 'file' && isItemExcluded(item)" 
                     class="absolute top-2 right-2 z-10 px-2 py-0.5 bg-gray-600 text-white text-xs rounded-full font-medium">
                    {{ $t('admin.manage.media.selected') }}
                </div>
                
                <!-- Thumbnail -->
                <div class="aspect-square rounded-lg bg-gray-50 flex items-center justify-center overflow-hidden">
                    <svg v-if="item.type === 'folder'" class="w-12 h-12 text-yellow-400" fill="currentColor" viewBox="0 0 20 20"><path d="M2 6a2 2 0 012-2h5l2 2h5a2 2 0 012 2v6a2 2 0 01-2 2H4a2 2 0 01-2-2V6z"/></svg>
                    <img v-else :src="item.url" class="w-full h-full object-cover" />
                </div>
                
                <!-- Info / Rename Input -->
                <div v-if="editingItem?.path === item.path" class="flex items-center gap-1" @click.stop>
                    <input 
                        v-model="editingName" 
                        ref="editInputRef"
                        @keyup.enter="saveRename(item)" 
                        @keyup.esc="cancelRename"
                        type="text" 
                        class="flex-1 text-xs px-1 py-0.5 border rounded focus:outline-none focus:border-indigo-500"
                    />
                    <button @click="saveRename(item)" class="p-0.5 text-green-600 hover:bg-green-50 rounded">
                        <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" /></svg>
                    </button>
                    <button @click="cancelRename" class="p-0.5 text-red-600 hover:bg-red-50 rounded">
                        <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                    </button>
                </div>
                <div v-else class="text-xs truncate text-center font-medium text-gray-700 select-none">{{ item.name }}</div>

                <!-- Actions (Hover) -->
                <div v-if="isManageMode || item.type === 'folder'" class="absolute top-1 right-1 opacity-0 group-hover:opacity-100 transition-opacity flex gap-1">
                    <button @click.stop="startRename(item)" class="p-1 bg-white rounded-full text-blue-500 shadow-sm hover:bg-blue-50">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
                    </button>
                    <button @click.stop="confirmDelete(item)" class="p-1 bg-white rounded-full text-red-500 shadow-sm hover:bg-red-50">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                    </button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Footer (Select Mode) -->
    <div v-if="!isManageMode" class="p-3 border-t bg-gray-50 flex justify-between items-center">
        <div class="text-sm text-gray-500">
            <span v-if="props.multiSelect">
                {{ $t('admin.manage.media.selected_multi', { count: selectedItems.length }) }}
            </span>
            <span v-else-if="selectedItem">
                {{ $t('admin.manage.media.selected_single', { name: selectedItem.name }) }}
            </span>
            <span v-else>{{ $t('admin.manage.media.select_placeholder', { type: props.multiSelect ? $t('admin.manage.media.images') : $t('admin.manage.media.an_image') }) }}</span>
        </div>
        
        <div class="flex gap-2">
            <button type="button" @click.stop="$emit('cancel')" class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50">{{ $t('common.cancel') }}</button>
            <button type="button" @click.stop="emitSelection" 
                    :disabled="props.multiSelect ? selectedItems.length === 0 : !selectedItem" 
                    class="px-4 py-2 text-sm font-medium text-white bg-indigo-600 rounded-lg hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed">
                {{ $t('common.select') }}
            </button>
        </div>
    </div>

    <!-- Confirm Delete Modal -->
    <ConfirmModal
        :isOpen="deleteModal.show"
        :title="$t('admin.media.confirm_delete_title')"
        :message="deleteModal.message"
        :confirmText="$t('common.delete')"
        :type="'danger'"
        :loading="deleteModal.loading"
        @close="deleteModal.show = false"
        @confirm="executeDelete"
    />

    <!-- Alert Modal -->
    <AlertModal
        v-model="alertModal.show"
        :title="alertModal.title"
        :message="alertModal.message"
        :variant="alertModal.variant"
        @close="alertModal.show = false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { mediaApi, type FileItem } from '@/api/mediaApi';
import ConfirmModal from '@/components/common/ConfirmModal.vue';
import AlertModal from '@/components/common/AlertModal.vue';

const { t } = useI18n();

const props = defineProps<{
    mode?: 'manage' | 'select';
    multiSelect?: boolean;
    excludedUrls?: string[];
}>();

const emit = defineEmits(['select', 'cancel']);

const isManageMode = computed(() => props.mode !== 'select');

const items = ref<FileItem[]>([]);
const currentPath = ref('');
const loading = ref(false);
const uploading = ref(false);

const selectedItem = ref<FileItem | null>(null);
const selectedItems = ref<FileItem[]>([]);

const showNewFolderInput = ref(false);
const newFolderName = ref('');
const newFolderRef = ref<HTMLInputElement | null>(null);

// Rename state
const editingItem = ref<FileItem | null>(null);
const editingName = ref('');
const editInputRef = ref<HTMLInputElement | null>(null);

// Delete modal state
const deleteModal = ref({
    show: false,
    item: null as FileItem | null,
    message: '',
    loading: false
});

// Alert modal state
const alertModal = ref({
    show: false,
    title: '',
    message: '',
    variant: 'info' as 'success' | 'error' | 'warning' | 'info'
});

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
        showAlert(t('admin.media.error_title'), t('admin.media.load_failed'), 'error');
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
    if (props.multiSelect) {
        const index = selectedItems.value.findIndex(i => i.path === item.path);
        if (index > -1) {
            selectedItems.value.splice(index, 1);
        } else {
            selectedItems.value.push(item);
        }
    } else {
        selectedItem.value = item;
    }
};

const isItemSelected = (item: FileItem): boolean => {
    if (props.multiSelect) {
        return selectedItems.value.some(i => i.path === item.path);
    }
    return selectedItem.value?.path === item.path;
};

const isItemExcluded = (item: FileItem): boolean => {
    return props.excludedUrls?.includes(item.url) || false;
};

const emitSelection = () => {
    if (props.multiSelect) {
        emit('select', selectedItems.value);
    } else {
        emit('select', selectedItem.value);
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
        showAlert(t('admin.media.error_title'), t('admin.media.create_failed'), 'error');
    }
};

watch(showNewFolderInput, (val) => {
    if (val) nextTick(() => newFolderRef.value?.focus());
});

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
            try {
                await mediaApi.uploadFile(file, currentPath.value, false);
            } catch (error: any) {
                console.error("Upload error", error);
                showAlert(t('admin.media.error_title'), t('admin.manage.media.upload_failed', { name: file.name }), 'error');
            }
        }
        loadFiles();
    } finally {
        uploading.value = false;
    }
};

// Rename functionality
const startRename = (item: FileItem) => {
    editingItem.value = item;
    editingName.value = item.name;
    nextTick(() => editInputRef.value?.focus());
};

const cancelRename = () => {
    editingItem.value = null;
    editingName.value = '';
};

const saveRename = async (item: FileItem) => {
    if (!editingName.value || editingName.value === item.name) {
        cancelRename();
        return;
    }

    // Check if name already exists
    const exists = items.value.some(i => i.name === editingName.value && i.path !== item.path);
    if (exists) {
        const typeLabel = item.type === 'folder' ? t('admin.media.folder') : t('admin.media.file');
        showAlert(t('admin.media.error_title'), t('admin.media.name_exists', { type: typeLabel }), 'warning');
        return;
    }

    try {
        await mediaApi.renameItem(item.path, editingName.value);
        showAlert(t('admin.media.alert_title'), t('admin.media.rename_success'), 'success');
        cancelRename();
        loadFiles();
    } catch (e: any) {
        showAlert(t('admin.media.error_title'), e.response?.data?.message || t('admin.media.rename_failed'), 'error');
    }
};

// Delete functionality
const confirmDelete = (item: FileItem) => {
    const typeLabel = item.type === 'folder' ? t('admin.media.folder') : t('admin.media.file');
    let message = t('admin.media.confirm_delete_message', { type: typeLabel, name: item.name });
    
    if (item.type === 'folder') {
        message += '\n\n' + t('admin.media.confirm_delete_folder_content');
    }

    deleteModal.value = {
        show: true,
        item: item,
        message: message,
        loading: false
    };
};

const executeDelete = async () => {
    if (!deleteModal.value.item) return;
    
    deleteModal.value.loading = true;
    try {
        await mediaApi.deleteItem(deleteModal.value.item.path);
        showAlert(t('admin.media.alert_title'), t('admin.media.delete_success'), 'success');
        deleteModal.value.show = false;
        loadFiles();
    } catch (e: any) {
        showAlert(t('admin.media.error_title'), e.response?.data?.message || t('admin.media.delete_failed'), 'error');
    } finally {
        deleteModal.value.loading = false;
    }
};

// Alert helper
const showAlert = (title: string, message: string, variant: 'success' | 'error' | 'warning' | 'info') => {
    alertModal.value = {
        show: true,
        title,
        message,
        variant
    };
};

onMounted(() => {
    loadFiles();
});
</script>
