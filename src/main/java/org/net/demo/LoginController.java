package org.net.demo;


import java.util.Map;

import org.net.demo.DTO.LoginRequest;
import org.net.demo.DTO.RegisterRequest;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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
        btnLogin.setOnAction(event->
            {
                String email=emailField.getText();
                String password=passwordField.getText();
                LoginRequest loginRequest=new LoginRequest(email,password);
                Gson gson= new Gson();
                String jsonString=gson.toJson(loginRequest);

                HTTPService.sendRequestAsync("POST", "/api/auth/Login", null,jsonString,null).thenAccept(response->{
                    System.out.println("Dăng nhập thành công");
                    Map<String,String> tokenMap = gson.fromJson(response, Map.class);
                    String token=tokenMap.get("token");
                    mainController.logIn(token);
                });
                
            }
        );
        btnRegister.setOnAction(event->{
            if(!regPasswordField.getText().equals(regConfirmPasswordField.getText()))
            {
                new Alert(Alert.AlertType.ERROR, "Password and Confirm Password do not match!").show();
                return;
            }
           RegisterRequest registerRequest=new RegisterRequest(regEmailField.getText(),regNameField.getText(),regPasswordField.getText());
              Gson gson= new Gson();
                String jsonString=gson.toJson(registerRequest);
                HTTPService.sendRequestAsync("POST", "/api/auth/Register", null,jsonString,null).thenAccept(response->{
                    System.out.println("Đăng ký thành công");
                    new Alert(Alert.AlertType.INFORMATION, "Registration successful! Please log in.").show();
                    for(Node node : Pages.getChildren())
                {
                    node.setVisible(false);
                    node.setManaged(false);
                }
                loginContainer.toFront();
                loginContainer.setVisible(true);
                loginContainer.setManaged(true);
                    
                });
        });
        
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
