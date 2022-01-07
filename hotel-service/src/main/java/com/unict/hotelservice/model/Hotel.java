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

    @Id
    private ObjectId _id;
    private ObjectId idHotel;
    private List<Room> stanze = new ArrayList<>();

    public Hotel() {
    }
}
