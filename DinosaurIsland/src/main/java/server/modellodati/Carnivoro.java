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
 * Classe che identifica un dinosauro Carnivoro,
 * differente da Erbivoro per il metodo calcolaForza() e 
 * per il modo in cui mangia Dinosauri e Occupanti.
 */
public class Carnivoro extends Dinosauro {

	/**
	 * @param id identificativo del dinosauro composto da una String di 2 elementi "XY", dove 'X'=id giocatore e 'Y'=numero dinosauro.
	 * @param riga int che rappresenta la riga della mappa in cui si trova il dinosauro.
	 * @param colonna int che rappresenta la colonna della mappa in cui si trova il dinosauro.
	 * @param turnoNascita int che rappresenta il turno della partita in cui e' stato creato il Carnivoro.
	 * 			Lo scopo di questo valore e' quello di rendere molto semplice il calcolo dell'eta' del dinosauro.
	 */
	public Carnivoro(String id, int riga, int colonna, int turnoNascita) {
		super(id, riga, colonna, turnoNascita);
	}

	@Override
	public int calcolaForza() {
		return (2 * super.getEnergia() * super.getEnergiaMax()/1000);
	}

	@Override
	public boolean mangia(Occupante occupante) {
		//questo metodo e' chiamato SOLO se this si e' mosso su una cella con un occupante.
		//se e' un caragona la mangio e restituisco true o false in base al fatto che potrei non averla
		//mangiata completamente
		if (occupante instanceof Carogna) {
			Carogna carogna = (Carogna)occupante;

			//mangio tutta la carogna
			if(carogna.getEnergia()<=(super.getEnergiaMax() - super.getEnergia())) {
				super.setEnergia(super.getEnergia() + carogna.getEnergia());
				return true; //avvisa di rimuovere la carogna
			}
			//mangio solo una parte della carogna	 
			else {
				// la carogna sara consumata della diff dell'energia max e quella attuale del dino
				carogna.setEnergia(carogna.getEnergia() - (super.getEnergiaMax() - super.getEnergia()));
				//il dinosauro avra la sua energia al massimo
				super.setEnergia(super.getEnergiaMax());
				return false; //avvisa di non rimuovere la carogna
			}		
		} 
		return false;
	}

	@Override
	public boolean combatti(Dinosauro dinosauro)  {
		//e' il dinosauro carnivoro a muoversi su una cella per combattere con un ERBIVORO
		if (dinosauro instanceof Erbivoro) {
			Erbivoro nemico = (Erbivoro)dinosauro;
			if(this.calcolaForza()>=nemico.calcolaForza()) {
				//il carnivoro vince il combattimento e mangia l'erbivoro
				if((int)(0.75 * nemico.getEnergia())<=(super.getEnergiaMax() - super.getEnergia())) {
					super.setEnergia(super.getEnergia() + ((int)(0.75 * nemico.getEnergia())));
				} else {
					super.setEnergia(super.getEnergiaMax());
				}
				return true; //avvisa di rimuovere l'erbivoro (perdente)
			}
			else {
				return false; //avvisa di rimuovere il carnivoro (perdente)
			}
		} else {
			//e' il dinosauro carnivoro a muoversi su una cella per combattere con un altro CARNIVORO
			if (dinosauro instanceof Carnivoro) {	
				Carnivoro nemico = (Carnivoro)dinosauro;
				if(this.calcolaForza()>=nemico.calcolaForza()) {
					//il carnivoro vince il combattimento e mangia l'altro carnivoro
					if(((int)(0.75 * nemico.getEnergia()))<=(super.getEnergiaMax() - super.getEnergia())) {
						super.setEnergia(super.getEnergia() + ((int)(0.75 * nemico.getEnergia())));
					} else {
						super.setEnergia(super.getEnergiaMax());
					}
					return true; //avvisa di rimuovere l'altro carnivoro, cioe' l'attaccato (perdente)
				}
				else {
					//il carnivoro perde il combattimento e l'attaccato Carnivoro assorbe l'energia
					if(((int)(0.75 * super.getEnergia()))<=(nemico.getEnergiaMax() - nemico.getEnergia())) {
						nemico.setEnergia(nemico.getEnergia() + ((int)(0.75 * super.getEnergia())));
					} else {
						nemico.setEnergia(nemico.getEnergiaMax());
					}
					return false; //avvisa di rimuovere l'attaccante carnivoro (perdente)
				}
			}
			return false; //attenzione a dove posizionare questo
		}
	}
}
