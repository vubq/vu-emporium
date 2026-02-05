import axiosClient from './axiosClient';

export const i18nApi = {
    // Public
    getLanguages: () => axiosClient.get('/v1/public/i18n/languages'),
    getMessages: (lang: string) => axiosClient.get(`/v1/public/i18n/messages?lang=${lang}`),

    // User Profile
    updatePreferredLanguage: (lang: string) => axiosClient.post('/v1/user/profile/language', { language: lang }),

    // Admin
    admin: {
        getAllLanguages: () => axiosClient.get('/v1/admin/i18n/languages'),
        createLanguage: (data: any) => axiosClient.post('/v1/admin/i18n/languages', data),
        updateLanguage: (code: string, data: any) => axiosClient.put(`/v1/admin/i18n/languages/${code}`, data),
        deleteLanguage: (code: string) => axiosClient.delete(`/v1/admin/i18n/languages/${code}`),
        setDefault: (code: string) => axiosClient.post(`/v1/admin/i18n/languages/${code}/default`),
        getMatrix: () => axiosClient.get('/v1/admin/i18n/matrix'),
        updateTranslations: (lang: string, data: Record<string, string>) =>
            axiosClient.post(`/v1/admin/i18n/translations/${lang}`, data),
        addKey: (key: string) => axiosClient.post(`/v1/admin/i18n/keys?key=${key}`),
        renameKey: (oldKey: string, newKey: string) =>
            axiosClient.put(`/v1/admin/i18n/keys?oldKey=${oldKey}&newKey=${newKey}`),
        deleteKey: (key: string) => axiosClient.delete(`/v1/admin/i18n/keys?key=${key}`),
    }
};
