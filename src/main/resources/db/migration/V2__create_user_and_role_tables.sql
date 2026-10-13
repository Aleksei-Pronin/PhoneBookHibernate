CREATE TABLE role
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE application_user
(
    id        BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    role_id   BIGINT       NOT NULL,

    CONSTRAINT fk_application_user_role
        FOREIGN KEY (role_id)
            REFERENCES role (id)
);