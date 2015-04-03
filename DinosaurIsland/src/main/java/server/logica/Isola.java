/*
Copyright 2011-2015 Stefano Cappa
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

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
