package isoladinosauri.modellodati;

import isoladinosauri.Cella;

import java.util.Random;

public abstract class Dinosauro extends Organismo implements Animale {
	
	protected int dimensione;
	protected int durataVita;
	protected int turnoNascita;
	
	//ESISTONO I COSTRUTTORI IN CLASSI ASTRATTE CHE POI POSSONO IMPLEMENTARE
	//LE SOTTOCLASSI? BOH, SE SI PUO' SAREBBE BELLO FARLO
	
	public abstract int calcolaForza();
	
	public abstract void deponi(); //toglie energia per deposizione
	public abstract boolean aggCordinate(int posX, int posY); //toglie energie per movimento
	
	public void aumentaDimensione() {
		if(dimensione < 5) {
			dimensione++;
			super.energia -= super.energiaMax / 2;
		}
		/* manca l'else perche il liv massimo e' 5*/
		//creare un else che avvisa l'utente che la dim massima e' 5
	}
}
