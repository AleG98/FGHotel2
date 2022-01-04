package com.unict.bookingservice.controller;
import com.unict.bookingservice.model.Booking;
import com.unict.bookingservice.repository.ReactiveBookingRepository;


import org.springframework.beans.factory.annotation.Autowired;
public class BookingService {
        @Autowired
        private static ReactiveBookingRepository bookingRepository;

        public BookingService(ReactiveBookingRepository bookingRepository) {
            this.bookingRepository = bookingRepository;
        }

        public static Booking createBooking(String idHotel, String roomId, String datebegin, String dateend, String userId)  {
            Booking booking = new Booking(idHotel, roomId, datebegin, dateend, userId);
            bookingRepository.save(booking);
            return booking;
        }

        public void approveOrder(Long orderId) {
         //   ReactiveBookingRepository.findById(orderId).get().approve();
        }
/*
        public void rejectOrder(Long orderId, RejectionReason rejectionReason) {
           // bookingRepository.findById(orderId).get().reject(rejectionReason);
        }*/
    }

