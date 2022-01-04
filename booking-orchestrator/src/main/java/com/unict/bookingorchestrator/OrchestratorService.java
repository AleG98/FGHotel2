package com.unict.bookingorchestrator;

import com.unict.bookingorchestrator.steps.UserStep;
import com.unict.bookingservice.controller.BookingService;
import com.unict.bookingservice.model.Booking;
import com.unict.bookingservice.model.BookingStatus;
import com.unict.bookingservice.repository.ReactiveBookingRepository;
import com.unict.hotelservice.roomNotAvailable;
import com.unict.hotelservice.roomNotFound;
import io.eventuate.examples.tram.sagas.ordersandcustomers.orders.sagas.createorder.CreateOrderSagaData;
import io.eventuate.examples.tram.sagas.ordersandcustomers.orders.sagas.participants.ReserveCreditCommand;
import io.eventuate.examples.tram.sagas.ordersandcustomers.orders.service.RejectOrderCommand;
import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

@Service
public class OrchestratorService implements SimpleSaga<Booking> {


    private DomainEventPublisher domainEventPublisher;

    private BookingService bookingService;
    private ReactiveBookingRepository repository;
    public OrchestratorService() {
        bookingService=new BookingService(repository);
    }
    SagaDefinition<Booking> sagaDefinition;
    private String[] prenotazione;
    public Mono<String> bookingRoom (final String[] prenotazione){
        //Workflow bookingWorkflow = this.getBookingWorkflow(prenotazione);
            this.prenotazione=prenotazione;
         sagaDefinition = step()
                .invokeLocal(this::create)
                .withCompensation(this::reject)
                 .step().invokeParticipant(this::foundUser)
                 .withCompensation(this::reject)
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
    return Mono.just("ciao");
    }

    private void handleCustomerNotFound() {
    }

    private void handleCustomerCreditLimitExceeded() {

    }


    private CommandWithDestination foundUser(Booking booking) {
        return send(new userCommand(prenotazione[2]))
                .to("userService")
                .build();
    }

    private void create(Booking bookingb) {
        Booking booking= BookingService.createBooking(prenotazione[5], prenotazione[2],prenotazione[3],prenotazione[4], prenotazione[1]);
    }

    private CommandWithDestination reserveRoom(Booking booking) {
        return send(new ReserveRoomCommand(prenotazione[5],prenotazione[2],prenotazione[3],prenotazione[4]))
                .to("hotelService")
                .build();
    }

    @Override
    public SagaDefinition<Booking> getSagaDefinition() {
        return this.sagaDefinition;
    }


    private CommandWithDestination approve(Booking booking) {
        return send(new RejectOrderCommand(Long.parseLong(booking.get_Id_string())))
                .to("orderService")
                .build(); // da modificare
    }

    private CommandWithDestination reject(Booking booking) {
        return send(new RejectOrderCommand(Long.parseLong(booking.get_Id_string())))
                .to("orderService")
                .build();
    }





}
