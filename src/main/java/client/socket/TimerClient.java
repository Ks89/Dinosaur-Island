package client.socket;

import java.io.IOException;
import java.util.TimerTask;

import javax.swing.JOptionPane;

/**
 * Classe per la gestione del Timer sul Client.
 */
public class TimerClient extends TimerTask {

	private Gui gui;
	
	/**
	 * Costruttore della classe TimerClient per impostare il riferimanto a Gui.
	 * @param gui riferimanto alla classe Gui.
	 */
	public TimerClient(Gui gui) {
		this.gui = gui;
	}
	
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	public void run() {	
		try {
			this.gui.getClientGui().passaTurno();
		} catch (IOException ec) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ec) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
	}
 }