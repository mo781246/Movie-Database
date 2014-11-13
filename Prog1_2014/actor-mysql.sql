drop table if exists actor;
create table actor (
  id integer auto_increment primary key not null,
  name varchar(255) unique not null,
  birthYear integer not null
);
