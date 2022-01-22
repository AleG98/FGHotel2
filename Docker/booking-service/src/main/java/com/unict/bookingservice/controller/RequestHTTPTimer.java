package com.unict.bookingservice.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RequestHTTPTimer {

    private static RequestHTTPTimer single_instance = null;

    private Timer timer_http;
    long start;
    String booking;

    private List<ResponseTimer> responseTimerList = new ArrayList<>();

    public static RequestHTTPTimer getInstance()
    {
        if (single_instance == null)
            single_instance = new RequestHTTPTimer();
        return single_instance;
    }

    public void start(String booking) {
        //System.out.println("faccio partire pippo");
        //this.start = System.currentTimeMillis();
        //this.timer_http = Metrics.timer("pippo");
        //this.booking = booking;
        ResponseTimer timer = new ResponseTimer(booking);
        timer.start();
        responseTimerList.add(timer);
    }

    public void stop(String id) {
        //System.out.println("fermo pippo");
        //timer_http.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
        for (ResponseTimer it : responseTimerList) {
            if (it.getBooking().equals(id)){
                it.stop();
            }
            responseTimerList.remove(it);
            break;
        }
    }



    public String getBooking() {
        return booking;
    }
}


