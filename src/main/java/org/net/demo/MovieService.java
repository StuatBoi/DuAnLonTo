package org.net.demo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.application.Platform;
import javafx.scene.Node;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import org.net.demo.Service.LoadingOverlayManager;


public class MovieService {

    public static ArrayList<Movie> movies = new ArrayList<>();
    private static String endpoint="/api/feature/getMoviesWithPage";
    private static Node anchorNode;

    public static void loadMovies(Runnable callback) {
        LoadingOverlayManager.start(anchorNode);
        Map<String,Object> param= Map.of("page",0);
    HTTPService.sendFullRequestAsync("GET", endpoint,param, null,null).thenAcceptAsync(response -> {
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
    })
    ;
    
}
public static void setAnchorNode(Node anchorNode)
    {
       MovieService.anchorNode=anchorNode;
       System.out.println("set anchor node!!!!!!!!!!!!! : "+ MovieService.anchorNode);
    }
      
   
}