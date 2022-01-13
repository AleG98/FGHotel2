package com.unict.hotelservice.repository;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.unict.hotelservice.model.Hotel;
import com.unict.hotelservice.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Mono;


public class CustomHotelImplementation implements CustomHotelRepository {


    @Override
    public Mono<Hotel> addNewRoom(String IdHotel, String room, String data_inizio, String data_fine) {

        String s = String.format("mongodb://%s:%s@%s:%s/%s", "root", "toor", "hotel-service-db", "27017", "admin");
        ReactiveMongoTemplate mongoTemplate = new ReactiveMongoTemplate(MongoClients.create(s),"admin");

        Query query = new Query(Criteria.where("_id").is(IdHotel));
        Update update = new Update().push("stanze", new Room(room,data_inizio, data_fine));
        // Update updateWithPush = new Update().push("roles", role);
        return mongoTemplate.findAndModify(query, update, Hotel.class);
    }

}
