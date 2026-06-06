package org.net.demo;


public class Movie {
    private Long id;
    private String title;
    private String genre;
    private String image;
    private String releaseDate;

    public Movie(){

    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getImage() {
        return image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}