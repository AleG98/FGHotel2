package com.unict.userservice.repository;

import com.unict.userservice.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReactiveUserRepository extends ReactiveCrudRepository<User, ObjectId> {
}
