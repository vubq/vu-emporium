CREATE TABLE product_variant_images (
    variant_id BIGINT NOT NULL,
    image_url VARCHAR(500),
    CONSTRAINT fk_variant_images_variant FOREIGN KEY (variant_id) REFERENCES product_variants(id) ON DELETE CASCADE
);
