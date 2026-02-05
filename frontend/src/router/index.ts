import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { useAdminAuthStore } from '@/stores/adminAuthStore';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        // Customer Routes
        {
            path: '/',
            component: () => import('@/layouts/CustomerLayout.vue'),
            children: [
                {
                    path: '',
                    name: 'home',
                    component: () => import('@/modules/customer/pages/HomePage.vue'),
                    meta: { title: 'Home' },
                },
                {
                    path: 'products',
                    name: 'products',
                    component: () => import('@/modules/customer/pages/ProductListPage.vue'),
                    meta: { title: 'Products' },
                },
                {
                    path: 'products/:slug',
                    name: 'product-detail',
                    component: () => import('@/modules/customer/pages/ProductDetailPage.vue'),
                    meta: { title: 'Product Detail' },
                },
                {
                    path: 'cart',
                    name: 'cart',
                    component: () => import('@/modules/customer/pages/CartPage.vue'),
                    meta: { title: 'Shopping Cart' },
                },
                {
                    path: 'checkout',
                    name: 'checkout',
                    component: () => import('@/modules/customer/pages/CheckoutPage.vue'),
                    meta: { requiresAuth: true, title: 'Checkout' },
                },
                {
                    path: 'orders',
                    name: 'orders',
                    component: () => import('@/modules/customer/pages/OrderHistoryPage.vue'),
                    meta: { requiresAuth: true, title: 'My Orders' },
                },
                {
                    path: 'profile',
                    name: 'profile',
                    component: () => import('@/modules/customer/pages/ProfilePage.vue'),
                    meta: { requiresAuth: true, title: 'My Profile' },
                },
            ],
        },
        // Customer Auth Routes
        {
            path: '/login',
            name: 'login',
            component: () => import('@/pages/LoginPage.vue'),
            meta: { title: 'Login' },
        },
        {
            path: '/register',
            name: 'register',
            component: () => import('@/pages/RegisterPage.vue'),
            meta: { title: 'Register' },
        },
        // Admin Auth Routes
        {
            path: '/admin/login',
            name: 'admin-login',
            component: () => import('@/pages/admin/AdminLoginPage.vue'),
            meta: { title: 'Admin Login' },
        },
        // Admin Routes
        {
            path: '/admin',
            component: () => import('@/layouts/AdminLayout.vue'),
            meta: { requiresAdminAuth: true },
            children: [
                {
                    path: '',
                    name: 'admin-dashboard',
                    component: () => import('@/pages/admin/AdminDashboardPage.vue'),
                    meta: {
                        title: 'Dashboard',
                        icon: 'HomeIcon',
                        showInSidebar: true
                    },
                },
                {
                    path: 'products',
                    name: 'admin-products',
                    component: () => import('@/modules/admin/pages/ProductManagementPage.vue'),
                    meta: {
                        title: 'Product Management',
                        model: 'Products', // Group name
                        icon: 'CubeIcon',
                        showInSidebar: true
                    },
                },
                {
                    path: 'categories',
                    name: 'admin-categories',
                    component: () => import('@/modules/admin/pages/CategoryManagementPage.vue'),
                    meta: {
                        title: 'Category Management',
                        model: 'Products', // Group with Products? Or standalone? Let's group if Sidebar supports it.
                        // Actually Products route has `model: 'Products'`. 
                        // If I give this `model: 'Products'`, they might be grouped.
                        // Let's try grouping them.
                        icon: 'TagIcon',
                        showInSidebar: true
                    },
                },
                {
                    path: 'orders',
                    name: 'admin-orders',
                    component: () => import('@/pages/admin/AdminOrdersPage.vue'),
                    meta: {
                        title: 'Order Management',
                        icon: 'ShoppingBagIcon',
                        showInSidebar: true
                    },
                },
                {
                    path: 'users',
                    name: 'admin-users',
                    component: () => import('@/pages/admin/AdminUsersPage.vue'),
                    meta: {
                        title: 'User Management',
                        label: 'Customers',
                        icon: 'UsersIcon',
                        showInSidebar: true
                    },
                },
                {
                    path: 'media',
                    name: 'admin-media',
                    component: () => import('@/modules/admin/pages/MediaPage.vue'),
                    meta: {
                        title: 'Media Manager',
                        icon: 'PhotoIcon',
                        showInSidebar: true
                    },
                },
                {
                    path: 'languages',
                    name: 'admin-languages',
                    component: () => import('@/modules/admin/pages/LanguageManagementPage.vue'),
                    meta: {
                        title: 'Languages',
                        model: 'Settings',
                        icon: 'LanguageIcon',
                        showInSidebar: true
                    },
                },
                {
                    path: 'translations',
                    name: 'admin-translations',
                    component: () => import('@/modules/admin/pages/TranslationManagementPage.vue'),
                    meta: {
                        title: 'Translations',
                        model: 'Settings',
                        icon: 'TranslateIcon',
                        showInSidebar: true
                    },
                },
            ],
        },
        // 404 Route
        {
            path: '/:pathMatch(.*)*',
            name: 'not-found',
            component: () => import('@/pages/NotFoundPage.vue'),
            meta: { title: 'Page Not Found' },
        },
    ],
});

// Navigation guards
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();
    const adminAuthStore = useAdminAuthStore();

    // Update document title
    const defaultTitle = 'Vu Emporium';
    const adminTitleSuffix = ' - Admin Panel';
    const customerTitleSuffix = ' - Vu Emporium';

    if (to.meta.title) {
        if (to.path.startsWith('/admin')) {
            document.title = `${to.meta.title}${adminTitleSuffix}`;
        } else {
            document.title = `${to.meta.title}${customerTitleSuffix}`;
        }
    } else {
        document.title = defaultTitle;
    }

    // Admin routes protection
    if (to.meta.requiresAdminAuth) {
        if (!adminAuthStore.isAuthenticated) {
            next({ name: 'admin-login', query: { redirect: to.fullPath } });
            return;
        }
    }

    // Customer routes protection
    if (to.meta.requiresAuth) {
        if (!authStore.isAuthenticated) {
            next({ name: 'login', query: { redirect: to.fullPath } });
            return;
        }
    }

    // Guards removed to allow concurrent sessions
    // Each route now solely relies on its specific authentication requirement
    // defined in 'requiresAuth' or 'requiresAdminAuth' blocks above.

    next();
});

export default router;
