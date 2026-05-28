package org.net.demo;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.net.InetSocketAddress;

public class servertest {

    public static void main(String[] args) throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080), 0);

        server.createContext("/movies", (HttpExchange exchange) -> {

            String json =
                    """
                    [
                        {
                            "title":"Avengers: Endgame",
                            "genre":"Action",
                            "releaseDate":"2019-04-26",
                            "image":"https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg"
                        },
                        {
                            "title":"Batman Begins",
                            "genre":"Action",
                            "releaseDate":"2005-06-15",
                            "image":"https://image.tmdb.org/t/p/w500/4MpN4kIEqUjW8OPtOQJXlTdHiJV.jpg"
                        },
                        {
                            "title":"Interstellar",
                            "genre":"Sci-Fi",
                            "releaseDate":"2014-11-07",
                            "image":"https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg"
                        },
                        {
                            "title":"Titanic",
                            "genre":"Romance",
                            "releaseDate":"1997-12-19",
                            "image":"https://image.tmdb.org/t/p/w500/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg"
                        },
                        {
                            "title":"Joker",
                            "genre":"Drama",
                            "releaseDate":"2019-10-04",
                            "image":"https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg"
                        }
                    ]
                    """;

            exchange.getResponseHeaders()
                    .add("Content-Type", "application/json");

            exchange.sendResponseHeaders(200, json.length());

            OutputStream os = exchange.getResponseBody();

            os.write(json.getBytes());

            os.close();
        });

        server.start();

        System.out.println("Server running...");
    }
}