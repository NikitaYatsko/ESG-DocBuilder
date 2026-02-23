create table if not exists "esg-schema".roles
(
    id   BIGSERIAL          not null primary key,
    name varchar(64) unique not null
);

create table if not exists "esg-schema".user_roles
(
    user_id BIGSERIAL references "esg-schema".users (id) not null,
    role_id BIGSERIAL references "esg-schema".roles (id) not null,
    primary key (user_id, role_id)
);
