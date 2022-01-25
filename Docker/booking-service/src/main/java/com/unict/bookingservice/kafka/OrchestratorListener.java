package com.unict.bookingservice.kafka;


import com.unict.bookingservice.controller.RequestHTTPTimer;
import com.unict.bookingservice.model.Booking;
import com.unict.bookingservice.model.BookingStatus;
import com.unict.bookingservice.repository.ReactiveBookingRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorListener {

    final Counter confermati = Metrics.counter("booking.confirmed");
    final Counter cancellati = Metrics.counter("booking.deleted");

    @Autowired
    ReactiveBookingRepository repository;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_MAIN_TOPIC}")
    private String mainTopic;

    @KafkaListener(topics="${KAFKA_MAIN_TOPIC}")
    public void listen(String message) {
        RequestHTTPTimer timer = RequestHTTPTimer.getInstance();
        System.out.println("Received message " + message);

        String[] messageParts = message.split("\\|");

        if (messageParts[0].equals("RoomReserved")) {

            String oid = messageParts[1];
            setBookingStatus(message, oid, BookingStatus.CONFIRMED, "BookingConfirmed|");
            confermati.increment();
            timer.stop(oid);
        }
        if (messageParts[0].equals("UserNotExists")) {
            String oid = messageParts[1];
            System.out.println("L'utente non esiste, setto lo status di booking con id: "+ oid + " a deleted");
            setBookingStatus(message, oid, BookingStatus.DELETED, "BookingDeleted|");
            cancellati.increment();
            timer.stop(oid);
        }
        if (messageParts[0].equals("RoomNotExists")) {
            String oid = messageParts[1];
            System.out.println("La stanza non esiste, setto lo status di booking con id: "+ oid + " a deleted");
            setBookingStatus(message, oid, BookingStatus.DELETED, "BookingDeleted|");
            cancellati.increment();
            timer.stop(oid);
        }
        if (messageParts[0].equals("RoomNotAvailable")) {
            String oid = messageParts[1];
            System.out.println("La stanza non e' disponibile, setto lo status di booking con id: "+ oid + " a deleted");
            setBookingStatus(message, oid, BookingStatus.DELETED, "BookingDeleted|");
            cancellati.increment();
            timer.stop(oid);
        }

    }

        private ResponseEntity<Mono<Booking>> setBookingStatus(String message, String id, BookingStatus status, String key) {

            ObjectId oid = new ObjectId(id);
            Booking old = repository.findById(oid).block();
            if (old == null)
                return new ResponseEntity<>(Mono.just(new Booking(null, null)), HttpStatus.NOT_FOUND);

            old.setBookingStatus(status);
            repository.save(old).subscribe();
            return new ResponseEntity<>(repository.save(old), HttpStatus.OK);

    }
}