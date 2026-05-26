package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class HomeController extends Controller{

    @FXML
    private VBox card1;

    @FXML
    private VBox homeView;

    @FXML
    private ImageView thumb1;

    @Override
    public void OnShowing() {
        mainController.setActiveHomeButton(true);
        
    }

    @Override
    public void Refresh() {
       
    }

    @Override
    public void OnAttached() {
       
    }
    
}
