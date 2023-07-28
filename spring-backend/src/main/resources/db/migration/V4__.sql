CREATE TABLE product_images (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(255) NOT NULL,
   product_id BIGINT,
   CONSTRAINT pk_product_images PRIMARY KEY (id)
);

ALTER TABLE product_images ADD CONSTRAINT FK_PRODUCT_IMAGES_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);