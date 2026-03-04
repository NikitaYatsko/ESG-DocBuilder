create table categories
(
    id   bigserial primary key,
    name varchar(100) not null
);
CREATE TABLE products
(
    id             BIGSERIAL PRIMARY KEY,

    name           VARCHAR(255)   NOT NULL,              -- Наименование товара
    category_id    bigint references categories (id),

    has_vat        BOOLEAN        NOT NULL DEFAULT true, -- НДС (да/нет)

    cost_price     NUMERIC(12, 2) NOT NULL CHECK (cost_price >= 0),
    -- Себестоимость

    markup_percent NUMERIC(6, 2)  NOT NULL CHECK (markup_percent >= 0),
    -- Наценка в процентах
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,

    created_at     TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    type_of_unit   varchar(50)    not null
);



CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,

    email      VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,

    password   VARCHAR(255) NOT NULL,

    created_at TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE TABLE invoice
(
    id             BIGSERIAL PRIMARY KEY,
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    created_at     TIMESTAMP          NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP          NOT NULL DEFAULT now()
);

CREATE TABLE invoice_item
(
    id          BIGSERIAL PRIMARY KEY,
    invoice_id  BIGINT         NOT NULL REFERENCES invoice (id) ON DELETE CASCADE,
    product_id  BIGINT         NOT NULL REFERENCES products (id),
    vat_rate    NUMERIC(5, 2)  NOT NULL DEFAULT 0 CHECK (vat_rate >= 0),
    quantity    NUMERIC(10, 2) NOT NULL CHECK (quantity > 0),
    unit_price  NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_price NUMERIC(12, 2) NOT NULL
);

