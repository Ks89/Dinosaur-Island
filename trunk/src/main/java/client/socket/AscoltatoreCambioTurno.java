package client.socket;

import java.io.IOException;

import javax.swing.JOptionPane;

/**
 *	Classe che si occupa di ascoltare il server per intercettare i messaggi di CambioTurno.
 *  Funziona su un thread a parte ed utilizza un socket differente da quello per la comunicazione.
 *  La porta è ovviamente differente.
 */
public class AscoltatoreCambioTurno extends Thread {

	private Gui gui;

	/**
	 * Costruttore della classe AscoltatoreCambioTurno.
	 * @param gui riferimanto per inizializzare Gui.
	 */
	public AscoltatoreCambioTurno(Gui gui) {
		this.gui = gui;
	}

	public void run() {
		while(true) {
			try {
				String messaggioServer = this.gui.getClientGui().getBroadcastServerTurno();
				System.out.println("risposta broadcast: " + messaggioServer);
				
				if(messaggioServer.contains("@cambioTurno") ) {
					System.out.println("User da confrontare in ascoltatorecambioturno: " + this.gui.getClientGui().getNomeUtente());
					if(messaggioServer.split(",")[1].equals(this.gui.getClientGui().getNomeUtente())) {
						this.gui.ottieniIlTurno();
					}
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"InterruptedException");
			} catch (InterruptedException e) {
				JOptionPane.showMessageDialog(null,"InterruptedException");
			}
		}
		
	}

}
