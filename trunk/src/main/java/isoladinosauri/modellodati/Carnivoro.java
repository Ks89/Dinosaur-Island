package isoladinosauri.modellodati;

import isoladinosauri.Cella;

/**
 * Classe che identifica un dinosauro Carnivoro,
 * differente da Erbivoro per il metodo calcolaForza() e 
 * per il modo in cui mangia Dinosauri e Occupanti
 */
public class Carnivoro extends Dinosauro {

	public Carnivoro(String id, int posX, int posY, int turnoNascita) {
		super(id, posX, posY, turnoNascita);
	}

	@Override
	public int calcolaForza() {
		return (2 * super.energia * super.dimensione);
	}

	@Override
	public void mangia(Cella cella) {
		//questo metodo e' chiamato SOLO se this si e' mosso su una cella con un occupante
		Occupante occupante = cella.getOccupante();
		
		//se e' un caragona
		if (occupante instanceof Carogna) {
			//mangia un animale che puo essere Dinosauro o una carogna
			//NB: passo anche la Cella per sapere dove si trova l'animale e la carogna
			//in modo che possa rimuoverli nel caso uno dei 2 muoia/si esaurisca
			Carogna carogna = (Carogna)occupante;
			//mangio tutta la carogna
			if(carogna.getEnergia()<=(super.getEnergiaMax() - super.getEnergia())) {
				super.setEnergia(super.getEnergia() + carogna.getEnergia());
				//rimuovi la carogna
				cella.setOccupante(null);
			}
			//mangio solo una parte della carogna	 
			else {
				// la carogna sara consumata della diff dell'energia max e quella attuale del dino
				carogna.setEnergia(carogna.getEnergia() - (super.getEnergiaMax() - super.getEnergia()));
				//il dinosauro avra la sua energia al massimo
				super.setEnergia(super.getEnergiaMax());
			}		
		} 

	
	}
	
	@Override
	public void combatti(Cella cella)  {
		Dinosauro dinosauro =  cella.getDinosauro();
		
		//il dinosauro carnivoro a muoversi su una cella per combattere con un erbivoro
		if (dinosauro instanceof Erbivoro) {
			Erbivoro nemico = (Erbivoro)dinosauro;
			if(this.calcolaForza()>=nemico.calcolaForza()) {
				//il carnivoro vince il combattimento e mangia l'erbivoro
				if(nemico.getEnergia()<=(super.getEnergiaMax() - super.getEnergia()))
					super.setEnergia(super.getEnergia() + ((int)(0.75 * nemico.getEnergia())));
				else super.setEnergia(super.getEnergiaMax());
				cella.setDinosauro(this);
			}
			else {
				//il carnivoro perde il combattimento e l'erbivoro non fa nulla
				cella.setDinosauro(nemico);
			}
		}
		//il dinosauro carnivoro a muoversi su una cella per combattere con un altro carnivoro
		if (dinosauro instanceof Carnivoro) {	
			Carnivoro nemico = (Carnivoro)dinosauro;
			if(this.calcolaForza()>=nemico.calcolaForza()) {
				//il carnivoro vince il combattimento e mangia l'altro carnivor
				if(nemico.getEnergia()<=(super.getEnergiaMax() - super.getEnergia()))
					super.setEnergia(super.getEnergia() + ((int)(0.75 * nemico.getEnergia())));
				else super.setEnergia(super.getEnergiaMax());
				cella.setDinosauro(this);
			}
			else {
				//il carnivoro perde il combattimento e l'erbivoro non fa nulla
				nemico.setEnergia(nemico.getEnergia() + ((int)(0.75 * super.getEnergia())));
				cella.setDinosauro(nemico);
			}
		}			
		if(this.calcolaForza()>=dinosauro.calcolaForza()) {
			//l'erbivoro vince il combattimento e sconfigge il carnivoro, ma non lo mangia
			cella.setDinosauro(this);
		}
		else {
			//FIXME GRANDE DUBBIO: l'erbivoro perde il combattimento e il carnivoro non fa nulla (anche se mi sembra strano)
			cella.setDinosauro(dinosauro);
		}
	}
}
