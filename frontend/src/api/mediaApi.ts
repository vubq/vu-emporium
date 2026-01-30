import apiClient from './axiosClient';

export interface FileItem {
    name: string;
    type: 'file' | 'folder';
    path: string;
    url?: string;
    size?: number;
}

export const mediaApi = {
    // List files/folders
    listFiles(path: string = '') {
        return apiClient.get<{ data: FileItem[] }>('/admin/media/files', { params: { path } });
    },

    // Create new folder
    createFolder(path: string, name: string) {
        return apiClient.post<{ data: void }>('/admin/media/folder', { path, name });
    },

    // Upload file
    uploadFile(file: File, path: string = '', overwrite: boolean = false) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('path', path);
        formData.append('overwrite', overwrite.toString());

        return apiClient.post<{ data: string }>('/admin/media/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
    },

    // Delete file/folder
    deleteItem(path: string) {
        return apiClient.delete<{ data: void }>('/admin/media', { params: { path } });
    }
};
