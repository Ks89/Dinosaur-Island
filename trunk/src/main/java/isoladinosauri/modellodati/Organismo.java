package isoladinosauri.modellodati;

/**
 * Classe ASTRATTA che identifica un generico Organismo,
 * il quale puo' essere un Dinosauro, un Vegetale o una Carogna
 * Possiede solo metodi astratti comuni a tutte le sottoclassi 
 */
public abstract class Organismo {
	
	private int riga;
	private int colonna;
	private int energiaMax;
	private int energia;
	
	/**
	 * @return the energiaMax
	 */
	public int getEnergiaMax() {
		return energiaMax;
	}
	/**
	 * @param energiaMax the energiaMax to set
	 */
	public void setEnergiaMax(int energiaMax) {
		this.energiaMax = energiaMax;
	}
	/**
	 * @return the energia
	 */
	public int getEnergia() {
		return energia;
	}
	/**
	 * @param energia the energia to set
	 */
	public void setEnergia(int energia) {
		this.energia = energia;
	}
	/**
	 * @return the riga
	 */
	public int getRiga() {
		return riga;
	}
	/**
	 * @param riga the riga to set
	 */
	public void setRiga(int riga) {
		this.riga = riga;
	}
	/**
	 * @return the colonna
	 */
	public int getColonna() {
		return colonna;
	}
	/**
	 * @param colonna the colonna to set
	 */
	public void setColonna(int colonna) {
		this.colonna = colonna;
	}
	
	

}
