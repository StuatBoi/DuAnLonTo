package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class TicketDetailController extends Controller {

    @FXML
    private ImageView imgQRCode;

    @FXML
    private Label lblTicketCode;

    @FXML
    private Label lblMovieTitle;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblRoom;

    @FXML
    private Label lblSeats;

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblPrice;

    @FXML
    private Button btnClose;

    @FXML
    public void initialize() {
        // initialization if needed
    }

    @FXML
    private void onCloseClick(ActionEvent event) {
        if (btnClose != null && btnClose.getScene() != null) {
            btnClose.getScene().getWindow().hide();
        } else if (event != null && event.getSource() instanceof javafx.scene.Node) {
            ((javafx.scene.Node) event.getSource()).getScene().getWindow().hide();
        }
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

    @Override
    public void OnLogin() {
        
    }

    @Override
    public void OnLogout() {
        
    }
}
