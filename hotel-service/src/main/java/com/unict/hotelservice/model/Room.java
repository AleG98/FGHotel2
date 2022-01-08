package com.unict.hotelservice.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
public class Room {

    String numero;
    List<Prenotazione> prenotazioni = new ArrayList<>();

    public Room(String numero, String data_inizio, String data_fine) {
        this.numero = numero;
        Prenotazione prenotazione = new Prenotazione(LocalDate.parse(data_inizio), LocalDate.parse(data_fine));
        prenotazioni.add(prenotazione);
    }
}
