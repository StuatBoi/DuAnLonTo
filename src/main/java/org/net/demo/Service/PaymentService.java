package org.net.demo.Service;

import org.net.demo.TransactionSocket;
import org.springframework.lang.NonNull;
import org.net.demo.CineverseAlert;
import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

/**
 * JavaFX Payment Service
 * Handles payment confirmation by connecting to backend via TransactionSocket
 * Uses CineverseAlert for toast notifications
 */
public class PaymentService {
    
    private TransactionSocket socket;
    private String serverUrl;
    private String username;
    private Long orderId;
    private Runnable onPaymentSuccess;
    private Runnable onPaymentFailure;
    private Region currentRegion; // For CineverseAlert

    /**
     * Constructor
     * @param serverUrl Backend server URL (e.g., "http://localhost:8080")
     * @param username Current logged-in username
     * @param currentRegion JavaFX Region for alert display (e.g., BorderPane, VBox, etc.)
     */
    public PaymentService(@NonNull String serverUrl, String username, Region currentRegion) {
        this.serverUrl = serverUrl;
        this.username = username;
        this.currentRegion = currentRegion;
    }
    
    /**
     * Start payment confirmation listener
     * @param orderId The order ID to monitor
     * @param onSuccess Callback when payment succeeds
     * @param onFailure Callback when payment fails
     */
    public void startPaymentConfirmation(Long orderId, Runnable onSuccess, Runnable onFailure) {
        this.orderId = orderId;
        this.onPaymentSuccess = onSuccess;
        this.onPaymentFailure = onFailure;
        
        // Create socket with callbacks
        socket = new TransactionSocket(
            // Success callback
            response -> {
                System.out.println("✓ Payment successful! Order: " + response);
                Platform.runLater(() -> {
                    CineverseAlert.show("Thanh toán thành công", 
                        "Đơn hàng #" + response + " đã được xác nhận!\nVé của bạn đã được cấp phát.", 
                        currentRegion);
                    if (onPaymentSuccess != null) {
                        onPaymentSuccess.run();
                    }
                });
            },
            // Failure callback
            errorMsg -> {
                System.out.println("✗ Payment failed: " + errorMsg);
                Platform.runLater(() -> {
                    CineverseAlert.show("Thanh toán thất bại", 
                        "Lỗi: " + errorMsg, 
                        currentRegion);
                    if (onPaymentFailure != null) {
                        onPaymentFailure.run();
                    }
                });
            },
            // Connection error callback
            error -> {
                System.out.println("⚠ Connection error: " + error);
                Platform.runLater(() -> {
                    CineverseAlert.showToast("⚠ Lỗi kết nối: " + error, currentRegion);
                    if (onPaymentFailure != null) {
                        onPaymentFailure.run();
                    }
                });
            }
        );
        
        // Start polling in background thread
        new Thread(() -> {
            try {
                System.out.println("Connecting to payment service...");
                Platform.runLater(()->CineverseAlert.showToast("Đang kết nối đến dịch vụ thanh toán...", currentRegion));
                
                final String serverUrl2 = serverUrl;
                if (serverUrl2 != null) {
                    socket.connect(serverUrl2, username, String.valueOf(orderId));
                } else {
                    Platform.runLater(()->CineverseAlert.showToast("null server url", currentRegion));
                }
                
                // Wait for payment confirmation (up to 10 minutes)
                boolean received = socket.waitForMessage(600);
                
                if (!received) {
                    Platform.runLater(() -> {
                        CineverseAlert.show("Hết thời gian chờ", 
                            "Xác nhận thanh toán đã hết thời gian (5 phút).\n" +
                            "Vui lòng kiểm tra trạng thái thanh toán trong tài khoản của bạn.",
                            currentRegion);
                    });
                }
            } catch (Exception e) {
                System.err.println("Payment confirmation error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                socket.close();
            }
        }).start();
    }
    
    /**
     * Create a waiting UI component
     * @return VBox with loading indicator
     */
    
    
    /**
     * Stop payment confirmation listener
     */
    public void stopPaymentConfirmation() {
        if (socket != null) {
            socket.close();
        }
    }
    
    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }
    public Long getOrderId()
    {
        return this.orderId;
    }
}
