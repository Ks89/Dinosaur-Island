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

import java.util.Random;


/**
 * Classe che identifica una carogna,
 * differente da Vegetazione principalmente per
 * il metodo consuma() e per l'energiaMax casuale.
 */
public class Carogna extends Organismo implements Occupante {
	
	/**
	 * Costruttore senza parametri che inizializza la Carogna con : 
	 * una energiaMax tra 350 e 650
	 * e un'energia uguale a quella massima.
	 */
	public Carogna() {
		Random random = new Random();
		super.setEnergiaMax(random.nextInt(301) + 350);
		super.setEnergia(super.getEnergiaMax());
	}
	
	/**
	 * Metodo senza parametri che diminuisce la vita di una Carogna
	 * del valore: energiaMax/20.
	 */
	public void consuma() {
		if(super.getEnergiaMax()/20<=super.getEnergia()) {
			super.setEnergia(super.getEnergia() - super.getEnergiaMax()/20);
		} else {
			super.setEnergia(0);
		}
	}

}
