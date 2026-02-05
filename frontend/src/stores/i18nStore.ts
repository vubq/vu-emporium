import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { i18nApi } from '@/api/i18nApi';
import i18n from '@/i18n';

export interface Language {
    code: string;
    name: string;
    flagIcon: string;
    isActive: boolean;
    isDefault: boolean;
    displayOrder: number;
}

export const useI18nStore = defineStore('i18n', () => {
    const languages = ref<Language[]>([]);
    const isLoading = ref(false);
    const messagesLoaded = ref<Record<string, boolean>>({});

    const activeLanguages = computed(() => languages.value.filter(l => l.isActive));
    const defaultLanguage = computed(() => languages.value.find(l => l.isDefault) || languages.value[0]);

    async function fetchLanguages() {
        try {
            const res = await i18nApi.getLanguages();
            languages.value = res.data;
        } catch (error) {
            console.error('Failed to fetch languages:', error);
        }
    }

    async function loadMessages(langCode: string) {
        if (messagesLoaded.value[langCode]) return;

        try {
            isLoading.value = true;
            const res = await i18nApi.getMessages(langCode);
            // Replace static messages with dynamic ones or merge them?
            // Usually, we set the messages for the specific locale in vue-i18n
            if (i18n.global.mode === 'legacy') {
                i18n.global.setLocaleMessage(langCode, res.data);
            } else {
                // @ts-ignore
                i18n.global.setLocaleMessage(langCode, res.data);
            }
            messagesLoaded.value[langCode] = true;
        } catch (error) {
            console.error(`Failed to load messages for ${langCode}:`, error);
        } finally {
            isLoading.value = false;
        }
    }

    async function changeLanguage(langCode: string, saveToProfile = false) {
        // First load messages if not already loaded
        await loadMessages(langCode);

        // Change locale in vue-i18n
        if (i18n.global.mode === 'legacy') {
            i18n.global.locale = langCode;
        } else {
            // @ts-ignore
            i18n.global.locale.value = langCode;
        }

        // Save to profile if needed (and logged in)
        if (saveToProfile) {
            try {
                await i18nApi.updatePreferredLanguage(langCode);
            } catch (error) {
                console.warn('Failed to save language preference to profile:', error);
            }
        }

        // Save to local storage for persistence across sessions (if not logged in)
        localStorage.setItem('user_language', langCode);
        document.documentElement.lang = langCode;
    }

    return {
        languages,
        activeLanguages,
        defaultLanguage,
        isLoading,
        fetchLanguages,
        loadMessages,
        changeLanguage
    };
});
