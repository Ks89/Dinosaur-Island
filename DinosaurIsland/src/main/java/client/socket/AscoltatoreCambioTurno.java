/*
Copyright 2011-2015 Stefano Cappa
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package client.socket;

import java.io.IOException;

import javax.swing.JOptionPane;

/**
 *	Classe che si occupa di ascoltare il server per intercettare i messaggi di CambioTurno.
 *  Funziona su un thread a parte ed utilizza un socket differente da quello per la comunicazione.
 *  La porta e' ovviamente differente.
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

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while(true) {
			try {
				String messaggioServer = this.gui.getClientGui().getBroadcastServerTurno();
				if(messaggioServer.contains("@cambioTurno") ) {
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
