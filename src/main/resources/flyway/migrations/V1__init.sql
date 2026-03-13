create table esgschema.categories
(
    id   bigserial primary key,
    name varchar(100) not null
);

CREATE TABLE esgschema.products
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



CREATE TABLE esgschema.users
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

CREATE TABLE esgschema.invoice
(
    id             BIGSERIAL PRIMARY KEY,
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    created_at     TIMESTAMP          NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP          NOT NULL DEFAULT now()
);

CREATE TABLE esgschema.invoice_item
(
    id          BIGSERIAL PRIMARY KEY,
    invoice_id  BIGINT         NOT NULL REFERENCES invoice (id) ON DELETE CASCADE,
    product_id  BIGINT         NOT NULL REFERENCES products (id),
    quantity    NUMERIC(10, 2) NOT NULL CHECK (quantity > 0),
    unit_price  NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_price NUMERIC(12, 2) NOT NULL CHECK (total_price >= 0)
);

create table if not exists esgschema.roles
(
    id   BIGSERIAL          not null primary key,
    name varchar(64) unique not null
);

create table if not exists esgschema.user_roles
(
    user_id BIGSERIAL references esgschema.users (id) not null,
    role_id BIGSERIAL references esgschema.roles (id) not null,
    primary key (user_id, role_id)
);


INSERT INTO esgschema.categories (name)
VALUES ('Панели');
INSERT INTO esgschema.categories (name)
VALUES ('Инверторы');
INSERT INTO esgschema.categories(name)
VALUES ('Система крепления');
INSERT INTO esgschema.categories(name)
VALUES ('Солнечный кабель');
INSERT INTO esgschema.categories (name)
VALUES ('Кабель-каналы');
INSERT INTO esgschema.categories (name)
VALUES ('Щитовая IP65 DC');
INSERT INTO esgschema.categories (name)
VALUES ('Щитовая IP65 AC');
INSERT INTO esgschema.categories(name)
VALUES ('Кабеля и провода');
INSERT INTO esgschema.categories (name)
VALUES ('Учет и измерение');

INSERT INTO esgschema.categories (name)
VALUES ('Монтаж / Пусконаладка / Доставка');
INSERT INTO esgschema.categories (name)
VALUES ('Пакеты документов');
INSERT INTO esgschema.categories (name)
VALUES ('Спецтехника');

INSERT INTO esgschema.categories (name)
VALUES ('Подстанционные работы');
INSERT INTO esgschema.categories (name)
VALUES ('Трасса / Опоры / Земляные работы');
INSERT INTO esgschema.categories (name)
VALUES ('Щитовые работы и замена');

