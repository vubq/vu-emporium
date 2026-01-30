-- Create admins table
CREATE TABLE admins (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Drop user_roles table as we no longer use role-based access
DROP TABLE IF EXISTS user_roles;

-- Insert default admin account
-- Password: admin123 (BCrypt encoded)
INSERT INTO admins (email, password, full_name, enabled, created_at, updated_at)
VALUES ('admin@vuemporium.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
