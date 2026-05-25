package org.net.demo;

public class MovieInfo {
    private String id;
    private String movieTitle;
    private String description;
    private String genre;
    private String posterUrl;
    private String rating;
    

    // 1. Constructor có tham số
    public MovieInfo(String id, String movieTitle, String description, String genre, String posterUrl, String rating) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.description = description;
        this.genre = genre;
        this.posterUrl = posterUrl;
        this.rating = rating;
    }

    
    public MovieInfo() {
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    // Getter & Setter cho description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter & Setter cho genre
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Getter & Setter cho posterUrl
    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    // Getter & Setter cho rating
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
