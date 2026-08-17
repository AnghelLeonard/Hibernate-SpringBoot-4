-- tag::schema-oracle[]
-- Oracle has no "drop table if exists", so ignore the error when it is absent.
begin execute immediate 'drop table portable_post'; exception when others then null; end;
/
-- On Oracle 21c and newer, JsonType binds JSON through the native "json" column
-- type; below 21c it falls back to text, where a "clob" column would be used.
create table portable_post (
    id number(19,0) not null,
    title varchar2(255 char),
    properties json,
    attributes json,
    primary key (id)
)
/
-- end::schema-oracle[]
