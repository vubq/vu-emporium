-- Sync missing i18n keys used by frontend but not yet in database
-- Using ON CONFLICT to safely skip if key already exists

INSERT INTO i18n_translations (language_code, translation_key, translation_value) VALUES
-- common.activate
('vi', 'common.activate', 'Kích hoạt'),
('en', 'common.activate', 'Activate'),

-- common.deactivate
('vi', 'common.deactivate', 'Ngừng kích hoạt'),
('en', 'common.deactivate', 'Deactivate'),

-- common.create
('vi', 'common.create', 'Tạo mới'),
('en', 'common.create', 'Create'),

-- common.default
('vi', 'common.default', 'Mặc định'),
('en', 'common.default', 'Default'),

-- common.enter
('vi', 'common.enter', 'Nhập'),
('en', 'common.enter', 'Enter'),

-- common.saving
('vi', 'common.saving', 'Đang lưu...'),
('en', 'common.saving', 'Saving...'),

-- common.no_results
('vi', 'common.no_results', 'Không có kết quả'),
('en', 'common.no_results', 'No results'),

-- media.add_image (used in MediaManager standalone mode)
('vi', 'media.add_image', 'Thêm hình ảnh'),
('en', 'media.add_image', 'Add Image'),

-- media.empty (used in MediaManager standalone mode)
('vi', 'media.empty', 'Thư mục trống'),
('en', 'media.empty', 'Folder is empty'),

-- product.benefits.return_title (used in HomePage benefits section)
('vi', 'product.benefits.return_title', 'Đổi trả dễ dàng'),
('en', 'product.benefits.return_title', 'Easy Returns'),

-- admin.i18n.languages.search_placeholder
('vi', 'admin.i18n.languages.search_placeholder', 'Tìm kiếm ngôn ngữ...'),
('en', 'admin.i18n.languages.search_placeholder', 'Search languages...'),

-- admin.i18n.translations.edit_key_title
('vi', 'admin.i18n.translations.edit_key_title', 'Chỉnh sửa mã bản dịch'),
('en', 'admin.i18n.translations.edit_key_title', 'Edit Translation Key'),

-- admin.media.confirm_delete_title (used by MediaManager confirm modal)
('vi', 'admin.media.confirm_delete_title', 'Xác nhận xóa'),
('en', 'admin.media.confirm_delete_title', 'Confirm Delete')

ON CONFLICT (translation_key, language_code) DO NOTHING;
