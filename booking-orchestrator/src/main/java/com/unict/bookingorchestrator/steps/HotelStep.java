package com.unict.bookingorchestrator.steps;

import com.unict.bookingorchestrator.WorkflowStep;
import com.unict.bookingorchestrator.WorkflowStepStatus;
import reactor.core.publisher.Mono;

public class HotelStep implements WorkflowStep {
    @Override
    public WorkflowStepStatus getStatus() {
        return null;
    }

    @Override
    public Mono<Boolean> process() {
        return null;
    }

    @Override
    public Mono<Boolean> revert() {
        return null;
    }
}
