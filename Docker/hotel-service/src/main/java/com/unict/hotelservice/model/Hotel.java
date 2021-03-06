package com.unict.hotelservice.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document
public class Hotel {

    private ObjectId _id;

    private String nome;
    private List<Room> stanze = new ArrayList<>();


    @PersistenceConstructor
    public Hotel(ObjectId _id, List<Room> stanze) {
        this._id = _id;
        this.stanze = stanze;
    }


    public Hotel(String room, String data_inizio, String data_fine, String hotelId) {
        this._id = new ObjectId(hotelId);
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

    public String getNome() {
        return nome;
    }

}
