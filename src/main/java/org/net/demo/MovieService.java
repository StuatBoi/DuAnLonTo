package org.net.demo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MovieService {

    public static ArrayList<Movie> movies = new ArrayList<>();
    private static String endpoint="/api/feature/getMovies";

    public static void loadMovies(Runnable callback) {
    HTTPService.sendRequestAsync("GET", endpoint,null, null, null).thenAccept(response -> {
        ArrayList<Movie> savedMovies = parseJsonToMovieList(response);
        
        Platform.runLater(() -> {
            movies.clear();
            movies.addAll(savedMovies);
            
            // Tải xong rồi mới báo cho Controller biết
            if (callback != null) {
                callback.run();
            }
        });
    });
}
       private static ArrayList<Movie> parseJsonToMovieList(String json) {
        try {
            // Định nghĩa kiểu dữ liệu là ArrayList<Movie> để Gson hiểu
            Type movieListType = new TypeToken<ArrayList<Movie>>(){}.getType();
            Gson gson=new Gson();
            return gson.fromJson(json, movieListType);
        } catch (Exception e) {
            System.err.println("Lỗi parse JSON bằng Gson: " + e.getMessage());
            return new ArrayList<>();
        }
    }

   
}