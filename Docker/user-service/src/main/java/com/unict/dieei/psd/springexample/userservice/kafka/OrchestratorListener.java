package com.unict.dieei.psd.springexample.userservice.kafka;

import com.unict.dieei.psd.springexample.userservice.repository.ReactiveUserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorListener {

    @Autowired
    ReactiveUserRepository repository;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_USER_RESPONSE_TOPIC}")
    private String userResponseTopic;

    @KafkaListener(topics = "${KAFKA_USER_REQUEST_TOPIC}")
    public void listen(String message) {
        System.out.println("Received message dall'user request dell'orchestratore: " + message);

      String[] messageParts = message.split("\\|");
/*
        if (messageParts[0].equals("OrderCreated")) {
            String uid = messageParts[1];
            repository.existsById(new ObjectId(uid)).flatMap(exists -> {
                kafkaTemplate.send(mainTopic, (exists ? "UserExists|" : "UserNotExists|") + messageParts[2]);
                return Mono.just(exists);
            }).subscribe();
        }


*/
            String uid = messageParts[1];
            repository.existsById(new ObjectId(uid)).flatMap(exists -> {
                kafkaTemplate.send(userResponseTopic, (exists ? "UserExists|" : "UserNotExists|") + message);
                return Mono.just(exists);
            }).subscribe();


    }
    }
