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
	
	public Occupante getOccupante() {
		return occupante;
	}
	
	public void setOccupante(Occupante occupante) {
		this.occupante = occupante;
	}
	
	public Dinosauro getDinosauro() {
		return dinosauro;
	}
	
	public void setDinosauro(Dinosauro dinosauro) {
		this.dinosauro = dinosauro;
	}
}
