package org.net.demo;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.net.demo.Service.LoadingOverlayManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.lang.reflect.Type;


public class HomeController extends BaseController {


    @FXML
    private Label navAll;
    @FXML
    private Button btnBookTicket;
    @FXML
    private HBox MovieContainer;

    @FXML
    private VBox homeView;

    @FXML
    private ImageView moviePoster;

    @FXML
    private StackPane recommendedMovie;

    @FXML
    private Label recommendedMovieTitle;


    public ArrayList<Movie> movies = new ArrayList<>();
    private String endpoint="/api/feature/getMoviesForHomepage";
    

    @Override
    public void OnShowing() {
        mainController.setActiveHomeButton(true); 
         
        }


    @Override
    public void Refresh() {
      
      loadMoviesCard();
      
    }

    @Override
    public void OnAttached() {
      loadMoviesCard();
      navAll.setOnMouseClicked(e->
        {
            mainController.getSearchField().clear();
            mainController.showPage(mainController.getPage("searchView"));
        }
      );
      
    }

     private void loadMoviesCard()
    {
        LoadingOverlayManager.start(MovieContainer);
        Map<String,Object> param= Map.of("page",0,"size",7);
    HTTPService.sendFullRequestAsync("GET", endpoint,param, null,null).thenAcceptAsync(response -> {
        if(response.statusCode()==200)
        {
        Gson gson= new Gson();
        Type pageResponseType = new TypeToken<PageResponse<Movie>>(){}.getType();
        System.out.println(response.body());
        PageResponse<Movie> mPageResponse= gson.fromJson(response.body(), pageResponseType);
            movies.clear();
            movies.addAll(mPageResponse.getContent());
        }
        else{
            LoadingOverlayManager.stop();
            Platform.runLater(()->
        {
            CineverseAlert.showToast("Không thể lấy dữ liệu thẻ phim", MovieContainer);
        });
        }
           

    })
    .orTimeout(10,TimeUnit.SECONDS)
     .exceptionallyAsync(ex->
        { 
            if (ex.getCause() instanceof TimeoutException) {
            System.err.println("Lỗi: Server không phản hồi trong vòng 10 giây!");
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (Timeout)", btnBookTicket));
        } else {
            System.err.println("Lỗi hệ thống khác: " + ex.getMessage());
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (No Connection)", btnBookTicket));
        }
        LoadingOverlayManager.stop();
        return null;
    }
     );
        
        Platform.runLater(()->{
            MovieContainer.getChildren().clear();
            boolean featured=false;
            Parent DetailPage= mainController.getPage("detailView");
            DetailController detailController=(DetailController)mainController.getController("detailView");
      for(Movie movie : movies) {
                try {
                    if(!featured)
                    {
                        moviePoster.setImage(new Image(movie.getImage()));
                        featured=true;
                        btnBookTicket.setOnAction(event->{
                    detailController.PushID(movie.getId());
                    mainController.showPage(DetailPage);
                   });
                    }

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieCard.fxml"));

                    Parent card = loader.load();

                   

                    MovieCardController controller = loader.getController();

                    controller.setMovie(movie);
                   card.setOnMouseClicked(event->{
                    detailController.PushID(movie.getId());
                    mainController.showPage(DetailPage);
                   });


                    MovieContainer.getChildren().add(card);


                }
                catch (Exception e) {

                    System.out.println("LỖI MOVIE CARD:");
                    e.printStackTrace();
                    LoadingOverlayManager.stop();

                }
            }
            LoadingOverlayManager.stop();
        }
        
        );
    }


     @Override
     public void OnLogin() {
        
     }


     @Override
     public void OnLogout() {
      
     }


     @Override
     public void OnExit() {
        
     }

    
}