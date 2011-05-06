package isoladinosauri.modellodati;

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

	public void mangia(Vegetale vegetale) {
		//TODO
	}

}
