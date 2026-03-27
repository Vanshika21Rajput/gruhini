package com.example.Gruhani.dtos;

/**
 * DTO for Seller to Accept an Order with Delivery Details
 * Used by: POST /seller/accept-order
 */
public class AcceptOrderDto {
    private Long orderId;
    private String deliveryTime;      // e.g., "Today at 8:00 PM"
    private String deliveryMethod;    // SELLER_DELIVERY, CUSTOMER_PICKUP, THIRD_PARTY
    private String deliveryNotes;     // Optional special instructions

    // Constructors
    public AcceptOrderDto() {}

    public AcceptOrderDto(Long orderId, String deliveryTime, String deliveryMethod, String deliveryNotes) {
        this.orderId = orderId;
        this.deliveryTime = deliveryTime;
        this.deliveryMethod = deliveryMethod;
        this.deliveryNotes = deliveryNotes;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(String deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    @Override
    public String toString() {
        return "AcceptOrderDto{" +
                "orderId=" + orderId +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", deliveryNotes='" + deliveryNotes + '\'' +
                '}';
    }
}
