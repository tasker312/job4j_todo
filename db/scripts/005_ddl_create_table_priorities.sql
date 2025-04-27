CREATE TABLE priorities
(
    id       SERIAL PRIMARY KEY,
    name     TEXT UNIQUE NOT NULL,
    position int
);

INSERT INTO priorities (name, position)
VALUES ('Срочный', 1),
       ('Обычный', 2);

ALTER TABLE tasks
    ADD COLUMN priority_id int REFERENCES priorities (id);

UPDATE tasks
SET priority_id = (SELECT id FROM priorities WHERE name = 'Срочный');
