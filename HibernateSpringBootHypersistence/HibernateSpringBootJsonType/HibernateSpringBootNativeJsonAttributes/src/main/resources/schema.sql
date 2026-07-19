-- bookstoredb is shared by all example modules. Drop the tables that sibling
-- modules create with a foreign key to this module's tables, so Hibernate's
-- ddl-auto=create can recreate the schema cleanly.
drop table if exists post_comment;
drop table if exists post_tag;
drop table if exists post_details;
drop table if exists tag;
