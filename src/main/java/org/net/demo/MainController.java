package org.net.demo;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.scene.control.Hyperlink;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainController{

     @FXML
    private Button btnLogin;

    @FXML
    private Label navAccount;

    @FXML
    private Label navHome;

    @FXML
    private StackPane page;

    @FXML
    private TextField searchField;
    @FXML
    private VBox mainView;

    private Parent CurrentPage;
    private Parent lastPage;


    private HashMap<String, Controller> Controllers= new HashMap<String,Controller>();

    private HashMap<String,Parent> Pages =new HashMap<String,Parent>();

    

    @FXML
 void initialize() {
       
        System.out.println("CineVerse UI loaded successfully.!!!!!!!!!!!!!!!!!!!");

        //Gán trang
        try 
        {
        AttachPage("HomeView.fxml");
        AttachPage("AccountView.fxml");
        AttachPage("LoginView_cuaDang.fxml");
        AttachPage("DetailView.fxml");

        System.out.print("attaching finished");

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        //gán chức năng cho nút
        btnLogin.setOnAction(event -> 
            {
                showPage(getPage("loginView"));
                setActiveAccountButton(false);
                setActiveHomeButton(false);
            }
        );
        navHome.setOnMouseClicked(event-> 
            {
                showPage(getPage("homeView"));
                setActiveAccountButton(false);
                setActiveHomeButton(true);
            }
        );
        navAccount.setOnMouseClicked(event->
            {
                showPage(getPage("accountView"));
                setActiveAccountButton(true);
                setActiveHomeButton(false);
            }
        );
        searchField.setOnMouseClicked(event->
            {
                showPage(getPage("searchView"));
                setActiveAccountButton(false);
                setActiveAccountButton(false);
            }
        );

        //
        showDefaultPage(getPage("homeView"));


        //hàm để bật trang bằng phím D vì chưa có phim card của Hoàng Anh
        mainView.setOnKeyPressed(event->
            {
                System.out.println("key pressed");
                if(event.getCode()==KeyCode.D)
                {
                    showPage(getPage("detailView"));
                }
            }
        );

    }

    public void AttachPage  (String fxmlpath) throws IOException
    {
        FXMLLoader loader= new FXMLLoader(getClass().getResource(fxmlpath));
        Parent node = loader.load();
        page.getChildren().add(node);
        node.setVisible(false);
        node.setManaged(false);
        Controller controller = loader.getController();
        Controllers.put(node.getId(),controller);
        Pages.put(node.getId(),node);
        controller.getMainController(this);
        controller.OnAttached();

    }

    public void showPage(Parent Npage)
    {
        
        if(Npage==null)
        {
            System.err.println("null page");
            return;
        }
        for(Node node : page.getChildren())
    {
      node.setVisible(false);
      node.setManaged(false);
    }
        Npage.toFront();
        Npage.setVisible(true);
        Npage.setManaged(true);
        System.out.print(Npage.getId());
        Controller controller = getController(Npage.getId());
        controller.OnShowing();
        lastPage=CurrentPage;
        CurrentPage=Npage;
    }


    public void setActiveHomeButton(boolean value)
    {
        if(value)
        {
            if(!navHome.getStyleClass().contains("nav-active"))
            {
                navHome.getStyleClass().add("nav-active");
            }
        }
        else
        {
            if(navHome.getStyleClass().contains("nav-active"))
            {
                navHome.getStyleClass().remove("nav-active");
            }
        }
        System.out.println("set active home button "+ value);

        
    }

    public void setActiveAccountButton(boolean value)
    {
        if(value)
        {
            if(!navAccount.getStyleClass().contains("nav-active"))
            {
                navAccount.getStyleClass().add("nav-active");
            }
        }
        else
        {
            if(navAccount.getStyleClass().contains("nav-active"))
            {
                navAccount.getStyleClass().remove("nav-active");
            }
        }
        System.out.println("set active account button "+ value);

        

        
    }

    public Parent getPage(String pageID)
    {
     if(Pages.containsKey(pageID))
     {
        return Pages.get(pageID);
     }
     else return null;
    }

    public Controller getController(String pageID)
    {
     if(Controllers.containsKey(pageID))
     {
        return Controllers.get(pageID);
     }
     else return null;
    }

private void showDefaultPage(Parent defaultPage)
{
    showPage(defaultPage);
}
public Parent getCurrentPage()
{
    return CurrentPage;
}
public Parent getLastPage()
{
    return lastPage;
}


    }




    


