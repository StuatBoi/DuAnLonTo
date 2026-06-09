package org.net.demo.DTO;

import java.time.LocalDateTime;

public class TicketDetail {

    private Long id;
    private String ticketCode;   
    private String qrCodeBase64; 
    private String customerName;
    private String movieTitle;
    private LocalDateTime startTime;
    private String roomName;
    private String seatName;    
    private String seatType;    
    private Double price;        
    private LocalDateTime bookingTime;

    public TicketDetail() {
    }

    public TicketDetail(Long id, String ticketCode, String qrCodeBase64, String customerName, String movieTitle, LocalDateTime startTime, String roomName, String seatName, String seatType, Double price, LocalDateTime bookingTime) {
        this.id = id;
        this.ticketCode = ticketCode;
        this.qrCodeBase64 = qrCodeBase64;
        this.customerName = customerName;
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.roomName = roomName;
        this.seatName = seatName;
        this.seatType = seatType;
        this.price = price;
        this.bookingTime = bookingTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getQrCodeBase64() {
        return qrCodeBase64;
    }

    public void setQrCodeBase64(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
}
