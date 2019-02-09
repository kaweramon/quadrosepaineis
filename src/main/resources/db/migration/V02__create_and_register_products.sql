CREATE TABLE product (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    price FLOAT(7,4) NOT NULL,    
    description VARCHAR(255),
    register_date DATETIME NOT NULL,
    width FLOAT(7,4),
    height FLOAT(7,4),
    diameter FLOAT(7,4),
    weight FLOAT(7,4),
    is_active BOOLEAN NOT NULL DEFAULT true,
    sequence INT(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO product (name, price, description, is_active, register_date, sequence) 
VALUES ('Teste 1', 100, 'Teste 1 Descrição', true, NOW(), 1);
INSERT INTO product (name, price, description, is_active, register_date, sequence) 
VALUES ('Teste 2', 100, 'Teste 2 Descrição', true, NOW(), 2);