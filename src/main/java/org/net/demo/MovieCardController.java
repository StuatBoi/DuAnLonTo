package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MovieCardController {

    @FXML
    private ImageView movieImage;

    @FXML
    private Label movieTitle;

    @FXML
    private Label movieGenre;

    public void setMovie(Movie movie) {

        movieTitle.setText(movie.getTitle());

        movieGenre.setText(movie.getGenre() + " • " + movie.getReleaseDate());

        movieImage.setImage(new Image(movie.getImage())
        );
    }
}