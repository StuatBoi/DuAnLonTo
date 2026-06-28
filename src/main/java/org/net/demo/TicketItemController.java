package org.net.demo;


import org.net.demo.DTO.TicketDTO;

import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TicketItemController extends Controller {

    @FXML
    private Label lblDay;

    @FXML
    private Label lblMonthYear;

    @FXML
    private Label lblMovieTitle;

    @FXML
    private Label lblRoom;

    @FXML
    private Label lblSeats;

    @FXML
    private Label lblTime;

    private String ticketCode;

    @FXML
    void onDetailClick(ActionEvent event) {
            TicketDetailController controller=(TicketDetailController) mainController.getController("ticketDetail");

            mainController.showPage(mainController.getPage("ticketDetail"));
            controller.getData(ticketCode);
    }

    public void setData(TicketDTO ticket)
    {
        if (ticket == null || ticket.getStartTime() == null) {
            lblDay.setText("");
            lblMonthYear.setText("");
            lblTime.setText("");
            lblMovieTitle.setText(ticket != null ? ticket.getMovieTitle() : "");
            lblRoom.setText(ticket != null ? ticket.getRoomName() : "");
            lblSeats.setText(ticket != null ? ticket.getSeatName() : "");
            return;
        }

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ticketCode = ticket.getTicketCode();
        lblDay.setText(ticket.getStartTime().format(dayFormatter));
        lblMonthYear.setText(ticket.getStartTime().format(monthYearFormatter));
        lblTime.setText(ticket.getStartTime().format(timeFormatter));
        lblMovieTitle.setText(ticket.getMovieTitle());
        lblRoom.setText(ticket.getRoomName());
        lblSeats.setText(ticket.getSeatName());
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

