package com.unict.bookingservice.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.TimeUnit;

public class RequestHTTPTimer {

    private static RequestHTTPTimer single_instance = null;

    private Timer timer_http;
    long start;
    String booking;

    public static RequestHTTPTimer getInstance()
    {
        if (single_instance == null)
            single_instance = new RequestHTTPTimer();
        return single_instance;
    }

    public void start(String booking) {
        System.out.println("faccio partire pippo");
        this.start = System.currentTimeMillis();
        this.timer_http = Metrics.timer("pippo", "type", "ping");
        this.booking = booking;
    }

    public void stop() {
        System.out.println("fermo pippo");
        timer_http.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
    }

    public String getBooking() {
        return booking;
    }
}
