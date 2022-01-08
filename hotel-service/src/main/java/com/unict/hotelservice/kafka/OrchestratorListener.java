package com.unict.hotelservice.kafka;


import com.mongodb.reactivestreams.client.MongoClients;
import com.unict.hotelservice.model.Hotel;
import com.unict.hotelservice.model.Room;
import com.unict.hotelservice.repository.ReactiveHotelRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Optional;


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

        System.out.println("Received message dall'hotel request dell'orchestratore: " + message);

        String[] messageParts = message.split("\\|");

        Hotel hotel = new Hotel(messageParts[3],messageParts[4],messageParts[5],messageParts[6]);
        repository.save(hotel).subscribe();
        System.out.println("salvato");


        //repository.addNewRoom(messageParts[6],messageParts[3],messageParts[4],messageParts[5]);
        ObjectId id = new ObjectId(messageParts[6]);

        //Mono <Hotel> hotelMono = repository.findById(id);
        String s = String.format("mongodb://%s:%s@%s:%s/%s", "root", "toor", "hotel-service-db", "27017", "admin");
        ReactiveMongoTemplate mongoTemplate = new ReactiveMongoTemplate(MongoClients.create(s),"admin");

        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().addToSet("stanze", new Room("8",messageParts[4],messageParts[5]));
        mongoTemplate.findAndModify(query, update, Hotel.class);

        //System.out.println("PERCHE' NON FUNZIONA STO SCHIFO: " + hotelMono);
        System.out.println("fine listen");

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
