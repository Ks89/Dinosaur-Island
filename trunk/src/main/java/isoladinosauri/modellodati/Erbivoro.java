package isoladinosauri.modellodati;

import isoladinosauri.Cella;

public class Erbivoro extends Dinosauro {

	@Override
	public int calcolaForza() {
		return (super.energia * super.dimensione);
	}

	@Override
	public void deponi() {
		super.energia -= 1500;
		/* manca la gestione del nuovo dinosauro (uovo) */
	}

	@Override
	public void muovi(int posX, int posY) {
		super.energia -= 10 * (int)Math.pow(2, (double)super.dimensione);
		/* manca la gestione del movimento con le coordinate e la verifica se la destinazione e' raggiungibile */
	}
	
	public void mangia(Vegetale vegetale, Cella cella) {
		// mangia un vegetale
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
			// il vegetale sara consumato della diff dell'energia max e quella attuale del dino
			vegetale.setEnergia(vegetale.getEnergia() - (this.getEnergiaMax() - this.getEnergia()));
			//il dinosauro avra la sua energia al massimo
			this.setEnergia(this.getEnergiaMax());
		}		
	}

}
