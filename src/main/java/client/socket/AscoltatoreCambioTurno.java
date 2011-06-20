package client.socket;

import java.io.IOException;

public class AscoltatoreCambioTurno extends Thread {

	private Gui gui;

	public AscoltatoreCambioTurno(Gui gui) {
		this.gui = gui;
	}

	public void run() {
		while(true) {
			try {
				System.out.println(this.gui.getClientGui().getRispostaServerTurno());
				System.out.println("risposta broadcast: " + this.gui.getClientGui().getRispostaServerTurno());
				
				//qui c'e' qualche cosa che non va
				
				if(this.gui.getClientGui().getRispostaServerTurno().contains("@cambioTurno") ) {
					System.out.println("entrato nell'if 2");
					System.out.println("User da confrontare in ascoltatorecambioturno: " + this.gui.getClientGui().getNomeUtente());
					if(this.gui.getClientGui().getRispostaServerTurno().split(",")[1].equals(this.gui.getClientGui().getNomeUtente())) {
						this.gui.attivaAzioniGui();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
