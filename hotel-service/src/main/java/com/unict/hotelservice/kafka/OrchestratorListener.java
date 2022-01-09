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

        LocalDate inizio = LocalDate.parse(messageParts[4]);
        LocalDate fine = LocalDate.parse(messageParts[5]);
        Boolean overlap = false;
        boolean exists = false;
        boolean prenotazione_possibile = true;
        for (Room it : hotel_temp.getStanze()) {
            if (it.getNumero().equals(messageParts[3])){
                for (Prenotazione it_p : it.getPrenotazioni()) {
                    overlap = isoverlapping(it_p.getData_inizio(),it_p.getData_fine(), inizio,fine);
                    System.out.println("Si overlappa? Risulato: " + overlap);
                    if (overlap) {prenotazione_possibile = false; break;}
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

        if (exists && prenotazione_possibile) {
            kafkaTemplate.send(hotelResponseTopic, "RoomReserved|" + messageParts[7]);
        } else if (exists == false) {
            kafkaTemplate.send(hotelResponseTopic, "RoomNotExists|" + messageParts[7]);
        } else if (prenotazione_possibile == false) {
            kafkaTemplate.send(hotelResponseTopic, "RoomNotAvailable|" + messageParts[7]);
        }



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


            String uid = messageParts[1];
            repository.existsById(new ObjectId(uid)).flatMap(exists -> {
                kafkaTemplate.send(hotelResponseTopic, (exists ? "UserExists|" : "UserNotExists|") + message);
                return Mono.just(exists);
            }).subscribe();

 */
        }

    public boolean isoverlapping(LocalDate A, LocalDate B, LocalDate C, LocalDate D) {
        if ( (C.isBefore(A) && D.isBefore(A)) || (C.isAfter(B) && D.isAfter(B))  ) {
            return false;
        }
        return true;
    }

    }
