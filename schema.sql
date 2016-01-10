drop table if exists url_hash_map;

create table url_hash_map(
  id                bigserial                            not null      primary key,
  original_url      text                            not null,
  hash              text
);
create index on url_hash_map(hash);
