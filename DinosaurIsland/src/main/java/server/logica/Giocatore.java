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

import gestioneeccezioni.DeposizioneException;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.modellodati.Carnivoro;
import server.modellodati.Dinosauro;
import server.modellodati.Erbivoro;

/**
 * Classe Giocatore, costituita da tutti gli attributi che 
 * lo identificano della partita.
 */
public class Giocatore {

	private static final int MAX = 40;
	private static final int MAXGIOC = 8;
	private static final int MAXDINO = 5;
	private Partita partita;
	private Utente utente;

	private int idGiocatore; //serve per identificare il giocatore quando creo gli id dei suoi dinosauri
	private int etaAttualeGiocatore; //da quanto e' in vita. Se arriva a 120 il giocatore "muore"
	private int turnoNascita; //turno di nascita del giocatore
	private String nomeSpecie;
	private String tipoSpecie;
	private List<Dinosauro> dinosauri; //squadra di dinosauri del giocatore
	private boolean[][] mappaVisibile; //gestisce visuale giocatore con buio (se e' false)
	private List<String> uova; //e' un array di uova del giocatore che viene svuotato alla fine di ogni giro dei giocatori

	/*
	 * ATTENZIONE: SOLO IL PRIMO DINOSAURO DEL GIOCATORE E' POSIZIONATO A CASO NELLA MAPPA
	 * GLI ALTRI NASCONO DALLE UOVA VICINO AL DINOSAURO CHE LE GENERA
	 * PER QUESTO IL METODO POSIZIONADINOSAURO() CHIAMATO NEL METODO AGGIUNGIINPARTITA()
	 * VIENE USATO SOLO NELLA CREAZIONE DEL GIOCATORE (E DI CONSEGUENZA DEL SUO PRIMO DINO)
	 * DOPO NON SARA' PIU' USATO
	 */

	/**
	 * Costruttore che crea ed inizializza il Giocatore.
	 * @param turnoNascita int che rappresenta il tuno di nascita del Giocatore.
	 * @param nomeSpecie String che rappresenta il nome della specie.
	 * @param tipoSpecie String che rappresenta il tipo della specie. Puo' essere "c" o "e".
	 */
	public Giocatore(int turnoNascita, String nomeSpecie, String tipoSpecie) {
		
		this.etaAttualeGiocatore=0;
		this.turnoNascita = turnoNascita;
		this.nomeSpecie = nomeSpecie;
		this.tipoSpecie = tipoSpecie;
		dinosauri = new ArrayList<Dinosauro>();

		//gestione mappa (buio e luce)
		//non serve inizializzarla perche' e' di default a FALSE
		this.mappaVisibile = new boolean[MAX][MAX];
		
		//inizializzo l'array per le uova, ovviamente parte da vuoto perche' non ho uova all'inizio
		this.uova = new ArrayList<String>();
	}
	
	/**
	 * Metodo che permette di aggiungere un Giocatore (razza) alla partita in corso, creare l'id, far nascere il primo Dinosauro 
	 * 	ed illuminare l'area intorno ad esso.
	 * @param Riferimento alla classe Partita in cui si vuole aggiungere il Giocatore.
	 */
	public void aggiungiInPartita(Partita partita) {
		this.partita = partita;
		
		//aggiungo il giocatore creato alla lista dei giocatori in Partita
		//questo serve perche' se no non sarebbe ancora fisicamente nella lista giocatori di Partita
		this.partita.aggiungiGiocatore(this);
		
		//ottengo riga e colonna in un vettore di interi di 2 elementi
		int[] pos = this.posizionaDinosauro();

		this.setIdGiocatore(this.generaIdGiocatore());
		String idDinosauro = this.generaIdDinosauro();
		
		Dinosauro dinosauro;
		if(this.tipoSpecie.equals("c")) {
			dinosauro = new Carnivoro(idDinosauro,pos[0], pos[1], turnoNascita);
			dinosauri.add((Carnivoro)dinosauro);
			this.partita.getIsola().getMappa()[pos[0]][pos[1]].setDinosauro(dinosauro); 
		} else { //se e' erbivoro
			dinosauro = new Erbivoro(idDinosauro,pos[0], pos[1], turnoNascita);
			dinosauri.add((Erbivoro)dinosauro);
			this.partita.getIsola();
			this.partita.getIsola().getMappa();
			this.partita.getIsola().getMappa()[pos[0]][pos[1]].setDinosauro(dinosauro);
		}

		//illumino la mappa della visibilita' nella zona in cui ho creato il dinosauro
		//il raggio e' 2 costante, perche' tutti i giocatori appena creati hanno un solo dino e sempre con dimensione =1
		//quindi per le specifiche il raggio deve essere =2 (sezione Visibilita')
		this.partita.getTurnoCorrente().illuminaMappa(this, dinosauro.getRiga(), dinosauro.getColonna(), 2);
	}
	
