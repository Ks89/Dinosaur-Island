package server;



import java.util.TimerTask;


public class TimerServer extends TimerTask {

	private GestoreClient ch;
	
	public TimerServer(GestoreClient ch) {
		this.ch = ch;
	}
	
	public void run() {	
		//invio il messaggio in broadcast
		this.ch.cambioTurno();
	}
 }