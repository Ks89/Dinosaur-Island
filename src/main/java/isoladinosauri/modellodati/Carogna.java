package isoladinosauri.modellodati;

import java.util.Random;

public class Carogna extends Organismo implements Occupante, Animale {
	
	public Carogna() {
		Random random = new Random();
		super.setEnergiaMax(random.nextInt(300) + 350);
	}
	
	public void consuma() {
		super.energia -= super.energiaMax/10;
	}

}
