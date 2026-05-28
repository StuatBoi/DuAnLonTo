package org.net.demo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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


    private String ShowRoomID;
    private ArrayList<Seat> seatsList = new ArrayList<Seat>();

    String jsonString = "["
    + "{\"id\":\"A1\", \"row\":0, \"col\":0, \"status\":\"AVAILABLE\",\"price\":\"50000\",\"roomID\":\"50000\"},"
    + "{\"id\":\"A2\", \"row\":0, \"col\":1, \"status\":\"BOOKED\",\"price\":\"50000\",\"roomID\":\"50000\"},"
    + "{\"id\":\"E5\", \"row\":4, \"col\":4, \"status\":\"AVAILABLE\",\"price\":\"50000\",\"roomID\":\"50000\"}"
    + "]";

    @FXML
    private void initialize()
    {
       btnBackToDetail.setOnAction(event->
        {
            mainController.showPage(mainController.getLastPage());
        }
       );
    }

    @Override
    public void OnShowing() {
        populateSeatGrid(jsonString, gridSeats);
        
    }

    @Override
    public void Refresh() {
      
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
                    ((ToggleButton)seatNode).setText(seat.getId());                    
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
        total+=seat.getPrice();
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
}
