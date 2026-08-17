-- tag::schema-postgresql[]
drop table if exists portable_post;

create table portable_post (
    id bigint not null,
    title varchar(255),
    properties jsonb,
    attributes jsonb,
    primary key (id)
);
-- end::schema-postgresql[]
