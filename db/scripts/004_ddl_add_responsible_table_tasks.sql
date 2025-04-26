ALTER TABLE tasks
    ADD COLUMN responsible_id INT REFERENCES users (id) NOT NULL;

