package org.net.demo;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import java.lang.reflect.Type;

public class DetailController extends Controller{

    @FXML
    private Button btnBack;

    @FXML
    private Button btnBookTicket;

    @FXML
    private FlowPane flowShowtimes;

    @FXML
    private ImageView imgPoster;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblDuration;

    @FXML
    private Label lblGenre;

    @FXML
    private Label lblMovieTitle;

    @FXML
    private Label lblRating;



    @FXML
    private void initialize()
    {
        
        btnBack.setOnAction(event->
            {
                mainController.showPage(mainController.getLastPage());
            }
        );
    }

    @Override
    public void OnShowing() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void OnAttached() {
        // TODO Auto-generated method stub
        
    }

    public List<MovieInfo> getMovieListFromJson(String jsonString) {
        Gson gson = new Gson();
        
        
        Type movieListType = new TypeToken<List<MovieInfo>>(){}.getType();
        
        // Chuyển đổi JSON thành List
        List<MovieInfo> movieList = gson.fromJson(jsonString, movieListType);
        
        return movieList;
    }
    public MovieInfo getSingleMovieFromJson(String jsonString) {
        // Khởi tạo đối tượng Gson
        Gson gson = new Gson();
        
        // Chuyển đổi trực tiếp JSON thành class MovieInfo
        MovieInfo movie = gson.fromJson(jsonString, MovieInfo.class);
        
        return movie;
    }

    

}
