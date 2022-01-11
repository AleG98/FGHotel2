package com.unict.bookingservice.controller;

import com.unict.bookingservice.model.Booking;
import com.unict.bookingservice.repository.ReactiveBookingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class BookingController {

    @Resource
    private CustomCounter customCounter;

    //static final Counter requests = Counter.build()
    //        .name("requests_booking").help("Total number of requests.").register();


   //private MeterRegistry meterRegistry;
   //final Counter prova= meterRegistry.counter("order.created");


    @Autowired
    ReactiveBookingRepository repository;

    @Autowired
    ReactiveMongoOperations operations;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value="${KAFKA_MAIN_TOPIC}")
    private String maintopic;

    @GetMapping("/")
    public Flux<Booking> getBooking() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable("id") String id) {
        Booking o = repository.findById(new ObjectId(id)).block();
        if (o == null) {
            return new ResponseEntity<>(o, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBooking(@PathVariable("id") String id) {
        Boolean ret = repository.existsById(new ObjectId(id)).block();

        if (ret) {
            repository.deleteById(new ObjectId(id)).subscribe();
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PutMapping(path="/{id}", consumes={"application/JSON"}, produces="application/json")
    public ResponseEntity<Mono<Booking>> editBooking(@PathVariable("id") String id, @RequestBody Booking o) {
        ObjectId oid = new ObjectId(id);
        Booking old = repository.findById(oid).block();
        if (old == null)
            return new ResponseEntity<>(Mono.just(new Booking(null, null)), HttpStatus.NOT_FOUND);

        o.set_id(oid);

        return new ResponseEntity<>(repository.save(o), HttpStatus.OK);
    }

    @PostMapping(path="/", consumes = "application/JSON", produces = "application/JSON")
    public Mono<Booking> newBooking(@RequestBody Booking o) {
        //meterRegistry.counter("prova").increment();
        //prova.increment();
        //requests.increment();
        customCounter.incrementCounter();
        return repository.save(o).flatMap(booking -> {
            kafkaTemplate.send(maintopic, "BookingCreated|" + o.get_user_string() + "|" + o.get_room_string()+ "|" + o.get_Datebegin_string()+ "|" + o.get_Dateend_string()+ "|"+ o.get_idHotel_string()+ "|"+ o.get_Id_string());
            return Mono.just(o);
        });

        //return Mono.just(o);
        //return o;
    }

    @GetMapping(path="/{id}/exists")
    public Mono<Boolean> exists(@PathVariable("id") String id) {
        ObjectId oid = new ObjectId(id);
        return repository.existsById(oid);
    }
}

