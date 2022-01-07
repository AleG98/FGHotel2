package com.unict.hotelservice.repository;

import com.unict.hotelservice.model.Hotel;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReactiveHotelRepository extends ReactiveCrudRepository<Hotel, ObjectId> {
}
