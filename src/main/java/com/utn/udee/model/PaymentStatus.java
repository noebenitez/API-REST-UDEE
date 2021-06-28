package com.utn.udee.model;

public enum PaymentStatus {
    PAID("Paid"),
    UNPAID("Not Paid");

    private String paymentStatus;

    PaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
