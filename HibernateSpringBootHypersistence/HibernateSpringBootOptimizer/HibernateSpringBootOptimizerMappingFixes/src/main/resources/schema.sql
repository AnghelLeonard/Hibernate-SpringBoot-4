-- Hibernate no longer generates the schema (ddl-auto=none), so it lives here.
-- In a real application this would be a Flyway or Liquibase migration; the point
-- of the event is that the schema is versioned alongside the code, whoever runs it.

create sequence if not exists fixed_post_seq start with 1 increment by 50;
create sequence if not exists fixed_post_comment_seq start with 1 increment by 50;
create sequence if not exists fixed_tag_seq start with 1 increment by 50;

create table if not exists fixed_post (
    id bigint not null,
    title varchar(255),
    primary key (id)
);

-- No surrogate key: the details share the post identifier (@MapsId), so the
-- primary key IS the foreign key.
create table if not exists fixed_post_details (
    id bigint not null,
    created_by varchar(255),
    created_on timestamp(6),
    primary key (id),
    constraint fk_post_details_post foreign key (id) references fixed_post
);

create table if not exists fixed_post_comment (
    id bigint not null,
    post_id bigint,
    review varchar(255),
    primary key (id),
    constraint fk_post_comment_post foreign key (post_id) references fixed_post
);

create table if not exists fixed_tag (
    id bigint not null,
    name varchar(255),
    primary key (id)
);

create table if not exists fixed_post_tag (
    post_id bigint not null,
    tag_id bigint not null,
    primary key (post_id, tag_id),
    constraint fk_post_tag_post foreign key (post_id) references fixed_post,
    constraint fk_post_tag_tag foreign key (tag_id) references fixed_tag
);
