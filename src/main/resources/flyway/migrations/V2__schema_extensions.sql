
-- 1. Таблица refresh_token (из V2)
CREATE TABLE IF NOT EXISTS esgschema.refresh_token
(
    id      SERIAL PRIMARY KEY,
    token   VARCHAR(128) NOT NULL,
    created TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT       NOT NULL REFERENCES esgschema.users (id) ON DELETE CASCADE
    );

-- 2. Таблицы для банковского сервиса (из V3) и изменение типа (из V4)
CREATE TABLE IF NOT EXISTS esgschema.account
(
    id      bigserial primary key,
    name    varchar(100)   not null,
    balance numeric(10, 2) not null default 0
    );

-- Создаём ENUM тип (будет использован в operation)
DO $$ BEGIN
CREATE TYPE esgschema.operation_type AS ENUM ('INCOME', 'EXPENSE');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS esgschema.operation
(
    id         bigserial primary key,
    type       esgschema.operation_type not null,
    amount     numeric(10, 2) not null,
    account_id bigint not null references esgschema.account(id) on delete cascade,
    comment    varchar(255),
    created_at timestamp default current_timestamp
    );

-- Изменяем тип колонки type с ENUM на VARCHAR (из V4)
ALTER TABLE esgschema.operation ALTER COLUMN type TYPE VARCHAR;

-- 3. Добавление колонки power_kwt в invoice (V5)
ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS power_kwt NUMERIC(10, 2);

-- 4. Переименование invoice_number → invoice_name (V6)
ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS invoice_name VARCHAR(255);
UPDATE esgschema.invoice SET invoice_name = invoice_number WHERE invoice_name IS NULL;
ALTER TABLE esgschema.invoice ALTER COLUMN invoice_name SET NOT NULL;
ALTER TABLE esgschema.invoice DROP COLUMN IF EXISTS invoice_number;

-- 5. Добавление vat_amount и sum_amount в invoice (V7)
ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS vat_amount NUMERIC(12, 2) DEFAULT 0 NOT NULL;
ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS sum_amount NUMERIC(12, 2) DEFAULT 0 NOT NULL;

-- 6. Добавление marginality в invoice_item и sum_marginality в invoice (V8)
ALTER TABLE esgschema.invoice_item ADD COLUMN IF NOT EXISTS marginality NUMERIC(12, 2) DEFAULT 0 NOT NULL;
ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS sum_marginality NUMERIC(12, 2) DEFAULT 0 NOT NULL;