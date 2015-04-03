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

package server.logica;

/**
 * Questa classe viene utilizzata solo per gestire 
 * il nome e la password dell'Utente su cui 
 * puo' essere poi creato un giocatore in una partita.
 * La differenza e' che Utente identifica l'ACCOUNT
 * del giocatore, mentre la classe Giocatore l'identita'
 * creata dall'Utente nella partita in corso,
 * cioe' quella associata alla specie.
 */
public class Utente {
	
	private String nomeUtente;
	private String password;
	
	/**
	 * Costruttore della classe Utente che riceve come paramentri due stringhe: il nome e la password.
	 * @param nomeUtente String per stabilire il nome per il login dell'Utente.
	 * @param password String per stabilire la password per il login dell'Utente.
	 */
	public Utente(String nomeUtente, String password) {
		this.setNomeUtente(nomeUtente);
		this.setPassword(password);
	}
	
	
	/**
	 * @return Una String che indica il nome dell'Utente.
	 */
	public String getNomeUtente() {
		return this.nomeUtente;
	}
	
	/**
	 * @param nomeUtente String che imposta il nome dell'Utente.
	 */
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
	
	/**
	 * @return Una String che indica la password dell'Utente.
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * @param password String che imposta la password dell'Utente.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	

}
