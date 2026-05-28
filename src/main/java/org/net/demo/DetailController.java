package org.net.demo;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.lang.reflect.Type;

public class DetailController extends Controller{

    private String filmInfo= "{\"id\":\"MV-9999\",\"movieTitle\":\"Avatar: The Way of Water\",\"description\":\"Jake Sully lives with his newfound family formed on the extraterrestrial moon of Pandora.\",\"genre\":\"Sci-Fi, Action\",\"image\":\"https://image.tmdb.org/t/p/w1280/luCqqHf10eQuGAYFO3U7JEqT4XC.jpg\",\"rating\":\"7.6\",\"releaseDate\":\"2024\"}";
   
    private String ShowTimeInfo = """
[
  {
    "id": "ROOM_01",
    "address": "123 Đường Nguyễn Trãi, Quận 1",
    "cinemaName": "CGV Cinema Hùng Vương",
    "startTime": "09:30 AM"
  },
  {
    "id": "ROOM_02",
    "address": "123 Đường Nguyễn Trãi, Quận 1",
    "cinemaName": "CGV Cinema Hùng Vương",
    "startTime": "13:15 PM"
  },
  {
    "id": "ROOM_01",
    "address": "123 Đường Nguyễn Trãi, Quận 1",
    "cinemaName": "CGV Cinema Hùng Vương",
    "startTime": "16:00 PM"
  },
  {
    "id": "ROOM_03",
    "address": "123 Đường Nguyễn Trãi, Quận 1",
    "cinemaName": "CGV Cinema Hùng Vương",
    "startTime": "19:45 PM"
  }
]
""";

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
        // TODO Auto-generated method stub
        parseInfo(getSingleMovieFromJson(filmInfo));
        renderShowtimesFromJSON(ShowTimeInfo);
        
    }

    @Override
    public void Refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void OnAttached() {
        // TODO Auto-generated method stub
        Parent lastPage= mainController.getPage("homeView");
         btnBack.setOnAction(event->
            {
                mainController.showPage(lastPage);
            }
        );
        btnBookTicket.setOnAction(event->
            {
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

    public void PushID(String MovieID)
    {
       
    }

    public void GetShowTimes(String MoveID)
    {

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

}
