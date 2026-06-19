CREATE TABLE author
(
    id    BIGSERIAL NOT NULL,
    age   INTEGER NOT NULL,
    genre CHARACTER VARYING(255) COLLATE pg_catalog."default",
    name  CHARACTER VARYING(255) COLLATE pg_catalog."default",
    books CHARACTER VARYING(255) COLLATE pg_catalog."default",
    CONSTRAINT author_pk PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE author
    OWNER to postgres;