	//***********************************************************************************************************************
	//**************************************************POSIZIONAMENTO*******************************************************
	//***********************************************************************************************************************
	/**
	 * Metodo che assegna delle coordinate casuali (valide) al Dinosauro.
	 * @return Un array di int contente la poszione del Dinosauro appena creato:
	 * 		[0] - riga, 
	 * 		[1] - colonna.
	 */
	private int[] posizionaDinosauro () {
		//metodo che fornisce le coordinate in cui andare
		//a mettere in dinosauro, stando attento a non
		//posizionarlo dove c'e' gia' un altro o dove c'e'
		//acqua. Il metodo mette in un array con 2 interi
		//riga e colonna e fa il return
		int[] coordinate = new int[2];
		Cella cella;
		Random random = new Random();
		do {
			coordinate[0] = random.nextInt(MAX);
			coordinate[1] = random.nextInt(MAX);
			cella = this.partita.getIsola().getMappa()[coordinate[0]][coordinate[1]];
		} while(cella==null || cella.getDinosauro()!=null);
		return coordinate;
	}

	//***********************************************************************************************************************
	//*****************************************************GENERO ID*********************************************************
	//***********************************************************************************************************************
	/**
	 * Metodo che genera l'id del Giocatore, ovvero la prima parte dell'id del Dinosauro.
	 * @return Un int che rappresenta l'id del Giocatore.
	 */
	private int generaIdGiocatore() {
		//array che indica quali id dei giocatori sono disponibili nella partita
		int[] posizioni = {1,2,3,4,5,6,7,8};
		//se non ci sono giocatori di certo quello nuovo sara' il primo
		if(this.partita.getGiocatori().isEmpty()) {
			return 1;
		}
		else { 
			for(int j=0;j<this.partita.getGiocatori().size();j++) {
				for(int i=0;i<MAXGIOC;i++) {
					//mette a '0' gli id gia' occupati nell'array posizioni[]
					if(this.partita.getGiocatori().get(j).getIdGiocatore()==posizioni[i]) {
						posizioni[i]=0;
					}
				}
			} //cerco il primo che non e' '0' nell'array e ritorno l'indice + 1 
			for(int i=0;i<MAXGIOC;i++) {
				if(posizioni[i]!=0) {
					return i+1;
				}
			}
		}
		return -1; //se c'e' un problema fa il return con -1
	}

	/**
	 * Metodo che genera l'id parziale, ovvero solo quello associato al numero del Dinosauro, per poi essere combinato con la
	 * parte del Giocatore e restituire, tramite il metodo generaIdDiinosauro, l'id completo nella forma "XY".
	 * @return Un int che rappresenta l'id parziale generato, associato al Giocatore.
	 */
	private int generaIdParziale() {
		//uguale a generaIdGiocatore() ma sta volta lavoro sui char, perche' l'id del dinosauro
		//e' il carattere [1] nel campo della classe Dinosauro
		char[] posizioni = {'1','2','3','4','5'};
		if(this.getDinosauri().isEmpty()) {
			return 1;
		}
		else {
			for(int j=0;j<this.getDinosauri().size();j++) {
				for(int i=0;i<MAXDINO;i++) {
					if(this.getDinosauri().get(j).getId().charAt(1)==posizioni[i]) {
						posizioni[i]='0';
					}
				}
			}
			for(int i=0;i<MAXDINO;i++) {
				if(posizioni[i]!='0') {
					return i+1;
				}
			}
		}
		return -1; //se c'e' un problema fa il return con -1
	}

