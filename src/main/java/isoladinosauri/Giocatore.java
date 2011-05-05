package isoladinosauri;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;

import java.util.List;

public class Giocatore extends Utente {
	
	private Partita partita;
	private int vitaTurni;
	private int turnoNascita;
	private String nomeSpecie;
	private List<Dinosauro> dinosauri;
	
	
	void niente() {
		if (dinosauri.get(0) instanceof Carnivoro) {
//			((Carnivoro)dinosauri.get(0)). bla bla
			//TODO
		}
	}

}
