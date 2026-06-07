package org.net.demo.DTO;

import java.util.List;

public class TicketBookingRequest {
    
    
    private Long showtimeId;

   
    private List<Long> seatIds;

    public TicketBookingRequest() {
    }

    public TicketBookingRequest(Long showtimeId, List<Long> seatIds) {
        this.showtimeId = showtimeId;
        this.seatIds = seatIds;
    }   
    public Long getShowtimeId() {
        return showtimeId;
    }
    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }
    public List<Long> getSeatIds() {
        return seatIds;
    }
    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }
    
    
}