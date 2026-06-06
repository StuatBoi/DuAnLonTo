package org.net.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;


public class HomeController extends Controller {

    @FXML
    private HBox MovieContainer;

    @Override
    public void OnShowing() {
        mainController.setActiveHomeButton(true); 
        System.out.println("ON SHOWING RUNNING");
        System.out.println(MovieContainer);   
        }


    @Override
    public void Refresh() {
      MovieService.loadMovies();
      loadMoviesCard();
    }

    @Override
    public void OnAttached() {
      MovieService.loadMovies();
      loadMoviesCard();
    }

     private void loadMoviesCard()
    {
        Platform.runLater(()->{
            MovieContainer.getChildren().clear();
      for(Movie movie : MovieService.movies) {
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieCard.fxml"));

                    Parent card = loader.load();

                    MovieCardController controller = loader.getController();

                    controller.setMovie(movie);

                    MovieContainer.getChildren().add(card);


                }
                catch (Exception e) {

                    System.out.println("LỖI MOVIE CARD:");
                    e.printStackTrace();
                }
            }
        }
        );
    }
}