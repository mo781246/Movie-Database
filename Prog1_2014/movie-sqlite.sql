drop table if exists movie;
create table movie (
  id integer primary key not null,
  title varchar(255) unique not null,
  year integer not null,
  description text not null default ''
);
