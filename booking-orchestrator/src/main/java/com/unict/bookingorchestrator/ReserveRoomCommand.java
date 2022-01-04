package com.unict.bookingorchestrator;

import io.eventuate.examples.tram.sagas.ordersandcustomers.commondomain.Money;
import io.eventuate.tram.commands.common.Command;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class ReserveRoomCommand implements Command {
    private ObjectId hotelId;
    private ObjectId roomId;
    private LocalDate datebegin;
    private LocalDate dateend;

    public ReserveRoomCommand(String hotelId, String roomId, String datebegin, String dateend) { //aggiustare localDate
        this.hotelId = new ObjectId(hotelId);
        this.roomId = new ObjectId(roomId);
        this.datebegin = LocalDate.now(); //aggiustare
        this.dateend = LocalDate.now();
    }

    public ObjectId getHotelId() {
        return hotelId;
    }

    public void setHotelId(ObjectId hotelId) {
        this.hotelId = hotelId;
    }

    public ObjectId getRoomId() {
        return roomId;
    }

    public void setRoomId(ObjectId roomId) {
        this.roomId = roomId;
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
}
