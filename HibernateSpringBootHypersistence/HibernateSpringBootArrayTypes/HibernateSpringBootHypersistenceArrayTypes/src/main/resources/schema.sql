-- The post_status[] column needs a PostgreSQL enum type that exists before
-- Hibernate generates the table. This runs ahead of schema generation because
-- spring.jpa.defer-datasource-initialization is left at its default (false).
-- DROP ... CASCADE keeps the script idempotent across ddl-auto=create runs.
DROP TYPE IF EXISTS post_status CASCADE;
CREATE TYPE post_status AS ENUM ('DRAFT', 'PUBLISHED', 'ARCHIVED');
