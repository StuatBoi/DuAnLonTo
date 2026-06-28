package org.net.demo;

import org.net.demo.DTO.AccountDTO;

import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class AccountController extends Controller{
    @FXML
    private Button btnTicketView;
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

    @FXML
    private Label lblUsername;
    @FXML
    private Label lblEmail;

  

    @Override
    public void OnShowing() {
        mainController.setActiveAccountButton(true);
        
        
    }

    @Override
    public void Refresh() {
       
    }

    @Override
    public void OnAttached() {
       btnTicketView.setOnAction(e->{
        mainController.showPage(mainController.getPage("ticketView"));});
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
        HTTPService.sendRequestAsync("GET", "/api/account/getInfo", null, null, mainController.getToken()).thenAccept(response->{
            Gson gson=new Gson();
            AccountDTO accountInfo=gson.fromJson(response, AccountDTO.class);
            Platform.runLater(()->{
                if(accountInfo==null)
                {
                    System.err.println("Failed to parse account info from response: "+response);
                    return;
                }
                updateInfo(accountInfo);
            });
        }).exceptionally(ex->{
            System.err.println("Failed to load account info: "+ex.getMessage());
            return null;
        });
    }
    public void updateInfo(AccountDTO accountInfo)
    {
      lblUsername.setText(accountInfo.getUsername());
      lblEmail.setText(accountInfo.getEmail());
    }
    
}
