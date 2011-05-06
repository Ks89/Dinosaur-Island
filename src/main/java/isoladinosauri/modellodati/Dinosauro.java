package isoladinosauri.modellodati;

public abstract class Dinosauro extends Organismo implements Animale {
	
	protected int dimensione;
	protected int vitaTurni;
	protected int turnoNascita;
	
	public abstract int calcolaForza();
	
	public abstract void deponi(); //toglie energia per deposizione
	public abstract void muovi(int posX, int posY); //toglie energie per movimento
	
	public void aumentaDimensione() {
		if(dimensione < 5) {
			dimensione++;
			super.energia -= super.energiaMax / 2;
		}
		/* manca l'else perche il liv massimo e' 5*/
		//creare un else che avvisa l'utente che la dim massima e' 5
	}
}
