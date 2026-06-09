package org.net.demo;


import java.util.Map;

import org.net.demo.DTO.LoginRequest;
import org.net.demo.DTO.RegisterRequest;

import com.google.gson.Gson;

import javafx.application.Platform;
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
    private Hyperlink linkLogin;

    @FXML
    private Hyperlink linkRegister;

    @FXML
    private TextField logInNameField;

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
                showLoginView();
            }
        );

        linkRegister.setOnAction(event->
            {
                showRegisterView();
            }
        );
        btnLogin.setOnAction(event->
            {
                String name=logInNameField.getText();
                String password=passwordField.getText();
                LoginRequest loginRequest=new LoginRequest(name,password);
                Gson gson= new Gson();
                String jsonString=gson.toJson(loginRequest);

                HTTPService.sendFullRequestAsync("POST", "/api/auth/login", null,jsonString,null).thenAccept(response->{
                    if(response.statusCode()!=200)
                    {
                        Platform.runLater(()->{
                            new Alert(Alert.AlertType.ERROR, "Login failed! Please check your credentials.").show();
                        });
                        return;
                    }
                    Map<String,String> tokenMap = gson.fromJson(response.body(), Map.class);
                    String token=tokenMap.get("token");
                    Platform.runLater(()->{mainController.logIn(token);
                     new Alert(Alert.AlertType.INFORMATION, "Login successful!").show();
                    });
                    
                    
                });
                
            }
        );
        btnRegister.setOnAction(event->{
            if(!regPasswordField.getText().equals(regConfirmPasswordField.getText()))
            {
                new Alert(Alert.AlertType.ERROR, "Password and Confirm Password do not match!").show();
                return;
            }
           RegisterRequest registerRequest=new RegisterRequest(regNameField.getText(),regPasswordField.getText(),regEmailField.getText());
              Gson gson= new Gson();
                String jsonString=gson.toJson(registerRequest);
                HTTPService.sendFullRequestAsync("POST", "/api/auth/register", null,jsonString,null).thenAccept(response->{
                    if(response.statusCode()!=200)
                    {
                        Platform.runLater(()->{
                            new Alert(Alert.AlertType.ERROR, "Registration failed! Please try again."+ response.statusCode()).show();
                        });
                        return;
                    }
                    
                    Platform.runLater(()->{
                        showLoginView();
                        new Alert(Alert.AlertType.INFORMATION, "Registration successful! Please log in.").showAndWait();

                    });
                    
                    
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

    public void showLoginView() {
        for(Node node : Pages.getChildren())
        {
            node.setVisible(false);
            node.setManaged(false);
        }
        loginContainer.toFront();
        loginContainer.setVisible(true);
        loginContainer.setManaged(true);
    }
    public void showRegisterView() {
        for(Node node : Pages.getChildren())
        {
            node.setVisible(false);
            node.setManaged(false);
        }
        registerContainer.toFront();
        registerContainer.setVisible(true);
        registerContainer.setManaged(true);
    }

}
