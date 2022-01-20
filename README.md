# FGHotel2
Il progetto prevede un sistema di prenotazione di stanze all'interno di hotel.
L'utente prima di effettuare una qualsiasi prenotazione deve procedere con la registrazione, inserendo informazioni personali quali:
-nome
- nome
- cognome
- indirizzo
- n_telefono

Il microservizio responsabile della registrazione utente è lo user-service, che inserisce le informazioni all'interno del proprio database assegnandogli un identificativo.
Altro microservizio è l'hotel service, responsabile della gestione degli hotel.
Il microservizio si occupa di inserire nuovi hotel all'interno del proprio database con il rispettivo nome e numero di stanze. Per ogni stanza, all'atto della prenotazione, verrà verificato lo stato della stanza per la data richiesta.
Infine vi è il microservizio del booking-service che si occupa di creare le prenotazioni e impostare lo stato delle prenotazioni a confermato o cancellato.
Il processo di prenotazione della camera richiede due step: lo user-id indicato in fase di prenotazione deve essere già registrato, così come l'hotel-id. Tuttavia, per prenotare una stanza bisogna verificare se la stanza richiesta è disponibile per la data di prenotazione richiesta. Siamo così in presenza di una saga che gestiamo tramite orchestrator(booking-orchestrator).
Sono presenti quindi quattro microservizi:
-booking-service
-booking-orchestrator
-hotel-service
-user-service
La saga è così costituita:
1. il booking-orchestrator richiede allo user-service se l'id è disponibile:
CASO DI SUCCESSO: si passa allo step successivo, ovvero si verifica la disponibilità della camera e l'esistenza dell'hotel.
CASO DI INSUCCESSO: la booking passa nello stato di DELETED.
2. hotel-service verifica l'esistenza dell'id hotel e la disponibilità della stanza:
CASO DI SUCCESSO: la prenotazione viene confermata.
CASO DI INSUCCESSO: la prenotazione passa nello stato di DELETED.


I microservizi agiscono dietro INGRESS che si occupa di smistare le richieste tra i vari micro-services.

Come sistema di monitoraggio è stato utilizzato PROMETHEUS per il whitebox monitoring dove le metriche analizzate sono state: 

- booking creati totali
- memoriadisponibile/memoriatotale * 100
- container_cpu_load_average_10s
Sono stati creati degli alert per notificare tramite mail il verificarsi di particolari condizioni, per esempio memoriadisponibile/memoriatotale * 100< 10. Immaginando una situazione in cui la memoria sta per saturarsi.
