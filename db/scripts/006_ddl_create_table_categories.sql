CREATE TABLE categories
(
    id   SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

INSERT INTO categories (name)
VALUES ('Работа'),
       ('Учеба'),
       ('Дом');

CREATE TABLE task_category
(
    task_id     INT REFERENCES tasks (id)      NOT NULL,
    category_id INT REFERENCES categories (id) NOT NULL
);

