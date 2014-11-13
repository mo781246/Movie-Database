package mymdb;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.*;
import views.*;

public class MyMDB {

    private final TheFrame frame = new TheFrame();

    // listModel objects
    private final DefaultListModel moviesModel = new DefaultListModel();
    private final DefaultListModel actorsModel = new DefaultListModel();

    // renderer objects
    private final MovieCellRenderer movieRenderer = new MovieCellRenderer();
    private final ActorCellRenderer actorRenderer = new ActorCellRenderer();

    // dialogs
    private final AddActorDialog addActorDialog = new AddActorDialog(frame, true);
    private final AddMovieDialog addMovieDialog
            = new AddMovieDialog(frame, true);
    private final EditDescriptionDialog editDescriptionDialog
            = new EditDescriptionDialog(frame, true);

    public MyMDB() throws Exception {

        ORM.init(DBProps.getProps());

        String subproto = ORM.getUrl().split(":")[1];
        frame.setTitle(getClass().getSimpleName() + " - " + subproto);
        frame.setSize(750, 500);

        frame.setMoviesModel(moviesModel);
        frame.setActorsModel(actorsModel);

        frame.setMovieRenderer(movieRenderer);
        frame.setActorRenderer(actorRenderer);

        // Loads movies into list and orders by title
        loadListModel(moviesModel,
                ORM.findAll(Movie.class, "1 order by title"));
        // Loads Actors into list and orders by name
        loadListModel(actorsModel, ORM.findAll(Actor.class, "1 order by name"));

        frame.addMovieOrderActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected item
                String selection = (String) frame.getMovieOrderSelectedItem();

                switch (selection) { // Checks for a selection match
                    case "Title":
                        try {
                            // Re-Order list by title
                            loadListModel(moviesModel, ORM.findAll(Movie.class, "1 order by title"));
                        } catch (Exception ex) {
                            Logger.getLogger(MyMDB.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "Year":
                        try {
                            // Re-Order list by year
                            loadListModel(moviesModel, ORM.findAll(Movie.class, "1 order by year"));
                        } catch (Exception ex) {
                            Logger.getLogger(MyMDB.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                }
            }
        });
        // Displays selected movie within its respective text area
        frame.addMoviesListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (evt.getValueIsAdjusting()) {
                    return;
                }
                Movie movie = frame.getSelectedMovie();
                if (movie == null) {
                    return;
                }
                try {
                    frame.setMovieText(getMovieInfo(movie));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });

        // Displays selected movie within its respective text area
        frame.addActorsListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (evt.getValueIsAdjusting()) {
                    return;
                }
                Actor actor = frame.getSelectedActor();
                if (actor == null) {
                    return;
                }
                try {
                    frame.setActorText(getActorInfo(actor));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });

        class MyException extends Exception {

