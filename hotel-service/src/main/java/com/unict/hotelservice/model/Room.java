package com.unict.hotelservice.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Room {

    String numero;
    List<Prenotazione> prenotazioni;
}
