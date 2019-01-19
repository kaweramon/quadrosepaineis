CREATE TABLE product_img_url (
	id_product BIGINT(20) NOT NULL,
    id_img_url BIGINT(20) NOT NULL,
    PRIMARY KEY (`id_product`,`id_img_url`),
    CONSTRAINT `fk_product_img_url_product` FOREIGN KEY (id_product) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_product_img_url` FOREIGN KEY (id_img_url) REFERENCES img_url(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;