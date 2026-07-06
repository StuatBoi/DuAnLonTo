package org.net.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.net.demo.DTO.BookingPaymentResponse;
import org.net.demo.DTO.TicketBookingRequest;
import org.net.demo.Service.LoadingOverlayManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import java.lang.reflect.Type;

public class SeatViewController extends BaseController{

    @FXML
    private Button btnBackToDetail;

    @FXML
    private Button btnConfirmSeats;

    @FXML
    private GridPane gridSeats;

    @FXML
    private Label lblSelectedSeatsInfo;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private BorderPane seatView;


    private String ShowTimeID;
    private ArrayList<Seat> seatsList = new ArrayList<Seat>();

    @FXML
    private void initialize()
    {
       btnBackToDetail.setOnAction(event->
        {
            mainController.showPage(mainController.getPage("detailView"));
        }
       );
       btnConfirmSeats.setOnAction(event->
        {
           toPaymentPage();
        });
    }

    @Override
    public void OnShowing() {
        seatsList.clear();
        
        CalculateTotalPrice();
        LoadSeatView(ShowTimeID);
        
    }

    @Override
    public void Refresh() {
      LoadSeatView(ShowTimeID);
    }

    @Override
    public void OnAttached() {
        
    }


    public void populateSeatGrid(String jsonString, GridPane gridPane) {
        gridPane.getChildren().clear();

        try {
            Gson gson = new Gson();
            Type seatListType = new TypeToken<List<Seat>>(){}.getType();
            List<Seat> seatList = gson.fromJson(jsonString, seatListType);

            if (seatList == null) return;

            for (Seat seat : seatList) {
                int row = seat.getRow();
                int col = seat.getCol();

                if (row >= 0 && row < 10 && col >= 0 && col < 10) {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Seat.fxml"));
                    Parent seatNode = loader.load();
                    ToggleButton seatButton = (ToggleButton) seatNode;

                    seatButton.setText((char) ('A' + row) + String.valueOf(col));

                    // 🌟 Đánh dấu ghế VIP dựa theo dữ liệu thật từ backend
                    boolean isVip = "VIP".equalsIgnoreCase(seat.getType());
                    if (isVip) {
                        seatButton.getStyleClass().add("seat-vip");
                    }

                    gridPane.add(seatNode, col, row);

                    if (seat.getStatus().equals("BOOKED")) {
                        seatButton.setDisable(true);
                        seatButton.getStyleClass().add("seat-sold");
                    }

                    seatButton.setOnMouseClicked(event -> {
                        if (seatButton.isSelected()) {
                            SelectSeat(seat);
                        } else {
                            RemoveSeat(seat);
                        }
                        CalculateTotalPrice();
                    });
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi nạp dữ liệu ghế từ FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void CalculateTotalPrice()
    {
        Double total =0d;
      for(Seat seat : seatsList)
      {
        total+=seat.getBasePrice();
        System.out.println(total);
      }
      lblTotalPrice.setText(total+" đ");
    }
    public void SelectSeat(Seat seat)
    {
        System.out.println("select "+ seat.getId());
      seatsList.add(seat);
    }
    public void RemoveSeat(Seat seat)
    {
        System.out.println("remove "+ seat.getId());
        if(seatsList.contains(seat))
        seatsList.remove(seat);
    }

    public void LoadSeatView(String showTimeID)
    {
        gridSeats.getChildren().clear();
        LoadingOverlayManager.start(btnBackToDetail);
        if(showTimeID==null) 
        {
            LoadingOverlayManager.stop();
            CineverseAlert.showToast("Mã số suất chiếu không hợp lệ", btnBackToDetail);
            return;
        }
        Map<String,Object> param=Map.of("showTimeID",showTimeID
            
        );
        HTTPService.sendFullRequestAsync("GET", "/api/feature/getSeatView", param, null, mainController.getToken()).
        thenAccept(response->{
            if(response.statusCode()==200)
            Platform.runLater(()->{
                populateSeatGrid(response.body(), gridSeats);
                LoadingOverlayManager.stop();
            });
            else 
            {
                LoadingOverlayManager.stop();
                CineverseAlert.showToast("Không thể lấy thông tin phòng chiếu", btnBackToDetail);
            }
        })
        .orTimeout(10,TimeUnit.SECONDS)
     .exceptionallyAsync(ex->
        { 
            if (ex.getCause() instanceof TimeoutException) {
            System.err.println("Lỗi: Server không phản hồi trong vòng 10 giây!");
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (Timeout)", btnBackToDetail));
        } else {
            System.err.println("Lỗi hệ thống khác: " + ex.getMessage());
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (No Connection)", btnBackToDetail));
        }
        LoadingOverlayManager.stop();
        return null;
    }
     );
    }
    public void setShowTimeID(String showTimeID)
    {
        this.ShowTimeID=showTimeID;
    }

    @Override
    public void OnLogin() {
        
    }

    @Override
    public void OnLogout() {
        
    }

    public TicketBookingRequest CreateBookingRequest()
    {
        TicketBookingRequest request = new TicketBookingRequest();
        request.setShowtimeId(Long.parseLong(ShowTimeID));
        List<Long> seatIDs = new ArrayList<>();
        for(Seat seat : seatsList)
        {
            seatIDs.add(Long.parseLong(seat.getId()));
        }
        request.setSeatIds(seatIDs);
        return request;
    }
    public void BookTickets()
    {
        TicketBookingRequest request = CreateBookingRequest();
        if(request.getSeatIds().isEmpty())
        {
            CineverseAlert.showToast("Vui lòng chọn ít nhất một ghế để đặt vé!", btnConfirmSeats);
            return;
        }
        String jsonRequest = new Gson().toJson(request);
        System.out.println(jsonRequest);
        
        HTTPService.sendFullRequestAsync("POST", "/api/payment/bookTickets", null, jsonRequest, mainController.getToken()).thenAccept(
            response->{
              int statusCode = response.statusCode();
              if(statusCode == 200) {
                Platform.runLater(() -> {
                    CineverseAlert.showToast("Đặt vé thành công! Truy cập trang tài khoản để xem vé", btnConfirmSeats);
                    
                    
                });
            }
                else{
                    Platform.runLater(() -> {
                        CineverseAlert.showToast("Đặt vé thất bại! Vui lòng thử lại.", btnConfirmSeats);
                    });
                }
              
            
            }
        )
        .orTimeout(10,TimeUnit.SECONDS)
     .exceptionallyAsync(ex->
        { 
            if (ex.getCause() instanceof TimeoutException) {
            System.err.println("Lỗi: Server không phản hồi trong vòng 10 giây!");
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (Timeout)", btnBackToDetail));
        } else {
            System.err.println("Lỗi hệ thống khác: " + ex.getMessage());
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (No Connevtion)", btnBackToDetail));
        }
        LoadingOverlayManager.stop();
        return null;
    }
     );
       
    }

public void toPaymentPage()
{
    TicketBookingRequest request = CreateBookingRequest();
        if(request.getSeatIds().isEmpty())
        {
            CineverseAlert.showToast("Vui lòng chọn ít nhất một ghế để đặt vé!", btnConfirmSeats);
            return;
        }
        String jsonRequest = new Gson().toJson(request);
        System.out.println(jsonRequest);
    LoadingOverlayManager.start(btnBackToDetail);
    HTTPService.sendFullRequestAsync("POST", "/api/payment/bookTickets", null, jsonRequest, mainController.getToken()).thenAcceptAsync(response->
        {
            if(response.statusCode()==200)
            {
                System.out.println(response.body());
                Gson gson= new Gson();
                Type bpResponse=new TypeToken<BookingPaymentResponse>(){}.getType();
                BookingPaymentResponse bookingPaymentResponse= gson.fromJson(response.body(),bpResponse);
            PaymentViewController paymentViewController =(PaymentViewController)mainController.getController("paymentView");
            Platform.runLater(()->{
            paymentViewController.setData(bookingPaymentResponse.getPaymentUrl(),bookingPaymentResponse.getOrderId());
            mainController.showPage(mainController.getPage("paymentView"));
            });
            LoadingOverlayManager.stop();
            }
            else
                { Platform.runLater(()->
                {
                     LoadingOverlayManager.stop();
                    CineverseAlert.showToast("không thể lấy liên kết thanh toán do xung đột", btnBackToDetail);
                   
                });
            System.out.print(response.statusCode());
        }
        }
    )
    .orTimeout(10,TimeUnit.SECONDS)
     .exceptionallyAsync(ex->
        { 
            if (ex.getCause() instanceof TimeoutException) {
            System.err.println("Lỗi: Server không phản hồi trong vòng 10 giây!");
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (Timeout)", btnBackToDetail));
        } else {
            System.err.println("Lỗi hệ thống khác: " + ex.getMessage());
            Platform.runLater(() -> CineverseAlert.showToast("Kết nối server thất bại (No Connection)", btnBackToDetail));
        }
        LoadingOverlayManager.stop();
        return null;
    }
     );
    

}

@Override
public void OnExit() {
    ShowTimeID=null;
}
}
