package isoladinosauri.modellodati;

public abstract class Dinosauro extends Organismo implements Animale {
	
	protected int dimensione;
	protected int vitaTurni;
	protected int turnoNascita;
	
	public abstract int calcolaForza();
	
	public abstract void deponi(); //toglie energia per deposizione
	public abstract void muovi(int posX, int posY); //toglie energie per movimento
	public abstract void aumentaDimensione(int dimensione); //...e toglie energia per dimensione

}
