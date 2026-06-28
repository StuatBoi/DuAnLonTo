package org.net.demo.DTO;

public class BookingPaymentResponse {
    private Long orderId;
    private String paymentUrl;

    /**
     * No-arg constructor
     */
    public BookingPaymentResponse() {
    }

    /**
     * All-args constructor
     */
    public BookingPaymentResponse(Long orderId, String paymentUrl) {
        this.orderId = orderId;
        this.paymentUrl = paymentUrl;
    }

    /**
     * Getters and Setters
     */
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    @Override
    public String toString() {
        return "BookingPaymentResponse{" +
                "orderId=" + orderId +
                ", paymentUrl='" + paymentUrl + '\'' +
                '}';
    }
}
