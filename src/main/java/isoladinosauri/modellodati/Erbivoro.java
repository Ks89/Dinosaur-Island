package isoladinosauri.modellodati;

import isoladinosauri.Cella;

/**
 * Classe che identifica un dinosauro Erbivoro,
 * differente da Carnivoro per il metodo calcolaForza() e 
 * per il fatto che puo' solo combattere contro Dinosauri
 * invece puo' mangiare Occupanti (Vegetazione a runtime)
 */
public class Erbivoro extends Dinosauro {

	public Erbivoro(String id, int posX, int posY, int turnoNascita) {
		super(id, posX, posY, turnoNascita);
	}

	@Override
	public int calcolaForza() {
		return (super.energia * super.dimensione);
	}

	@Override
	public void mangia(Cella cella) {
		//mangia un vegetale
		//NB: passo anche la Cella per sapere dove si trova il vegetale
		//in modo che possa rimuoverli nel caso uno dei 2 muoia/si esaurisca
		//mangio tutto il vegetale
		Occupante occupante = cella.getOccupante();

		if (occupante instanceof Vegetale) {
			Vegetale vegetale = (Vegetale)occupante;
			if(vegetale.getEnergia()<=(super.getEnergiaMax() - super.getEnergia())) {
				super.setEnergia(super.getEnergia() + vegetale.getEnergia());
				//rimuovo il vegetale perche' mangiato tutto
				cella.setOccupante(null);
			}
			//mangio solo una parte del vegetale	 
			else {
				// il vegetale sara' consumato della diff dell'energia max e quella attuale del dino
				vegetale.setEnergia(vegetale.getEnergia() - (super.getEnergiaMax() - super.getEnergia()));
				//il dinosauro avra' la sua energia al massimo
				super.setEnergia(super.getEnergiaMax());
			}
		}
	}

	@Override
	public void combatti(Cella cella)  {
		Dinosauro dinosauro =  cella.getDinosauro();
		Carnivoro nemico = (Carnivoro)dinosauro;
		if(this.calcolaForza()>=nemico.calcolaForza()) {
			//l'erbivoro vince il combattimento e sconfigge il carnivoro, ma non lo mangia
			cella.setDinosauro(this);
		}
		else {
			//FIXME GRANDE DUBBIO: l'erbivoro perde il combattimento e il carnivoro non fa nulla (anche se mi sembra strano)
			cella.setDinosauro(nemico);
		}
	}

}
