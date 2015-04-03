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

package server.modellodati;

/**
 * Classe ASTRATTA che identifica un generico Organismo,
 * il quale puo' essere un Dinosauro, un Vegetale o una Carogna
 * Possiede solo metodi astratti comuni a tutte le sottoclassi.
 */
public abstract class Organismo {
	
	private int riga;
	private int colonna;
	private int energiaMax;
	private int energia;
	
	/**
	 * @return Un int che indica l'energiaMax posseduta dall'Organismo.
	 */
	public int getEnergiaMax() {
		return energiaMax;
	}
	/**
	 * @param energiaMax int per stabilire l'energiaMax dell'organismo.
	 */
	public void setEnergiaMax(int energiaMax) {
		this.energiaMax = energiaMax;
	}
	/**
	 * @return Un int che indica l'energia posseduta dall'Organismo.
	 */
	public int getEnergia() {
		return energia;
	}
	/**
	 * @param energia int per stabilire l'energia dell'organismo.
	 */
	public void setEnergia(int energia) {
		this.energia = energia;
	}
	/**
	 * @return Un int che indica la riga in cui e' presente.
	 */
	public int getRiga() {
		return riga;
	}
	/**
	 * @param riga int per stabilire la riga della posizione dell'organismo.
	 */
	public void setRiga(int riga) {
		this.riga = riga;
	}
	/**
	 * @return Un int che indica la colonna in cui e' presente.
	 */
	public int getColonna() {
		return colonna;
	}
	/**
	 * @param colonna int per stabilire la colonna della posizione dell'organismo.
	 */
	public void setColonna(int colonna) {
		this.colonna = colonna;
	}
	
	

}
