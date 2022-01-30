package com.unict.hotelservice.controller;

import com.mongodb.client.MongoClients;
import com.unict.hotelservice.kafka.BookingListener;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RestController
public class HotelController {
    @Autowired
    ReactiveHotelRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_CHECKOUT_TOPIC}")
    private String checkoutTopic;

    @GetMapping("/")
    public Flux<Hotel> getHotels() {
        return repository.findAll();
    }

    @PostMapping(path="/", consumes={"application/JSON"}, produces="application/json")
    public Mono<Hotel> createHotel(@RequestBody Hotel h) {
        return repository.save(h);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable("id") String id) {
        Hotel o = repository.findById(new ObjectId(id)).block();
        if (o == null) {
            return new ResponseEntity<>(o, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(o, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteHotel(@PathVariable("id") String id) {
        Boolean ret = repository.existsById(new ObjectId(id)).block();
        if (ret) {
            repository.deleteById(new ObjectId(id)).subscribe();
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/prova/{id}")
    public ResponseEntity<Boolean> provaHotel(@PathVariable("id") String id) {
        BookingListener b = new BookingListener();
        b.listen("BookingToDelete|" + id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/checkout/{id}")
    public ResponseEntity<Boolean> checkoutBooking(@PathVariable("id") String id) {
        kafkaTemplate.send(checkoutTopic, "Checkout|" + id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
