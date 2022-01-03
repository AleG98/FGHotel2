package com.unict.bookingorchestrator;

import com.unict.bookingorchestrator.steps.UserStep;
import com.unict.bookingservice.model.BookingStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrchestratorService {

    public OrchestratorService() {
    }
/*
    public Mono<String> bookingRoom (final String[] prenotazione){
        Workflow bookingWorkflow = this.getBookingWorkflow(prenotazione);


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
    }


    private Workflow getBookingWorkflow(String[] prenotazione){
        WorkflowStep userStep = new UserStep(prenotazione[1]);
       // WorkflowStep hotelStep = new HotelStep(this.inventoryClient, this.getInventoryRequestDTO(requestDTO));
        return new BookingWorkflow(List.of(userStep, hotelStep));
    }

*/
    //togliere tutti i commenti

}
