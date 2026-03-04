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
