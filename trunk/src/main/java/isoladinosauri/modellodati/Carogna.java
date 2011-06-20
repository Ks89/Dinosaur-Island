package isoladinosauri.modellodati;

import java.util.Random;


/**
 * Classe che identifica una carogna,
 * differente da Vegetazione principalmente per
 * il metodo consuma() e per l'energiaMax casuale.
 */
public class Carogna extends Organismo implements Occupante {
	
	/**
	 * Costruttore senza parametri che inizializza la Carogna con : 
	 * una energiaMax tra 350 e 650
	 * e un'energia uguale a quella massima.
	 */
	public Carogna() {
		Random random = new Random();
		super.setEnergiaMax(random.nextInt(301) + 350);
		super.setEnergia(super.getEnergiaMax());
	}
	
	/**
	 * Metodo senza parametri che diminuisce la vita di una Carogna
	 * del valore: energiaMax/20.
	 */
	public void consuma() {
		if(super.getEnergiaMax()/20<=super.getEnergia()) {
			super.setEnergia(super.getEnergia() - super.getEnergiaMax()/20);
		} else {
			super.setEnergia(0);
		}
	}

}
