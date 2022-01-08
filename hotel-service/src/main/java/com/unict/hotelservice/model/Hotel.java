package com.unict.hotelservice.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
public class Hotel {

    //@Id
    //private ObjectId _id;
    @Id
    private ObjectId idHotel;
    private List<Room> stanze = new ArrayList<>();

    @JsonCreator
    public Hotel(String room, String data_inizio, String data_fine, String hotelId) {
        this.idHotel = new ObjectId(hotelId);
        Room stanza = new Room(room,data_inizio,data_fine);
        stanze.add(stanza);
    }


    public String get_id_string() {
        return idHotel.toHexString();
    }

    /*
    public boolean isPrenotazionePossibile(int codVeicolo, Date inizio, Date fine) {
        boolean esito = true;
        Noleggio n;
        for(int i=0; i<numNoleggi; i++) {
            //si procura la prenotazione i-sima
            n = noleggi[i];
            //se essa riguarda il veicolo in oggetto
            if(n.getCodVeicolo() == codVeicolo) {
                //ipotizza la prenotazione non possibile
                esito = false;
                //verifica la compatibilità dei periodi
                if(fine.before(n.getDataInizio()) || inizio.after(n.getDataFine()))
                    esito = true;
                else
                    break;
            }
        }
        return esito;
    }

    */


}