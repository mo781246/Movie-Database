PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;

DROP TABLE IF EXISTS actor_movie;
DROP TABLE IF EXISTS actor;
DROP TABLE IF EXISTS movie;

CREATE TABLE actor (
  id integer primary key not null,
  name varchar(255) unique not null,
  birthYear integer not null
);
INSERT INTO "actor" VALUES(1,'Al Pacino',1940);
INSERT INTO "actor" VALUES(2,'Marlon Brando',1924);
INSERT INTO "actor" VALUES(3,'Jack Nickolson',1937);
INSERT INTO "actor" VALUES(4,'Humphrey Bogart',1899);
INSERT INTO "actor" VALUES(5,'Lauren Bacall',1924);
INSERT INTO "actor" VALUES(6,'Robert DeNiro',1943);
INSERT INTO "actor" VALUES(7,'Robert Duvall',1931);

CREATE TABLE movie (
  id integer primary key not null,
  title varchar(255) unique not null,
  year integer not null,
  description text not null default ''
);
INSERT INTO "movie" VALUES(1,'The Godfather',1972,'Director: Francis Ford Coppola

The aging patriarch of an organized 
crime dynasty transfers control of his 
clandestine empire to his reluctant son.
');
INSERT INTO "movie" VALUES(2,'GoodFellas',1990,'');
INSERT INTO "movie" VALUES(3,'Analyze This',1999,'A comedy about a psychiatrist whose number 
one-patient is an insecure mob boss. ');
INSERT INTO "movie" VALUES(4,'One Flew Over the Cuckoo''s Nest',1975,'');
INSERT INTO "movie" VALUES(5,'The Godfather: Part II',1974,'Director: Francis Ford Coppola

The early life and career of Vito Corleone in 
1920s New York is portrayed while his son, Michael, 
expands and tightens his grip on his crime 
syndicate stretching from Lake Tahoe, Nevada 
to pre-revolution 1958 Cuba.
');
INSERT INTO "movie" VALUES(6,'Casablanca',1942,'');
INSERT INTO "movie" VALUES(7,'The Big Sleep',1946,'Director: Howard Hawks
');
INSERT INTO "movie" VALUES(8,'The Missouri Breaks',1976,'');
INSERT INTO "movie" VALUES(9,'Lonesome Dove',1989,'');

CREATE TABLE actor_movie (
  id integer primary key not null,
  actor_id integer key not null,
  movie_id integer key not null,
  unique(actor_id, movie_id)
);
INSERT INTO "actor_movie" VALUES(1,1,1);
INSERT INTO "actor_movie" VALUES(7,1,5);
INSERT INTO "actor_movie" VALUES(2,2,1);
INSERT INTO "actor_movie" VALUES(13,2,8);
INSERT INTO "actor_movie" VALUES(6,3,4);
INSERT INTO "actor_movie" VALUES(14,3,8);
INSERT INTO "actor_movie" VALUES(10,4,6);
INSERT INTO "actor_movie" VALUES(11,4,7);
INSERT INTO "actor_movie" VALUES(12,5,7);
INSERT INTO "actor_movie" VALUES(4,6,2);
INSERT INTO "actor_movie" VALUES(5,6,3);
INSERT INTO "actor_movie" VALUES(8,6,5);
INSERT INTO "actor_movie" VALUES(3,7,1);
INSERT INTO "actor_movie" VALUES(9,7,5);
INSERT INTO "actor_movie" VALUES(15,7,9);

COMMIT;
