package com.unict.hotelservice.repository;

import com.unict.hotelservice.model.Hotel;
import reactor.core.publisher.Mono;

public interface CustomHotelRepository {

    Mono<Hotel> addNewRoom (String hotelId, String room,String data_inizio, String data_fine);


}
