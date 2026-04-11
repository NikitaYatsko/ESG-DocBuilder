CREATE TABLE IF NOT EXISTS esgschema.categories
(
    id   bigserial primary key,
    name varchar(100) not null
);

CREATE TABLE IF NOT EXISTS esgschema.products
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255)   NOT NULL,
    category_id  bigint references categories (id),
    cost_price   NUMERIC(12, 2) NOT NULL CHECK (cost_price >= 0),
    sell_price   NUMERIC(12, 2) NOT NULL CHECK (cost_price >= 0),
    marginality  NUMERIC(5, 2)  NOT NULL DEFAULT 0 CHECK (marginality >= 0),
    vat          numeric                 default 0,
    updated_at   timestamp               DEFAULT CURRENT_TIMESTAMP,
    created_at   TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    type_of_unit varchar(50)    not null
);

CREATE TABLE IF NOT EXISTS esgschema.users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),
    image_url  varchar(255),
    phone      varchar(15)
);

CREATE TABLE IF NOT EXISTS esgschema.invoice
(
    id             BIGSERIAL PRIMARY KEY,
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    created_at     TIMESTAMP          NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP          NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS esgschema.invoice_item
(
    id             BIGSERIAL PRIMARY KEY,
    invoice_id     BIGINT         NOT NULL REFERENCES invoice (id) ON DELETE CASCADE,
    product_id     BIGINT         NOT NULL REFERENCES products (id),
    quantity       NUMERIC(10, 2) NOT NULL CHECK (quantity > 0),
    vat_multiplier numeric default 0,
    unit_price     NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_price    NUMERIC(12, 2) NOT NULL CHECK (total_price >= 0)
);

CREATE TABLE IF NOT EXISTS esgschema.roles
(
    id   BIGSERIAL          not null primary key,
    name varchar(64) unique not null
);

CREATE TABLE IF NOT EXISTS esgschema.user_roles
(
    user_id BIGSERIAL references esgschema.users (id) not null,
    role_id BIGSERIAL references esgschema.roles (id) not null,
    primary key (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS esgschema.refresh_token
(
    id      SERIAL PRIMARY KEY,
    token   VARCHAR(128) NOT NULL,
    created TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT       NOT NULL REFERENCES esgschema.users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS esgschema.account
(
    id      bigserial primary key,
    name    varchar(100)   not null,
    balance numeric(10, 2) not null default 0
);

CREATE TYPE operation_type AS ENUM ('INCOME', 'EXPENSE');

CREATE TABLE IF NOT EXISTS esgschema.operation
(
    id         bigserial primary key,
    type       operation_type not null,
    amount     numeric(10, 2) not null,
    account_id bigint         not null
        references esgschema.account (id)
            on delete cascade,
    comment    varchar(255),
    created_at timestamp default current_timestamp
);

ALTER TABLE esgschema.operation
    ALTER COLUMN type TYPE VARCHAR;

insert into esgschema.roles(id, name)
VALUES (1, 'ADMIN');

insert into esgschema.roles(id, name)
VALUES (2, 'USER');

INSERT INTO esgschema.categories (name)
VALUES ('Панели'),
       ('Инверторы'),
       ('Система крепления'),
       ('Солнечный кабель'),
       ('Кабель-каналы'),
       ('Щитовая IP65 DC'),
       ('Щитовая IP65 AC'),
       ('Кабеля и провода'),
       ('Учет и измерение'),
       ('Монтаж / Пусконаладка / Доставка'),
       ('Пакеты документов'),
       ('Спецтехника'),
       ('Подстанционные работы'),
       ('Трасса / Опоры / Земляные работы'),
       ('Щитовые работы и замена');

INSERT INTO esgschema.users (email, first_name, last_name, password)
VALUES ('esgroupandrei@gmail.com',
        'Андрей',
        'Яцко',
        '$2a$10$RA7gVQdQ4gudgZmbuOrzbe1FnWbw1LjBHtQVOSjo.li1tQspLnFI.');

INSERT INTO esgschema.account(name, balance)
values ('Cashbox', 0);

INSERT INTO esgschema.account(name, balance)
values ('Bank', 0);

insert into esgschema.user_roles(user_id, role_id) VALUES (1,1)