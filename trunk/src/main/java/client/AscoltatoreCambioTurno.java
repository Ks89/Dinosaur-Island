package client;

import java.io.IOException;

public class AscoltatoreCambioTurno extends Thread {

	private Gui gui;

	public AscoltatoreCambioTurno(Gui gui) {
		this.gui = gui;
	}

	public void run() {
		while(true) {
//			try {
//				System.out.println(this.gui.getClientGui().getRispostaServer());
//				System.out.println("risposta server: " + this.gui.getClientGui().getRispostaServer());
				
				//qui c'e' qualche cosa che non va
				
//				if(this.gui.getClientGui().getRispostaServer().contains("@cambioTurno") ) {
//					System.out.println("entrato nell'if 2");
////					System.out.println("User da confrontare in ascoltatorecambioturno: " + this.gui.getClientGui().getNomeUtente());
//					if(this.gui.getClientGui().getRispostaServer().split(",")[1].equals(this.gui.getClientGui().getNomeUtente())) {
//						this.gui.attivaAzioniGui();
//					}
//				} else {
//					System.out.println("non sono entrato nell'if 2");
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		
	}

}
