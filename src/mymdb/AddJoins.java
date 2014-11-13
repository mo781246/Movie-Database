package mymdb;

import models.*;
import java.util.*;

public class AddJoins {

    public static void main(String[] args) {
        try {
            ORM.init(DBProps.getProps());

            System.out.println("using url: " + ORM.getUrl());

            Movie movie1 = (Movie) ORM.load(Movie.class, 1); // American History X
            Movie movie2 = (Movie) ORM.load(Movie.class, 2); // Fight Club
            Movie movie3 = (Movie) ORM.load(Movie.class, 3); // Forrest Gump
            Movie movie4 = (Movie) ORM.load(Movie.class, 4); // Ocean's Eleven
            Movie movie5 = (Movie) ORM.load(Movie.class, 5); // Saving Private Ryan
            Movie movie6 = (Movie) ORM.load(Movie.class, 6); // The Departed

            List values;

            values = Arrays.asList(new Object[]{"Tom Hanks"});
            Actor Hanks = (Actor) ORM.findOne(Actor.class, "name = ?", values);

            values = Arrays.asList(new Object[]{"Leonardo Dicaprio"});
            Actor DiCaprio = (Actor) ORM.findOne(Actor.class, "name = ?", values);

            values = Arrays.asList(new Object[]{"Matt Damon"});
            Actor Damon = (Actor) ORM.findOne(Actor.class, "name = ?", values);

            values = Arrays.asList(new Object[]{"Brad Pitt"});
            Actor Pitt = (Actor) ORM.findOne(Actor.class, "name = ?", values);

            values = Arrays.asList(new Object[]{"Edward Norton"});
            Actor Norton = (Actor) ORM.findOne(Actor.class, "name = ?", values);

            values = Arrays.asList(new Object[]{"George Clooney"});
            Actor Clooney = (Actor) ORM.findOne(Actor.class, "name = ?", values);

            boolean added;

            //Join Actors with their respective Movies
            added = ORM.addJoin(Hanks, movie3);
            if (added) {
                System.out.println(Hanks.getName() + "=>" + movie3.getTitle());
                ORM.save(movie3);
            }

            added = ORM.addJoin(Hanks, movie5);
            if (added) {
                System.out.println(Hanks.getName() + "=>" + movie5.getTitle());
                ORM.save(movie5);
            }

            added = ORM.addJoin(DiCaprio, movie6);
            if (added) {
                System.out.println(DiCaprio.getName() + "=>" + movie6.getTitle());
                ORM.save(movie6);
            }

            added = ORM.addJoin(Damon, movie4);
            if (added) {
                System.out.println(Damon.getName() + "=>" + movie4.getTitle());
                ORM.save(movie4);
            }

            added = ORM.addJoin(Damon, movie5);
            if (added) {
                System.out.println(Damon.getName() + "=>" + movie5.getTitle());
                ORM.save(movie5);
            }

            added = ORM.addJoin(Damon, movie6);
            if (added) {
                System.out.println(Damon.getName() + "=>" + movie6.getTitle());
                ORM.save(movie6);
            }

            added = ORM.addJoin(Pitt, movie2);
            if (added) {
                System.out.println(Pitt.getName() + "=>" + movie2.getTitle());
                ORM.save(movie2);
            }

            added = ORM.addJoin(Pitt, movie4);
            if (added) {
                System.out.println(Pitt.getName() + "=>" + movie4.getTitle());
                ORM.save(movie4);
            }

            added = ORM.addJoin(Norton, movie1);
            if (added) {
                System.out.println(Norton.getName() + "=>" + movie1.getTitle());
                ORM.save(movie1);
            }

            added = ORM.addJoin(Norton, movie2);
            if (added) {
                System.out.println(Norton.getName() + "=>" + movie2.getTitle());
                ORM.save(movie2);
            }

            added = ORM.addJoin(Clooney, movie4);
            if (added) {
                System.out.println(Clooney.getName() + "=>" + movie4.getTitle());
                ORM.save(movie4);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
