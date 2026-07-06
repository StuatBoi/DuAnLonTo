module org.net.demo {
    
    requires static lombok;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive com.google.gson;
    requires transitive javafx.graphics;
    requires com.google.zxing;
    requires jdk.httpserver;
    requires java.net.http;
    
    requires spring.core;
    requires spring.context;
    requires spring.messaging;
    requires jakarta.websocket.client;
    requires spring.websocket;
    requires com.fasterxml.jackson.databind;
    requires org.glassfish.tyrus.client;
    requires org.glassfish.tyrus.container.grizzly.client;
    
    
    // Nếu hàm handleFrame cần ép kiểu ngược hoặc can thiệp vào các Object nội bộ
    opens org.net.demo to javafx.fxml,com.google.gson,spring.core,spring.context;
    opens org.net.demo.DTO to com.google.gson;
    exports org.net.demo;
    exports org.net.demo.DTO;
}
