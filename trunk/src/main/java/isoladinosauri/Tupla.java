package isoladinosauri;

/**
 * Questa classe costituisce una singola riga della classifica dei giocatori
 */
public class Tupla {
	
	private String nomeUtente;
	private String nomeSpecie;
	private int punti;
	private String stato;
	
	public String getNomeUtente() {
		return nomeUtente;
	}
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
	public String getNomeSpecie() {
		return nomeSpecie;
	}
	public void setNomeSpecie(String nomeSpecie) {
		this.nomeSpecie = nomeSpecie;
	}
	public int getPunti() {
		return punti;
	}
	public void setPunti(int punti) {
		this.punti = punti;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	
}
