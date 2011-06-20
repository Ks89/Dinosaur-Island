package isoladinosauri.modellodati;


/**
 * Classe che identifica un dinosauro Erbivoro,
 * differente da Carnivoro per il metodo calcolaForza() e 
 * per il fatto che puo' solo combattere contro Dinosauri
 * invece puo' mangiare Occupanti (Vegetazione a runtime).
 */
public class Erbivoro extends Dinosauro {

	/**
	 * @param id identificativo del dinosauro composto da una String di 2 elementi "XY", dove 'X'=id giocatore e 'Y'=numero dinosauro.
	 * @param riga int che rappresenta la riga della mappa in cui si trova il dinosauro.
	 * @param colonna int che rappresenta la colonna della mappa in cui si trova il dinosauro.
	 * @param turnoNascita int che rappresenta il turno della partita in cui e' stato creato il Carnivoro.
	 * 			Lo scopo di questo valore e' quello di rendere molto semplice il calcolo dell'eta' del dinosauro.
	 */
	public Erbivoro(String id, int riga, int colonna, int turnoNascita) {
		super(id, riga, colonna, turnoNascita);
	}

	@Override
	public int calcolaForza() {
		return (super.getEnergia() * super.getEnergiaMax()/1000);
	}

	@Override
	public boolean mangia(Occupante occupante) {
		//questo metodo e' chiamato SOLO se this si e' mosso su una cella con un occupante.
		//se e' un vegetale la mangio e restituisco true o false in base al fatto che potrei non averlo
		
		//mangio tutto il vegetale
		if (occupante instanceof Vegetale) {
			Vegetale vegetale = (Vegetale)occupante;
			if(vegetale.getEnergia()<=(super.getEnergiaMax() - super.getEnergia())) {
				super.setEnergia(super.getEnergia() + vegetale.getEnergia());
				return true; //avvisa di rimuovere il vegetale
			}
			
			//mangio solo una parte del vegetale	 
			else {
				// il vegetale sara' consumato della diff dell'energia max e quella attuale del dino
				vegetale.setEnergia(vegetale.getEnergia() - (super.getEnergiaMax() - super.getEnergia()));
				//il dinosauro avra' la sua energia al massimo
				super.setEnergia(super.getEnergiaMax());
				return false; //avvisa di NON rimuovere il vegetale
			}
		}
		return false;
	}

	@Override
	public boolean combatti(Dinosauro dinosauro)  {
		//e' il dinosauro erbivoro a muoversi su una cella per combattere con un CARNIVORO
		Dinosauro nemico;
		if(dinosauro instanceof Carnivoro) {
			nemico = (Carnivoro)dinosauro;
		} else {
//			if(dinosauro instanceof Erbivoro) { 
				nemico = (Erbivoro)dinosauro;
//			}
		}
		
		if(this.calcolaForza()>=nemico.calcolaForza()) {
			//l'erbivoro vince il combattimento e sconfigge il carnivoro, ma non lo mangia
			return true; //avvisa di rimuovere il carnivoro, cioe' l'attaccato
		}
		else {
			//l'erbivoro perde il combattimento e l'attaccato Carnivoro lo mangia
			if(((int)(0.75 * super.getEnergia()))<=(nemico.getEnergiaMax() - nemico.getEnergia())) {
				nemico.setEnergia(nemico.getEnergia() + ((int)(0.75 * super.getEnergia())));
			} else {
				nemico.setEnergia(nemico.getEnergiaMax());
			}
			return false; //avvisa di rimuovere l'erbivoro, cioe' l'attaccante
		}
	}

}
