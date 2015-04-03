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

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che contiene un ArrayList di Tupla (anch'essa una classe)
 * Costituita da metodi per inserire elementi nella classifica,
 * aggiornarne i punteggi e rimuovere giocatori, mantenendo il tutto
 * ordinato in modo decrescente tramite bubblesort
 */
public class Classifica {

	private Partita partita;
	private List<Tupla> classificaGiocatori;

	/**
	 * Costruttore della Classifica che riceve la Partita alla quale e' associata.
	 * Esso inizializza anche l'ArrayList di Tuple che rappresenta la Classifica.
	 * @param partita
	 */
	public Classifica(Partita partita) {
		this.classificaGiocatori = new ArrayList<Tupla>();
		this.partita = partita;
	}


	/**
	 * @return Una List di Tuple che rappresentano le singole righe della classifica.
	 */
	public List<Tupla> getClassifica() {
		return this.classificaGiocatori;
	}

	/**
	 * Metodo per calcolare i punti dei Giocatori in base alla dimensione dei Dinosauri presente in squadra.
	 * @param giocatore riferimento al Giocatore di cui bisogna calcolare il punti attuale.
	 * @return un int che rappresenta il punteggio del Giocatore.
	 */
	private int calcolaPunti(Giocatore giocatore) {
		int punti=0;
		for(int i=0;i<giocatore.getDinosauri().size();i++) {
			punti += (giocatore.getDinosauri().get(i).getEnergiaMax()/1000) + 1;
		}
		return punti;
	}


	/**
	 * Metodo che serve per aggiungere elementi nella classifica.
	 * Deve esssre usato solo per inserire nuovi Giocatori e per aggiornare i punteggi.
	 * Se gli si passa un Giocatore nuovo, lo inserisce, se gia' presente ne aggiorna il punteggio.
	 * Per gestire lo stato online/offline richiama automaticamente il metodo privato aggiornaClassificaStati().
	 * @param giocatore riferimento al Giocatore da inserire nella Classifica.
	 */
	public void aggiungiTuplaClassifica(Giocatore giocatore) {	
		//creo la Tupla da inserire in classifica
		Tupla tupla = new Tupla();
		tupla.setNomeUtente(giocatore.getUtente().getNomeUtente());
		tupla.setNomeSpecie(giocatore.getNomeSpecie());
		tupla.setPunti(this.calcolaPunti(giocatore));
		tupla.setStato("s");

		//se la tupla non e' presente in classifica (cioe' risultato==-1) l'aggiungo
		//altrimenti cerco la tupla nella classifica e la aggiorno col metodo apposta "aggiornaTupla"
		if(this.cercaInClassifica(giocatore)==-1) {
			this.getClassifica().add(tupla);
		} else {
			this.aggiornaPuntiTupla(tupla,giocatore);
		}
	}


	/**
	 * Metodo per aggiornare una Tupla (riga della Classifica) quando il Giocatore e' gia' presente in Classifica.
	 * @param tupla riferimento a Tupla che rappresenta la singola riga della Classifica.
	 * @param giocatore riferimento al Giocatore per cui si vuole aggiorna il punteggio ed inserirlo nella Tupla della Classifica.
	 */
	private void aggiornaPuntiTupla(Tupla tupla, Giocatore giocatore) {
		//se la tupla e' riferita ad un giocatore offline lo rendo online
		if(tupla.getStato().equals("n")) {
			tupla.setStato("s");
		}
		//aggiorno il punteggio del giocatore e lo inserisco nella tupla
		tupla.setPunti(this.calcolaPunti(giocatore));
	}


	/**
	 * Metodo che aggiorna lo stato online/offline di tutte le Tuple in Classifica utilizzando il metodo cercaInGiocatori
	 * per capire queli Giocatori sono ancora nella Partita e quali sono usciti.
	 * Inoltre, si occupa anche di ordinare la classifica in modo decrescente per punteggio tramite il metodo
	 * privato ordinaClassifica.
	 */
	public void aggiornaClassificaStati() {
		//metodo che viene richiamata alla fine di ogni giro dei turni dei gicoatori (cioe' ad ogni turno della partita)

		Giocatore giocatore;
		for(int i=0;i<this.getClassifica().size();i++) {
			//per ogni tupla della classifica controllo se il giocatore ad esso associata e' ancora
			//presente in partita, tramite l'apposito metodo
			giocatore = this.cercaInGiocatori(this.getClassifica().get(i));

			//se giocatore==null vuol dire che non e' stato trovato da cercaInGiocatori e quindi non e' piu' presente nella
			//partita. Di conseguenza, aggiorno la tupla modificando lo stato in offline "n"
			if(giocatore==null) {
				this.getClassifica().get(i).setStato("n");
			} else {
				this.aggiornaPuntiTupla(this.getClassifica().get(i), giocatore);
			}
		}
		//eseguo l'ordinamento decrescente della classifica per punteggio
		this.ordinaClassifica();
	}


	/**
	 * Metodo che scansiona la Classifica e verifica se ogni Tupla e' associata al Giocatore ricevuto in ingresso e
	 * in caso positivo restituisce la posiziona del Giocatore nella Classifica.
	 * @param giocatore riferimento al Giocatore che deve essere cercato nella Classifica.
	 * @return un int che rappresenta la posizione del Giocatore Cella classifica. E' uguale a '-1' nel caso in cui
	 * 			nella Classifica non sia presente tale Giocatore.
	 */
	private int cercaInClassifica(Giocatore giocatore) {
		Tupla tupla;
		for(int i=0;i<this.getClassifica().size();i++) {
			tupla = this.getClassifica().get(i);
			if(tupla.getNomeUtente().equals(giocatore.getUtente().getNomeUtente()) &&
					(tupla.getNomeSpecie().equals(giocatore.getNomeSpecie()))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Metodo che scansiona i Giocatori e verifica se la Tupla e' associata ad un Giocatore nella Partita.
	 * In caso positivo restituisce il Giocatore associato alla Tupla, altrimenti 'null'.
	 * @param tupla riferimento a Tupla che rappresenta la singola riga della Classifica.
	 * @return Il riferimento al Giocatore associato alla Tupla.
	 */
	private Giocatore cercaInGiocatori(Tupla tupla) {
		//scansiona i giocatori e verifica se il giocatore corrente e' associato alla tupla ricevuto in ingresso
		//in caso positivo restituisce il giocatore associato all tupla
		//in caso negativo restituisce null
		for(int j=0;j<this.partita.getGiocatori().size();j++) {
			if(tupla.getNomeUtente().equals(this.partita.getGiocatori().get(j).getUtente().getNomeUtente()) &&
					(tupla.getNomeSpecie().equals(this.partita.getGiocatori().get(j).getNomeSpecie()))) {
				return this.partita.getGiocatori().get(j);
			}
		}
		return null;
	}


	/**
	 * Metodo che ordina la Classifica in modo decescente per punteggio, tramite BubbleSort.
	 */
	private void ordinaClassifica() {
		//si occupa dell'ordinamento della classifica tramite bubblesort
		//E' un metodo privato chiamato da aggiornaClassificaStati()
		Tupla temp;
		for(int j=0;j<this.getClassifica().size();j++) {
			for(int i=j;i<this.getClassifica().size();i++) {
				if(this.getClassifica().get(j).getPunti()<this.getClassifica().get(i).getPunti()) {
					temp = this.getClassifica().get(j);
					this.getClassifica().set(j, this.getClassifica().get(i));
					this.getClassifica().set(i, temp);
				}
			}
		}
	}

	/**
	 * Metodo per stampare la Classifica.
	 */
	public void stampaClassifica() {
		for(int i=0;i<this.getClassifica().size();i++) {
			System.out.println(this.classificaGiocatori.get(i).getNomeUtente() + "," +
					this.classificaGiocatori.get(i).getNomeSpecie() + "," +
					this.classificaGiocatori.get(i).getPunti() + "," + 
					this.classificaGiocatori.get(i).getStato());
		}
	}
	
	/**
	 * Metodo per ottenere la Classifica completa.
	 */
	public String ottieniClassifica() {
		String classificaOttenuta=new String();
		for(int i=0;i<this.getClassifica().size();i++) {
			classificaOttenuta = classificaOttenuta.concat(",{").concat(this.classificaGiocatori.get(i).getNomeUtente()).concat(",").concat(
				this.classificaGiocatori.get(i).getNomeSpecie()).concat(",").concat(""+this.classificaGiocatori.get(i).getPunti()).concat(",").concat( 
			this.classificaGiocatori.get(i).getStato()).concat("}");
		}
		return classificaOttenuta;
	}


	/**
	 * @return Una List di Tuple rappresentanti la classifica dei Giocatori.
	 */
	public List<Tupla> getClassificaGiocatori() {
		return classificaGiocatori;
	}
}
