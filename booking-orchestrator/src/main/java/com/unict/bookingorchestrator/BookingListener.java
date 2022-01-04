package com.unict.bookingorchestrator;

import com.unict.bookingservice.repository.ReactiveBookingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;

public class BookingListener {
    @Autowired
    ReactiveBookingRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_MAIN_TOPIC}")
    private String mainTopic;

    @KafkaListener(topics="${KAFKA_MAIN_TOPIC}")
    public void listen(String message) {
        System.out.println("Received message " + message);

        String[] messageParts = message.split("\\|");

        if (messageParts[0].equals("BookingCreated")) {
            String uid = messageParts[1];
            OrchestratorService orchestratorService = new OrchestratorService();
           orchestratorService.bookingRoom(messageParts);


            repository.existsById(new ObjectId(uid)).flatMap(exists -> {
                kafkaTemplate.send(mainTopic, (exists?"UserExists|":"UserNotExists|") + messageParts[2]);
                return Mono.just(exists);
            }).subscribe();
        }
    }
}

