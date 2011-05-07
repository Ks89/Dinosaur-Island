package isoladinosauri.modellodati;

import isoladinosauri.Cella;

public class Carnivoro extends Dinosauro {

	@Override
	public int calcolaForza() {
		return (2 * super.energia * super.dimensione);
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

	@Override
	public void aumentaDimensione() { /* questa funzione e' uguale a quella dell'erbivoro,forse meglio spostarla nella superclasse */
		if(super.dimensione < 5) {
			super.dimensione++;
			super.energia -= super.energiaMax / 2;
		}
		/* manca l'else */
	}
	
	public void mangia(Animale animale, Cella cella) {
		// mangia un animale che puo essere Dinosauro o una carogna
		//NB: passo anche la Cella per sapere dove si trova l'animale e la carogna
		//in modo che possa rimuoverli nel caso uno dei 2 muoia/si esaurisca
		if (animale instanceof Carogna) {
			Carogna mangiato = (Carogna)animale;
			//mangio tutta la carogna
			if(mangiato.getEnergia()<=(this.getEnergiaMax() - this.getEnergia())) {
				this.setEnergia(this.getEnergia() + mangiato.getEnergia());
				//rimuovi la carogna
				cella.setOccupante(null);
			}
			//mangio solo una parte della carogna	 
			if(mangiato.getEnergia()>(this.getEnergiaMax() - this.getEnergia())) {
				//il dinosauro avra la sua energia al massimo
				this.setEnergia(this.getEnergiaMax());
				// la carogna sara consumata della diff dell'energia max e quella attuale del dino
				mangiato.setEnergia(mangiato.getEnergia() - this.getEnergiaMax() - this.getEnergia());
			}		
			
		} else if (animale instanceof Dinosauro) {
			Dinosauro mangiato = (Dinosauro)animale;
			//TODO eseguono il combattimento
		}
			
	}

}
