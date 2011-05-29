package isoladinosauri;
 
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Occupante;

/**
 * Classe usata per contenere due interfacce
 * Occupante: puo' essere vegetale o carogna a runtime
 * Dinosauro: puo' essere erbivoro o carnivoro a runtime
 * Permette la presenza di un dinosauro e un occupante sulla stessa cella
 * contemporaneamente, ma non di 2 occupanti e/o dinosauri.
 */

public class Cella {
	
	private Occupante occupante;
	private Dinosauro dinosauro;
	
	/**
	 * @return Il riferimento all'Occupante presente nella Cella.
	 */
	public Occupante getOccupante() {
		return occupante;
	}
	
	/**
	 * @param occupante L'Occupante che si vuole impostare nella Cella.
	 */
	public void setOccupante(Occupante occupante) {
		this.occupante = occupante;
	}
	
	/**
	 * @return Il riferimento al Dinosauro presente nella Cella.
	 */
	public Dinosauro getDinosauro() {
		return dinosauro;
	}
	
	/**
	 * @param dinosauro Il Dinosauro che si vuole impostare nella Cella.
	 */
	public void setDinosauro(Dinosauro dinosauro) {
		this.dinosauro = dinosauro;
	}
}
