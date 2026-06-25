package org.net.demo.DTO;

import java.time.LocalDateTime;

public class TicketDTO {
    private String ticketCode;   // UUID
    private String customerName;
    private String movieTitle;
    private LocalDateTime startTime;
    private String roomName;
    private String seatName;     // VD: A1, B2
    private String seatType;     // VIP, NORMAL
    private Double price;
    private LocalDateTime bookingTime;

    // Getters and Setters
    public String getTicketCode() { return ticketCode; }
    public String getCustomerName() { return customerName; }
    public String getMovieTitle() { return movieTitle; }
    public LocalDateTime getStartTime() { return startTime; }
    public String getRoomName() { return roomName; }
    public String getSeatName() { return seatName; }
    public String getSeatType() { return seatType; }
    public Double getPrice() { return price; }
    public LocalDateTime getBookingTime() { return bookingTime; }


}
