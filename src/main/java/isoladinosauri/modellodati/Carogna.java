package isoladinosauri.modellodati;

import java.util.Random;


/**
 * Classe che identifica una carogna,
 * differente da Vegetazione principalmente per
 * il metodo consuma() e per l'energiaMax casuale
 */
public class Carogna extends Organismo implements Occupante, Animale {
	
	public Carogna() {
		Random random = new Random();
		super.setEnergiaMax(random.nextInt(301) + 350);
		super.setEnergia(super.getEnergiaMax());
	}
	
	public void consuma() {
		if(super.getEnergiaMax()/20<=super.getEnergia()) 
			super.setEnergia(super.getEnergia() - super.getEnergiaMax()/20);
		else super.setEnergia(0);
	}

}
