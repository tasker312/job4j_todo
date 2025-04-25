CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR,
    login    VARCHAR UNIQUE,
    password VARCHAR
);
