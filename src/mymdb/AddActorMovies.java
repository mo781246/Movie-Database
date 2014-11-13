package mymdb;

import models.*;

public class AddActorMovies {

    public static void main(String[] args) {
        try {
            ORM.init(DBProps.getProps());

            System.out.println("using url: " + ORM.getUrl());

            Movie[] movies = new Movie[]{
                new Movie("American History X", 1998, ""
                + "A former neo-nazi skinhead tries to prevent his younger "
                + "brother from going down the same wrong path that he did."),
                new Movie("Fight Club", 1999, "An insomniac office worker looking for "
                + "a way to change his life crosses paths with a devil-may-care "
                + "soap maker and they form an underground fight club that "
                + "evolves into something much, much more..."),
                new Movie("Forrest Gump", 1994, "Forrest Gump, while not intelligent, "
                + "has accidentally been present at many historic moments, but "
                + "his true love, Jenny Curran, eludes him."),
                new Movie("Ocean's Eleven", 2001, "Danny Ocean and his eleven accompl"
                + "ices plan to rob three Las Vegas casinos simultaneously."),
                new Movie("Saving Private Ryan", 1998, "Following the Normandy Landings,"
                + " a group of U.S. soldiers go behind enemy lines to retrieve a "
                + "paratrooper whose brothers have been killed in action."),
                new Movie("The Departed", 2006, "An undercover state cop who has "
                + "infiltrated an Irish gang and a mole in the police force "
                + "working for the same mob race to track down and identify each "
                + "other before being exposed to the enemy, after both sides "
                + "realize their outfit has a rat."),};

            Actor Hanks, DiCaprio, Damon, Pitt, Norton, Clooney;

            Actor[] actors = new Actor[]{
                Hanks = new Actor("Tom Hanks", 1953),
                DiCaprio = new Actor("Leonardo DiCaprio", 1974),
                Damon = new Actor("Matt Damon", 1970),
                Pitt = new Actor("Brad Pitt", 1963),
                Norton = new Actor("Edward Norton", 1969),
                Clooney = new Actor("George Clooney", 1961),};

            System.out.println("\n---> movies");
            for (Movie movie : movies) {
                ORM.save(movie);
                System.out.println(movie);
            }

            System.out.println("\n---> actors");
            for (Actor actor : actors) {
                ORM.save(actor);
                System.out.println(actor);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
