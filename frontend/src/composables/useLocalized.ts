import { computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { useI18nStore } from '@/stores/i18nStore';

export function useLocalized() {
    const { locale } = useI18n();
    const i18nStore = useI18nStore();

    const getLocalized = (entity: any, field: string): string => {
        if (!entity) return '';

        const currentLocale = locale.value;
        const defaultLocale = i18nStore.defaultLanguage?.code || 'vi';

        // 1. Try Current Locale
        if (entity.translations?.[currentLocale]?.[field]) {
            return entity.translations[currentLocale][field];
        }

        // 2. Try Default Locale (Fallback)
        if (entity.translations?.[defaultLocale]?.[field]) {
            return entity.translations[defaultLocale][field];
        }

        // 3. Try Root Field
        if (entity[field]) {
            return entity[field];
        }

        return '';
    };

    return {
        getLocalized
    };
}
