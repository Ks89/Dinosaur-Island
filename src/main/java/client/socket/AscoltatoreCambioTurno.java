package client.socket;

import java.io.IOException;

import javax.swing.JOptionPane;

public class AscoltatoreCambioTurno extends Thread {

	private Gui gui;

	public AscoltatoreCambioTurno(Gui gui) {
		this.gui = gui;
	}

	public void run() {
		while(true) {
			try {
				String messaggioServer = this.gui.getClientGui().getRispostaServerTurno();
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
