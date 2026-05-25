package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class DetailController extends Controller{

    @FXML
    private Button btnBack;

    @FXML
    private Button btnBookTicket;

    @FXML
    private FlowPane flowShowtimes;

    @FXML
    private ImageView imgPoster;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblDuration;

    @FXML
    private Label lblGenre;

    @FXML
    private Label lblMovieTitle;

    @FXML
    private Label lblRating;



    @FXML
    private void initialize()
    {
        
        btnBack.setOnAction(event->
            {
                mainController.showPage(mainController.getLastPage());
            }
        );
    }

    @Override
    public void OnShowing() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void OnAttached() {
        // TODO Auto-generated method stub
        
    }

}
