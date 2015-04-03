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