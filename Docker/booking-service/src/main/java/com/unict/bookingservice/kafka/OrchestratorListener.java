package com.unict.bookingservice.kafka;


import com.mongodb.client.MongoClients;
import com.unict.bookingservice.model.Booking;
import com.unict.bookingservice.model.BookingStatus;
import com.unict.bookingservice.repository.ReactiveBookingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorListener {

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

        if (messageParts[0].equals("RoomReserved")) {
            String oid = messageParts[1];
            setBookingStatus(message, oid, BookingStatus.CONFIRMED, "BookingConfirmed|");
        }
        if (messageParts[0].equals("UserNotExists")) {
            String oid = messageParts[1];
            //ObjectId ooid = new ObjectId(oid);
            System.out.println("L'utente non esiste, setto lo status di booking con id: "+ oid + " a deleted");
            setBookingStatus(message, oid, BookingStatus.DELETED, "BookingDeleted|");
        }
        if (messageParts[0].equals("RoomNotExists")) {
            String oid = messageParts[1];
            //ObjectId ooid = new ObjectId(oid);
            System.out.println("La stanza non esiste, setto lo status di booking con id: "+ oid + " a deleted");

            setBookingStatus(message, oid, BookingStatus.DELETED, "BookingDeleted|");
        }
        if (messageParts[0].equals("RoomNotAvailable")) {
            String oid = messageParts[1];
            //ObjectId ooid = new ObjectId(oid);
            System.out.println("La stanza non e' disponibile, setto lo status di booking con id: "+ oid + " a deleted");
            setBookingStatus(message, oid, BookingStatus.DELETED, "BookingDeleted|");
        }

    }
/*
    private void setBookingStatus(String message, String oid, BookingStatus status, String key) {

        System.out.println("Sono dentro setBookingStatus");
        repository.existsById(new ObjectId(oid)).flatMap(exists -> {
            if (exists) {
                System.out.println("Sono dentro exists (u truvau) oid: " + oid);

                repository.findById(new ObjectId(oid)).flatMap(booking -> {
                    System.out.println("Sono dentro il secondo exists");
                    System.out.println("Stato booking prima: " + booking.getBookingStatus().toString());
                   booking.setBookingStatus(status);
                    System.out.println("Stato booking dopo: " + booking.getBookingStatus().toString());
                    return repository.save(booking);
                }).subscribe();

                //kafkaTemplate.send(mainTopic, key + oid);
            } else {
                System.out.println("Sono dentro sto else strano");

                //kafkaTemplate.send(mainTopic,"BadMessage||" + message);
            }
            return Mono.just(exists);
        }).subscribe();
    }

    */
    private void setBookingStatus(String message, String oid, BookingStatus status, String key) {
        String s = String.format("mongodb://%s:%s@%s:%s/", "root", "toor", "booking-service-db", "27017");
        MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(s),"admin");

        System.out.println(status);

        ObjectId id = new ObjectId(oid);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("bookingStatus",status);

        mongoTemplate.findAndModify(query,update, Booking.class);

    }

}