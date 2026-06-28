package org.net.demo;



public class ShowTime {
    private String id;              // ID phòng chiếu (Ví dụ: "P01", "ROOM_IMAX")
    private String address;         // Địa chỉ rạp
    private String cinemaName;      // Tên rạp chiếu (Ví dụ: "CGV Vincom", "BHD Star")
    private String startTime; // Thời gian bắt đầu chiếu (Bao gồm cả ngày và giờ)

    // Constructor không tham số (Default Constructor)
    public ShowTime() {
    }

    // Constructor có đầy đủ tham số (Parameterized Constructor)
    public ShowTime(String id, String address, String cinemaName, String startTime) {
        this.id = id;
        this.address = address;
        this.cinemaName = cinemaName;
        this.startTime = startTime;
    }

    // ================= GETTER AND SETTER =================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    // Hàm toString hỗ trợ việc in/kiểm tra dữ liệu nhanh khi debug
    @Override
    public String toString() {
        return "ShowTime{" +
                "id='" + id + '\'' +
                ", cinemaName='" + cinemaName + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}