-- Add preferred_language column to admins table
ALTER TABLE admins 
ADD COLUMN preferred_language VARCHAR(5) DEFAULT 'vi';

-- Add preferred_language column to customers table
ALTER TABLE users
ADD COLUMN preferred_language VARCHAR(5) DEFAULT 'vi';

-- Add comments for documentation
COMMENT ON COLUMN admins.preferred_language IS 'User preferred language (en or vi)';
COMMENT ON COLUMN users.preferred_language IS 'User preferred language (en or vi)';
