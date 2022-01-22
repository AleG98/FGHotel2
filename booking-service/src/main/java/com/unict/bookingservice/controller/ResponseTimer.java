package com.unict.bookingservice.controller;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.TimeUnit;

public class ResponseTimer {

    String booking;
    private Timer timer_http;
    long start;

    public ResponseTimer(String booking) {
        this.booking = booking;
    }

    public void start() {
        System.out.println("faccio partire pippo");
        this.start = System.currentTimeMillis();
        this.timer_http = Metrics.timer("pippo");
        this.booking = booking;
    }

    public void stop() {
        System.out.println("fermo pippo");
        timer_http.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
        System.out.println("\n\n\nTempo response: "+ (System.currentTimeMillis() - start) + "ms\n\n");
    }

    public String getBooking() {
        return booking;
    }
}
