package server;


import java.util.ArrayList;
import java.util.List;

import server.logica.Giocatore;

/**
 * Classe per gestire gli Utenti loggati e le specie create.
 */
public class GestioneGiocatori {
	private List<String> tokenLoggati;
	private List<Giocatore> giocatoriCreati;

	/**
	 * Costruttore della classe GestioneGiocatori per inizializzare gli ArrayList di giocatoriCreati e tokenLoggati.
	 */
	public GestioneGiocatori() {
		this.giocatoriCreati = new ArrayList<Giocatore>();
		this.tokenLoggati = new ArrayList<String>();
	}
	
	/**
	 * @return Un ArrayList di Giocatore contenente i Giocatori creati.
	 */
	public List<Giocatore> ottieniGiocatoriCreati() {
		return this.giocatoriCreati;
	}
	
	/**
	 * Metodo per aggiungere un Giocatore (specie) alla lista di quelli creati.
	 * @param giocatore Giocatore da aggiungere alla lista di quelli creati.
	 */
	public void aggiungiGiocatoreCreato(Giocatore giocatore) {
		this.giocatoriCreati.add(giocatore);
	}
	
	/**
	 * @return Un ArrayList di String contenente i Token (utente-password) loggati.
	 */
	public List<String> ottieniTokenLoggati() {
		return this.tokenLoggati;
	}
	
	/**
	 * Metodo per aggiungere un Token (utente-password) alla lista di quelli loggati.
	 * @param utente String che rappresenta l'utente associato al Token.
	 * @param token String che rappresenta il Token, composta da utente-password.
	 */
	public void aggiungiTokenLoggati(String utente, String token) {
		this.tokenLoggati.add(utente+" "+token);
	}
	
	/**
	 * Metodo per rimuovere un Token loggato.
	 * @param utente String che rappresenta l'utente associato al Token.
	 * @param token String che rappresenta il Token, composta da utente-password.
	 */
	public void rimuoviTokenLoggati(String utente, String token) {
		this.tokenLoggati.remove(utente+" "+token);
	}
	
	/**
	 * Metodo per ottenre l'indice nella lista dei Giocatori creati partendo dal suo nome Utente.
	 * @param nomeUtente String che rappresenta l'utente associato al Token.
	 * @return Un int che rappresenta l'indice nella lista dei Giocatori creati.
	 */
	public int indiceGiocatoreCreato(String nomeUtente) {
		int k=0;
		if(this.ottieniGiocatoriCreati().isEmpty()) {
			return -1;
		}
		//cerco l'indice del giocatore nell'array dei giocatoriCreati
		for(k=0; k<this.ottieniGiocatoriCreati().size(); k++) {
			if(this.ottieniGiocatoriCreati().get(k).getUtente().getNomeUtente()!=null) {
				if(this.ottieniGiocatoriCreati().get(k).getUtente().getNomeUtente().equals(nomeUtente)) {
					break;
				}
			}
		}
		return k;
	}
	
	/**
	 * Metodo per ottenre l'indice nella lista dei Token loggati partendo da un nome utente e dal Token associato.
	 * @param nomeUtente String che rappresenta l'utente associato al Token.
	 * @param token String che rappresenta il Token, composta da utente-password.
	 * @return Un int che rappresenta l'indice nella lista dei Token loggati.
	 */
	public int indiceTokenLoggato(String nomeUtente, String token) {
		int k=0;
		if(this.ottieniTokenLoggati().isEmpty()) {
			return -1;
		}
		
		for(k=0; k<this.ottieniTokenLoggati().size(); k++) {
			if(this.ottieniTokenLoggati().get(k)!=null) {
				if(this.ottieniTokenLoggati().get(k).equals(nomeUtente+" "+token)) {
					break;
				}
			}
		}
		return k;
	}
	
	/**
	 * Metodo per controllare se un Token e' loggato.
	 * @param token String che rappresenta il Token, composta da utente-password.
	 * @return Un boolean: 'true' - loggato, 'false' - non loggato.
	 */
	public boolean controlloSeLoggato(String token) {
		if(this.ottieniTokenLoggati().isEmpty()) {
			return false;
		}
		//cerco se l'utente e' gia' loggato
		boolean loggato = false;
		for(int i=0; i<this.ottieniTokenLoggati().size(); i++) {
			if(this.ottieniTokenLoggati().get(i)!=null) {
				if(this.ottieniTokenLoggati().get(i).split(" ")[1].equals(token)) {
					loggato = true;
					break;
				}
			}
		}
		return loggato;
	}
}