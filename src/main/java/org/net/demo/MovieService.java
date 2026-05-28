package org.net.demo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    public static void loadMovies() {

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/movies"))
                            .build();

            HttpResponse<String> response =
                    client.send(request,
                            HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            System.out.println(json);

            Gson gson = new Gson();

            Type type =
                    new TypeToken<ArrayList<Movie>>(){}.getType();

            movies = gson.fromJson(json, type);

            System.out.println(movies.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}