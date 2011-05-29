package client;

import isoladinosauri.CaricamentoMappa;
import isoladinosauri.Cella;

public class MainGui {

	public static void main (String[] args) {
		Gui gui = new Gui();
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappa = cm.caricaDaFile();
		gui.inizializzaGrafica(mappa);
		
	}
	
}
	