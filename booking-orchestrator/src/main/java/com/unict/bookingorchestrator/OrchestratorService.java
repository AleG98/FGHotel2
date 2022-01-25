package com.unict.bookingorchestrator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorService {

    final Counter prova= Metrics.counter("orchestrator.created");

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_MAIN_TOPIC}")
    private String mainTopic;

    @Value(value = "${KAFKA_USER_REQUEST_TOPIC}")
    private String userRequestTopic;

    @Value(value = "${KAFKA_HOTEL_REQUEST_TOPIC}")
    private String hotelRequestTopic;


    public OrchestratorService() {
    }


    @KafkaListener(topics="${KAFKA_MAIN_TOPIC}")
    public Mono<String> bookingRoom (String prenotazione){
        String[] messageParts = prenotazione.split("\\|");
        if (messageParts[0].equals("BookingCreated")) {

            prova.increment();
            System.out.println("Messaggio arrivato: " + prenotazione);
            kafkaTemplate.send(userRequestTopic, prenotazione);

        }
            return Mono.just("orchestrator");
    }

    @KafkaListener(topics="${KAFKA_USER_RESPONSE_TOPIC}")
    public void userResponseListen(String message) {

        String[] messageParts = message.split("\\|");
        System.out.println("Received message dall'user response topic:" + message);
        if (messageParts[0].equals("UserExists")) {
            kafkaTemplate.send(hotelRequestTopic, message);
        } else if (messageParts[0].equals("UserNotExists")) {
            kafkaTemplate.send(mainTopic,  "UserNotExists|" + messageParts[7]);
        }
    }

    @KafkaListener(topics="${KAFKA_HOTEL_RESPONSE_TOPIC}")
    public void hotelResponseListen(String message) {
        System.out.println("Received message dall'hotel response topic:" + message);
        kafkaTemplate.send(mainTopic,message);
    }

}
