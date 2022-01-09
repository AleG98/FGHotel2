package com.unict.bookingservice.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Date;

public class Booking{
    @Id
    private ObjectId _id;
    private ObjectId idHotel;
    private String room;  //le stanze vanno da 1 a 10
    private LocalDate datebegin;
    private LocalDate dateend;
    private ObjectId userId;
    private BookingStatus bookingStatus;
/*
    @JsonCreator
    public Booking(ObjectId idHotel, ObjectId roomId, Date date, ObjectId userId) {
        this._id = new ObjectId();
        this.idHotel = idHotel;
        this.roomId = roomId;
        //this.date = date;
        this.userId = userId;
        this.bookingStatus= BookingStatus.PENDING;
    }

 */
    @JsonCreator
    public Booking(String idHotel, String room, String datebegin, String dateend, String userId){
        this._id = new ObjectId();
        this.bookingStatus= BookingStatus.PENDING;
        this.idHotel = new ObjectId(idHotel);
        this.room =  room;
        this.datebegin= LocalDate.parse(datebegin);
        this.dateend= LocalDate.parse(dateend);
        this.userId =  new ObjectId(userId);
        this.bookingStatus= BookingStatus.PENDING;
        System.out.println("Booking creato da costruttore");
    }
    public Booking(Object idHotel, Object roomId) {

    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(ObjectId idHotel) {
        this.idHotel = idHotel;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDate getDatebegin() {
        return datebegin;
    }

    public void setDatebegin(LocalDate datebegin) {
        this.datebegin = datebegin;
    }

    public LocalDate getDateend() {
        return dateend;
    }

    public void setDateend(LocalDate dateend) {
        this.dateend = dateend;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }



    @JsonGetter("idHotel")
    public String get_idHotel_string() {
        return idHotel.toHexString();
    }

    @JsonGetter("userId")
    public String get_user_string() {
        return userId.toHexString();
    }

    @JsonGetter("room")
    public String get_room_string() { return room; }

    @JsonGetter("_id")
    public String get_Id_string() {
        return _id.toHexString();
    }

    @JsonGetter("Dateend")
    public String get_Dateend_string() {
        return dateend.toString();
    }

    @JsonGetter("Datebegin")
    public String get_Datebegin_string() {
        return datebegin.toString();
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}