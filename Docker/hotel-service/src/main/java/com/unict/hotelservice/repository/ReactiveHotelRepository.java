package com.unict.hotelservice.repository;

import com.unict.hotelservice.model.Hotel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


public interface ReactiveHotelRepository extends ReactiveCrudRepository<Hotel, ObjectId>{
}



/*
@Repository
public interface ReactiveHotelRepository extends ReactiveMongoRepository<Hotel, ObjectId>, CustomHotelRepository {
}

 */
