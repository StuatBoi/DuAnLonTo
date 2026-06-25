package org.net.demo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class MovieService {

    public static ArrayList<Movie> movies = new ArrayList<>();
    private static String endpoint="/api/feature/getMoviesWithPage";

    public static void loadMovies(Runnable callback) {
        Map<String,Object> param= Map.of("page",0);
    HTTPService.sendFullRequestAsync("GET", endpoint,param, null,null).thenAccept(response -> {
        Gson gson= new Gson();
        Type pageResponseType = new TypeToken<PageResponse<Movie>>(){}.getType();
        PageResponse<Movie> mPageResponse= gson.fromJson(response.body(), pageResponseType);
        
        
        Platform.runLater(() -> {
            movies.clear();
            movies.addAll(mPageResponse.getContent());
            
            // Tải xong rồi mới báo cho Controller biết
            if (callback != null) {
                callback.run();
            }
        });
    });
}
      
   
}