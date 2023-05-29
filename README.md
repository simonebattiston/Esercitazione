# Esercizio 32: L'artista di strada

In questo esercizio la sincronizzazione avviene grazie ai semafori, uno a conteggio e un mutex.
Dato l'utilizzo, grazie a quest'ultimo della mutua esclusione per l’accesso alle risorse condivise tra i processi in gioco, si riesce a scongiurare il verificarsi dello stallo (deadlock)

## Classe main
```
public static int MIN_WAIT = 1000; 
public static int MAX_WAIT = 3000; 
static Random rand = new Random();
public static int nClienti = rand.nextInt(50); 
public static int id = 1; 

public static Semaphore mutexArtista = new Semaphore(1); 
public static Semaphore semaforoSedie = new Semaphore(4); 
```
Le variabili `MIN_WAIT` e `MAX_WAIT` definiscono l'intervallo di tempo in millisecondi tra l'arrivo di un cliente e il successivo. La variabile nClienti rappresenta il numero casuale di clienti che arriveranno, con un massimo di 50. La variabile id viene utilizzata per assegnare un ID sequenziale a ogni cliente. I semafori `mutexArtista`(mutex) e `semaforoSedie`(semaforo a conteggio) vengono inizializzati con un valore iniziale rispettivamente di 1 e 4, questo al fine che l'artista esegua un ritratto alla volta e che le sedie totali disponibili siano 4.

### Creazione dei clienti
```
for (int i = 0; i < nClienti; i++) {
    Cliente cliente = new Cliente(id++);
    cliente.start();
    System.out.println("Cliente " + (id -1) + " in attesa di sedersi");

    try {
        Thread.sleep(MIN_WAIT, MAX_WAIT);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```
Viene eseguito un ciclo for per creare e avviare i thread dei clienti. Ad ogni cliente viene assegnato un ID sequenziale utilizzando la variabile `id`. Successivamente, il thread del cliente viene avviato e viene visualizzato un messaggio di attesa. Ecco che infine il thread va in "sleep" per un tempo compreso tra 1 e 3 secondi prima di fare in modo che un altro cliente sia interessato a farsi ritrarre.

## Classe Cliente
```
public class Cliente extends Thread {
    public int id;

    public Cliente(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            if (Main.semaforoSedie.tryAcquire(5, TimeUnit.SECONDS)) {
                System.out.println("Il cliente " + id + " si è seduto e attende...");
                Main.mutexArtista.acquire();
                System.out.println("Ritratto per il cliente " + id + " iniziato");

                try {
                    Thread.sleep(5000, 10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Ritratto per il cliente " + id + " finito");
                Main.mutexArtista.release();
                Main.semaforoSedie.release();
            } else {
                System.out.println("Il cliente " + id + " ha aspettato troppo e se ne è andato");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

La classe `Cliente` deriva dalla classe `Thread`, ciò porta al fatto che viene eseguito l'Override del metodo `run()` per definire le operazioni eseguite dal thread del cliente. Ogni cliente ha un ID assegnato nel costruttore.

All'interno del metodo `run()`, il cliente tenta di acquisire `semaforoSedie` utilizzando la primitiva```tryAcquire(5, TimeUnit.SECONDS)```. 
Se riesce ad acquisirlo entro 5 secondi, il cliente potrà sedersi ed attendere, dato che l'artista deve prima liberarsi per potersi occupare del thread corrente. Successivamente, quindi,  viene acquisito anche `mutexArtista` utilizzando la medesima primitiva `acquire()`. Viene quindi eseguito il ritratto simulando un intervallo di tempo di sleep tra 5 e 10 secondi. Dopo aver completato il ritratto, i semafori `mutexArtista` e `semaforoSedie` vengono rilasciati, consentendo rispettivamente all'artista di occuparsi di raffigurare altri clienti e ai clienti di potersi sedere, dunque, sulla sedia appena liberata.

Se il cliente non riesce, invece, a ottenere una sedia entro 5 secondi, viene visualizzato un messaggio indicando che ha aspettato troppo e rinuncia a farsi fare il ritratto.

L'esecuzione del programma termina non appena non ci sono più clienti e l'ultimo ritratto è stato eseguito dall'artista.
