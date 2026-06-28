package org.net.demo;

public class Movie {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String rating;
    private String image;
    private String releaseDate; // Kiểu String giúp Gson bóc tách mượt mà từ API
    private Integer duration;

    // Constructor mặc định bắt buộc cho Gson
    public Movie() {}

    // --- CÁC HÀM GETTER ---
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public String getDescription() { return description; }
    public String getGenre() { return genre; }
    public String getRating() { return rating; }
    public Integer getDuration() { return duration; }

    // HÀM BỊ THIẾU: Thêm vào đây để lấy ngày phát hành ra UI
    public String getReleaseDate() { return releaseDate; }

    // --- CÁC HÀM SETTER (Nên có phòng trường hợp bạn cần gán dữ liệu tay) ---
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setImage(String image) { this.image = image; }
    public void setDescription(String description) { this.description = description; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setRating(String rating) { this.rating = rating; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}