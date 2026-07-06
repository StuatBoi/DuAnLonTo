package org.net.demo;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import org.net.demo.DTO.AccountDTO;
import org.net.demo.DTO.OrderDTO;
import org.net.demo.Service.LoadingOverlayManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class AccountController extends BaseController{
    @FXML
    private Button btnTicketView;
    @FXML
    private VBox accountView;

    @FXML
    private Button btnLogout;

    @FXML
    private TableView<OrderDTO> ticketHistoryTable;

    @FXML
    private TableColumn<OrderDTO, String> colDate; // Hoặc LocalDateTime tùy thuộc vào cách bạn hiển thị

    @FXML
    private TableColumn<OrderDTO, String> colMovie;

    @FXML
     private TableColumn<OrderDTO, Double> colPrice; // Giả định giá tiền là Double/BigDecimal

    @FXML
    private TableColumn<OrderDTO, String> colSeat;

    @FXML
    private Label lblUsername;
    @FXML
    private Label lblEmail;

    @FXML
    private Button btnPrevPage;

    @FXML
    private Button btnNextPage;

    @FXML
    private Label lblPageInfo;

    private int currentPage = 0;
    private int totalPages = 1;
    private String username;
    public String getUsername() 
    {
        return username;
    }

  

    @Override
    public void OnShowing() {
        mainController.setActiveAccountButton(true);
        gotoHistoryPage(currentPage);
        
        
    }

    @Override
    public void Refresh() {
       gotoHistoryPage(currentPage);
       loadAccountInfo();
    }

    @Override
    public void OnAttached() {
       btnTicketView.setOnAction(e->{
        mainController.showPage(mainController.getPage("ticketView"));});

        btnPrevPage.setOnAction(e -> {
        if (currentPage > 0) {
            gotoHistoryPage(currentPage - 1);
        }
        else{
            Platform.runLater(()->
             CineverseAlert.showToast("Đã ở trang đầu", accountView));
        }
        
    });
    btnLogout.setOnAction(e->{
        mainController.logOut();
    });

    // Sự kiện khi bấm nút Trang sau
    btnNextPage.setOnAction(e -> {
        if (currentPage < totalPages - 1) {
            gotoHistoryPage(currentPage + 1);
        }
        else
        {
            Platform.runLater(()->
             CineverseAlert.showToast("Đã ở trang cuối", accountView));
        }
    });
    }

    @Override
    public void OnLogin() {
       loadAccountInfo();
    }

    @Override
    public void OnLogout() {
        
    }

    public void loadAccountInfo()
    {if(mainController.IsLoggedIn())
        LoadingOverlayManager.start(accountView);
        HTTPService.sendRequestAsync("GET", "/api/account/getInfo", null, null, mainController.getToken()).thenAccept(response->{
            Gson gson=new Gson();
            AccountDTO accountInfo=gson.fromJson(response, AccountDTO.class);
            Platform.runLater(()->{
                if(accountInfo==null)
                {
                    System.err.println("Failed to parse account info from response: "+response);
                    LoadingOverlayManager.stop();
                    return;
                }
                updateInfo(accountInfo);
                LoadingOverlayManager.stop();
            });
        }).exceptionally(ex->{
            LoadingOverlayManager.stop();
            Platform.runLater(()->
            CineverseAlert.showToast("Không thể lấy dữ liệu tài khoản", accountView));
            return null;
        });
    }
    public void updateInfo(AccountDTO accountInfo)
    {
        username=accountInfo.getUsername();
      lblUsername.setText(accountInfo.getUsername());
      lblEmail.setText(accountInfo.getEmail());
    }

    public void gotoHistoryPage(int page)
    {
     LoadingOverlayManager.start(accountView);
     ticketHistoryTable.getItems().clear();
     Map<String,Object> params= Map.of("page",page);
     HTTPService.sendFullRequestAsync("GET", "/api/account/getOrderHistory",params, null, mainController.getToken()).thenAcceptAsync(
        response->
        {
            if(response.statusCode()==200)
            {
            Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();
            Type pageResponseType = new TypeToken<PageResponse<OrderDTO>>(){}.getType();
            PageResponse<OrderDTO> result= gson.fromJson(response.body(), pageResponseType);
            List<OrderDTO> contents=result.getContent();
            Platform.runLater(()->
            {
            populateTable(contents);
            setPageNavData(result.getNumber(), result.getTotalPages());
            });
            }
            else
            {
                Platform.runLater(()->CineverseAlert.showToast("Không thể lấy thông tin giao dịch : "+response, accountView));
            }
            LoadingOverlayManager.stop();

        }
     ).orTimeout(10,TimeUnit.SECONDS)
     .exceptionallyAsync(ex->
        { 
            if (ex.getCause() instanceof TimeoutException) {
            System.err.println("Lỗi: Server không phản hồi trong vòng 10 giây!");
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (Timeout)", accountView));
        } else {
            System.err.println("Lỗi hệ thống khác: " + ex.getMessage());
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (No connection)", btnLogout));
        }
        LoadingOverlayManager.stop();
        return null;
    }
     );
    }

    public void setPageNavData(int currentPage, int totalPages )
    {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        lblPageInfo.setText(String.format("Trang %d / %d", currentPage + 1, totalPages));

    }


    private void initializeTableColumns() {
    colDate.setCellValueFactory(new PropertyValueFactory<>("createdAt")); 
    colMovie.setCellValueFactory(new PropertyValueFactory<>("movieName")); 
    colPrice.setCellValueFactory(new PropertyValueFactory<>("totalAmount")); 
    colSeat.setCellValueFactory(new PropertyValueFactory<>("seatIdsJson"));
}

/**
 * Hàm đổ danh sách contents vào TableView
 */
public void populateTable(List<OrderDTO> contents) {
    if (contents == null) return;

    // Chuyển đổi List thành ObservableList để JavaFX TableView có thể nhận diện
    ObservableList<OrderDTO> observableList = FXCollections.observableArrayList(contents);
    
    // Cập nhật giao diện trên giao xới UI Thread của JavaFX để tránh lỗi xung đột luồng
    Platform.runLater(() -> {
        initializeTableColumns(); // Đảm bảo các cột đã được map property
        ticketHistoryTable.setItems(observableList);
    });
}
@Override
public void OnExit() {
    
}
    
}
