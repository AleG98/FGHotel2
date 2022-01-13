package com.unict.hotelservice.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
public class Hotel {

    //@Id
    private ObjectId _id;
    //@Id
    //private ObjectId idHotel;
    private String nome;
    private List<Room> stanze = new ArrayList<>();


    @PersistenceConstructor
    public Hotel(ObjectId _id, List<Room> stanze) {
        //this.idHotel = idHotel;
        this._id = _id;
        this.stanze = stanze;
    }


    public Hotel(String room, String data_inizio, String data_fine, String hotelId) {
        this._id = new ObjectId(hotelId);
        //this.idHotel = new ObjectId(hotelId);
        Room stanza = new Room(room,data_inizio,data_fine);
        stanze.add(stanza);
    }

    @JsonCreator
    public Hotel(String nome, String stanze) {
        System.out.println("N stanze: " + stanze);
        int n = Integer.parseInt(stanze);
        this.nome = nome;
        for (int i = 1; i <= n; i++ )
            this.stanze.add(new Room(String.valueOf(i)));
    }

    public List<Room> getStanze() {
        return stanze;
    }

    public void setStanze(List<Room> stanze) {
        this.stanze = stanze;
    }
    @JsonGetter("_id")
    public String get_id_string() { return _id.toHexString(); }

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
