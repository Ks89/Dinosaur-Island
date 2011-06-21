package server.logica;

import server.modellodati.Carogna;
import server.modellodati.Vegetale;


/**
 * Classe Isola costituita dal solo attributo mappa.
 * Essa contiene tutti i metodi per caricare la mappa di gioco.
 */ 
public class Isola {

	private static final int MAX = 40;

	private Cella[][] mappa; 

	/**
	 * Metodo per far crescere e consumare la vegetazione e le carogne. Viene richimato alla fine di ogni Turno.
	 */
	public void cresciEConsuma() {
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				if(this.getMappa()[i][j]!=null && this.getMappa()[i][j].getOccupante()!=null) {
					if(this.getMappa()[i][j].getOccupante() instanceof Vegetale) {
						Vegetale vegetale = (Vegetale)(this.getMappa()[i][j].getOccupante());
						vegetale.cresci();
					} else { 
						if(this.getMappa()[i][j].getOccupante() instanceof Carogna) {
							Carogna carogna = (Carogna)(this.getMappa()[i][j].getOccupante());
							carogna.consuma();
						}
					}
				} 
			}
		}
	}


	/**
	 * Costruttore per inizializzare la mappa.
	 * @param mappa Array bidimensionali di Celle per impostare la mappa.
	 */
	public Isola(Cella[][] mappa) {
		this.mappa = mappa.clone();
	}

	/**
	 * @return Un array bidimensionale di Celle che rappresenta la mappa di gioco.
	 */
	public Cella[][] getMappa() {
		return mappa.clone();        
	}
}
