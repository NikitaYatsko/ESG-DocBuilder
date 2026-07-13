create table password_reset_token
(
    id         serial          not null primary key,
    token      varchar(255) not null,
    expires_at timestamp    not null,
    is_used    boolean      not null,
    user_id    bigint       not null references esgschema.users (id)
)