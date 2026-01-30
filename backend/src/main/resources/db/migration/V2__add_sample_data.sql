-- Insert admin user (password: admin123)
INSERT INTO users (email, password, full_name, phone, enabled, account_non_locked)
VALUES ('admin@vuemporium.com', '$2a$10$xqTjN5QqKqKqKqKqKqKqKuN5QqKqKqKqKqKqKqKqKqKqKqKqKqKqK', 'Administrator', '0123456789', TRUE, TRUE);

-- Add admin role
INSERT INTO user_roles (user_id, role)
VALUES (1, 'ROLE_ADMIN');

-- Insert sample categories
INSERT INTO categories (name, slug, description, active, display_order) VALUES
('Electronics', 'electronics', 'Electronic devices and accessories', TRUE, 1),
('Fashion', 'fashion', 'Clothing and fashion accessories', TRUE, 2),
('Home & Garden', 'home-garden', 'Home and garden products', TRUE, 3),
('Sports', 'sports', 'Sports and outdoor equipment', TRUE, 4),
('Books', 'books', 'Books and educational materials', TRUE, 5);

-- Insert sample products
INSERT INTO products (name, slug, description, price, compare_at_price, stock_quantity, sku, category_id, status, featured) VALUES
('Wireless Headphones', 'wireless-headphones', 'High-quality wireless headphones with noise cancellation', 99.99, 149.99, 50, 'WH-001', 1, 'ACTIVE', TRUE),
('Smart Watch', 'smart-watch', 'Feature-rich smartwatch with health tracking', 199.99, 249.99, 30, 'SW-001', 1, 'ACTIVE', TRUE),
('Cotton T-Shirt', 'cotton-tshirt', 'Comfortable cotton t-shirt in various colors', 19.99, 29.99, 100, 'TS-001', 2, 'ACTIVE', FALSE),
('Running Shoes', 'running-shoes', 'Professional running shoes for athletes', 79.99, 99.99, 40, 'RS-001', 4, 'ACTIVE', TRUE),
('Coffee Maker', 'coffee-maker', 'Automatic coffee maker with timer', 59.99, 79.99, 25, 'CM-001', 3, 'ACTIVE', FALSE),
('Yoga Mat', 'yoga-mat', 'Non-slip yoga mat for home workouts', 24.99, 34.99, 60, 'YM-001', 4, 'ACTIVE', FALSE),
('Programming Book', 'programming-book', 'Learn programming from scratch', 39.99, 49.99, 80, 'PB-001', 5, 'ACTIVE', FALSE),
('Laptop Stand', 'laptop-stand', 'Ergonomic laptop stand for better posture', 34.99, 44.99, 45, 'LS-001', 1, 'ACTIVE', FALSE);

-- Insert sample product images
INSERT INTO product_images (product_id, image_url) VALUES
(1, 'https://via.placeholder.com/400x400?text=Wireless+Headphones'),
(2, 'https://via.placeholder.com/400x400?text=Smart+Watch'),
(3, 'https://via.placeholder.com/400x400?text=Cotton+T-Shirt'),
(4, 'https://via.placeholder.com/400x400?text=Running+Shoes'),
(5, 'https://via.placeholder.com/400x400?text=Coffee+Maker'),
(6, 'https://via.placeholder.com/400x400?text=Yoga+Mat'),
(7, 'https://via.placeholder.com/400x400?text=Programming+Book'),
(8, 'https://via.placeholder.com/400x400?text=Laptop+Stand');
