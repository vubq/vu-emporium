---
trigger: "Khi tạo tính năng mới, thêm trang mới, thêm component mới, hoặc bất kỳ thay đổi nào có text hiển thị cho người dùng."
---

# i18n Convention - Dự Án Vu-Emporium

## Quy tắc BẮT BUỘC

Dự án này sử dụng **i18n dạng API** (load từ database), KHÔNG dùng file JSON hardcoded.

### Khi thêm bất kỳ text nào hiển thị trên UI:

1. **LUÔN dùng `$t('key')`** trong Vue template thay vì hardcode text
2. **Tạo Flyway migration mới** (version tiếp theo: V13, V14...) để INSERT keys mới cho **tất cả ngôn ngữ** (hiện tại: `vi` và `en`)
3. **Sử dụng `ON CONFLICT (translation_key, language_code) DO NOTHING`** để tránh lỗi trùng key
4. **Đặt tên key** theo format: `{section}.{subsection}.{name}` 
   - Ví dụ: `admin.orders.title`, `checkout.payment_method`, `profile.edit_avatar`

### Database Schema

```sql
-- Bảng: i18n_translations
-- Columns: id, translation_key, language_code, translation_value, group_name, created_at, updated_at
-- Unique: (translation_key, language_code)
```

### Logic Fallback

Backend (`I18nServiceImpl.getTranslationsForLanguage()`) tự động merge bản dịch default language làm base → ngôn ngữ mới chưa dịch sẽ hiển thị theo default.

### Ví dụ Migration

```sql
INSERT INTO i18n_translations (language_code, translation_key, translation_value) VALUES
('vi', 'checkout.title', 'Thanh toán'),
('en', 'checkout.title', 'Checkout')
ON CONFLICT (translation_key, language_code) DO NOTHING;
```

### Các section key hiện có

- `common.*` - Các từ chung (save, cancel, delete, loading...)
- `nav.*` - Navigation
- `home.*` - Trang chủ customer
- `product.*` - Sản phẩm (customer view)
- `cart.*` - Giỏ hàng
- `admin.*` - Admin panel
- `admin.manage.*` - Admin management pages
- `admin.forms.*` - Admin form fields
- `admin.i18n.*` - Admin i18n management
- `media.*` - Media manager
