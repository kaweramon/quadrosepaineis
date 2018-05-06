CREATE TABLE product (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    register_date DATETIME NOT NULL,
    width FLOAT(7,4),
    height FLOAT(7,4),
    diameter FLOAT(7,4),
    weight FLOAT(7,4),
    is_active BOOLEAN NOT NULL DEFAULT true
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO product (name, description, register_date, width, height, diameter, weight, is_active) VALUES 
('Mandala Sol 01', 'Mandala Sol Teste 01', NOW(), 100, 70, 50, null, true);

INSERT INTO product (name, description, register_date, width, height, diameter, weight, is_active) VALUES 
('Mandala Sol 02', 'Mandala Sol Teste 02', NOW(), 120, 50, 30, null, true);