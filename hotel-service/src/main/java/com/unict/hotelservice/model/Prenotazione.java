package com.unict.hotelservice.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Document
public class Prenotazione {
    LocalDate data_inizio;
    LocalDate data_fine;

    public Prenotazione(LocalDate data_inizio, LocalDate data_fine) {
        this.data_inizio = data_inizio;
        this.data_fine = data_fine;
    }
}

