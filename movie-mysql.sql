drop table if exists movie;
create table movie (
  id integer auto_increment primary key not null,
  title varchar(255) unique not null,
  year integer not null,
  description text not null
);
