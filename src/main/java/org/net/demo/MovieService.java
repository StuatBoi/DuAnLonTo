package org.net.demo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class MovieService {

    public static ArrayList<Movie> movies = new ArrayList<>();
    private static String endpoint="/api/feature/getMovies";

    public static void loadMovies() {

        HTTPService.sendRequestAsync("GET", endpoint, null, null).thenAccept(
            response->
            {
                ArrayList<Movie> savedMovies= parseJsonToMovieList(response);
                Platform.runLater(()->
            {
                movies.addAll(savedMovies);
                loadMovies();
            });
            }
        );
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