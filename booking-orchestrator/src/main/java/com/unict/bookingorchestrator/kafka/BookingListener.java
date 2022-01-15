package com.unict.bookingorchestrator.kafka;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.unict.bookingorchestrator.OrchestratorService;


//@Service
public class BookingListener {
    //@Autowired
   // ReactiveBookingRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

   // @Value(value = "${KAFKA_MAIN_TOPIC}")
   // private String mainTopic;
/*
    @Value(value = "${KAFKA_SAGA_TOPIC}")
    private String sagaTopic;
*/
//    @KafkaListener(topics="${KAFKA_MAIN_TOPIC}")
    public void listen(String message) {
        System.out.println("Received message dal main topic" + message);

        String[] messageParts = message.split("\\|");

        if (messageParts[0].equals("BookingCreated")) {
            String uid = messageParts[1];
            System.out.println("Stampo messageParts: " + message);
            OrchestratorService orchestratorService = new OrchestratorService();
           orchestratorService.bookingRoom(message);
            //kafkaTemplate.send("user-request-topic", "61d70a91d6018000093d5cb2");

            System.out.println("fine saga?");
/*
            repository.existsById(new ObjectId(uid)).flatMap(exists -> {
                kafkaTemplate.send(mainTopic, (exists?"UserExists|":"UserNotExists|") + messageParts[2]);
                return Mono.just(exists);
            }).subscribe();

 */
        }
    }


}


