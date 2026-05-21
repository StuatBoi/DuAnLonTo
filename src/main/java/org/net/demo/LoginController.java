package org.net.demo;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;


public class LoginController {

    @FXML
    private VBox loginContainer;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button btnSubmitLogin;
    @FXML
    private Hyperlink linkGoToRegister;

    @FXML
    public void initialize() {
        // Xử lý logic nghiệp vụ khi người dùng bấm nút "ĐĂNG NHẬP" bên trong form
        btnSubmitLogin.setOnAction(event -> {
            handleLogin();
        });
    }

    /**
     * Hàm xử lý logic Đăng nhập (Kiểm tra tài khoản, mật khẩu, kết nối DB...)
     */
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Lỗi: Vui lòng nhập đầy đủ thông tin!");
            // Bạn có thể tỉa thêm Alert thông báo lỗi ở đây
            return;
        }

        // TODO: Viết code kiểm tra tài khoản mật khẩu từ Database của bạn ở đây
        System.out.println("Đang tiến hành đăng nhập với Email: " + email);
    }

    /**
     * Hàm Getter bắt buộc phải có để MainController (Controller cha)
     * lấy được phần tử Hyperlink và thực hiện hoán đổi giao diện.
     */
    public Hyperlink getLinkGoToRegister() {
        return linkGoToRegister;
    }

    // Các Getter phụ phòng trường hợp Controller cha cần tương tác sâu hơn
    public VBox getLoginContainer() {
        return loginContainer;
    }
    @FXML
    private void onGoToRegister(ActionEvent event) {
        try {
            // 1. Tải file RegisterView.fxml
            Parent registerView = FXMLLoader.load(getClass().getResource("/org/net/demo/RegisterView.fxml"));

            // Định danh ID để MainController có thể xóa nó khi bấm quay lại trang chủ
            registerView.setId("dynamicLoginView");

            // 2. Tìm cái StackPane "page" từ Scene hiện tại
            // Vì nút Đăng ký nằm trong Scene, ta có thể dùng lookup để tìm ID "#page"
            StackPane page = (StackPane) ((Node) event.getSource()).getScene().lookup("#page");

            if (page != null) {
                // 3. Xóa màn hình Login hiện tại và thêm màn hình Register vào
                page.getChildren().removeIf(node -> "dynamicLoginView".equals(node.getId()));
                page.getChildren().add(registerView);

                StackPane.setAlignment(registerView, Pos.CENTER);
                System.out.println("Đã chuyển sang trang Đăng ký");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Không thể load trang RegisterView.fxml");
        }
    }

        }



