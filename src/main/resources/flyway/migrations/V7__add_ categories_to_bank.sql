CREATE TABLE esgschema.banking_categories(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

ALTER TABLE esgschema.operation
    ADD COLUMN id_categories bigint;

ALTER TABLE esgschema.operation
    ADD CONSTRAINT fk_operation_category
        FOREIGN KEY (id_categories)
            REFERENCES esgschema.banking_categories(id);

INSERT INTO esgschema.banking_categories (name)
VALUES
    ('Топливо'),
    ('Аренда склада'),
    ('Аренда офиса'),
    ('Услуги Бухгалтера'),
    ('Услуги Автосервиса'),
    ('Услуги Коммунальные'),
    ('Услуги телефонии, интернет и софта'),
    ('Налоги'),
    ('Зарплаты'),
    ('Реклама'),
    ('НДС'),
    ('maib комиссия'),
    ('Услуги проектантов и Авториз'),
    ('Прочие расходы компании'),
    ('Пусконаладочные работы'),
    ('Закупка материалов');