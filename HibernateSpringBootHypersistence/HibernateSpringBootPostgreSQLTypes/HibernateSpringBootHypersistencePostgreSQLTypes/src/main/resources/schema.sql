-- The hstore and citext columns need their PostgreSQL extensions to exist
-- before Hibernate generates the tables that reference them. This script runs
-- ahead of schema generation because spring.jpa.defer-datasource-initialization
-- is left at its default (false). CREATE EXTENSION IF NOT EXISTS is idempotent.
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS citext;
