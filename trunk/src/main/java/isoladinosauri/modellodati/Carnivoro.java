package isoladinosauri.modellodati;

public class Carnivoro extends Dinosauro {

	@Override
	public int calcolaForza() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deponi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void muovi(int posX, int posY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aumentaDimensione(int dimensione) {
		// TODO Auto-generated method stub

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
