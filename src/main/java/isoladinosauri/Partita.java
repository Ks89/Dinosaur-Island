package isoladinosauri;

import java.util.List;

public class Partita {
	
	private Cella[][] mappa;
	private Turno turnoCorrente; //Conserva solo il turno corrente
	private List<Giocatore> giocatori;
	private int contatoreTurni;
	
	
	void daTogliere() {
		//creo un nuovo turno
		Turno t = new Turno(this);
	}

}
