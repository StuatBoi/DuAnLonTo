package org.net.demo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.net.demo.Service.LoadingOverlayManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.lang.reflect.Type;

public class DetailController extends BaseController{

    private String CurrentMovieID=null;

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
    private Label lblReleaseDate;
    
    private final ToggleGroup showtimeGroup=new ToggleGroup();

    



    @FXML
    private void initialize()
    {
        
       
       
       

    }

    @Override
    public void OnShowing() {
        GetShowTimes(CurrentMovieID);
        
    }

    @Override
    public void Refresh() {
    
        PushID(Long.parseLong(CurrentMovieID));
        GetShowTimes(CurrentMovieID);
    }

    @Override
    public void OnAttached() {
        
         btnBack.setOnAction(event->
            {
                mainController.showPage(mainController.getLastPage());
            }
        );
        btnBookTicket.setOnAction(event->
            {
                
                ToggleButton button =(ToggleButton)showtimeGroup.getSelectedToggle();
                if(button==null)
                {
                    CineverseAlert.showToast("Không có suất chiếu nào được chọn", btnBookTicket);
                    return;
                }
                if(!mainController.IsLoggedIn())
                {
                    CineverseAlert.showToast("Vui lòng đăng nhập để đặt vé", btnBookTicket);
                    return;
                }
                ShowTime showTime= (ShowTime)button.getUserData();
                SeatViewController seatViewController= (SeatViewController)mainController.getController("seatView");
                seatViewController.setShowTimeID(showTime.getId());
                mainController.showPage(mainController.getPage("seatView"));
            }
        );
        
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
        lblMovieTitle.setText(movie.getTitle());
        lblRating.setText(movie.getRating());
        lblGenre.setText(movie.getGenre());
        lblDescription.setText(movie.getDescription());
        setMoviePoster(movie.getImage());
        lblReleaseDate.setText(movie.getReleaseDate());
        

    }

    public void PushID(Long MovieID)
    {
       if(CurrentMovieID==null ||!CurrentMovieID.equals(MovieID.toString())) 
       {
        CurrentMovieID=MovieID.toString();
        LoadingOverlayManager.start(btnBack);
       Map<String,Object> param= Map.of("movieID",CurrentMovieID); 
       HTTPService.sendFullRequestAsync("GET", "/api/feature/getMovieDetail", param, null, null).thenAccept(
        response->{
            if(response.statusCode()==200)
            {
            Platform.runLater(()->parseInfo(getSingleMovieFromJson(response.body())));
            LoadingOverlayManager.stop();
            }
            else{
            
            LoadingOverlayManager.stop();
            Platform.runLater(()->CineverseAlert.showToast("Không thể lấy thông tin phim từ server", btnBack));
            }
        }
       ).orTimeout(10,TimeUnit.SECONDS)
     .exceptionallyAsync(ex->
        { 
            if (ex.getCause() instanceof TimeoutException) {
            System.err.println("Lỗi: Server không phản hồi trong vòng 10 giây!");
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (Timeout)",btnBack));
        } else {
            System.err.println("Lỗi hệ thống khác: " + ex.getMessage());
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (No Connection)", btnBack));
        }
        LoadingOverlayManager.stop();
        return null;
    }
     );
       
    }
    else
       {
        LoadingOverlayManager.stop();
        if(CurrentMovieID==null)
        CineverseAlert.showToast("mã số phim không hợp lệ", btnBack);
       }
    }
    
    public void GetShowTimes(String movieID)
    {
        LoadingOverlayManager.start(btnBack);
        Map<String,Object> param=Map.of("movieID",movieID);
        HTTPService.sendFullRequestAsync("GET", "/api/feature/getShowTimes",param, null, mainController.getToken()).thenAccept(response->
            {
                if(response.statusCode()==200)
                {
                Platform.runLater(()->
                {renderShowtimesFromJSON(response.body());
                    LoadingOverlayManager.stop();
                });
                }
                else{
                    LoadingOverlayManager.stop();
                    CineverseAlert.showToast("Không thể lấy thông tin xuất chiểu", btnBack);
                }
            }
        ).orTimeout(10, TimeUnit.SECONDS)
        .exceptionallyAsync(ex->
        { 
            if (ex.getCause() instanceof TimeoutException) {
            System.err.println("Lỗi: Server không phản hồi trong vòng 10 giây!");
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (Timeout)",btnBack));
        } else {
            System.err.println("Lỗi hệ thống khác: " + ex.getMessage());
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (No Connection)", btnBack));
        }
        LoadingOverlayManager.stop();
        return null;
    }
     );
    }

    public void setMoviePoster(String urlString) {
        try {
    // Đổi tham số thứ 2 thành 'false' để ép tải đồng bộ, nếu lỗi sẽ báo ngay lập tức
    Image image = new Image(urlString, true);
    
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
    
    public void renderShowtimesFromJSON(String jsonString) {
        // 1. Xóa toàn bộ các nút cũ (nếu có) trong FlowPane trước khi nạp mới
        flowShowtimes.getChildren().clear();

        // 2. Sử dụng Gson để parse chuỗi JSON thành List<ShowTime>
        Gson gson = new Gson();
        // Định nghĩa kiểu dữ liệu đại diện cho List<ShowTime> để Gson hiểu
        Type showTimeListType = new TypeToken<List<ShowTime>>(){}.getType();
        List<ShowTime> showTimeList = gson.fromJson(jsonString, showTimeListType);

        if (showTimeList == null || showTimeList.isEmpty()) {
            System.out.println("Không có suất chiếu nào hoặc chuỗi JSON trống.");
            return;
        }

        // 3. Vòng lặp duyệt qua danh sách để tạo và gắn nút vào giao diện
        for (ShowTime showTime : showTimeList) {
            try {
                // Tải file thành phần ShowtimeButton.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowtimeBtn.fxml"));
                ToggleButton btnShowtime = loader.load();

                btnShowtime.setText(showTime.getStartTime()+" , "+showTime.getAddress()); 

                // Gom nút vào nhóm chung để xử lý logic: "chọn 1 nút, tự bỏ chọn nút kia"
                btnShowtime.setToggleGroup(showtimeGroup);

                // Lưu trữ đối tượng showTime vào thuộc tính UserData của nút để khi click dễ dàng lấy ra xử lý tiếp
                btnShowtime.setUserData(showTime);

                // Bắt sự kiện khi người dùng click vào suất chiếu này
                btnShowtime.setOnAction(event -> {
                    if (btnShowtime.isSelected()) {
                        
                        ShowTime selectedShowTime = (ShowTime) btnShowtime.getUserData();
                        System.out.println("Đang chọn suất tại rạp: " + selectedShowTime.getCinemaName());
                        System.out.println("Giờ chiếu: " + selectedShowTime.getStartTime());
                    }
                });

                // Thêm nút vừa tạo hoàn chỉnh vào FlowPane
                flowShowtimes.getChildren().add(btnShowtime);

            } catch (IOException e) {
                System.err.println("Lỗi khi load file ShowtimeButton.fxml: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnLogin() {
        
    }

    @Override
    public void OnLogout() {
        
    }

    @Override
    public void OnExit() {
        
        showtimeGroup.selectToggle(null);
    }

}
