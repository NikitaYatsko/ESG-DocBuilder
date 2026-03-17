create table esgschema.account
(
    id      bigserial primary key,
    name    varchar(100)   not null,
    balance numeric(10, 2) not null default 0
);

CREATE TYPE operation_type AS ENUM ('INCOME', 'EXPENSE');

create table esgschema.operation
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