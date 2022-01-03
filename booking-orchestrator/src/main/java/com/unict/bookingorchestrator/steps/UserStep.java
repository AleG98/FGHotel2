package com.unict.bookingorchestrator.steps;

import com.unict.bookingorchestrator.WorkflowStep;
import com.unict.bookingorchestrator.WorkflowStepStatus;
import com.unict.userservice.repository.ReactiveUserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;

public class UserStep implements WorkflowStep {

    private final String id;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    @Autowired
    ReactiveUserRepository repository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_MAIN_TOPIC}") //cambiare topic
    private String mainTopic;

    public UserStep(String id) {
        this.id = id;
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public Mono<Boolean> process() {

        repository.existsById(new ObjectId(id)).flatMap(exists -> {
            kafkaTemplate.send(mainTopic, (exists? "UserExists|":"UserNotExists|") );
            return Mono.just(exists);
        }).subscribe();
        return Mono.just(true);
        /*
        return this.webClient
                .post()
                .uri("/inventory/deduct")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(InventoryResponseDTO.class)
                .map(r -> r.getStatus().equals(InventoryStatus.AVAILABLE))
                .doOnNext(b -> this.stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
         */
    }

    @Override
    public Mono<Boolean> revert() {
     /*   return this.webClient
                .post()
                .uri("/inventory/add")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r ->true)
                .onErrorReturn(false);*/
        return Mono.just(true); //da togliere sta schifezza
    }
    
 
}
