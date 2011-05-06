package isoladinosauri.modellodati;

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
	
	public void mangia(Animale animale) {
		if (animale instanceof Carogna) {
			Carogna mangiato = (Carogna)animale;
			//TODO
			
		} else if (animale instanceof Dinosauro) {
			Dinosauro mangiato = (Dinosauro)animale;
			//TODO
		}
			
	}

}
