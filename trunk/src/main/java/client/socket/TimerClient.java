package client.socket;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.io.IOException;
import java.util.TimerTask;


public class TimerClient extends TimerTask {
//	private DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");

	private Gui gui;
	
	public TimerClient(Gui gui) {
		this.gui = gui;
	}
	
	public void run() {	
		try {
			this.gui.getClientGui().passaTurno();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println(formatter.format(new Date()));
	}
 }