package com.unict.hotelservice.kafka;

import com.mongodb.client.MongoClients;
import com.unict.hotelservice.model.Hotel;
import com.unict.hotelservice.model.Prenotazione;
import com.unict.hotelservice.model.Room;
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
import java.util.List;

@Service
public class BookingListener {

    @Autowired
    ReactiveHotelRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_MAIN_TOPIC}")
    private String mainTopic;

    @Value(value = "${KAFKA_BILLING_TOPIC}")
    private String billingTopic;

    @KafkaListener(topics="${KAFKA_MAIN_TOPIC}")
    public void listen(String message) {
        String[] messageParts = message.split("\\|");
        ObjectId id = new ObjectId(messageParts[1]);

        if (messageParts[0].equals("BookingToDelete")) {
            String s = String.format("mongodb://%s:%s@%s:%s/", "root", "toor", "hotel-service-db", "27017");
            MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(s),"admin");
            Update update = new Update().pull("hotel.stanze.prenotazioni",Collections.singletonMap("bookingId", new ObjectId(messageParts[1])));
            mongoTemplate.updateMulti(new Query(),update, Prenotazione.class);
            List<Hotel> lista = mongoTemplate.findAll(Hotel.class);
            for (Hotel ith : lista) {
                System.out.println("itero l'hotel: " + ith.getNome());
                Hotel h = ith;
                for (Room itr : ith.getStanze()){
                    System.out.println("itero la stanza: " + itr.getNumero());
                    for (Prenotazione itp : itr.getPrenotazioni()) {
                        System.out.println("itero la prenotazione con: " + itp.getBookingId());
                        if(itp.getBookingId().equals(id)) {
                            itr.getPrenotazioni().remove(itp);
                            mongoTemplate.save(h);
                            break;
                        }
                    }
                }
            }

        }

    }

    @KafkaListener(topics="${KAFKA_CHECKOUT_TOPIC}")
    public void checkoutListen(String message) {
        System.out.println("Received message " + message);

        String[] messageParts = message.split("\\|");

        if (!messageParts[0].equals("Checkout")) {


            if (messageParts[0].equals("CheckoutInfo")) {
                System.out.println("Invio le info al billing service per l'emissione della fattura: " + message);
                kafkaTemplate.send(billingTopic, "Billing:" + message);
            }
            if (messageParts[0].equals("BookingNotFound")) {
                System.out.println("Prenotazione non trovata");
            }
            if (messageParts[0].equals("BookingNotConfirmed")) {
                System.out.println("Prenotazione non confermata");
            }

        }
    }

}
