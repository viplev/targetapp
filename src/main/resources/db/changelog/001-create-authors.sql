--liquibase formatted sql

--changeset targetapp:001-create-authors
CREATE TABLE authors (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL
);
