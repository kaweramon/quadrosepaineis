CREATE TABLE product_categories (
	id_product BIGINT(20) NOT NULL,
	id_category BIGINT(20) NOT NULL,
    PRIMARY KEY (`id_product`,`id_category`),
	KEY `fk_product_categories_id` (`id_product`),
	CONSTRAINT `fk_product_categories_product` FOREIGN KEY (id_product) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_product_categories_category` FOREIGN KEY (id_category) REFERENCES category(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO product_categories (id_product, id_category) VALUES (1,1);
INSERT INTO product_categories (id_product, id_category) VALUES (1,2);