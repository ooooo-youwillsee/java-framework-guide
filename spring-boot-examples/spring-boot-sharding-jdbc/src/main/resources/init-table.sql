## mysql

CREATE TABLE IF NOT EXISTS t_order
(
    order_id   BIGINT AUTO_INCREMENT,
    user_id    BIGINT NOT NULL,
    address_id BIGINT NOT NULL,
    status     VARCHAR(50),
    PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS t_order_item
(
    order_item_id BIGINT AUTO_INCREMENT,
    order_id      BIGINT,
    user_id       INT NOT NULL,
    status        VARCHAR(50),
    PRIMARY KEY (order_item_id)
);


CREATE TABLE IF NOT EXISTS t_address
(
    address_id   BIGINT       NOT NULL,
    address_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (address_id)
);

CREATE TABLE IF NOT EXISTS t_user
(
    user_id   BIGINT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(200),
    pwd       VARCHAR(200),
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS t_shadow_user
(
    user_id   BIGINT NOT NULL AUTO_INCREMENT,
    user_type INT(11),
    user_name VARCHAR(200),
    pwd       VARCHAR(200),
    PRIMARY KEY (user_id)
);