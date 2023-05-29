package pack;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

	public static int MIN_WAIT = 1000; 	//minimo intervallo tra l'arrivo di un cliente ed un altro
	public static int MAX_WAIT = 3000;	//massimo intervallo tra l'arrivo di un cliente ed un altro
	static Random rand = new Random();
	public static int nClienti = rand.nextInt(50);	//numero randomico di clienti con massimo 50
	public static int id = 1;	//id sequenziale per ogni cliente
	
	public static Semaphore mutexArtista = new Semaphore(1); 	//mutex per l'artista
    public static Semaphore semaforoSedie = new Semaphore(4);	//semaforo a conteggio per le sedie
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(nClienti + " clienti sono in arrivo...\n");
		
	    for (int i = 0; i < nClienti; i++) {	//ciclo for adibito all'istanziamento dei clienti necessari che tenteranno di eseguire l'acquire del semaforo delle sedie in un determinato intervallo di tempo
	    	Cliente cliente = new Cliente(id++);
	    	cliente.start();
	    	System.out.println("Cliente " + (id -1) + " in attesa di sedersi");
	    	
	    	try {
				Thread.sleep(MIN_WAIT, MAX_WAIT);	//try and catch adibito a gestire l'intervallo tra l'arrivo dei clienti
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
	    }
	}

}

