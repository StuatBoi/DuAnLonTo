package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.event.ActionEvent;

public class TicketsListController {

    @FXML
    private FlowPane ticketsContainer;

    @FXML
    public void initialize() {
        // initialization if needed
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        if (event != null && event.getSource() instanceof javafx.scene.Node) {
            javafx.scene.Node node = (javafx.scene.Node) event.getSource();
            if (node.getScene() != null && node.getScene().getWindow() != null) {
                node.getScene().getWindow().hide();
            }
        }
    }
}