	/**
	 * @return Una String contenente l'id del Dinosauro in questa forma: "X": idGiocatore, "Y": numero del Dinosauro.
	 */
	public String generaIdDinosauro() {
		return "" + this.getIdGiocatore() + this.generaIdParziale();
	}

	/**
	 * @return Un int che rappresente l'id del Giocatore.
	 */
	public int getIdGiocatore() {
		return idGiocatore;
	}

	/**
	 * @param idGiocatore int per impostare l'id del Giocatore.
	 */
	public void setIdGiocatore(int idGiocatore) {
		this.idGiocatore = idGiocatore;
	}	

	//***********************************************************************************************************************
	//***************************************************GESTIONE DINOSAURO**************************************************
	//***********************************************************************************************************************
	/**
	 * Metodo per aggiungere un Dinosauro alla lista del Giocatore.
	 * @param dinosauro riferimento al dinosauro da aggiungere.
	 * @return Un boolean che puo' indicare con 'true': tutto e'
	 * andato bene, 'false': non e' stato possibile aggiungere il Dinosauro.
	 */
	public boolean aggiungiDinosauro(Dinosauro dinosauro) {
		if(this.dinosauri.size()<MAXDINO ) {
			this.dinosauri.add(dinosauro);
		} else {
			return false; //non posso aggiungere il dinosauro
		}
		//se tutto va bene restituisce true
		return true;
	}
	

	/**
	 * Metodo per rimuovere un Dinosauro dalla lista associata al Giocatore passando anche la Cella.
	 * Il vantaggio rispetto al metodo rimuoviDinosauro(Dinosauro dinosauro) e' che lo posso usare per rimuovere Dinosauri
	 * presenti in celle di partenza durante un'azione di movimento.
	 * @param dinosauro riferimento al Dinosauro che deve essere rimosso.
	 * @param cella riferimento alla Cella su cui si trova il Dinosauro
	 */
	public void rimuoviDinosauro(Dinosauro dinosauro, Cella cella) {
		boolean stato = this.dinosauri.remove(dinosauro);
		if(stato) {
			//dinosauro rimosso correttamente
			//lo cancello anche dalla mappa
			cella.setDinosauro(null);
		}
		else {
			//dinosauro non trovato 
			System.out.println("Errore nella rimozione del dinosauro");
		}
	}
	
	/**
	 * Metodo per rimuovere un Dinosauro dalla lista associata al Giocatore.
	 * @param dinosauro riferimento al Dinosauro che deve essere rimosso.
	 */
	public void rimuoviDinosauro(Dinosauro dinosauro) {
		Cella cella = this.partita.getIsola().getMappa()[dinosauro.getRiga()][dinosauro.getColonna()];
		if(this.dinosauri.remove(dinosauro)) {
			//dinosauro rimosso correttamente
			//lo cancello anche dalla mappa
			cella.setDinosauro(null);
		}
		else {
			//dinosauro non trovato
			System.out.println("Errore nella rimozione del dinosauro");
		}
	}


	//***********************************************************************************************************************
	//******************************************************GESTIONE UOVA****************************************************
	//***********************************************************************************************************************
	/**
	 * @return Una lista delle uova del Giocatore.
	 */
	public List<String> getUova() {
		return uova;
	}

	/**
	 * Metodo per aggiungere un Uovo.
	 * @param riga int che rappresenta la riga dove e' stato deposto l'uovo.
	 * @param colonna int che rappresenta la colonna dove e' stato deposto l'uovo.
	 * @param idDinosauro String che rappresenta l'id che avra' il Dinosauro quando nascera'.
	 */
	public void aggiungiUovo(int riga, int colonna, String idDinosauro) {
		this.uova.add(riga + "-" + colonna + "-" + idDinosauro);
	}

	/**
	 * Metodo per rimuovere tutte le Uova del Giocatore.
	 */
	public void rimuoviUova() {
		this.uova.clear();
	}

