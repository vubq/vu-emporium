import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import i18n from './i18n';
import './styles/main.css';

import { useI18nStore } from './stores/i18nStore';

const app = createApp(App);
const pinia = createPinia();
app.use(pinia);

// Initialize i18n before mounting
const initApp = async () => {
    const i18nStore = useI18nStore();

    // 1. Fetch available languages
    await i18nStore.fetchLanguages();

    // 2. Determine initial language (from localStorage, preference, or default)
    const storedLang = localStorage.getItem('user_language') ||
        localStorage.getItem('adminLocale') ||
        localStorage.getItem('customerLocale');

    const initialLang = storedLang || i18nStore.defaultLanguage?.code || 'vi';

    // 3. Load initial translations
    await i18nStore.changeLanguage(initialLang);

    app.use(router);
    app.use(i18n);
    app.mount('#app');
};

initApp();
