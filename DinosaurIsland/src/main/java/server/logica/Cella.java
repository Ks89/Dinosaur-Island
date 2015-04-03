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
 
import server.modellodati.Dinosauro;
import server.modellodati.Occupante;

/**
 * Classe usata per contenere due interfacce
 * Occupante: puo' essere vegetale o carogna a runtime
 * Dinosauro: puo' essere erbivoro o carnivoro a runtime
 * Permette la presenza di un dinosauro e un occupante sulla stessa cella
 * contemporaneamente, ma non di 2 occupanti e/o dinosauri.
 */

public class Cella {
	
	private Occupante occupante;
	private Dinosauro dinosauro;
	
	/**
	 * @return Il riferimento all'Occupante presente nella Cella.
	 */
	public Occupante getOccupante() {
		return occupante;
	}
	
	/**
	 * @param occupante L'Occupante che si vuole impostare nella Cella.
	 */
	public void setOccupante(Occupante occupante) {
		this.occupante = occupante;
	}
	
	/**
	 * @return Il riferimento al Dinosauro presente nella Cella.
	 */
	public Dinosauro getDinosauro() {
		return dinosauro;
	}
	
	/**
	 * @param dinosauro Il Dinosauro che si vuole impostare nella Cella.
	 */
	public void setDinosauro(Dinosauro dinosauro) {
		this.dinosauro = dinosauro;
	}
}
