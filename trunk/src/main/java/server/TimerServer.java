package server;

import java.util.TimerTask;


/**
 * Classe per la gestione del Timer
 */
public class TimerServer extends TimerTask {

	private GestoreClient ch;
	
	/**
	 * Costruttore che inizializza il riferimento a GestoreClient.
	 */
	public TimerServer(GestoreClient ch) {
		this.ch = ch;
	}
	
	public void run() {	
		//invio il messaggio in broadcast
		this.ch.cambioTurno();
	}
 }