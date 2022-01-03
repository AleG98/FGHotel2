package com.unict.bookingservice.repository;

import com.unict.bookingservice.model.Booking;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReactiveBookingRepository extends ReactiveCrudRepository<Booking, ObjectId> {
}