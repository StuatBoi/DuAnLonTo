package org.net.demo;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginController extends Controller{

     

    @FXML
    private VBox Pages;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private TextField emailField;

    @FXML
    private Hyperlink linkLogin;

    @FXML
    private Hyperlink linkRegister;

    @FXML
    private VBox loginContainer;

    @FXML
    private StackPane loginView;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField regConfirmPasswordField;

    @FXML
    private TextField regEmailField;

    @FXML
    private TextField regNameField;

    @FXML
    private PasswordField regPasswordField;

    @FXML
    private VBox registerContainer;
    @FXML
    public void initialize()
    {
        linkLogin.setOnAction(event->
            {
                for(Node node : Pages.getChildren())
                {
                    node.setVisible(false);
                    node.setManaged(false);
                }
                loginContainer.toFront();
                loginContainer.setVisible(true);
                loginContainer.setManaged(true);
            }
        );

        linkRegister.setOnAction(event->
            {
                for(Node node : Pages.getChildren())
                {
                    node.setVisible(false);
                    node.setManaged(false);
                }
                registerContainer.toFront();
                for(Node node : Pages.getChildren())
                {
                    node.setVisible(false);
                    node.setManaged(false);
                }
                registerContainer.toFront();
                registerContainer.setVisible(true);
                registerContainer.setManaged(true);
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
