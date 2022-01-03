package com.unict.bookingservice.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import java.util.Date;

public class Booking{
    @Id
    private ObjectId _id;
    private ObjectId idHotel;
    private ObjectId roomId;
    private Date date;
    private ObjectId userId;
    private BookingStatus bookingStatus;

    @JsonCreator
    public Booking(ObjectId idHotel, ObjectId roomId, Date date, ObjectId userId) {
        this._id = new ObjectId();
        this.idHotel = idHotel;
        this.roomId = roomId;
        this.date = date;
        this.userId = userId;
        this.bookingStatus= BookingStatus.PENDING;
    }
    @JsonCreator
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

    public ObjectId getRoomId() {
        return roomId;
    }

    public void setRoomId(ObjectId roomId) {
        this.roomId = roomId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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



    @JsonGetter("_id")
    public String get_idHotel_string() {
        return idHotel.toHexString();
    }

    @JsonGetter("userId")
    public String get_user_string() {
        return userId.toHexString();
    }

    @JsonGetter("roomId")
    public String get_roomId_string() {
        return roomId.toHexString();
    }
    @JsonGetter("Date")
    public String get_Date_string() {
        return date.toString();
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}