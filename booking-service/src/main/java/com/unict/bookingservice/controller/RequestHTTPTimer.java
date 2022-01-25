package com.unict.bookingservice.controller;

import io.micrometer.core.instrument.Timer;

import java.util.ArrayList;
import java.util.List;

public class RequestHTTPTimer {

    private static RequestHTTPTimer single_instance = null;

    private List<ResponseTimer> responseTimerList = new ArrayList<>();

    public static RequestHTTPTimer getInstance()
    {
        if (single_instance == null)
            single_instance = new RequestHTTPTimer();
        return single_instance;
    }

    public void start(String booking) {
        ResponseTimer timer = new ResponseTimer(booking);
        timer.start();
        responseTimerList.add(timer);
    }

    public void stop(String id) {
        for (ResponseTimer it : responseTimerList) {
            if (it.getBooking().equals(id)){
                it.stop();
            }
            responseTimerList.remove(it);
            break;
        }
    }
}


