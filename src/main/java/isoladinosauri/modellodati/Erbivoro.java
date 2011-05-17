package isoladinosauri.modellodati;

import java.util.Random;

import isoladinosauri.Cella;

public class Erbivoro extends Dinosauro {

	@Override
	public int calcolaForza() {
		return (super.energia * super.dimensione);
	}
	
	public Erbivoro(String id, int posX, int posY, int turnoNascita) {
		super.setId(id);
		super.setEnergia(750);
		super.energiaMax=1000;
		super.posX = posX;
		super.posY = posY;
		super.dimensione=1;
		Random random = new Random();
		super.durataVita = random.nextInt(13) + 24;
		super.turnoNascita = turnoNascita;
		super.setEtaDinosauro(0);
	}
	
	public void mangia(Vegetale vegetale, Cella cella) {
		//mangia un vegetale
		//NB: passo anche la Cella per sapere dove si trova il vegetale
		//in modo che possa rimuoverli nel caso uno dei 2 muoia/si esaurisca
		//mangio tutto il vegetale
		if(vegetale.getEnergia()<=(this.getEnergiaMax() - this.getEnergia())) {
			this.setEnergia(this.getEnergia() + vegetale.getEnergia());
			//rimuovo il vegetale perche' mangiato tutto
			cella.setOccupante(null);
		}
		//mangio solo una parte del vegetale	 
		else {
			// il vegetale sara' consumato della diff dell'energia max e quella attuale del dino
			vegetale.setEnergia(vegetale.getEnergia() - (this.getEnergiaMax() - this.getEnergia()));
			//il dinosauro avra' la sua energia al massimo
			this.setEnergia(this.getEnergiaMax());
		}		
	}

}
