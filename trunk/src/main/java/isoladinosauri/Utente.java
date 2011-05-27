package isoladinosauri;

/**
 * Questa classe viene utilizzata solo per gestire 
 * il nomeUtente e la password dell'utente su cui 
 * puo' essere poi creato un giocatore in una partita
 * La differenza e' che Utente identifica l'ACCOUNT
 * del giocatore, mentre la classe Giocatore l'identita'
 * creata dall'Utente nella partita in corso.
 */
public class Utente {
	
	private Giocatore giocatore;
	private String nomeUtente;
	private String password;
	
	public Utente(String nomeUtente, String password) {
		this.setNomeUtente(nomeUtente);
		this.setPassword(password);
	}
	
	public Giocatore getGiocatore() {
		return giocatore;
	}

	public void setGiocatore(Giocatore giocatore) {
		this.giocatore = giocatore;
	}
	
	public String getNomeUtente() {
		return nomeUtente;
	}
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
