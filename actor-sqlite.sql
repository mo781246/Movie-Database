drop table if exists actor;
create table actor (
  id integer primary key not null,
  name varchar(255) unique not null,
  birthYear integer not null
);
