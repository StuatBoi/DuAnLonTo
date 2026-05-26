package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class SeatViewController extends Controller{

    @FXML
    private Button btnBackToDetail;

    @FXML
    private Button btnConfirmSeats;

    @FXML
    private FlowPane flowSeats;

    @FXML
    private Label lblSelectedSeatsInfo;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private BorderPane seatSelectionView;

    private String ShowRoomID;

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
        
    }

    @Override
    public void Refresh() {
      
    }

    @Override
    public void OnAttached() {
        
    }
    
}
