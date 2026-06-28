package org.net.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private String token;
    private Boolean isLoggedIn=false;

    private Parent CurrentPage;
    private Parent lastPage;


    private HashMap<String, BaseController> Controllers= new HashMap<String,BaseController>();

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
        AttachPage("SeatView.fxml");
        AttachPage("TicketView.fxml");
        AttachPage("TicketDetail.fxml");
        AttachPage("SearchView.fxml");
        AttachPage("PaymentView.fxml");

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
                if(!IsLoggedIn())
                {
                    CineverseAlert.showToast("Vui lòng đăng nhập để truy cập trang tài khoản", navAccount);
                    return;
                }
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
        searchField.setOnKeyPressed(event->{
            if(event.getCode().equals(KeyCode.ENTER))
            {
                SearchController searchController = (SearchController)getController("searchView");
                searchController.Search(searchField.getText());
            }
        });

        //
        CurrentPage=getPage("homeView");
        showDefaultPage(CurrentPage);


        //hàm để bật trang bằng phím D vì chưa có phim card của Hoàng Anh
        mainView.setOnKeyPressed(event->
            {
                
                if(event.getCode()==KeyCode.D)
                {
                    showPage(getPage("detailView"));
                }
                if(event.getCode()==KeyCode.S)
                {
                    showPage(getPage("seatView"));
                }
            }
        );

        setHotKey();

    }

    public void AttachPage  (String fxmlpath) throws IOException
    {
        FXMLLoader loader= new FXMLLoader(getClass().getResource(fxmlpath));
        Parent node = loader.load();
        page.getChildren().add(node);
        node.setVisible(false);
        node.setManaged(false);
        BaseController controller = loader.getController();
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
        setActiveAccountButton(false);
        setActiveHomeButton(false);
        for(Node node : page.getChildren())
    {
      node.setVisible(false);
      node.setManaged(false);
    }

        Npage.toFront();
        Npage.setVisible(true);
        Npage.setManaged(true);
        BaseController controller = Controllers.get(Npage.getId());
        System.out.println(Npage.isVisible());
        if(controller != null) {
            controller.OnShowing();
        }
        System.out.print(Npage.getId());
        getController(CurrentPage.getId()).OnExit();
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
        

        

        
    }

    public Parent getPage(String pageID)
    {
     if(Pages.containsKey(pageID))
     {
        return Pages.get(pageID);
     }
     else return null;
    }

    public BaseController getController(String pageID)
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

public Boolean IsLoggedIn()
{
    return isLoggedIn;
}
public void setIsLoggedIn(Boolean value)
{
    isLoggedIn=value;
}
public String getToken()
{
    return token;
}
public void setToken(String token)
{
    this.token=token;
}

public void logIn(String token)
{
    setToken(token);
     setIsLoggedIn(true);
     for(BaseController controller : Controllers.values())
    {
        controller.OnLogin();
    }
    showPage(getPage("homeView"));
    btnLogin.setText("Đăng xuất");
    btnLogin.setOnAction(event->
        {
            logOut();
        }
    );
    
}
public void logOut()
{
 setToken(null);
 setIsLoggedIn(false);
 for(BaseController controller : Controllers.values())
    {
        controller.OnLogout();
    }
    showPage(getPage("homeView"));
    btnLogin.setText("Đăng nhập");
    btnLogin.setOnAction(event->
        {
            showPage(getPage("loginView"));
                setActiveAccountButton(false);
                setActiveHomeButton(false);
        }
    );
    CineverseAlert.showToast("Đã đăng xuất !", btnLogin);
}

public StackPane getPageContainer()
{
    return page;
}
public TextField getSearchField()
{
    return searchField;
}
public void setHotKey()
{
Platform.runLater(() -> {
        Scene scene = getPage("homeView").getScene();
        if (scene != null) {
            
            // 1. Định nghĩa phím tắt (Ví dụ: phím F5)
            KeyCombination refreshKey = new KeyCodeCombination(KeyCode.F5);
            
            // 2. Đăng ký Accelerator cho Scene
            scene.getAccelerators().put(refreshKey, () -> {
                // 3. Gọi hàm bạn muốn thực thi tại đây
                System.out.println("Phím F5 được bấm toàn cục!");
                if(CurrentPage!=null)
                {
                    getController(CurrentPage.getId()).Refresh();
                }
            });
            
        }
    });
}


    }




    


