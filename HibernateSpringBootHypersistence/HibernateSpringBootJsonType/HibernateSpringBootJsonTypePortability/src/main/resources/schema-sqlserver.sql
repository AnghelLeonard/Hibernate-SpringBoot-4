-- tag::schema-sqlserver[]
drop table if exists portable_post;

create table portable_post (
    id bigint not null,
    title varchar(255),
    properties nvarchar(max),
    attributes nvarchar(max),
    primary key (id)
);
-- end::schema-sqlserver[]