            MyException(String message) {
                super(message);
            }
        }

// Uses a button actionlistener to join a selected Movie and Actor
        frame.addJoinActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Movie movie = frame.getSelectedMovie();
                Actor actor = frame.getSelectedActor();
                try {
                    if (movie == null && actor == null) {
                        throw new MyException("Please select an Actor and Movie");
                    }
                    if (movie == null) {
                        throw new MyException("Please select an Movie");
                    }
                    if (actor == null) {
                        throw new MyException("Please Select an Actor");
                    }
                    if (!ORM.addJoin(actor, movie)) {
                        throw new MyException("Actor is already stars in the movie");
                    }
                    ORM.save(movie);
                    frame.setMovieText(getMovieInfo(movie));
                    movieRenderer.actorMovies = ORM.getJoined(actor, Movie.class);
                    frame.repaint();
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });

        // Uses a button actionlistener to unjoin a selected Movie and Actor
        frame.addUnJoinActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Movie movie = frame.getSelectedMovie();
                Actor actor = frame.getSelectedActor();
                try {
                    if (movie == null && actor == null) {
                        throw new MyException("Please select an Actor and Movie");
                    }
                    if (movie == null) {
                        throw new MyException("Please select an Movie");
                    }
                    if (actor == null) {
                        throw new MyException("Please Select an Actor");
                    }
                    if (!ORM.removeJoin(actor, movie)) {
                        throw new MyException("Actor does not star in the Movie");
                    }
                    ORM.save(movie);
                    frame.setMovieText(getMovieInfo(movie));
                    movieRenderer.actorMovies = ORM.getJoined(actor, Movie.class);
                    frame.repaint();
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });

        // Clears all selected fields
        frame.addClearSelectionActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                frame.clearAllSelection();
            }
        });

        // Initialize addActorDialog
        frame.addAddActorActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                java.awt.Point pos = frame.getLocation();
                addActorDialog.setLocation(pos.x + 10, pos.y + 10);
                addActorDialog.setNameText("");
                addActorDialog.setBirthYearText("");
                addActorDialog.setVisible(true);
            }
        });

        // Removes a selected actor and reload list while maintaining order
        frame.addRemoveActorActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Actor actor = frame.getSelectedActor();
                try {
                    if (actor == null) {
                        throw new MyException("Please select an actor");
                    }
                    Set<Movie> movie = ORM.getJoined(actor, Movie.class);
                    if (!confirm("Are you sure you wish to remove this Actor?")) {
                        return;
                    }
                    ORM.remove(actor);
                    loadListModel(actorsModel, ORM.findAll(Actor.class));
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // Initialize addMovieDialog
        frame.addAddMovieActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                java.awt.Point pos = frame.getLocation();
                addMovieDialog.setLocation(pos.x + 10, pos.y + 10);
                addMovieDialog.setTitleText("");
                addMovieDialog.setYearText("");
                addMovieDialog.setVisible(true);
            }
        });

        // Remove a selected movie and reload list while maintaining order
        frame.addRemoveMovieActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Movie movie = frame.getSelectedMovie();
                try {
                    if (movie == null) {
                        throw new MyException("Please select a movie");
                    }
                    Set<Actor> actor = ORM.getJoined(movie, Actor.class);
                    if (!confirm("Are you sure you wish to remove this Movie?")) {
                        return;
                    }
                    ORM.remove(movie);
                    loadListModel(moviesModel, ORM.findAll(Movie.class));
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // Initialize and make editDescriptionDialog visible
        frame.addEditMovieDescriptionActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Movie movie = frame.getSelectedMovie();
                if (movie == null) {
                    JOptionPane.showMessageDialog(frame, "Please select a movie to edit");
                    return;
                }
                java.awt.Point pos = frame.getLocation();
                editDescriptionDialog.setLocation(pos.x + 10, pos.y + 10);
                editDescriptionDialog.setTitleText(movie.getTitle());
                editDescriptionDialog.setYearText("" + movie.getYear());
                editDescriptionDialog.setVisible(true);
            }
        });

        // Verifies user dialog input and adds new Actor object to list
        addActorDialog.addAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String name = addActorDialog.getNameText().trim();
                String birthYearText = addActorDialog.getBirthYearText().trim();
                try {
                    if (name.length() < 3) {
                        throw new MyException("The actor's name must contain at least 3"
                                + " characters");
                    }
                    int year = Integer.parseInt(birthYearText);
                    if (year < 1800 || year > 2014) {
                        throw new MyException("All actor's birth years must be between "
                                + "1800-2014");
                    }
                    Actor actor = new Actor(name, year);
                    ORM.save(actor);
                    loadListModel(actorsModel, ORM.findAll(Actor.class, "1 order by name"));
                    frame.setActorText(""); // clear actor text area since no actor will be selected
                    addActorDialog.setVisible(false);
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "bad birthYear");
                } catch (java.sql.SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "duplicate name");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // Closes actor dialog box if the user hits cancel
        addActorDialog.addCancelActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addActorDialog.setVisible(false);
            }
        });

        // Verifies user dialog input and adds new Movie object to list
        addMovieDialog.addAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String title = addMovieDialog.getTitleText().trim();
                String yearText = addMovieDialog.getYearText().trim();
                try {
                    if (title.length() < 3) {
                        throw new MyException("The movie's title must contain at least 3"
                                + " characters");
                    }
                    int year = Integer.parseInt(yearText);
                    if (year < 1900 || year > 2014) {
                        throw new MyException("All movie years must be between "
                                + "1900-2014");
                    }
                    String description = "";
                    Movie movie = new Movie(title, year, description);
                    ORM.save(movie);
                    loadListModel(moviesModel, ORM.findAll(Movie.class, "1 order by title"));
                    frame.setMovieText(""); // clears movie text area
                    addMovieDialog.setVisible(false);
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "bad year");
                } catch (java.sql.SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "duplicate title");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // Closes actor dialog box if the user hits cancel
        addMovieDialog.addCancelActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addMovieDialog.setVisible(false);
            }
        });

        // Verifies description input and adds it to the selected movie
        editDescriptionDialog.addChangeActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String description = editDescriptionDialog.getDescriptionText().trim();
                try {

                    if ("".equals(description)) {
                        throw new MyException("Please enter a description");
                    }
                    Movie movie = frame.getSelectedMovie();
                    movie.setDescription(description);
                    if (!confirm("Are you sure you wish to edit this Movie?")) {
                        return;
                    }
                    ORM.save(movie);
                    frame.setMovieText(getMovieInfo(movie));
                    editDescriptionDialog.setVisible(false);
                } catch (MyException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "bad description");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // Closes actor dialog box if the user hits cancel
        editDescriptionDialog.addCancelActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                editDescriptionDialog.setVisible(false);
            }
        });

    }  // end LibraryMVC constructor

    private boolean confirm(Object message) {
        int response = JOptionPane.showOptionDialog(
                frame, message, null, JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null,
                new String[]{"yes", "no"}, "no");
        return response == JOptionPane.YES_OPTION;
    }

    private String getMovieInfo(Movie movie) throws Exception {
        Set<Actor> actors = ORM.getJoined(movie, Actor.class);
        Object[] actornames = actors.toArray();
        for (int i = 0; i < actornames.length; ++i) {
            actornames[i] = ((Actor) actornames[i]).getName();
        }
        String info = "";
        info += "Title: " + movie.getTitle() + "\n";
        info += "Description: " + movie.getDescription() + "\n";
        info += "Year Released: " + movie.getYear() + "\n";
        info += "Actors: " + Arrays.toString(actornames) + "\n";
        return info;
    }

    private String getActorInfo(Actor actor) throws Exception {
        Set<Movie> movies = ORM.getJoined(actor, Movie.class);
        Object[] movienames = movies.toArray();
        for (int i = 0; i < movienames.length; ++i) {
            movienames[i] = ((Movie) movienames[i]).getTitle();
        }
        String info = "";
        info += "Name: " + actor.getName() + "\n";
        info += "Date of Birth: " + actor.getbirthYear() + "\n";
        info += "Movies Appeared In: " + Arrays.toString(movienames) + "\n";
        return info;
    }

    // loadListModel helper function
    private <E> void loadListModel(
            DefaultListModel model, Collection<E> coll) {
        model.clear();
        for (E elt : coll) {
            model.addElement(elt);
        }
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            MyMDB app = new MyMDB();
            app.frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
