CREATE TABLE financial_release (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    obs VARCHAR(512),
    value BIGINT(20) NOT NULL,
    type VARCHAR(20) NOT NULL,
    due_date DATE,
    register_date TIMESTAMP NOT NULL,
    pay_date DATE,
    id_category BIGINT(20) NOT NULL,
    id_product BIGINT(20) NOT NULL,
    FOREIGN KEY (id_category) REFERENCES category(id),
    FOREIGN KEY (id_product) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;