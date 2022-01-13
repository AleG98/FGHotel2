package com.unict.bookingservice.model;

import java.io.Serializable;

public enum BookingStatus implements Serializable {
    PENDING,
    CONFIRMED,
    DELETED
}