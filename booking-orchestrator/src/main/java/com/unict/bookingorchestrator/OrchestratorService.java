package com.unict.bookingorchestrator;

/*

import io.eventuate.examples.tram.sagas.ordersandcustomers.orders.service.RejectOrderCommand;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;
*/

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


/*
@Service
public class OrchestratorService implements SimpleSaga<String> {
*/
@Service
@RestController
public class OrchestratorService {


    //private DomainEventPublisher domainEventPublisher;
    final Counter prova= Metrics.counter("metrica_orchestrator.created");
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_MAIN_TOPIC}")
    private String mainTopic;

    @Value(value = "${KAFKA_USER_REQUEST_TOPIC}")
    private String userRequestTopic;

    @Value(value = "${KAFKA_HOTEL_REQUEST_TOPIC}")
    private String hotelRequestTopic;


    //private BookingService bookingService;
    //private ReactiveBookingRepository repository;
    public OrchestratorService() {
        //bookingService=new BookingService(repository);
    }
   // SagaDefinition<String> sagaDefinition;
    //private String prenotazione;

    @KafkaListener(topics="${KAFKA_MAIN_TOPIC}")
    public Mono<String> bookingRoom (String prenotazione){
        String[] messageParts = prenotazione.split("\\|");
        if (messageParts[0].equals("BookingCreated")) {
            System.out.println("All'interno di booking room, prima degli step");
            prova.increment();


            //Workflow bookingWorkflow = this.getBookingWorkflow(prenotazione);
            //String[] prenotazioneParts = prenotazione.split("\\|");


            System.out.println("Messaggio arrivato: " + prenotazione);

            //String id = "61d70a91d6018000093d5cb2";
            //kafkaTemplate.send(userRequestTopic, "61d70a91d6018000093d5cb2");
            kafkaTemplate.send(userRequestTopic, prenotazione);



        /*
            this.prenotazione=prenotazione;
         sagaDefinition = step()
                .invokeLocal(this::create)
                .withCompensation(this::reject)
                 .step().invokeParticipant(this::foundUser)
                 //.withCompensation(this::reject)
                 .step()
                .invokeParticipant(this::reserveRoom)
               // .onReply(roomNotFound.class, this::handleCustomerNotFound)
                //.onReply(roomNotAvailable.class, this::handleCustomerCreditLimitExceeded)
                .step()
                .invokeLocal(this::approve)
                .build();

         */
/*
        return Flux.fromStream(() -> bookingWorkflow.getSteps().stream())
                .flatMap(WorkflowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if(aBoolean)
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new WorkflowException("create booking failed!"));
                }))
                .then(Mono.fromCallable(() -> getResponseDTO(prenotazione, BookingStatus.CONFIRMED)))
                .onErrorResume(ex -> this.revertOrder(orderWorkflow, prenotazione));
*/

            System.out.println("All'interno di booking room, alla fine degli step");
        } //fine parentesi dell'if di booking created
            return Mono.just("ciao");
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

    private void handleCustomerNotFound() {
    }

    private void handleCustomerCreditLimitExceeded() {

    }

/*
    private CommandWithDestination foundUser(String booking) {
        System.out.println("All'interno di CommandWithDestination foundUser");
        return send(new userCommand(prenotazione[2]))
                .to("userService")
                .build();
    }


 */
    private void create(String bookingb) {
        System.out.println("All'interno di create del primo step");
       // Booking booking= BookingService.createBooking(prenotazione[5], prenotazione[2],prenotazione[3],prenotazione[4], prenotazione[1]);
    }
/*
    private CommandWithDestination reserveRoom(String booking) {
        System.out.println("All'interno di CommandWithDestination reserveRoom");
        return send(new ReserveRoomCommand(prenotazione[5],prenotazione[2],prenotazione[3],prenotazione[4]))
                .to("hotelService")
                .build();
    }

    @Override
    public SagaDefinition<String> getSagaDefinition() {
        return this.sagaDefinition;
    }


 */

    private void approve(String booking) {
        System.out.println("All'interno di CommandWithDestination approve");
       // return send(new RejectOrderCommand(Long.parseLong(booking.get_Id_string())))
               // .to("orderService")
              //  .build(); // da modificare
    }

    private void reject(String booking) {
        //return send(new RejectOrderCommand(Long.parseLong(booking.get_Id_string())))
              //  .to("orderService")
               // .build(); // da modificare (non era void)
    }





}
