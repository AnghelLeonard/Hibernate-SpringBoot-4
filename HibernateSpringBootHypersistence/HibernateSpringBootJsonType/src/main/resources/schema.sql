-- bookstoredb is shared by all example projects. Tables created by sibling
-- projects that are not mapped here would block Hibernate from recreating the
-- tables this project maps, so drop them before the schema is exported.
drop table if exists post_tag;
drop table if exists tag;
