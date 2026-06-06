package org.net.demo;


import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class HTTPService {

    private static String BASE_URL;
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // Khối static để đọc file cấu hình khi class được load
    static {
        try (InputStream input = HTTPService.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Xin lỗi, không tìm thấy file config.properties");
                BASE_URL = "http://localhost:8080"; // Giá trị mặc định nếu lỗi
            } else {
                prop.load(input);
                BASE_URL = prop.getProperty("base.url");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param endpoint Đuôi chức năng (vd: /login, /users)
     */
    public static CompletableFuture<String> sendRequestAsync(String method, String endpoint, String jsonBody, String token) {
        
        // Cộng BASE_URL với endpoint
        String fullUrl = BASE_URL + endpoint;

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Accept", "application/json");

        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        if ("POST".equalsIgnoreCase(method)) {
            builder.header("Content-Type", "application/json");
            String body = (jsonBody != null) ? jsonBody : ""; 
            builder.POST(HttpRequest.BodyPublishers.ofString(body));
        } else {
            builder.GET();
        }

        return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}
