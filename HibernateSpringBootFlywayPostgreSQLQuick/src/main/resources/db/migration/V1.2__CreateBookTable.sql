CREATE TABLE book
(
    id    BIGSERIAL NOT NULL,
    isbn  CHARACTER VARYING(255) COLLATE pg_catalog."default",
    title CHARACTER VARYING(255) COLLATE pg_catalog."default",
    author_id bigint,
    CONSTRAINT book_pk PRIMARY KEY (id),
    CONSTRAINT book_author_fk FOREIGN KEY (author_id)
        REFERENCES author (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE book
    OWNER to postgres;