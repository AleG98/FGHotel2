package com.unict.bookingorchestrator;




import io.eventuate.examples.tram.sagas.ordersandcustomers.orders.service.RejectOrderCommand;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

@Service
public class OrchestratorService implements SimpleSaga<String> {


    private DomainEventPublisher domainEventPublisher;

    //private BookingService bookingService;
    //private ReactiveBookingRepository repository;
    public OrchestratorService() {
        //bookingService=new BookingService(repository);
    }
    SagaDefinition<String> sagaDefinition;
    private String[] prenotazione;
    public Mono<String> bookingRoom (final String[] prenotazione){
        System.out.println("All'interno di booking room, prima degli step");
        //Workflow bookingWorkflow = this.getBookingWorkflow(prenotazione);
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
    return Mono.just("ciao");
    }

    private void handleCustomerNotFound() {
    }

    private void handleCustomerCreditLimitExceeded() {

    }


    private CommandWithDestination foundUser(String booking) {
        System.out.println("All'interno di CommandWithDestination foundUser");
        return send(new userCommand(prenotazione[2]))
                .to("userService")
                .build();
    }

    private void create(String bookingb) {
        System.out.println("All'interno di create del primo step");
       // Booking booking= BookingService.createBooking(prenotazione[5], prenotazione[2],prenotazione[3],prenotazione[4], prenotazione[1]);
    }

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
