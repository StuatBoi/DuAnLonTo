package org.net.demo;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import java.lang.reflect.Type;

public class DetailController extends Controller{

    private String filmInfo= "{\"id\":\"MV-9999\",\"movieTitle\":\"Avatar: The Way of Water\",\"description\":\"Jake Sully lives with his newfound family formed on the extraterrestrial moon of Pandora.\",\"genre\":\"Sci-Fi, Action\",\"posterUrl\":\"https://i.imgur.com/vHdfM0V.jpg\",\"rating\":\"7.6\"}";
   

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
        parseInfo(getSingleMovieFromJson(filmInfo));
        
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

    public void parseInfo(MovieInfo movie)
    {
        lblMovieTitle.setText(movie.getMovieTitle());
        lblRating.setText(movie.getRating());
        lblGenre.setText(movie.getGenre());
        lblDescription.setText(movie.getDescription());
        setMoviePoster(movie.getPosterUrl());
        

    }

    public void PushID(String MovieID)
    {
       
    }

    public void setMoviePoster(String urlString) {
        try {
    // Đổi tham số thứ 2 thành 'false' để ép tải đồng bộ, nếu lỗi sẽ báo ngay lập tức
    Image image = new Image(urlString, false);
    
    if (image.isError()) {
        // Nếu có lỗi (như lỗi mạng, lỗi proxy học đường, hoặc chặn SSL), dòng này sẽ chỉ rõ lý do
        System.out.println("Lỗi JavaFX không tải được ảnh: " + image.getException().getMessage());
    } else {
        imgPoster.setImage(image);
        System.out.println("Tải ảnh thành công!");
    }
} catch (Exception e) {
    e.printStackTrace();
}
    }
    

}
