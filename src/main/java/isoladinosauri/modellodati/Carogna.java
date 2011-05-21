package isoladinosauri.modellodati;

import java.util.Random;

public class Carogna extends Organismo implements Occupante, Animale {
	
	public Carogna() {
		Random random = new Random();
		super.setEnergiaMax(random.nextInt(301) + 350);
		super.setEnergia(super.getEnergiaMax());
	}
	
	public void consuma() {
		super.energia -= super.energiaMax/20;
	}

}
