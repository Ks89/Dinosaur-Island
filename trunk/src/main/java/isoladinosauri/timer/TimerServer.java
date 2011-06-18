package isoladinosauri.timer;

import isoladinosauri.ClientHandler;

import java.util.TimerTask;


public class TimerServer extends TimerTask {

	private ClientHandler ch;
	
	public TimerServer(ClientHandler ch) {
		this.ch = ch;
	}
	
	public void run() {	
		//invio il messaggio in broadcast
		this.ch.cambioTurno();
	}
 }