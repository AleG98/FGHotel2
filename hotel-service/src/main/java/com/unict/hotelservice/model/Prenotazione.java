package com.unict.hotelservice.model;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import java.time.LocalDate;
@Document
public class Prenotazione {
    ObjectId idUtente;
    LocalDate data_inizio;
    LocalDate data_fine;

    @PersistenceConstructor
    public Prenotazione(LocalDate data_inizio, LocalDate data_fine, ObjectId idUtente) {
        this.data_inizio = data_inizio;
        this.data_fine = data_fine;
        this.idUtente=idUtente;
    }

    public ObjectId getIdUtente() {
        return idUtente;
    }
    public Prenotazione(LocalDate data_inizio, LocalDate data_fine) {
        this.data_inizio = data_inizio;
        this.data_fine = data_fine;
    }
    public void setIdUtente(ObjectId idUtente) {
        this.idUtente = idUtente;
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

