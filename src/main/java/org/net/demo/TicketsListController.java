package org.net.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;

import java.util.List;

import org.net.demo.DTO.TicketDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
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
        System.out.println("send tickets request");

        HTTPService.sendFullRequestAsync("GET", "/api/Ticket/getUsersTickets", null, null, mainController.getToken()).thenAccept(
            response->{
                if(response.statusCode()!=200)
                {
                    System.out.println("Failed to load tickets: "+response+ "status :"+ response.statusCode());
                    return;
                }
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();
                List<TicketDTO> tickets= gson.fromJson(response.body(), new TypeToken<List<TicketDTO>>(){}.getType());
                System.out.println("phan hoi : "+response);
                Platform.runLater(()->{
                    ticketsContainer.getChildren().clear();
                    for(TicketDTO ticket : tickets)
                    {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketItem.fxml"));
                            Parent ticketItem = loader.load();
                            TicketItemController controller = loader.getController();
                            controller.setData(ticket);
                            controller.getMainController(mainController);
                            ticketsContainer.getChildren().add(ticketItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        ).whenComplete((res, ex)->{
            if(ex!=null)
            {
                System.out.println("Error loading tickets: "+ex.getMessage());
            }
        });
    }
}
