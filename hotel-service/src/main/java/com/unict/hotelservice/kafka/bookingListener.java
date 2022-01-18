package com.unict.hotelservice.kafka;

import com.mongodb.client.MongoClients;
import com.unict.hotelservice.model.Prenotazione;
import com.unict.hotelservice.repository.ReactiveHotelRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class bookingListener {
    @Autowired
    ReactiveHotelRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_MAIN_TOPIC}")
    private String mainTopic;
    @KafkaListener(topics="${KAFKA_MAIN_TOPIC}")
    public void listen(String message) {
        String[] messageParts = message.split("\\|");

        if (messageParts[0].equals("BookingToDelete")) {
            String s = String.format("mongodb://%s:%s@%s:%s/", "root", "toor", "hotel-service-db", "27017");
            MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(s),"admin");
            Update update = new Update().pull("stanze.prenotazioni",Collections.singletonMap("bookingId", new ObjectId(messageParts[1])));
            mongoTemplate.updateMulti(new Query(),update, Prenotazione.class);
        }

    }

}
