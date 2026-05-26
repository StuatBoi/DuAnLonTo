package org.net.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class HomeController extends Controller {

    @FXML
    private HBox MovieContainer;

    @Override
    public void OnShowing() {
        System.out.println("ON SHOWING RUNNING");
        System.out.println(MovieContainer);
        MovieService.loadMovies();
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


    @Override
    public void Refresh() {

    }

    @Override
    public void OnAttached() {

    }
}