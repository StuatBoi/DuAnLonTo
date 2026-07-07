package org.net.demo;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class HTTPService {

    private static String BASE_URL;
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

        static {
        Properties prop = new Properties();
        boolean loaded = false;

        // 1. Thử đọc file config.properties ở BÊN NGOÀI (ngay cạnh file .exe)
        try (java.io.FileInputStream externalInput = new java.io.FileInputStream("config.properties")) {
            prop.load(externalInput);
            BASE_URL = prop.getProperty("base.url");
            loaded = true;
            System.out.println("Đã nạp config.properties từ bên ngoài file exe. BASE_URL = " + BASE_URL);
        } catch (Exception e) {
            // Không tìm thấy file ở ngoài, bỏ qua để tìm trong JAR
        }

        // 2. Nếu bên ngoài không có, quay lại đọc file dự phòng bên TRONG JAR (Mặc định ban đầu)
        if (!loaded) {
            try (InputStream internalInput = HTTPService.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (internalInput != null) {
                    prop.load(internalInput);
                    BASE_URL = prop.getProperty("base.url");
                } else {
                    BASE_URL = "http://localhost:8080"; // Giá trị fallback cuối cùng
                }
                System.out.println("Không có file ngoài, dùng config.properties mặc định trong JAR. BASE_URL = " + BASE_URL);
            } catch (Exception ex) {
                BASE_URL = "http://localhost:8080";
                ex.printStackTrace();
            }
        }
    }


    /**
     * @param params Map chứa các tham số query (vd: Map.of("id", "1", "name", "test"))
     */
    public static CompletableFuture<String> sendRequestAsync(
            String method, 
            String endpoint, 
            Map<String, String> params,
            String jsonBody, 
            String token) {

        // Xử lý nối Param vào URL
        StringBuilder urlBuilder = new StringBuilder(BASE_URL).append(endpoint);
        if (params != null && !params.isEmpty()) {
            StringJoiner joiner = new StringJoiner("&", "?", "");
            params.forEach((k, v) -> {
                String encodedValue = URLEncoder.encode(v, StandardCharsets.UTF_8);
                joiner.add(k + "=" + encodedValue);
            });
            urlBuilder.append(joiner.toString());
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .header("Accept", "application/json");

        // Thêm Token
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        // Xử lý Method
        if ("POST".equalsIgnoreCase(method)) {
            builder.header("Content-Type", "application/json");
            builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : ""));
        } else if ("PUT".equalsIgnoreCase(method)) {
            builder.header("Content-Type", "application/json");
            builder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : ""));
        } else if ("DELETE".equalsIgnoreCase(method)) {
            builder.DELETE();
        } else {
            builder.GET();
        }

        Function<HttpResponse<String>, String> mapper = (HttpResponse<String> response) -> {
    if (response == null) {
        return ""; // xử lý null theo logic
    }
    return response.body();
};


        return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(mapper);//////HttpResponse::body
    }

  

public static CompletableFuture<HttpResponse<String>> sendFullRequestAsync(
        String method, 
        String endpoint, 
        Map<String, Object> params, 
        String jsonBody, 
        String token) {

    // Xử lý nối Param vào URL
    StringBuilder urlBuilder = new StringBuilder(BASE_URL).append(endpoint);
    if (params != null && !params.isEmpty()) {
        StringJoiner joiner = new StringJoiner("&", "?", "");
        
        params.forEach((k, v) -> {
            // Kiểm tra null để tránh lỗi NullPointerException nếu value truyền vào bị rỗng
            if (v != null) {
                
                String encodedValue = URLEncoder.encode(v.toString(), StandardCharsets.UTF_8);
                joiner.add(k + "=" + encodedValue);
            }
        });
        urlBuilder.append(joiner.toString());
    }

    HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(urlBuilder.toString()))
            .header("Accept", "application/json");

    // Thêm Token
    if (token != null && !token.isBlank()) {
        builder.header("Authorization", "Bearer " + token);
    }

    // Xử lý Method
    if ("POST".equalsIgnoreCase(method)) {
        builder.header("Content-Type", "application/json");
        builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : ""));
    } else if ("PUT".equalsIgnoreCase(method)) {
        builder.header("Content-Type", "application/json");
        builder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : ""));
    } else if ("DELETE".equalsIgnoreCase(method)) {
        builder.DELETE();
    } else {
        builder.GET();
    }

    // Trả về CompletableFuture chứa toàn bộ HttpResponse
    return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
}

public static String getBaseUrl()
{
    return HTTPService.BASE_URL;
}
}