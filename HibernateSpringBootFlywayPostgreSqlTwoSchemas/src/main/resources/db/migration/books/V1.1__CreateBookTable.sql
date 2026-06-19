CREATE TABLE book (
  id      BIGSERIAL NOT NULL,
  isbn    CHARACTER VARYING(50) COLLATE pg_catalog."default",
  title   CHARACTER VARYING(50) COLLATE pg_catalog."default",
  authors CHARACTER VARYING(50) COLLATE pg_catalog."default",
  CONSTRAINT book_pk PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE book
    OWNER to postgres;