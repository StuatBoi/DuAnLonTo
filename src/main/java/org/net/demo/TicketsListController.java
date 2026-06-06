package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.event.ActionEvent;

public class TicketsListController extends Controller {

     
    @FXML
    private FlowPane ticketsContainer;

    @FXML
    public void initialize() {
        // initialization if needed
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        mainController.showPage(mainController.getLastPage());
    }

    @Override
    public void OnShowing() {
      loadTickets();
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

    public void loadTickets() {

        HTTPService.sendRequestAsync("GET", "/api/Tickets/getTickets", null, null, mainController.getToken()).thenAccept(
            response->{System.out.println(response);}
        );
    }
}
