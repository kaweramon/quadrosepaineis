CREATE TABLE financial_release_categories (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO financial_release_categories (name) VALUES ('Venda');
INSERT INTO financial_release_categories (name) VALUES ('Material');