package com.unict.bookingorchestrator;

import java.util.List;

public class BookingWorkflow implements Workflow{
    private final List<WorkflowStep> steps;

    public BookingWorkflow(List<WorkflowStep> steps) {
        this.steps = steps;
    }

    @Override
    public List<WorkflowStep> getSteps() {
        return this.steps;
    }
}
