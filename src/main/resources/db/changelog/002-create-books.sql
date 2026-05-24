--liquibase formatted sql

--changeset targetapp:002-create-books
CREATE TABLE books (
    id             BIGSERIAL PRIMARY KEY,
    title          VARCHAR(255)   NOT NULL,
    isbn           VARCHAR(20)    UNIQUE,
    description    TEXT,
    published_date DATE,
    page_count     INT,
    language       VARCHAR(10),
    genre          VARCHAR(100),
    price          NUMERIC(10, 2),
    author_id      BIGINT         NOT NULL REFERENCES authors(id)
);

CREATE INDEX idx_books_author_id ON books(author_id);
