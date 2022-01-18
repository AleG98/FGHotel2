package com.unict.hotelservice.model;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import java.time.LocalDate;
@Document
public class Prenotazione {
    ObjectId bookingId;
    LocalDate data_inizio;
    LocalDate data_fine;

    @PersistenceConstructor
    public Prenotazione(LocalDate data_inizio, LocalDate data_fine, ObjectId bookingId) {
        this.data_inizio = data_inizio;
        this.data_fine = data_fine;
        this.bookingId=bookingId;
    }


    public Prenotazione(LocalDate data_inizio, LocalDate data_fine) {
        this.data_inizio = data_inizio;
        this.data_fine = data_fine;
    }

    public ObjectId getBookingId() {
        return bookingId;
    }

    public void setBookingId(ObjectId bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getData_inizio() {
        return data_inizio;
    }

    public void setData_inizio(LocalDate data_inizio) {
        this.data_inizio = data_inizio;
    }

    public LocalDate getData_fine() {
        return data_fine;
    }

    public void setData_fine(LocalDate data_fine) {
        this.data_fine = data_fine;
    }
}

