import { createI18n } from 'vue-i18n';

// All translations are loaded dynamically from API via i18nStore
const i18n = createI18n({
    legacy: false, // use Composition API
    locale: localStorage.getItem('locale') || 'vi',
    fallbackLocale: 'vi',
    messages: {},
    missingWarn: false,
    fallbackWarn: false,
});

export default i18n;
