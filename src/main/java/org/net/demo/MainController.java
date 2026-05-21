package org.net.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Hyperlink;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainController{

    @FXML private TextField searchField;
    @FXML private Button btnLogin;
    @FXML private VBox homeView;
    @FXML private VBox accountView;
    @FXML private Label navHome;
    @FXML private Label navAccount;
    @FXML private Hyperlink linkGoToRegister;
    @FXML private Hyperlink linkGoToLogin;
    @FXML private Hyperlink linkRegister;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Hover effects are handled purely via CSS.
        // Add any dynamic data-loading logic here.
        System.out.println("CineVerse UI loaded successfully.");
    }

    @FXML
    public void onLoginClicked(){
        System.out.println("Login button clicked");
        try {
            // 1. Tải giao diện Login lên
            Parent loginView = FXMLLoader.load(getClass().getResource("/org/net/demo/LoginView.fxml"));

            // Đặt ID cho loginView để lát nữa ta dễ dàng tìm và xóa nó đi khi chuyển trang
            loginView.setId("dynamicLoginView");

            // 2. Ẩn tạm thời các view gốc đi
            homeView.setVisible(false);
            homeView.setManaged(false);
            accountView.setVisible(false);
            accountView.setManaged(false);

            // 3. Xóa màn hình login cũ nếu có trước khi add màn hình mới (tránh trùng lặp)
            page.getChildren().removeIf(node -> "dynamicLoginView".equals(node.getId()));

            // 4. Thêm loginView vào StackPane
            page.getChildren().add(loginView);
            StackPane.setAlignment(loginView, Pos.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void onWatchNow() {
        System.out.println("Watch now clicked");
        // TODO: navigate to featured movie
    }

    @FXML
    public void onDetail() {
        System.out.println("Chi tiết clicked");
        // TODO: open movie detail view
    }
    @FXML
    public void onHomePageClick(){
        page.getChildren().removeIf(node -> "dynamicLoginView".equals(node.getId()));

        // 2. Hiện lại trang chủ
        homeView.setVisible(true);
        homeView.setManaged(true);

        // 3. Ẩn trang tài khoản
        accountView.setVisible(false);
        accountView.setManaged(false);

        // 4. Đổi trạng thái active cho Menu
        if (!navHome.getStyleClass().contains("nav-active")) {
            navHome.getStyleClass().add("nav-active");
        }
        navAccount.getStyleClass().remove("nav-active");
    }
   @FXML
           public void onAccountClick() {
       page.getChildren().removeIf(node -> "dynamicLoginView".equals(node.getId()));

       // 2. Ẩn trang chủ
       homeView.setVisible(false);
       homeView.setManaged(false);

       // 3. Hiện lại trang tài khoản
       accountView.setVisible(true);
       accountView.setManaged(true);

       // 4. Đổi trạng thái active cho Menu
       if (!navAccount.getStyleClass().contains("nav-active")) {
           navAccount.getStyleClass().add("nav-active");
       }
       navHome.getStyleClass().remove("nav-active");
   }

    @FXML
    private StackPane page;
    }




    


