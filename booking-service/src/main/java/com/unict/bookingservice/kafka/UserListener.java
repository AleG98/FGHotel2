package com.unict.bookingservice.kafka;


import com.unict.bookingservice.model.BookingStatus;
import com.unict.bookingservice.repository.ReactiveBookingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserListener {

    @Autowired
    ReactiveBookingRepository repository;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_MAIN_TOPIC}")
    private String mainTopic;

    @KafkaListener(topics="${KAFKA_MAIN_TOPIC}")
    public void listen(String message) {
        System.out.println("Received message " + message);

        String[] messageParts = message.split("\\|");

        if (messageParts[0].equals("UserExists")) {
            String oid = messageParts[1];
            setOrderStatus(message, oid, BookingStatus.CONFIRMED, "OrderConfirmed|");
        }

        if (messageParts[0].equals("UserNotExists")) {
            String oid = messageParts[1];
            ObjectId ooid = new ObjectId(oid);
            setOrderStatus(message, oid, BookingStatus.DELETED, "OrderDeleted|");
        }

    }

    private void setOrderStatus(String message, String oid, BookingStatus status, String key) {
        repository.existsById(new ObjectId(oid)).flatMap(exists -> {
            if (exists) {
                repository.findById(new ObjectId(oid)).flatMap(booking -> {
                   booking.setBookingStatus(status);
                    return repository.save(booking);
                }).subscribe();

                kafkaTemplate.send(mainTopic, key + oid);
            } else {
                kafkaTemplate.send(mainTopic,"BadMessage||" + message);
            }
            return Mono.just(exists);
        }).subscribe();
    }

}