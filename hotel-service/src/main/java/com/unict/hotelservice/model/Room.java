package com.unict.hotelservice.model;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
public class Room {

    String numero;
    List<Prenotazione> prenotazioni = new ArrayList<>();

    @PersistenceConstructor
    public Room(String numero, List<Prenotazione> prenotazioni) {
        this.numero = numero;
        this.prenotazioni = prenotazioni;
    }

    public Room(String numero, String data_inizio, String data_fine) {
        this.numero = numero;
        Prenotazione prenotazione = new Prenotazione(LocalDate.parse(data_inizio), LocalDate.parse(data_fine));
        prenotazioni.add(prenotazione);
    }

    public Room(String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }
}
