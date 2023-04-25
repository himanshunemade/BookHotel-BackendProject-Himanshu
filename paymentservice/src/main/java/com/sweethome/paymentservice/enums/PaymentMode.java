package com.sweethome.paymentservice.enums;

public enum PaymentMode {
    CARD("CARD"),
    UPI("UPI");

    private final String value;
    PaymentMode(String value) {
        this.value = value;
    }
}
