package com.unict.hotelservice.kafka;

import com.unict.hotelservice.repository.ReactiveHotelRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorListener {

    @Autowired
    ReactiveHotelRepository repository;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_HOTEL_RESPONSE_TOPIC}")
    private String hotelResponseTopic;

    @KafkaListener(topics = "${KAFKA_HOTEL_REQUEST_TOPIC}")
    public void listen(String message) {
        System.out.println("Received message dall'user request dell'orchestratore: " + message);

        String[] messageParts = message.split("\\|");
/*

 public boolean isPrenotazionePossibile(int codVeicolo, Date inizio, Date fine) {
        boolean esito = true;
        Noleggio n;
        for(int i=0; i<numNoleggi; i++) {
            //si procura la prenotazione i-sima
            n = noleggi[i];
            //se essa riguarda il veicolo in oggetto
            if(n.getCodVeicolo() == codVeicolo) {
                //ipotizza la prenotazione non possibile
                esito = false;
                //verifica la compatibilità dei periodi
                if(fine.before(n.getDataInizio()) || inizio.after(n.getDataFine()))
                    esito = true;
                else
                    break;
            }
        }
        return esito;
    }

            String uid = messageParts[1];
            repository.existsById(new ObjectId(uid)).flatMap(exists -> {
                kafkaTemplate.send(hotelResponseTopic, (exists ? "UserExists|" : "UserNotExists|") + message);
                return Mono.just(exists);
            }).subscribe();

 */
        }

    }
