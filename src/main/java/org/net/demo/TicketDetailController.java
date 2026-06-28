package org.net.demo;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.net.demo.DTO.TicketDetail;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public class TicketDetailController extends Controller {

    @FXML private ImageView imgQRCode;
    @FXML private Label lblTicketCode;
    @FXML private Label lblMovieTitle;
    @FXML private Label lblDate;
    @FXML private Label lblTime;
    @FXML private Label lblRoom;
    @FXML private Label lblSeats;
    @FXML private Label lblCustomer;
    @FXML private Label lblPrice;
    @FXML private Label lblStatus;
    @FXML private Button btnClose;

    private ScheduledExecutorService pollingScheduler;
    private String currentTicketCode;

    @FXML
    public void initialize() {}

    @FXML
    private void onCloseClick(ActionEvent event) {
        stopPolling(); // 👈 Dừng polling khi đóng
        mainController.showPage(mainController.getLastPage());
    }

    @Override public void OnShowing() {}
    @Override public void Refresh() {}
    @Override public void OnAttached() {}
    @Override public void OnLogin() {}

    @Override
    public void OnLogout() {
        stopPolling(); // 👈 Dừng polling khi logout
    }

    public void placeHolder() {
        lblTicketCode.setText("N/A");
        lblCustomer.setText("N/A");
        lblMovieTitle.setText("N/A");
        lblDate.setText("N/A");
        lblTime.setText("N/A");
        lblRoom.setText("N/A");
        lblSeats.setText("N/A");
        lblPrice.setText("N/A");
        imgQRCode.setImage(null);
        lblStatus.setText("⏳ Chưa xác thực");
        lblStatus.getStyleClass().setAll("ticket-status-pending");
    }

    public void setData(TicketDetail ticketDetail) {
        if (ticketDetail == null) {
            placeHolder();
            return;
        }
        lblTicketCode.setText(ticketDetail.getTicketCode());
        lblCustomer.setText(ticketDetail.getCustomerName());
        lblMovieTitle.setText(ticketDetail.getMovieTitle());
        lblDate.setText(ticketDetail.getStartTime().toLocalDate().toString());
        lblTime.setText(ticketDetail.getStartTime().toLocalTime().toString());
        lblRoom.setText(ticketDetail.getRoomName());
        lblSeats.setText(ticketDetail.getSeatName());
        lblPrice.setText(String.format("%.2f", ticketDetail.getPrice()));
        loadBase64ToImageView(ticketDetail.getQrCodeBase64(), imgQRCode);

        // Cập nhật trạng thái
        if (ticketDetail.isUsed()) {
            lblStatus.setText("✅ Đã xác thực");
            lblStatus.getStyleClass().setAll("ticket-status-confirmed");
            stopPolling(); // 👈 Đã xác thực rồi → dừng polling
        } else {
            lblStatus.setText("⏳ Chưa xác thực");
            lblStatus.getStyleClass().setAll("ticket-status-pending");
        }
    }

    public void getData(String ticketCode) {
        this.currentTicketCode = ticketCode;
        System.out.println("📌 getData() gọi với ticketCode: " + ticketCode);
        Map<String, Object> params = Map.of("ticketCode", ticketCode);
        String token = mainController.getToken();

        HTTPService.sendFullRequestAsync("GET", "/api/Ticket/getTicketDetail", params, null, token)
                .thenAccept(response -> {
                    try {
                        if (response.statusCode() != 200 || response.body() == null || response.body().isEmpty()) {
                            Platform.runLater(() ->
                                    CineverseAlert.showToast("Không tìm thấy thông tin vé!", btnClose));
                            return;
                        }

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
                                .create();
                        TicketDetail ticketDetail = gson.fromJson(response.body(), TicketDetail.class);

                        Platform.runLater(() -> setData(ticketDetail));

                        // Chỉ polling nếu chưa xác thực
                        if (!ticketDetail.isUsed()) {
                            startPolling(ticketCode);
                        }

                    } catch (Exception e) {
                        Platform.runLater(() ->
                                CineverseAlert.showToast("Lỗi xử lý dữ liệu!", btnClose));
                        e.printStackTrace();
                    }
                });
    }
    // ============ POLLING ============
    private void startPolling(String ticketCode) {
        stopPolling(); // Dừng polling cũ nếu có

        pollingScheduler = Executors.newSingleThreadScheduledExecutor();
        pollingScheduler.scheduleAtFixedRate(() -> {
            checkTicketStatus(ticketCode);
        }, 5, 5, TimeUnit.SECONDS); // Gọi mỗi 5 giây
    }

    private void checkTicketStatus(String ticketCode) {
        System.out.println("🔄 Polling đang chạy: " + ticketCode); // 👈
        Map<String, Object> params = Map.of("ticketCode", ticketCode);
        String token = mainController.getToken();

        HTTPService.sendFullRequestAsync("GET", "/api/Ticket/getTicketDetail", params, null, token)
                .thenAccept(response -> {
                    System.out.println("📡 Polling status: " + response.statusCode()); // 👈
                    try {
                        if (response.statusCode() != 200) return;

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
                                .create();
                        TicketDetail ticketDetail = gson.fromJson(response.body(), TicketDetail.class);
                        System.out.println("🎫 Polling isUsed: " + ticketDetail.isUsed()); // 👈

                        if (ticketDetail.isUsed()) {
                            Platform.runLater(() -> {
                                setData(ticketDetail);
                                CineverseAlert.showToast("✅ Vé đã được xác thực!", btnClose);
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
    private void stopPolling() {
        if (pollingScheduler != null && !pollingScheduler.isShutdown()) {
            pollingScheduler.shutdown();
        }
    }

    // ============ HELPER ============
    public void loadBase64ToImageView(String base64Data, ImageView imageView) {
        try {
            if (base64Data == null || base64Data.isEmpty()) return;
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1];
            }
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            Image image = new Image(bis);
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Lỗi load ảnh: " + e.getMessage());
        }
    }
}