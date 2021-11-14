DROP TABLE people IF EXISTS;

CREATE TABLE people  (
                         person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
                         first_name VARCHAR(20),
                         last_name VARCHAR(20)
);

DROP TABLE transaction IF EXISTS;

CREATE TABLE transaction  (
                         ACCOUNT VARCHAR(40) IDENTITY NOT NULL PRIMARY KEY,
                         AMOUNT BIGINT,
                         TIMESTAMP DATE
);
