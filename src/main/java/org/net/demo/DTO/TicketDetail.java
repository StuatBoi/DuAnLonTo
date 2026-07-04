package org.net.demo.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private boolean used;       // Map với "used" (true/false) từ JSON
    private LocalDateTime usedAt;

    

    
}
