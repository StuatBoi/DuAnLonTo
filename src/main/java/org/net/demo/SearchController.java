package org.net.demo;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SearchController extends Controller{

    private String currentKey="";
    private int currentPage=0;
    private int lastPage;

   @FXML
    private Button btnFirstPage;

    @FXML
    private Button btnLastPage;

    @FXML
    private Button btnNextPage;

    @FXML
    private Button btnPrevPage;

    @FXML
    private Label lblPageIndicator;

    @FXML
    private Label lblSearchStatus;

    @FXML
    private FlowPane movieGridContainer;

    @FXML
    private TextField txtJumpPage;

    @FXML
    void handleFirstPage(ActionEvent event) {
        clearPage();
        GotoPage(currentKey, 0);
    }

    @FXML
void handleJumpPage(ActionEvent event) {
    String value = txtJumpPage.getText().trim();
    if (value.isEmpty()) {
        CineverseAlert.showToast("Vui lòng nhập số trang cần đến!", txtJumpPage);
        return;
    }
    
    try {
        int targetPage = Integer.parseInt(value)-1;
        if (targetPage < 0 || targetPage > lastPage) {
            CineverseAlert.showToast("Số trang hợp lệ là từ 1 đến " + (lastPage+1), txtJumpPage);
            return;
        }
    
        currentPage = targetPage;
        clearPage();
        
        GotoPage(currentKey, targetPage);
        
    } catch (NumberFormatException e) {
        CineverseAlert.showToast("Số trang nhập vào phải là ký tự số!", txtJumpPage);
    }
}

    @FXML
    void handleLastPage(ActionEvent event) {
        clearPage();
        GotoPage(currentKey, lastPage);
    }

    @FXML
    void handleNextPage(ActionEvent event) {
       if(currentPage+1<=lastPage)
       {
        clearPage();
           GotoPage(currentKey,currentPage+1);
       }
       else 
       {
        CineverseAlert.showToast("Bạn đã đến trang cuối", btnFirstPage);
       }
    }

    @FXML
    void handlePrevPage(ActionEvent event) {
       if(currentPage-1>=0)
       {
        clearPage();
        GotoPage(currentKey, currentPage-1);
       }
       else{
        CineverseAlert.showToast("Bạn đang ở trang đầu", btnFirstPage);
       }
    }
    @Override
    public void OnShowing() {
        clearPage();
        GotoPage(mainController.getSearchField().getText(),0);
    }

    @Override
    public void Refresh() {
        
    }

    @Override
    public void OnAttached() {
        VBox searchView= (VBox)mainController.getPage("searchView");


        searchView.setMaxHeight(Double.MAX_VALUE);
        searchView.setMaxWidth(Double.MAX_VALUE);

    }

    @Override
    public void OnLogin() {
        
    }

    @Override
    public void OnLogout() {
        
    }

    public void Search(String key)
    {
        currentKey=key;
        Map<String,Object> params=Map.of("page",0,"key",key);
        HTTPService.sendFullRequestAsync("GET", "/api/feature/getSearchResultWithPage", params, null, null).thenAccept(
            response->
            {
                if(response.statusCode()!=200)
                {
                    CineverseAlert.showToast("Có lỗi trong quá trình lấy dữ liệu", btnFirstPage);
                }
                Type pageResponseType = new TypeToken<PageResponse<Movie>>(){}.getType();
                Gson gson= new Gson();
                PageResponse<Movie> mPageResponse= gson.fromJson(response.body(), pageResponseType);
                loadMoviesCard(mPageResponse.getContent());
                setPageNavData(0, mPageResponse.getTotalPages());
            }
        );
    }

    private void loadMoviesCard(List<Movie> movies)
    {
        Platform.runLater(()->{
            movieGridContainer.getChildren().clear();
            Parent DetailPage= mainController.getPage("detailView");
            DetailController detailController=(DetailController)mainController.getController("detailView");
      for(Movie movie : movies) {
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MovieCard.fxml"));

                    Parent card = loader.load();

                   

                    MovieCardController controller = loader.getController();

                    controller.setMovie(movie);
                   card.setOnMouseClicked(event->{
                    detailController.PushID(movie.getId());
                    mainController.showPage(DetailPage);
                   });

                    movieGridContainer.getChildren().add(card);


                }
                catch (Exception e) {

                    System.out.println("LỖI MOVIE CARD:");
                    e.printStackTrace();
                }
            }
        }
        );
    }

    public void GotoPage(String key,Integer page)
    {
        Map<String,Object> params=Map.of("page",page,"key",key);
        HTTPService.sendFullRequestAsync("GET", "/api/feature/getSearchResultWithPage", params, null, null).thenAccept(
            response->
            {
                if(response.statusCode()!=200)
                {
                    CineverseAlert.showToast("Có lỗi trong quá trình lấy dữ liệu", btnFirstPage);
                }
                Type pageResponseType = new TypeToken<PageResponse<Movie>>(){}.getType();
                Gson gson= new Gson();
                PageResponse<Movie> mPageResponse= gson.fromJson(response.body(), pageResponseType);
                loadMoviesCard(mPageResponse.getContent());
                setPageNavData(page, mPageResponse.getTotalPages());
                
            }
        );
    }
    public void setPageNavData(int currentPage,int lastPage)
    {
        int realcurrentPage=currentPage+1;
        Platform.runLater(()->lblPageIndicator.setText("Trang "+ realcurrentPage+ "/"+lastPage));
      this.currentPage=currentPage;
      this.lastPage=lastPage-1;
    }
    public void setJumpPageField(String value)
    {
        Platform.runLater(()->
        txtJumpPage.setText(value));
    }

    public void clearPage()
    {
        movieGridContainer.getChildren().clear();
    }
    


}
