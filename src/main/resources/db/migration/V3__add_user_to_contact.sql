ALTER TABLE contact
    ADD COLUMN user_id BIGINT;

ALTER TABLE contact
    ADD CONSTRAINT fk_contact_user
        FOREIGN KEY (user_id)
            REFERENCES "application_user" (id);