import { createI18n } from 'vue-i18n';
import en from './locales/en.json';
import vi from './locales/vi.json';

const messages = {
    en,
    vi
};

const i18n = createI18n({
    legacy: false, // use Composition API
    locale: localStorage.getItem('locale') || 'en',
    fallbackLocale: 'en',
    messages,
});

export default i18n;
