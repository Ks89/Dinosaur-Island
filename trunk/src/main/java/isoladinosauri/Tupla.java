package isoladinosauri;

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
