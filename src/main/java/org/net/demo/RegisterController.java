package org.net.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class RegisterController {

    @FXML private VBox registerContainer;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox acceptTermsCheckbox;
    @FXML private Button btnSubmitRegister;
    @FXML private Hyperlink linkGoToLogin;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        // Xử lý logic nghiệp vụ khi người dùng bấm nút "ĐĂNG KÝ NGAY" bên trong form
        btnSubmitRegister.setOnAction(event -> {
            handleRegister();
        });
        // Ép checkbox KHÔNG được ở trạng thái lấp lửng (indeterminate)
        acceptTermsCheckbox.setIndeterminate(false);

        // Logic nút đăng ký của bạn giữ nguyên
        btnSubmitRegister.setOnAction(event -> {
            handleRegister();
        });
    }

    /**
     * Hàm xử lý logic Đăng ký (Validate dữ liệu đầu vào, ràng buộc điều khoản...)
     */
    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        boolean isAccepted = acceptTermsCheckbox.isSelected();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Lỗi: Không được để trống bất kỳ trường nào!");
            return;
        }

        if (isAccepted) {
            errorLabel.setText("Lỗi: Bạn phải đồng ý với Điều khoản dịch vụ!");
            return;
        }

        // TODO: Viết code thêm tài khoản mới vào Database của bạn ở đây
        errorLabel.setText("Đăng ký thành công tài khoản: " + email);
    }
    @FXML
    private void onGoToLogin(ActionEvent event) {
        try {
            // 1. Tải file giao diện LoginView.fxml
            Parent loginView = FXMLLoader.load(getClass().getResource("/org/net/demo/LoginView.fxml"));

            // 2. Đặt ID là "dynamicLoginView" để đồng bộ với bộ lọc dọn dẹp của MainController
            loginView.setId("dynamicLoginView");

            // 3. Lấy Scene hiện tại và tìm StackPane mang id "page" (khung chứa ở MainView)
            StackPane page = (StackPane) ((Node) event.getSource()).getScene().lookup("#page");

            if (page != null) {
                // 4. Xóa màn hình Register hiện tại (vốn cũng dùng ID dynamicLoginView khi nạp vào)
                page.getChildren().removeIf(node -> "dynamicLoginView".equals(node.getId()));

                // 5. Thêm màn hình Login vừa tải vào khung chứa
                page.getChildren().add(loginView);

                // Căn giữa màn hình đăng nhập trong StackPane
                StackPane.setAlignment(loginView, Pos.CENTER);
                System.out.println("Đã chuyển về màn hình Đăng nhập thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Không thể chuyển sang trang LoginView.fxml. Hãy kiểm tra lại đường dẫn file!");
        }
    }
    /**
     * Hàm Getter bắt buộc phải có để MainController (Controller cha)
     * lấy được phần tử Hyperlink và thực hiện quay lại form đăng nhập.
     */
    public Hyperlink getLinkGoToLogin() {
        return linkGoToLogin;
    }

    // Getter phụ
    public VBox getRegisterContainer() {
        return registerContainer;
    }
}