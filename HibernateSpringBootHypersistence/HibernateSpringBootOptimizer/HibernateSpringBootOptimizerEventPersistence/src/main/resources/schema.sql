-- Hibernate no longer generates the schema (ddl-auto=none), so the application
-- tables live here. The three ho_* event tables are created by the Optimizer
-- itself, through the Event Persistence schema action, not by this script.

create sequence if not exists forum_post_seq start with 1 increment by 50;
create sequence if not exists forum_post_comment_seq start with 1 increment by 50;

create table if not exists forum_post (
    id bigint not null,
    title varchar(255),
    primary key (id)
);

create table if not exists forum_post_comment (
    id bigint not null,
    post_id bigint,
    review varchar(255),
    primary key (id),
    constraint fk_post_comment_post foreign key (post_id) references forum_post
);
