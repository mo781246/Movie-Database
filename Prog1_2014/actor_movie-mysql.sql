drop table if exists actor_movie;
create table actor_movie (
  id integer auto_increment primary key not null,
  movie_id integer not null,
  actor_id integer not null,
  unique(movie_id, actor_id),
  key(movie_id,actor_id)
);
