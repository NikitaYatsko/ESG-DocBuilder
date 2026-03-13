CREATE TABLE esgschema.refresh_token
(
    id      SERIAL PRIMARY KEY,
    token   VARCHAR(128) NOT NULL,
    created TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT       NOT NULL REFERENCES esgschema.users (id) ON DELETE CASCADE
);