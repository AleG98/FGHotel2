package com.unict.bookingorchestrator;

public class WorkflowException extends RuntimeException{
    public WorkflowException(String message) {
        super(message);
    }
}