CREATE TABLE contact
(
    id      SERIAL PRIMARY KEY,
    surname VARCHAR(100) NOT NULL,
    name    VARCHAR(100) NOT NULL,
    phone   VARCHAR(20)  NOT NULL
);