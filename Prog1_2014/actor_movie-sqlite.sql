drop table if exists actor_movie;
create table actor_movie (
  id integer primary key not null,
  actor_id integer key not null,
  movie_id integer key not null,
  unique(actor_id, movie_id)
);
