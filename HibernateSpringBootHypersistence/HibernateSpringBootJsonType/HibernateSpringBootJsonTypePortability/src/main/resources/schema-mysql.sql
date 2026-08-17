-- tag::schema-mysql[]
drop table if exists portable_post;

create table portable_post (
    id bigint not null,
    title varchar(255),
    properties json,
    attributes json,
    primary key (id)
);
-- end::schema-mysql[]
