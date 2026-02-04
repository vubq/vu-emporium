-- Insert default admin (password: 123456)
INSERT INTO admins (email, password, full_name, enabled, created_at, updated_at)
VALUES ('admin@vu.bq', '$2b$10$J/.0jX1.Nuzx2dkVfAFtmuhKB6F9LYppwWmk3wnEpHvRhEeUE7S2m', 'Administrator', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert default user (password: 123456)
INSERT INTO users (email, password, full_name, phone, enabled, account_non_locked, created_at, updated_at)
VALUES ('user@vu.bq', '$2b$10$J/.0jX1.Nuzx2dkVfAFtmuhKB6F9LYppwWmk3wnEpHvRhEeUE7S2m', 'Demo User', '0123456789', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample categories (Status: ACTIVE/DRAFT)
INSERT INTO categories (name, slug, description, status, display_order, created_at, updated_at) VALUES
('Electronics', 'electronics', 'Electronic devices and accessories', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Fashion', 'fashion', 'Clothing and fashion accessories', 'ACTIVE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Home & Garden', 'home-garden', 'Home and garden products', 'ACTIVE', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sports', 'sports', 'Sports and outdoor equipment', 'ACTIVE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Books', 'books', 'Books and educational materials', 'ACTIVE', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('New Arrivals', 'new-arrivals', 'Latest products', 'DRAFT', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample products
INSERT INTO products (name, slug, description, base_price, sale_price, stock_quantity, sku, category_id, status, featured, created_at, updated_at) VALUES
('Wireless Headphones', 'wireless-headphones', 'High-quality wireless headphones with noise cancellation', 149.99, 99.99, 50, 'WH-001', (SELECT id FROM categories WHERE slug = 'electronics'), 'ACTIVE', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Smart Watch', 'smart-watch', 'Feature-rich smartwatch with health tracking', 249.99, 199.99, 30, 'SW-001', (SELECT id FROM categories WHERE slug = 'electronics'), 'ACTIVE', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cotton T-Shirt', 'cotton-tshirt', 'Comfortable cotton t-shirt in various colors', 29.99, 19.99, 100, 'TS-001', (SELECT id FROM categories WHERE slug = 'fashion'), 'ACTIVE', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Running Shoes', 'running-shoes', 'Professional running shoes for athletes', 99.99, 79.99, 40, 'RS-001', (SELECT id FROM categories WHERE slug = 'sports'), 'ACTIVE', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Coffee Maker', 'coffee-maker', 'Automatic coffee maker with timer', 79.99, 59.99, 25, 'CM-001', (SELECT id FROM categories WHERE slug = 'home-garden'), 'ACTIVE', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Yoga Mat', 'yoga-mat', 'Non-slip yoga mat for home workouts', 34.99, 24.99, 60, 'YM-001', (SELECT id FROM categories WHERE slug = 'sports'), 'ACTIVE', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Programming Book', 'programming-book', 'Learn programming from scratch', 49.99, 39.99, 80, 'PB-001', (SELECT id FROM categories WHERE slug = 'books'), 'ACTIVE', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Laptop Stand', 'laptop-stand', 'Ergonomic laptop stand for better posture', 44.99, 34.99, 45, 'LS-001', (SELECT id FROM categories WHERE slug = 'electronics'), 'ACTIVE', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample product images
INSERT INTO product_images (product_id, image_url) VALUES
((SELECT id FROM products WHERE slug = 'wireless-headphones'), 'https://via.placeholder.com/400x400?text=Wireless+Headphones'),
((SELECT id FROM products WHERE slug = 'smart-watch'), 'https://via.placeholder.com/400x400?text=Smart+Watch'),
((SELECT id FROM products WHERE slug = 'cotton-tshirt'), 'https://via.placeholder.com/400x400?text=Cotton+T-Shirt'),
((SELECT id FROM products WHERE slug = 'running-shoes'), 'https://via.placeholder.com/400x400?text=Running+Shoes'),
((SELECT id FROM products WHERE slug = 'coffee-maker'), 'https://via.placeholder.com/400x400?text=Coffee+Maker'),
((SELECT id FROM products WHERE slug = 'yoga-mat'), 'https://via.placeholder.com/400x400?text=Yoga+Mat'),
((SELECT id FROM products WHERE slug = 'programming-book'), 'https://via.placeholder.com/400x400?text=Programming+Book'),
((SELECT id FROM products WHERE slug = 'laptop-stand'), 'https://via.placeholder.com/400x400?text=Laptop+Stand');
