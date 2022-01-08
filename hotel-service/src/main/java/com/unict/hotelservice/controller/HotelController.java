package com.unict.hotelservice.controller;

import com.unict.hotelservice.model.Hotel;
import com.unict.hotelservice.repository.ReactiveHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class HotelController {
    @Autowired
    ReactiveHotelRepository repository;
/*
    @GetMapping("/")
    public Flux<Hotel> getUsers() {
        return repository.findAll();
    }
*/
}
