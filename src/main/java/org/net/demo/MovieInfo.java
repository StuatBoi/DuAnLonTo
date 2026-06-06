package org.net.demo;

public class MovieInfo {
    private String id;
    private String description;
    private String genre;
    private String image;
    private String rating;
    private String title;
    private String releaseDate;
    

    // 1. Constructor có tham số
    public MovieInfo(String id, String title, String description, String genre, String image, String rating,String releaseDate) {
        this.id = id;
        this.description = description;
        this.genre = genre;
        this.image = image;
        this.rating = rating;
        this.title=title;
        this.releaseDate=releaseDate;
    }

    
    public MovieInfo() {
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Getter & Setter cho rating
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    // Getter & Setter cho title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter & Setter cho releaseDate
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
