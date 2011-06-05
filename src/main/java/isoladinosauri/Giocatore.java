package isoladinosauri;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe Giocatore, costituita da tutti gli attributi che 
 * lo identificano della partita. Il costruttore si preoccupaa anche 
 * di gestire l'illumnazione della mappa e genera in automatico il
 * primo dinosauro del giocatore.
 */
public class Giocatore {

	private Partita partita;
	private Utente utente;

	private int idGiocatore; //serve per identificare il giocatore quando creo gli id dei suoi dinosauri
	private int etaAttualeGiocatore; //da quanto e' in vita. Se arriva a 120 il giocatore "muore"
	private int turnoNascita; //turno di nascita del giocatore
	private String nomeSpecie;
	private List<Dinosauro> dinosauri; //squadra di dinosauri del giocatore
	private boolean[][] mappaVisibile; //gestisce visuale giocatore con buio (se e' false)
	private List<String> uova; //e' un array di uova del giocatore che viene svuotato alla fine di ogni giro dei giocatori

	//ATTENZIONE: SOLO IL PRIMO DINOSAURO DEL GIOCATORE E' POSIZIONATO A CASO NELLA MAPPA
	//GLI ALTRI NASCONO DALLE UOVA VICINO AL DINOSAURO CHE LE GENERA
	//PER QUESTO IL METODO POSIZIONADINOSAURO() CHIAMATO NEL COSTRUTTORE
	//VIENE USATO SOLO NELLA CREAZIONE DEL GIOCATORE (E DI CONSEGUENZA DEL SUO PRIMO DINO)
	//DOPO NON SARA' PIU' USATO

	/**
	 * Costruttore che assegna gli id, inzializza tutti i campi di Giocatore, crea un Dinosauro ed illumina l'area intorno al esso.
	 * @param partita riferiemento alla Partita.
	 * @param turnoNascita int che rappresenta il tuno di nascita del Giocatore.
	 * @param nomeSpecie String che rappresenta il nome della specie.
	 * @param tipoSpecie String che rappresenta il tipo della specie. Puo' essere "carnivoro" o "erbivoro".
	 */
	public Giocatore(Partita partita, int turnoNascita, String nomeSpecie, String tipoSpecie) {
		this.partita = partita;
		this.etaAttualeGiocatore=0;
		this.turnoNascita = turnoNascita;
		this.nomeSpecie = nomeSpecie;
		dinosauri = new ArrayList<Dinosauro>();

		//ottengo riga e colonna in un vettore di interi di 2 elementi
		int[] pos = this.posizionaDinosauro();

		this.setIdGiocatore(this.generaIdGiocatore());
		String idDinosauro = this.generaIdDinosauro();

		Dinosauro dinosauro;
		//creo il dinosauro con ID, POSIZIONE e TURNO NASCITA
		if(tipoSpecie.equals("carnivoro")) {
			dinosauro = new Carnivoro(idDinosauro,pos[0], pos[1], turnoNascita);
			dinosauri.add((Carnivoro)dinosauro);
			this.partita.getIsola().getMappa()[pos[0]][pos[1]].setDinosauro(dinosauro); 
		} else { //se e' erbivoro
			dinosauro = new Erbivoro(idDinosauro,pos[0], pos[1], turnoNascita);
			dinosauri.add((Erbivoro)dinosauro);
			this.partita.getIsola().getMappa()[pos[0]][pos[1]].setDinosauro(dinosauro);
		}

		//ora aggiungo il giocatore creato alla lista dei giocatori in Partita
		//questo serve se no non potrei usare illuminaMappa direttamente nel costruttore
		//perche' l'oggetto giocatore non sarebbe ancora fisicamente nella lista giocatori di Partita
		this.partita.aggiungiGiocatore(this);

		//gestione mappa (buio e luce)
		//non serve inizializzarla perche' e' di default a FALSE
		this.mappaVisibile = new boolean[40][40];

		//illumino la mappa della visibilita' nella zona in cui ho creato il dinosauro
		//il raggio e' 2 costante, perche' tutti i giocatori appena creati hanno un solo dino e sempre con dimensione =1
		//quindi per le specifiche il raggio deve essere =2 (sezione Visibilita')
		
		this.partita.getTurnoCorrente().illuminaMappa(this, dinosauro.getRiga(), dinosauro.getColonna(), 2);

		//inizializzo l'array per le uova, ovviamente parte da vuoto perche' non ho uova all'inizio
		this.uova = new ArrayList<String>();
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
			coordinate[0] = random.nextInt(40);
			coordinate[1] = random.nextInt(40);
			//			coordinate[0]=8;
			//			coordinate[1]=14;
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
				for(int i=0;i<8;i++) {
					//mette a '0' gli id gia' occupati nell'array posizioni[]
					if(this.partita.getGiocatori().get(j).getIdGiocatore()==posizioni[i]) {
						posizioni[i]=0;
					}
				}
			} //cerco il primo che non e' '0' nell'array e ritorno l'indice + 1 
			for(int i=0;i<8;i++) {
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
	 * @return
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
				for(int i=0;i<5;i++) {
					if(this.getDinosauri().get(j).getId().charAt(1)==posizioni[i]) {
						posizioni[i]='0';
					}
				}
			}
			for(int i=0;i<5;i++) {
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
//		System.out.println(this.generaIdParziale());
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
		if(this.dinosauri.size() + this.getUova().size()<5 ) {
			this.dinosauri.add(dinosauro);
		} else {
			return false; //non posso aggiungere il dinosauro
		}
		//se tutto va bene restituisce true
		return true;
	}
	

	//FIXME rimuovere questo metodo, e far funzionare in modo universale quello sotto.
	/**
	 * Metodo per rimuovere un Dinosauro dalla lista associata al Giocatore.
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
	 * @param riga int che rappresent la riga dove e' stato deposto l'uovo.
	 * @param colonna int che rappresente la colonna dove e' stato deposto l'uovo.
	 */
	public void aggiungiUovo(int riga, int colonna) {
		this.uova.add(riga + "-" + colonna);
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
	 * @return Un boolean che indica con 'false' che il Dinosauro non ha 
	 * 		abbastanza energia per compiere l'azione e quindi muore,
	 * 		con 'true' che e' andato tutto bene.
	 */
	public boolean eseguiDeposizionedeponiUovo(Dinosauro dinosauro) {
		dinosauro.setEnergia(dinosauro.getEnergia() - 1500);
		if(dinosauro.getEnergia()>0) {
			//il dinosauro pu' compiere l'azione di deposizione, ma solo se il num di dino
			//sommati a quello delle uova (perche' in futuro saranno anch'essi dino) sia <5
			if((this.getDinosauri().size() + this.getUova().size()) < 5 ) {
				this.aggiungiUovo(dinosauro.getRiga(),dinosauro.getColonna());
				return true;
			} else {
				return false; //squadra completa e non posso creare altri dinosauri deponendo uova	
			}
		} else { //il dinosauro muore perche' non ha sufficiente energia
			this.rimuoviDinosauro(dinosauro);	
			return false;
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
		//su consiglio di Sonar ritorno un oggetto clonato per una questione
		//di sicurezza
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
