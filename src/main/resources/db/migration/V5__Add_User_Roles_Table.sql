CREATE TABLE user_roles (
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(255) NOT NULL
);

INSERT INTO user_roles (user_id, role)
VALUES (1, 'user'), (2, 'guest');
