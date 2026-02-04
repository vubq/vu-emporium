---
trigger: always_on
---

# UI-CONSISTENCY.MD - Admin Dashboard Design System

> **Mục tiêu**: Đảm bảo giao diện Admin Dashboard đồng bộ tuyệt đối với `ProductManagementPage.vue` và `ProductForm.vue`.

---

## 🎨 1. CORE LAYOUT & STRUCTURE
- **Page Container**: `min-h-screen bg-gray-50 p-6 font-sans`
- **Content Wrapper**: `max-w-7xl mx-auto` (cho Forms) hoặc `w-full h-full flex flex-col` (cho Tables).
- **Cards/Containers**: `bg-white rounded-2xl shadow-sm border border-gray-100` (Tuyệt đối không dùng `rounded-lg` hay `shadow-md` mặc định).
- **Padding**: `p-6` hoặc `p-8` cho các section chính.

## 🔤 2. TYPOGRAPHY
- **Page Title**: `text-3xl font-bold text-gray-900 tracking-tight` (Form) hoặc `text-2xl` (List).
- **Section Headers**: `text-lg font-bold text-gray-900 mb-6 flex items-center`.
- **Labels**: `block text-sm font-semibold text-gray-700 mb-2`.
- **Subtitles**: `text-sm text-gray-500`.

## 🔘 3. COMPONENTS & INTERACTION

### Buttons
- **Primary**: `px-6 py-2.5 rounded-xl bg-gray-900 text-white hover:bg-black font-medium transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5`.
- **Secondary/Cancel**: `px-6 py-2.5 rounded-xl text-gray-700 bg-white border border-gray-200 hover:bg-gray-50`.
- **Icon Buttons**: `p-2 hover:bg-gray-100 rounded-lg transition-colors`.

### Inputs & Forms
- **Standard Input**: `w-full px-4 py-3 rounded-xl border border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 focus:outline-none transition-all placeholder-gray-400`.
- **Search Bar**: `rounded-xl border border-gray-300` (kết hợp icon search absolute left).

### Libraries (Bắt buộc)
- **Headless UI**: Sử dụng `Listbox`, `Menu`, `Dialog`, `Switch`, `Combobox` cho các dropdown/modal/toggle.
- **Icons**: SVG trực tiếp (Heroicons style), không dùng thư viện icon font.

## 🎭 4. COLOR PALETTE
- **Primary**: Indigo (`text-indigo-600`, `bg-indigo-600`, `ring-indigo-500`).
- **Background**: Gray 50 (`bg-gray-50`).
- **Status Colors**:
  - Active/Success: Green (`text-green-600`, `bg-green-50`).
  - Draft/Warning: Yellow (`text-yellow-600`, `bg-yellow-50`).
  - Archived/Inactive: Gray (`text-gray-500`, `bg-gray-50`).

## ⚠️ 5. QUY TẮC BẤT DI BẤT DỊCH
1. **Bo góc**: Luôn là `rounded-xl` (12px) hoặc `rounded-2xl` (16px) cho container lớn. KHÔNG dùng `rounded` hoặc `rounded-md` cho các phần tử chính.
2. **Shadow**: Ưu tiên `shadow-sm` cho card tĩnh, `shadow-lg` cho nút chính.
3. **Transition**: Luôn thêm `transition-all` hoặc `transition-colors` cho các phần tử tương tác.
4. **Icons**: Sử dụng nền màu nhạt (`bg-indigo-100`) cho icon tiêu đề section (theo phong cách trong `ProductForm.vue`).

---
*Rule này được tạo tự động để đảm bảo tính nhất quán UI theo yêu cầu của User.*
