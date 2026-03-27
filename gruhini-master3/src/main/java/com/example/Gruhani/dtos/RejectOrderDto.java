package com.example.Gruhani.dtos;

/**
 * DTO for Seller to Reject an Order with a Reason
 * Used by: POST /seller/reject-order
 */
public class RejectOrderDto {
    private Long orderId;
    private String rejectionReason;   // Why the order is being rejected

    // Constructors
    public RejectOrderDto() {}

    public RejectOrderDto(Long orderId, String rejectionReason) {
        this.orderId = orderId;
        this.rejectionReason = rejectionReason;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public String toString() {
        return "RejectOrderDto{" +
                "orderId=" + orderId +
                ", rejectionReason='" + rejectionReason + '\'' +
                '}';
    }
}
