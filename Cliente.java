package pack;

import java.util.concurrent.TimeUnit;

public class Cliente extends Thread{
	public int id;
	
	public Cliente(int id) {	//costruttore per assegnare un id ad ogni cliente
		this.id = id;
	}
	
	@Override
	public void run() { 	// override del metodo run
			try {
				if (Main.semaforoSedie.tryAcquire(5, TimeUnit.SECONDS)) {	//se in 5 secondi il thread riesce ad acquisire il semaforo, viene eseguita la sezione critica sottostante...
					System.out.println("Il cliente " + id + " si è seduto e attende...");
					Main.mutexArtista.acquire();	// non appena l'artista è libero eseguirà la primitiva acquire sul mutex, iniziando così un nuovo ritratto
					System.out.println("Ritratto per il cliente " + id + " iniziato");
					
					try {
						Thread.sleep(5000, 10000);	//tempo di sleep per eseguire il ritratto
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Ritratto per il cliente " + id + " finito");
					Main.mutexArtista.release();	//l'artista ora può rieseguire "acquire" sul mutex di un nuovo cliente
					Main.semaforoSedie.release();	//una sedia è così libera
				}
				else {
					System.out.println("Il cliente " + id + " ha aspettato troppo e se ne è andato");	//...in caso contrario, il cliente, ormai stufo, rinuncia a farsi fare il ritratto
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
