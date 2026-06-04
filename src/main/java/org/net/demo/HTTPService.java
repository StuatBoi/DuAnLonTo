package org.net.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HTTPService {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();


    public static CompletableFuture<String> sendRequestAsync(String method, String url, String jsonBody, String token) {
        
        // 1. Khởi tạo Builder cơ bản
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json");

        // 2. Nếu có token thì mới gắn vào Header
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        // 3. Kiểm tra Phương thức là GET hay POST
        if ("POST".equalsIgnoreCase(method)) {
            builder.header("Content-Type", "application/json");
            // Nếu jsonBody null thì gửi chuỗi rỗng để tránh lỗi
            String body = (jsonBody != null) ? jsonBody : ""; 
            builder.POST(HttpRequest.BodyPublishers.ofString(body));
        } else {
            builder.GET(); // Mặc định là GET
        }

        // 4. Gửi và trả về kết quả
        return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
    
}
