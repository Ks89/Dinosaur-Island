package isoladinosauri.modellodati;

import java.util.Random;

/**
 * Classe che identifica un vegetale,
 * differente da Carogna principalmente per
 * il metodo cresci() e per l'energiaMax casuale
 */
public class Vegetale extends Organismo implements Occupante {

	public Vegetale() {
		Random random = new Random();
		super.setEnergiaMax(random.nextInt(201) + 150);
		super.setEnergia(super.getEnergiaMax());
	}

	public void cresci() {
		if(super.getEnergiaMax()/20<=(super.getEnergiaMax()-super.getEnergia())) {
			super.setEnergia(super.getEnergia() + super.getEnergiaMax()/20);
		} else { 
			super.setEnergia(super.getEnergiaMax());
		}
	}	                                 
}
