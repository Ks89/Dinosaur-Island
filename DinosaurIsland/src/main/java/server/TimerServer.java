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

package server;

import java.util.TimerTask;


/**
 * Classe per la gestione del Timer
 */
public class TimerServer extends TimerTask {

	private GestoreClient ch;
	
	/**
	 * Costruttore che inizializza il riferimento a GestoreClient.
	 */
	public TimerServer(GestoreClient ch) {
		this.ch = ch;
	}
	
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	public void run() {	
		//invio il messaggio in broadcast
		this.ch.cambioTurno();
	}
 }