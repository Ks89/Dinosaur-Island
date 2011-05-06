package isoladinosauri;
 
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Occupante;

public class Cella {
	
	private Occupante occupante; //puo essere vegetale o carogna
	private Dinosauro dinosauro; //puo essere dinosauro o carogna
	
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
