package org.net.demo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MovieService {

    public static ArrayList<Movie> movies = new ArrayList<>();

    public static void loadMovies() {

        try {
            System.out.println(
                    MovieService.class.getResourceAsStream(
                            "/org/net/demo/info.json"
                    )
            );
            InputStreamReader reader = new InputStreamReader(MovieService.class.getResourceAsStream("/org/net/demo/info.json"));

            Gson gson = new Gson();

            Type movieListType = new TypeToken<ArrayList<Movie>>(){}.getType();

            movies = gson.fromJson(reader, movieListType);
            System.out.println(movies.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}