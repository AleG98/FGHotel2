package com.unict.bookingservice.controller;
//import io.prometheus.client.metrics.Counter
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.springframework.stereotype.Component;

@Component
public class CustomCounter {

     Counter mycounter;

    public void CustomCounter(CollectorRegistry registry) {
        mycounter = Counter.build().name("test").help("test").register(registry);
    }

    public void incrementCounter() {
        mycounter.inc();
    }
}
