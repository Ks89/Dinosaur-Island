package isoladinosauri.modellodati;

import java.util.Random;

public class Vegetale extends Organismo implements Occupante {
	
	public Vegetale() {
		Random random = new Random();
		super.setEnergiaMax(random.nextInt(201) + 150);
		super.setEnergia(0);
	}
	
	public void cresci() {
		if(super.getEnergiaMax()/20<=(super.getEnergiaMax()-super.getEnergia())) 
			super.setEnergia(super.getEnergia() + super.getEnergiaMax()/20);
		else super.setEnergia(super.getEnergiaMax());
	}	                                 
}
