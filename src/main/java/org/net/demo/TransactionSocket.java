package org.net.demo;

import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TransactionSocket {

    private final Consumer<String> onSuccess;
    private final Consumer<String> onFailure;
    private final Consumer<String> onError;
    private final CountDownLatch messageLatch;
    
    private StompSession stompSession;
    private volatile boolean isConnected = false;

    public TransactionSocket(Consumer<String> onSuccess, Consumer<String> onFailure, Consumer<String> onError) {
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
        this.onError = onError;
        this.messageLatch = new CountDownLatch(1);
    }

    /**
     * Kết nối thuần Socket đến Server và đăng ký nhận tín hiệu theo OrderId
     */
    public void connect(@NonNull String serverUrl, String username, String orderId) {
        try {
            // Do phía Server bạn có cấu hình .withSockJS(), nên Client cần khởi tạo SockJsClient để handshake thành công
            List<Transport> transports = new ArrayList<>(1);
            transports.add(new WebSocketTransport(new StandardWebSocketClient()));
            WebSocketClient sockJsClient = new SockJsClient(transports);

            WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
            // Sử dụng Jackson Message Converter để tự động ép kiểu JSON từ Server về Map.class
            stompClient.setMessageConverter(new org.springframework.messaging.converter.MappingJackson2MessageConverter());

            System.out.println("Connecting to WebSocket server: " + serverUrl);
            
            // serverUrl ví dụ: "http://localhost:8080/ws-payment"
            stompSession = stompClient.connectAsync(serverUrl, new StompSessionHandlerAdapter() {
                @Override
                public void handleException(@NonNull StompSession session, @Nullable StompCommand command, @NonNull StompHeaders headers,@NonNull byte[] payload,@NonNull Throwable exception) {
                    if (onError != null) onError.accept(exception.getMessage());
                }
            }).get(10, TimeUnit.SECONDS); // Timeout kết nối sau 10s

            isConnected = true;

            // ĐĂNG KÝ LẮNG NGHE (SUBSCRIBE) THUẦN SOCKET
            // Server cứ đẩy tín hiệu vào kênh nào, Client sẽ lập tức nhận được tại đây
            stompSession.subscribe("/topic/payment/"+"ORD_" + orderId, new StompFrameHandler() {
                @Override
                public @NonNull Type getPayloadType(@NonNull StompHeaders headers) {
                    return Map.class; // Định dạng dữ liệu nhận về là một Map
                }

                @Override
                @SuppressWarnings("unchecked")
                public void handleFrame(@NonNull StompHeaders headers, @Nullable Object payload) {
                    
                    if (payload instanceof Map) {
        

                    Map<String, Object> data = (Map<String, Object>) payload;
                    String status = (String) data.get("status");
                    System.out.println("!!!!!FRAME RECEIVED!");

                    // Bắn tín hiệu về lại các hàm callback xử lý giao diện JavaFX
                    if ("SUCCESS".equals(status)) {
                        if (onSuccess != null) onSuccess.accept(String.valueOf(data.get("message")));
                    } else {
                        if (onFailure != null) onFailure.accept(String.valueOf(data.get("message")));
                    }
                }
                else {
                      // Xử lý khi dữ liệu nhận về bị null hoặc không đúng cấu hình JSON Map
                      System.out.println("Cảnh báo: Payload rỗng hoặc sai định dạng!");
                      if (onFailure != null) onFailure.accept("Dữ liệu phản hồi từ máy chủ không hợp lệ");
                }

                    // Nhả chốt chặn (latch) ngay lập tức vì đã nhận được kết quả cuối cùng
                    messageLatch.countDown();
                
                }
            });

            System.out.println("Subscribed to topic: /topic/payment/" + orderId);

        } catch (Exception e) {
            isConnected = false;
            if (onError != null) {
                onError.accept("Không thể kết nối Socket: " + e.getMessage());
            }
            messageLatch.countDown();
        }
    }

    /**
     * Chặn luồng chính để chờ tín hiệu (Giữ nguyên cấu trúc logic ở Controller JavaFX của bạn)
     */
    public boolean waitForMessage(int timeoutSeconds) {
        try {
            return messageLatch.await(timeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void close() {
        isConnected = false;
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
            System.out.println("WebSocket connection closed gracefully.");
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}