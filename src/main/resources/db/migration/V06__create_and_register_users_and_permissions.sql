CREATE TABLE user (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permission (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_permissions (
	id_user BIGINT(20) NOT NULL,
    id_permission BIGINT(20) NOT NULL,
    PRIMARY KEY(id_user, id_permission),
    CONSTRAINT `fk_user_permissions_user` FOREIGN KEY (id_user) REFERENCES user(id),
    CONSTRAINT `fk_user_permissions_permission` FOREIGN KEY (id_permission) REFERENCES permission(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO permission (id, description) VALUES (1, "CREATE_PRODUCT");
INSERT INTO permission (id, description) VALUES (2, "UPDATE_PRODUCT");
INSERT INTO permission (id, description) VALUES (3, "RETRIEVE_PRODUCT");
INSERT INTO permission (id, description) VALUES (4, "DELETE_PRODUCT");

INSERT INTO permission (id, description) VALUES (5, "CREATE_CATEGORY");
INSERT INTO permission (id, description) VALUES (6, "UPDATE_CATEGORY");
INSERT INTO permission (id, description) VALUES (7, "RETRIEVE_CATEGORY");
INSERT INTO permission (id, description) VALUES (8, "DELETE_CATEGORY");

INSERT INTO permission (id, description) VALUES (9, "CREATE_FINANCIAL_RELEASE");
INSERT INTO permission (id, description) VALUES (10, "UPDATE_FINANCIAL_RELEASE");
INSERT INTO permission (id, description) VALUES (11, "RETRIEVE_FINANCIAL_RELEASE");
INSERT INTO permission (id, description) VALUES (12, "DELETE_FINANCIAL_RELEASE");

INSERT INTO user (id, name, email, password) VALUES 
(1, 'quadrosepaineis', 'arteemareias@gmail.com', '$2a$10$g0rO4knWhv4BFoSJDFxFn.DVUTSckBV5gh3kSG6cX4b1pJQioIgpm');
INSERT INTO user (id, name, email, password) VALUES 
(2, 'teste1', 'teste1@gmail.com', '$2a$10$7HmeHLwzWc8Y0Tq0hGCY.u1OX3XEssiC.3v1p5BLRyYqzM8W0U0M2');


INSERT INTO user_permissions (id_user, id_permission) VALUES 
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12);

INSERT INTO user_permissions (id_user, id_permission) VALUES 
(2,3),(2,7),(2,11);

