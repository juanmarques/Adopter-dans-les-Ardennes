CREATE TABLE users (
                       id BIGSERIAL primary key,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       roles TEXT[]
);
