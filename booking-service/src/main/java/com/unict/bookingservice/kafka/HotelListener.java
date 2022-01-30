package com.unict.bookingservice.kafka;

import com.unict.bookingservice.model.Booking;
import com.unict.bookingservice.model.BookingStatus;
import com.unict.bookingservice.repository.ReactiveBookingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class HotelListener {

    @Autowired
    ReactiveBookingRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_CHECKOUT_TOPIC}")
    private String checkoutTopic;

    @KafkaListener(topics="${KAFKA_CHECKOUT_TOPIC}")
    public void listen(String message) {

        System.out.println("Received message " + message);
        String[] messageParts = message.split("\\|");

        if (messageParts[0].equals("Checkout")) {

            ObjectId oid = new ObjectId(messageParts[1]);
            Booking booking = repository.findById(oid).block();
            if (booking == null)
                kafkaTemplate.send(checkoutTopic, "BookingNotFound|" + messageParts[1]);
            else if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                kafkaTemplate.send(checkoutTopic, "CheckoutInfo|" + booking.get_user_string() +"|"+ booking.get_idHotel_string() +"|"+ booking.get_Datebegin_string() +"|"+ booking.getDateend());
            } else if (!(booking.getBookingStatus() == BookingStatus.CONFIRMED)) {
                kafkaTemplate.send(checkoutTopic, "BookingNotConfirmed|" + messageParts[1]);
            }
        }
    }

}
