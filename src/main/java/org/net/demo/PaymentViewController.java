package org.net.demo;


import org.net.demo.Service.PaymentService;
import org.net.demo.Service.QRCodeGenerator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class PaymentViewController extends BaseController{

    @FXML private ImageView qrImageView;
    @FXML private Label lblCountdown;
    @FXML private Button btnCancel;
    private PaymentService paymentService;

    private Timeline timeline;
    private int timeSeconds = 600; // 10 phút = 600 giây
    private Long currentOrderID;

    @FXML
    public void initialize() {
        String qrUrlFromVnPay = "https://sandbox.vnpayment.vn/qr/example-code.png"; 
        setQrImage(qrUrlFromVnPay);
    }

    // Hàm load ảnh từ URL vào ImageView công khai để gọi từ lớp ngoài nếu cần
    public void setQrImage(String url) {
        if (url != null && !url.isEmpty()) {
            // Tạo mã QR với kích thước mong muốn (ví dụ: 250x250 px)
            Image qrImage = QRCodeGenerator.generateQRCodeImage(url, 250, 250);
            
            if (qrImage != null) {
                qrImageView.setImage(qrImage);
            }
        }
    }

    // Logic đếm ngược 10 phút sử dụng Timeline
    private void startCountdown() {
        btnCancel.setText("Hủy thanh toán");
    // 1. KIỂM TRA VÀ DỪNG TIMELINE CŨ (NẾU ĐANG CHẠY)
    if (timeline != null) {
        timeline.stop();
    }

    timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(
        new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds--;
            
            int minutes = timeSeconds / 60;
            int seconds = timeSeconds % 60;
            
            lblCountdown.setText(String.format("%02d:%02d", minutes, seconds));
            
            if (timeSeconds <= 0) {
                timeline.stop();
                handleTimeOut();
            }
        })
    );
    timeline.play();
}

    private void handleTimeOut() {
        lblCountdown.setText("ĐÃ HẾT HẠN");
        btnCancel.setText("Thoát về trang chủ");
        // Thêm logic thông báo cho người dùng hoặc tự động back về trang chủ...
    }

    @FXML
    void handleCancelPayment(ActionEvent event) {
        
    }

    @Override
    public void OnShowing() {
       startCountdown();
       connectToServer();
       
    }

    @Override
    public void Refresh() {
        
    }

    @Override
    public void OnAttached() {
        btnCancel.setOnAction(event->
    {
         if (timeline != null) {
            timeline.stop();
        }
        CineverseAlert.showToast("Đã hủy thanh toán", btnCancel);
        if(paymentService!=null)
          {
            paymentService.stopPaymentConfirmation();
          }
        
        mainController.showPage(mainController.getPage("homeView"));
    });
    }

    @Override
    public void OnLogin() {
        
    }

    @Override
    public void OnLogout() {
       
    }

    public void setData(String url,Long currentpaymentID)
    {
     setQrImage(url);
     this.currentOrderID=currentpaymentID;
    }
    public void connectToServer()
    {
        AccountController accountController= (AccountController)mainController.getController("accountView");
        paymentService= new PaymentService("http://localhost:8080/ws-payment", accountController.getUsername(), btnCancel);
        paymentService.startPaymentConfirmation(currentOrderID, 
            ()->{
                Platform.runLater(()->{
                CineverseAlert.showToast("Thanh toán thành công", btnCancel);
                OnFinish(true);});
                paymentService.stopPaymentConfirmation();
            },
            ()->{Platform.runLater(()->{CineverseAlert.showToast("Thanh Toán thất bại do lỗi", btnCancel);
                OnFinish(false);});
                paymentService.stopPaymentConfirmation();
            }
            );

    }
    public void OnFinish(boolean result)
    {
        String temp = result? " thành công": " thất bại";
        lblCountdown.setText("Thanh toán "+ temp);
        btnCancel.setText("Hoàn tất");
        paymentService.stopPaymentConfirmation();
        
    }

    @Override
    public void OnExit() {
       
    }
}
