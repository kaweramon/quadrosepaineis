CREATE TABLE category (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO category (name) VALUES ('Mandala');
INSERT INTO category (name) VALUES ('Mandala Sol');