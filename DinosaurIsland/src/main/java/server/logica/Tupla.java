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
 * Questa classe costituisce una singola riga della classifica dei giocatori.
 */
public class Tupla {
	
	private String nomeUtente;
	private String nomeSpecie;
	private int punti;
	private String stato;
	
	/**
	 * @return Una Stirng con il nome dell'Utente.
	 */
	public String getNomeUtente() {
		return nomeUtente;
	}
	
	/**
	 * @param nomeUtente - String per stabilire il nome dell'Utente.
	 */
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
	
	/**
	 * @return Una String che indica il nome della specie associata al Giocatore.
	 */
	public String getNomeSpecie() {
		return nomeSpecie;
	}
	
	/**
	 * @param nomeSpecie - String per stabilire il nome della specie associata al Giocatore.
	 */
	public void setNomeSpecie(String nomeSpecie) {
		this.nomeSpecie = nomeSpecie;
	}
	
	/**
	 * @return Un int che indica il punteggio del Giocatore.
	 */
	public int getPunti() {
		return punti;
	}
	
	/**
	 * @param punti - int per stabilire il punti che possiede il Giocatore.
	 */
	public void setPunti(int punti) {
		this.punti = punti;
	}
	
	/**
	 * @return Una String per stabilire lo stato del giocatore: 's' (online), 'n' (offline).
	 */
	public String getStato() {
		return stato;
	}
	
	/**
	 * @param stato - String per impostare lo stato del Giocatore.
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}
	
}
