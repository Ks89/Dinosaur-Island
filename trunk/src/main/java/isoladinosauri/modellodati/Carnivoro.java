package isoladinosauri.modellodati;

import isoladinosauri.Cella;

public class Carnivoro extends Dinosauro {

	public Carnivoro(String id, int posX, int posY, int turnoNascita) {
		super(id, posX, posY, turnoNascita);
	}

	@Override
	public int calcolaForza() {
		return (2 * super.energia * super.dimensione);
	}

	
	public void mangia(Animale animale, Cella cella) {
		//questo metodo e' chiamato SOLO se this si e' mosso su una cella con un altro dino
		
		//se e' un caragona
		if (animale instanceof Carogna) {
			//mangia un animale che puo essere Dinosauro o una carogna
			//NB: passo anche la Cella per sapere dove si trova l'animale e la carogna
			//in modo che possa rimuoverli nel caso uno dei 2 muoia/si esaurisca
			Carogna mangiato = (Carogna)animale;
			//mangio tutta la carogna
			if(mangiato.getEnergia()<=(super.getEnergiaMax() - super.getEnergia())) {
				super.setEnergia(super.getEnergia() + mangiato.getEnergia());
				//rimuovi la carogna
				cella.setOccupante(null);
			}
			//mangio solo una parte della carogna	 
			else {
				// la carogna sara consumata della diff dell'energia max e quella attuale del dino
				mangiato.setEnergia(mangiato.getEnergia() - (super.getEnergiaMax() - super.getEnergia()));
				//il dinosauro avra la sua energia al massimo
				super.setEnergia(super.getEnergiaMax());
			}		
		} 
		
		//il dinosauro carnivoro a muoversi su una cella con un erbivoro
		if (animale instanceof Erbivoro) {
			Erbivoro nemico = (Erbivoro)animale;
			if(this.calcolaForza()>=nemico.calcolaForza()) {
				//il carnivoro vince il combattimento e mangia l'erbivoro
				cella.setDinosauro(this);
				super.setEnergia(super.getEnergia() + ((int)0.75 * nemico.getEnergia()));
			}
			else {
				//il carnivoro perde il combattimento e l'erbivoro non fa nulla
				cella.setDinosauro(nemico);
			}
		}
		
		//il dinosauro carnivoro a muoversi su una cella con un altro carnivoro
		if (animale instanceof Carnivoro) {	
			Carnivoro nemico = (Carnivoro)animale;
			if(this.calcolaForza()>=nemico.calcolaForza()) {
				//il carnivoro vince il combattimento e mangia l'altro carnivoro
				cella.setDinosauro(this);
				this.setEnergia(super.getEnergia() + ((int)0.75 * nemico.getEnergia()));
			}
			else {
				//il carnivoro perde il combattimento e l'erbivoro non fa nulla
				cella.setDinosauro(nemico);
				nemico.setEnergia(nemico.getEnergia() + ((int)0.75 * super.getEnergia()));
			}
		}			
	}
}