	/**
	 * Metodo per far deporre un uovo al Dinosauro.
	 * @param dinosauro riferimento al Dinosauro che deve deporre l'uovo
	 * @return Una String che rappresenta l'id che avra' il Dinosauro quando nascera'. 
	 * @throws DeposizioneException Eccezione che puo' essere sollevata a causa della MORTE del Dinosauro perche' non possiede
	 * energia sufficiente per compiere l'azione, oppure per SQUADRACOMPLETA, dovuta al fatto che ogni Giocatore puo' avere al massimo
	 * 5 Dinosauri.
	 */
	public String eseguiDeposizionedeponiUovo(Dinosauro dinosauro) throws DeposizioneException {
		dinosauro.setEnergia(dinosauro.getEnergia() - 1500);
		if(dinosauro.getEnergia()>0) {
			//il dinosauro pu' compiere l'azione di deposizione, ma solo se il num di dino
			//sommati a quello delle uova (perche' in futuro saranno anch'essi dino) sia <5
			if((this.getDinosauri().size() + this.getUova().size()) < MAXDINO ) {
				String idDinosauro = this.generaIdDinosauro();
//				System.out.println("ID Dinosauro: " + idDinosauro);
				this.aggiungiUovo(dinosauro.getRiga(),dinosauro.getColonna(),idDinosauro);
				return idDinosauro;
			} else {
				throw new DeposizioneException(DeposizioneException.Causa.SQUADRACOMPLETA);
			}
		} else { //il dinosauro muore perche' non ha sufficiente energia
			this.rimuoviDinosauro(dinosauro);	
			throw new DeposizioneException(DeposizioneException.Causa.MORTE);
		}
	}

	//***********************************************************************************************************************
	//**************************************************GESTIONE TURNI*******************************************************
	//***********************************************************************************************************************
	/**
	 * Metodo richiamato da Turno, alla fine di ogni turno eseguito dal Giocatore.
	 */
	public void incrementaEtaAttuali() {
		this.etaAttualeGiocatore++;
		for(int i=0;i<this.getDinosauri().size();i++) {
			this.getDinosauri().get(i).incrementaEtaDinosauro();
		}
	}

	/**
	 * @return Un int che rappresenta il turno di nascita del Giocatore.
	 */
	public int getTurnoNascita() {
		return turnoNascita;
	}

	/**
	 * @param turnoNascita int per impostare il turno di nascita del Giocatore.
	 */
	public void setTurnoNascita(int turnoNascita) {
		this.turnoNascita = turnoNascita;
	}

	/**
	 * @return Un int che rappresenta l'eta' attuale del Giocatore.
	 */
	public int getEtaAttuale() {
		return etaAttualeGiocatore;
	}

	/**
	 * @param etaAttualeGiocatore int per impostare l'eta' attuale del Giocatore.
	 */
	public void setEtaAttuale(int etaAttualeGiocatore) {
		this.etaAttualeGiocatore = etaAttualeGiocatore;
	}


	//***********************************************************************************************************************
	//**************************************************GET e SET************************************************************
	//***********************************************************************************************************************
	/**
	 * @return La lista dei Dinosauri del Giocatore.
	 */
	public List<Dinosauro> getDinosauri() {
		return dinosauri;
	}

	/**
	 * @return Un array bidimensionale di boolean che rappresenta la mappa della visibilita' del Giocatore.
	 */
	public boolean[][] getMappaVisibile() {
		return mappaVisibile.clone();
	}

	/**
	 * @return Una String con il nome della specie del Giocatore.
	 */
	public String getNomeSpecie() {
		return nomeSpecie;
	}

	/**
	 * @param nomeSpecie String per impostare il nome della specie del Giocatore.
	 */
	public void setNomeSpecie(String nomeSpecie) {
		this.nomeSpecie = nomeSpecie;
	}
	
	/**
	 * @return L'Utente con il nome della specie del Giocatore.
	 */
	public Utente getUtente() {
		return utente;
	}

	/**
	 * @param utente Utente per impostare l'Utente associato al Giocatore.
	 */
	public void setUtente(Utente utente) {
		this.utente = utente;
	}

}
