package com.unict.hotelservice.kafka;




//import com.mongodb.reactivestreams.client.MongoClients;

import com.mongodb.client.MongoClients;
import com.unict.hotelservice.model.Hotel;
import com.unict.hotelservice.model.Prenotazione;
import com.unict.hotelservice.model.Room;
import com.unict.hotelservice.repository.ReactiveHotelRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;


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

        String s = String.format("mongodb://%s:%s@%s:%s/", "root", "toor", "hotel-service-db", "27017");
        MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(s),"admin");

        System.out.println("Received message dall'hotel request dell'orchestratore: " + message);

        String[] messageParts = message.split("\\|");

        Hotel hotel = new Hotel(messageParts[3],messageParts[4],messageParts[5],messageParts[6]);
        //repository.save(hotel).subscribe();
        ObjectId id = new ObjectId(messageParts[6]);

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Hotel hotel_temp = mongoTemplate.findOne(query, Hotel.class);

        LocalDate inizio = LocalDate.parse(messageParts[5]);
        LocalDate fine = LocalDate.parse(messageParts[6]);

        boolean exists = false;
        boolean prenotazione_possibile = false;
        for (Room it : hotel_temp.getStanze()) {
            if (it.getNumero().equals(messageParts[3])){
                for (Prenotazione it_p : it.getPrenotazioni()) {
                    if (fine.isBefore(it_p.getData_inizio()) || inizio.isAfter(it_p.getData_fine())) {
                        prenotazione_possibile = true;
                    }
                }
                if (prenotazione_possibile) {
                    it.getPrenotazioni().add(new Prenotazione(LocalDate.parse(messageParts[4]), LocalDate.parse(messageParts[5])));
                }
                exists = true;
            }
        }

        mongoTemplate.save(hotel_temp);
        System.out.println("Risultati:");
        System.out.println("Stanza esistente: " + exists);
        System.out.println("Prenotazione possibile: " + prenotazione_possibile);



/*
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.push("stanze", new Room(messageParts[3],messageParts[4],messageParts[5]));

        Query query2 = new Query();
        query2.addCriteria(Criteria.where("_id").is(id).and("stanze.numero").is(messageParts[3]));
        Update update2 = new Update();
        update2.push("stanze.prenotazioni", new Prenotazione(LocalDate.parse(messageParts[4]),LocalDate.parse(messageParts[5])));

        mongoTemplate.findAndModify(query2,update2,Hotel.class);
        //mongoTemplate.upsert(query2,update2,Hotel.class);
        //mongoTemplate.save(hotel,"hotel");
        System.out.println("salvato spiramu bonuchiui fainal melma su melma");
*/

        //repository.addNewRoom(messageParts[6],messageParts[3],messageParts[4],messageParts[5]);


        //Mono<Hotel> hotelMono = repository.findById(id);

        //String s = String.format("mongodb://%s:%s@%s:%s/%s", "root", "toor", "hotel-service-db", "27017", "admin");
        //ReactiveMongoTemplate mongoTemplate = new ReactiveMongoTemplate(MongoClients.create(s),"admin.hotel");

/*
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update().push("stanze", new Room("8",messageParts[4],messageParts[5]));
        mongoTemplate.findAndModify(query, update, Hotel.class);
*/
 /*
       // System.out.println("Canciau 6, Prima del for");
        List<Hotel> alberghi = mongoTemplate.find(query, Hotel.class);
        for (Hotel h : alberghi) {
            System.out.println("All'interno del for della lista, id hotel -> " + h.get_id_string());
        }
*/

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
