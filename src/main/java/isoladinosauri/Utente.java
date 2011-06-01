package isoladinosauri;

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
