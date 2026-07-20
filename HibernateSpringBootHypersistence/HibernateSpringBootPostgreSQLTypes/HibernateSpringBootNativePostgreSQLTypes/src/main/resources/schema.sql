-- The native_tag.name column is declared as citext, so the citext extension
-- must exist before Hibernate generates the table. This runs ahead of schema
-- generation because spring.jpa.defer-datasource-initialization is left at its
-- default (false). CREATE EXTENSION IF NOT EXISTS is idempotent.
CREATE EXTENSION IF NOT EXISTS citext;
