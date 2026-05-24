package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class AccountController extends Controller{

    @FXML
    private VBox accountView;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colMovie;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colSeat;

    @FXML
    private TableView<?> ticketHistoryTable;

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
