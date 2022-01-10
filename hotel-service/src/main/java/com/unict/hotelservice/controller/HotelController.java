package com.unict.hotelservice.controller;

import com.unict.hotelservice.model.Hotel;
import com.unict.hotelservice.repository.ReactiveHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class HotelController {
    @Autowired
    ReactiveHotelRepository repository;

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
}
