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