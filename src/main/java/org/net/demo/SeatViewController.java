package org.net.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.net.demo.DTO.TicketBookingRequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import java.lang.reflect.Type;

public class SeatViewController extends Controller{

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
            mainController.showPage(mainController.getLastPage());
        }
       );
       btnConfirmSeats.setOnAction(event->
        {
            BookTickets();
        });
    }

    @Override
    public void OnShowing() {
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
        gridPane.getChildren().clear(); // Làm sạch lưới cũ

        try {
            // 1. Parse JSON bằng Gson giống như trước
            Gson gson = new Gson();
            Type seatListType = new TypeToken<List<Seat>>(){}.getType();
            List<Seat> seatList = gson.fromJson(jsonString, seatListType);

            if (seatList == null) return;

            // 2. Duyệt qua từng ghế để load FXML
            for (Seat seat : seatList) {
                int row = seat.getRow();
                int col = seat.getCol();

                // Kiểm tra giới hạn grid 10x10
                if (row >= 0 && row < 10 && col >= 0 && col < 10) {
                    
                    
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Seat.fxml"));
                    Parent seatNode = loader.load();
                    ((ToggleButton)seatNode).setText((char)('A'+row)+String.valueOf(col+1));                    
                    gridPane.add(seatNode, col, row);
                    if(seat.getStatus().equals("BOOKED"))
                    {
                        seatNode.setDisable(true);
                    }
                    seatNode.setOnMouseClicked(event->
                        {
                         
                         if(((ToggleButton)seatNode).isSelected())
                         {
                            SelectSeat(seat);
                            
                         }
                         else{
                            RemoveSeat(seat);

                         }
                         CalculateTotalPrice();
                        }
                        
                    );
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
        if(showTimeID==null) return;
        Map<String,String> param=Map.of("showRoomID",showTimeID
            
        );
        HTTPService.sendRequestAsync("GET", "/api/feature/getSeatView", param, null, null).
        thenAccept(response->{
            Platform.runLater(()->{
                populateSeatGrid(response, gridSeats);
            });
        });
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
            new Alert(Alert.AlertType.INFORMATION,"vui lòng chọn ghế").showAndWait();
            return;
        }
        String jsonRequest = new Gson().toJson(request);
        System.out.println(jsonRequest);
        
        HTTPService.sendFullRequestAsync("POST", "/api/Ticket/bookTickets", null, jsonRequest, mainController.getToken()).thenAccept(
            response->{
              int statusCode = response.statusCode();
              if(statusCode == 200) {
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.INFORMATION, "Đặt vé thành công!").showAndWait();
                    
                });
            }
                else{
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR, "Đặt vé thất bại!").showAndWait();
                    });
                }
              
            
            }
        );
       
    }



}
