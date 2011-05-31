package isoladinosauri;

import java.util.Random;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;
import isoladinosauri.modellodati.Occupante;
import isoladinosauri.modellodati.Vegetale;

/**
 * Questa classe viene utilizzata per: 
 * gestire l'illuminazione, la vista per il
 * movimento, il combattimento ecc.. dei dinosauri
 * riposizionamento vegetazione e carogna dopo che sono
 * state mangiate.
 */
public class Turno {

	private Partita partita;

	/**
	 * @param partita imposta la Partita associandola alla classe Turno.
	 */
	public Turno(Partita partita) {
		this.partita = partita;
	}

	//*******************************************************************************************************************
	//****************************************GESTIONE VISTA E ILLUMINAZIONE*********************************************
	//*******************************************************************************************************************

	/**
	 * Metodo che esegue l'illuminazione della mappa intorno ad un punto, dato un certo raggio di celle, 
	 * richiamando il metodo ottieniVisuale(...).
	 * @param giocatore valore che rappresenta il Giocatore a cui si deve illuminare la mappa di visibilita'.
	 * @param riga int che rappresenta la riga della mappa in cui si trova il Dinosauro.
	 * @param colonna int che rappresenta la colonna della mappa in cui si trova il Dinosauro.
	 * @param raggio int che rappresenta il raggio di celle intorno alla posizione del Dinosauro.
	 */
	public void illuminaMappa (Giocatore giocatore, int riga, int colonna, int raggio) {
		int[] vista = this.ottieniVisuale(riga, colonna, raggio);

		//vista[0] e vista[2] sono la riga di origine e la riga di fine
		//vista[1] e vista[3] sono la colonna origine e la colonna fine
		for(int j=vista[1];j<vista[3]+1;j++) { //scansiono le colonne
			for(int i=vista[0];i<vista[2]+1;i++) {//scansiono le righe
				giocatore.getMappaVisibile()[i][j]=true; //illumino
			}
		}
	}

	/**
	 * Metodo per ottenere la visuale, cioe' una sottomappa, dato un certo raggio.
	 * @param riga int che rappresenta la riga della mappa in cui si trova il Dinosauro.
	 * @param colonna int che rappresenta la colonna della mappa in cui si trova il Dinosauro.
	 * @param raggio int che rappresenta il raggio di celle intorno alla posizione del Dinosauro.
	 * @return Un array di 4 int che rappresenta rispettivamente:
	 * 			[0] - riga dell'origine della vista,
	 * 			[1] - colonna dell'origine della vista,
	 * 			[2] - riga della fine della vista,
	 * 			[3] - colonna della fine della vista.
	 */
	public int[] ottieniVisuale (int riga, int colonna, int raggio) {
		int[] vista = new int[4];
		int[] origineVista = this.ottieniOrigineVisuale(riga, colonna, raggio);
		int[] fineVista = this.ottieniEstremoVisuale(riga, colonna, raggio);
		vista[0] = origineVista[0]; //riga
		vista[1] = origineVista[1]; //colonna
		vista[2] = fineVista[0]; //riga
		vista[3] = fineVista[1]; //colonna
		return vista;
	}

	/**
	 * Metodo per ottenere l'origine della visuale del Dinosauro
	 * @param riga int che rappresenta la riga della mappa in cui si trova il Dinosauro.
	 * @param colonna int che rappresenta la colonna della mappa in cui si trova il Dinosauro.
	 * @param raggio int che rappresenta il raggio di celle intorno alla posizione del Dinosauro.
	 * @return Un array di 2 int che rappresenta rispettivamente:
	 * 			[0] - riga dell'origine della vista,
	 * 			[1] - colonna dell'origine della vista,
	 */
	public int[] ottieniOrigineVisuale (int riga, int colonna, int raggio) {
		//e' fatto in modo che possa essere riutilizzato anche dal metodo sulla nascita del dino dall'uovo
		//e anche da quello per la gestione del movimento
		int[] coordinate = new int[2];
		int i=0, j=0;

		//calcolo in i e j il punto di origine della vista
		//inizio con i=0 nella posizione del dinosauro, dopo lo incremeneto, cioe' mi sto spostando
		//verso l'origine della dimensione del raggio
		//se arrivo a 0 termino subito, se no termino quando
		for(i=0;i<raggio;i++) {
			if(riga - i <= 0) {
				break;
			}
		}
		for(j=0;j<raggio;j++) {
			if(colonna - j <= 0) {
				break;
			}
		}
		coordinate[0] = riga - i;
		coordinate[1] = colonna - j;
		return coordinate;		
	}

	/**
	 * Metodo per ottenere la fine (estremo) della visuale del Dinosauro
	 * @param riga int che rappresenta la riga della mappa in cui si trova il Dinosauro.
	 * @param colonna int che rappresenta la colonna della mappa in cui si trova il Dinosauro.
	 * @param raggio int che rappresenta il raggio di celle intorno alla posizione del Dinosauro.
	 * @return Un array di 2 int che rappresenta rispettivamente:
	 * 			[0] - riga della fine della vista,
	 * 			[1] - colonna della fine della vista,
	 */
	public int[] ottieniEstremoVisuale (int riga, int colonna, int raggio) {
		//per ottenere le coordinate dell'estremo della vista del Dinosauro
		int[] coordinate = new int[2];
		int i=0, j=0;
		for(i=0;i<raggio;i++) {
			if(riga + i >=39) {
				break;
			}
		}
		for(j=0;j<raggio;j++) {
			if(colonna + j >=39) {
				break;
			}
		}
		coordinate[0] = riga + i;
		coordinate[1] = colonna + j;
		return coordinate;
	}

	//*******************************************************************************************************************
	//***************************************RAGGIUNGIBILITA E STRADA PERCORSA*******************************************
	//*******************************************************************************************************************
	
	/**
	 * Metodo che restituisce una mappa bidimensionale con all'interno un intero che
	 * rappresenta il numero di passi necessari per raggiungere tale posizione: 
	 * 0: posizione di partenza del Dinosauro
	 * 1,2,3: numero dei passi per raggiungere tale cella.
	 * @param sorgRiga int che rappresenta la riga in cui si trova il Dinosauro.
	 * @param sorgColonna int che rappresenta la colonna in cui si trova il Dinosauro.
	 * @return Un array bidimensionale di raggio 2 per un Erbivoro, 3 per un Carnivoro.
	 */
	public int [][] ottieniRaggiungibilita(int sorgRiga, int sorgColonna) {

		int riga,colonna,maxR,maxC,rigaSu,rigaGiu,colonnaSx,colonnaDx,passo,nPassi;
		int[] origineMappaMovimento;
		int[] estremoMappaMovimento;
		int[][] mappaMovimento;

		if(this.partita.getIsola().getMappa()[sorgRiga][sorgColonna].getDinosauro() instanceof Carnivoro){
			//essendo la raggiungibilita di un carnivoro il raggio deve essere 3
			nPassi=3;
			origineMappaMovimento = this.ottieniOrigineVisuale(sorgRiga, sorgColonna, nPassi); 
			estremoMappaMovimento = this.ottieniEstremoVisuale(sorgRiga, sorgColonna, nPassi);
		}
		else{ 
			//essendo la raggiungibilita di un erbivoro il raggio deve essere 2
			nPassi=2;
			origineMappaMovimento = this.ottieniOrigineVisuale(sorgRiga, sorgColonna, nPassi);
			estremoMappaMovimento = this.ottieniEstremoVisuale(sorgRiga, sorgColonna, nPassi);
		}

		//estremi del movimento del dinosauro
		maxR = estremoMappaMovimento[0]-origineMappaMovimento[0]+1;
		maxC = estremoMappaMovimento[1]-origineMappaMovimento[1]+1;

		mappaMovimento = new int[maxR][maxC];

		// numeri convenzione: 
		//8 = ACQUA
		//9 = NON RAGGIUNGIBILE		

		// copio l'acqua della mappa nella "sottomappa raggiungiblita" e metto non raggiungibile altrove
		for(int i=0; i<maxR; i++) {
			for(int j=0; j<maxC; j++) {
				if(this.partita.getIsola().getMappa()[origineMappaMovimento[0]+i][origineMappaMovimento[1]+j] == null) {
					mappaMovimento[i][j] = 8; // ACQUA=8
				} else {
					mappaMovimento[i][j] = 9; // NON RAGGIUNGIBILE = 9
				}
			}
		}
		// mi posiziono sul dinosauro e sono al passo 0
		mappaMovimento[sorgRiga-origineMappaMovimento[0]][sorgColonna-origineMappaMovimento[1]] = 0;

		// scandisco la mappaMovimento e per ogni passo stabilisco se posso muovermi sulla cella
		for(passo=1; passo<=nPassi; passo++) {
			for(riga=0; riga<maxR; riga++) {
				for(colonna=0; colonna<maxC; colonna++) {
					if(mappaMovimento[riga][colonna] == passo-1) { //mi posiziono sul passo precedente per trovare il passo successivo
						if(riga-1<0) { // controllo di non uscire fuori dalle righe della mappa
							rigaSu=riga;
						} else {
							rigaSu=riga-1;
						}
						if(riga+1>=maxR) {
							rigaGiu=riga;
						} else {
							rigaGiu=riga+1;
						}
						for(int i=rigaSu; i<=rigaGiu; i++) {
							if(colonna-1<0) { // controllo di non uscire fuori dalle colonne della mappa
								colonnaSx=colonna;
							} else {
								colonnaSx=colonna-1;
							}
							if(colonna+1>=maxC) {
								colonnaDx=colonna;
							} else {
								colonnaDx=colonna+1;
							}
							for(int j=colonnaSx; j<=colonnaDx; j++) {
								// non c'e' acqua e non c'e' un numero di passi inferiori che mi permettono di raggiungere la cella
								if(mappaMovimento[i][j] == 9) {
									mappaMovimento[i][j] = passo;
								}
							}
						}
					}
				}
			}
		}
		// ora tutte le celle che contengono numeri diversi da 8 (acqua) e diversi da 9 (non raggiungibile
		//sono raggiungibili dal dinosauro con percorso 0->1->2->3	
		return mappaMovimento;
	}

	/**
	 * Metodo che restituisce una mappa bidimensionale di int per indicare la strada che il Dinosauro
	 * compie per giungere a destinazione. Esso richiama anche il metodo per la raggiungibilita'.
	 * La posizione del Dinosauro e' indicata con il valore '-7' e i passi con un valore crescente
	 * come per esempio: -6, -5, -4 ecc...
	 * @param sorgRiga int che rappresenta la riga in cui si trova il Dinosauro.
	 * @param sorgColonna int che rappresenta la colonna in cui si trova il Dinosauro.
	 * @param destRiga int che rappresenta la riga in cui si dovra' muovere il Dinosauro.
	 * @param destColonna int che rappresenta la riga in cui si dovra' muovere il Dinosauro.
	 * @return Un array bidimensionale con la strada percorsa indicata da numeri negativi
	 */
	public int [][] ottieniStradaPercorsa(int sorgRiga, int sorgColonna, int destRiga, int destColonna) {
		int[][] mappaMovimento = this.ottieniRaggiungibilita(sorgRiga,sorgColonna);
		int i=0,j=0,maxR,maxC,riga,colonna,partenzaDinoRiga,partenzaDinoColonna;
		int arrivoDinoRiga,arrivoDinoColonna,deltaRiga,deltaColonna,passiDaPercorrere,cont=0;
		int rigaSu,rigaGiu,colonnaSx,colonnaDx;
		boolean trovato=false;

		deltaRiga = destRiga - sorgRiga; //numero di righe percorse
		deltaColonna = destColonna - sorgColonna; //numero di colonne percorse

		maxR = mappaMovimento.length;
		maxC = mappaMovimento[0].length;

		//cerco le coordinate fittizie (partenzaDinoRiga,partenzaDinoColonna) del dinosauro nella sottomappa
		for(i=0,trovato=false; i<maxR && trovato==false; i++) {
			for(j=0; j<maxC && trovato==false; j++) {
				if(mappaMovimento[i][j]==0) {
					trovato = true;
				}
			}
		}
		if(trovato==true){    	
			partenzaDinoRiga = i-1;
			partenzaDinoColonna = j-1;

			arrivoDinoRiga = partenzaDinoRiga + deltaRiga; //riga destinazione
			arrivoDinoColonna = partenzaDinoColonna + deltaColonna; //colonna destinazione

			passiDaPercorrere = mappaMovimento[arrivoDinoRiga][arrivoDinoColonna];

			mappaMovimento[arrivoDinoRiga][arrivoDinoColonna] -= 7; //parto dalla destinazione e sottraggo 7 al numero del passo

			riga = arrivoDinoRiga;
			colonna = arrivoDinoColonna;
			
			for(cont=passiDaPercorrere; cont>0; cont--) { //parto dalla destinazione e torno verso la partenza
				if(riga-1<0) { // controllo di non uscire fuori dalle righe della mappa
					rigaSu=riga;
				} else {
					rigaSu=riga-1;
				}
				if(riga+1>=maxR) {
					rigaGiu=riga;
				} else {
					rigaGiu=riga+1;
				}
				for(i=rigaSu,trovato=false; i<=rigaGiu && trovato==false; i++) {
					if(colonna-1<0) { // controllo di non uscire fuori dalle colonne della mappa
						colonnaSx=colonna;
					} else {
						colonnaSx=colonna-1;
					}
					if(colonna+1>=maxC) {
						colonnaDx=colonna;
					} else {
						colonnaDx=colonna+1;
					}
					for(j=colonnaSx; j<=colonnaDx && trovato==false; j++) {
						if(mappaMovimento[i][j] == cont-1) { // se trovo una cella contenente un numero di passi inferiori a quello corrente
							trovato=true;
							mappaMovimento[i][j] -= 7; // sottraggo 7 x marcare la strada
							riga=i;
							colonna=j;
						}
					}
				}
			}    					
		}
		return mappaMovimento;
	}


	//***********************************************************************************************************************
	//***************************************************SPOSTAMENTO*********************************************************
	//***********************************************************************************************************************
	/**
	 * Metodo che segue il vero spostamento di un Dinosauro da una Cella ad un'altra.
	 * Esso richiama i metodi per lo spostamento dui vari tipi di terreno ed eventualmente quelli per
	 * mangiare Dinosauri e/o Occupanti.
	 * E' strutturato in modo da permettere ad un Dinosauro di mangiare il nemico e successivamente
	 * mangiare anche l'occupante (solo dopo aver combattuto e vinto col nemico).
	 * @param mosso Dinosauro che esegue lo spostamento.
	 * @param riga int che rappresenta la riga della mappa in cui spostarsi.
	 * @param colonna int che rappresenta la colonna della mappa in cui spostarsi.
	 * @return Un boolean: 'true' se lo spostamento ha avuto successo, 
	 * 			'false' se ci sono stati problemi.
	 */
	public boolean spostaDinosauro(Dinosauro mosso, int riga, int colonna) {
		//ottengo cella di destinazione, individuata da (riga,colonna)
		Cella destinazione = this.partita.getIsola().getMappa()[riga][colonna];

		//se nella destinazione c'e' acqua blocca subito il metodo con return false;
		if(destinazione==null) {
			return false;
		} else {
			//XXX questa e' la logica per far si che dopo al combattimento si possa anche mangiare l'occupante
			if(destinazione.getDinosauro()!=null) {
				//combatto col dinosauro presente nella cella di destinazione
				boolean risultato = this.combatti(mosso, riga, colonna);
				if(destinazione.getOccupante()!=null) {
					//mangio l'occupante, distinguendo se vegetazione o carogna
					if(risultato==false || this.mangiaOccupante(mosso, riga, colonna)==false) {
						risultato=false;
					}
				}
				return risultato;	
			} else {
				if(destinazione.getOccupante()!=null) {
					//mangio l'occupante, distinguendo se vegetazione o carogna
					return this.mangiaOccupante(mosso, riga, colonna);
				} else {
					//mi sto muovendo su terra semplice
					return this.spostamentoSuTerreno(mosso, riga, colonna);
				}
			}
		}
	}
	
	/**
	 * Metodo per far mangiare al Dinosauro il Vegetale o la Carogna
	 * @param dinosauro Dinosauro che esegue lo spostamento.
	 * @param riga  int che rappresenta la riga della mappa in cui spostarsi.
	 * @param colonna int che rappresenta la colonna della mappa in cui spostarsi.
	 * @return Un boolean: 'true' se lo spostamento ha avuto successo, 
	 * 			'false' se ci sono stati problemi.
	 */
	private boolean mangiaOccupante(Dinosauro dinosauro, int riga, int colonna) {
		Cella[][] mappa = this.partita.getIsola().getMappa();
		Cella destinazione = mappa[riga][colonna];
		int vecchiaRiga = dinosauro.getRiga();
		int vecchiaColonna = dinosauro.getColonna();

		if(dinosauro instanceof Carnivoro && destinazione.getOccupante() instanceof Carogna) {
			Carnivoro mosso = (Carnivoro)dinosauro;
			return this.spostamentoConOccupante(mosso, riga, colonna);
		} else 
			if(dinosauro instanceof Erbivoro && destinazione.getOccupante() instanceof Vegetale) {
				Erbivoro mosso = (Erbivoro)dinosauro;
				return this.spostamentoConOccupante(mosso, riga, colonna);
			} else
				//eseguo il movimento in una cella in cui c'e' una carogna e io mi muovo
				//con un erbivoro o una vegetazione con un carnivoro
				if(dinosauro.aggCordinate(riga, colonna)==true) {
					destinazione.setDinosauro(dinosauro);
					mappa[vecchiaRiga][vecchiaColonna].setDinosauro(null);
					return true;
				} else {
					this.partita.identificaDinosauro(dinosauro).rimuoviDinosauro(dinosauro);
					return false;					
				}
	}

	/**
	 * Metodo che esegue lo spostamento di un Dinosauro nella cella di destinazione quando vi
	 * e' un Occupante. In questo caso il metodo capisce se la Carogna/Vegetazione puo' essere
	 * mangiata dal dinosauro e di conseguenza richiama il metodo mangia() per gestire
	 * l'assorbimento dell'energia. Questo metodo privato viene chiamato all'interno di mangiaOccupante.
	 * @param mosso Dinosauro che esegue lo spostamento.
	 * @param riga  int che rappresenta la riga della mappa in cui spostarsi.
	 * @param colonna int che rappresenta la colonna della mappa in cui spostarsi.
	 * @return Un boolean: 'true' se lo spostamento ha avuto successo, 
	 * 			'false' se ci sono stati problemi.
	 */
	private boolean spostamentoConOccupante(Dinosauro mosso, int riga, int colonna) {
		Cella[][] mappa = this.partita.getIsola().getMappa();
		Cella destinazione = mappa[riga][colonna];
		int vecchiaRiga = mosso.getRiga();
		int vecchiaColonna = mosso.getColonna();
		if(mosso.aggCordinate(riga, colonna)==true) {
			if(mosso.mangia(destinazione.getOccupante())==true) {
				//se l'azione "mangia" ha esaurito tutta l'energia dell'occupante,
				//quest'ultimo deve essere rimosso dalla cella e ne deve essere riscreato
				//uno nuovo con energia al max in un punto a caso della mappa 
				destinazione.setOccupante(null);
				this.riposizionaOccupante(riga, colonna, new Carogna());
			}
			destinazione.setDinosauro(mosso);
			
			//XXX l'if sotto e' importantissimo!!! Esso controlla se il dinosauro sta mangiando
			//l'occupante dopo un'azione di movimento o se invece il dinosauro ha prima combattuto
			//con un altro dino e poi nella sua destinazione ha mangiato l'occupante sulla sua stessa
			//cella, quindi con coordinate di partenza e arrivo uguali
			//se avviene cio' il dinosauro non deve essere cancellato, altrimenti si perde il riferimento
			//nella mappa, perche' si cancellerebbe proprio la destinazione raggiunta del dinosauro vincente
			if(riga!=vecchiaRiga || colonna!=vecchiaColonna) {
				mappa[vecchiaRiga][vecchiaColonna].setDinosauro(null);
			}
			return true;
		} else {
			this.partita.identificaDinosauro(mosso).rimuoviDinosauro(mosso);
			return false;	
		}
	}

	/**
	 * Metodo per far combattere due Dinosauri.
	 * @param dinosauro Dinosauro che esegue lo spostamento.
	 * @param riga  int che rappresenta la riga della mappa in cui spostarsi.
	 * @param colonna int che rappresenta la colonna della mappa in cui spostarsi.
	 * @return Un boolean: 'true' se lo spostamento ha avuto successo, 
	 * 			'false' se ci sono stati problemi.
	 */
	private boolean combatti(Dinosauro dinosauro, int riga, int colonna) {
		Cella[][] mappa = this.partita.getIsola().getMappa();
		Cella destinazione = mappa[riga][colonna];
		int vecchiaRiga = dinosauro.getRiga();
		int vecchiaColonna = dinosauro.getColonna();

		System.out.println("coordinate partenza: " + vecchiaRiga + "," + vecchiaColonna + "coord arrivo: " + riga + "," + colonna);

		//controllo di che tipo e' il dinosauro attaccante
		//se e' carnivoro posso mangiare qualunque altro dinosauro
		if(dinosauro instanceof Carnivoro) {
			Carnivoro attaccante = (Carnivoro)dinosauro;
			return this.spostamentoConDinosauro(attaccante, riga, colonna);
		} else { //se e' erbivoro posso combattere solo contro quelli carnivori (per sopravvivenza e non per mangiare)
			//quello che muovo e' erbivoro, quindi controllo cosa ho nella destinazione

			//se nella destinazione ho un altro erbivoro, essi non possono combattere e l'attaccante
			//dovra' scegliere uno nuova destinazione (nel main)
			if(destinazione.getDinosauro() instanceof Erbivoro) {
				System.out.println("Impossibile muoversi su un altro dinosauro erbivoro");
				return false;
			}
			else { //combatto contro un carnivoro
				Erbivoro attaccante = (Erbivoro)dinosauro; 
				return this.spostamentoConDinosauro(attaccante, riga, colonna);
			}
		}
	}	

	/**
	 * Metodo che esegue lo spostamento di un Dinosauro nella cella di destinazione quando in essa vi
	 * e' un altro Dinosauro. Questo metodo privato viene chiamato all'interno di combatti(...).
	 * @param mosso Dinosauro che esegue lo spostamento.
	 * @param riga  int che rappresenta la riga della mappa in cui spostarsi.
	 * @param colonna int che rappresenta la colonna della mappa in cui spostarsi.
	 * @return Un boolean: 'true' se lo spostamento ha avuto successo, 
	 * 			'false' se ci sono stati problemi.
	 */
	private boolean spostamentoConDinosauro(Dinosauro mosso, int riga, int colonna) {
		Cella[][] mappa = this.partita.getIsola().getMappa();
		Cella destinazione = mappa[riga][colonna];
		int vecchiaRiga = mosso.getRiga();
		int vecchiaColonna = mosso.getColonna();
		Dinosauro attaccato = destinazione.getDinosauro();

		//ottengo il giocatore che possiede il dinosauro attaccato
		//se giocatore==null e' perche' o attaccato non e' posseduto da nessuno
		//o se attaccato==null
		Giocatore giocatore = this.partita.identificaDinosauro(destinazione.getDinosauro());

		Dinosauro attaccante;
		if(mosso instanceof Carnivoro) {
			attaccante = (Carnivoro)mosso;
		} else {
			attaccante = (Erbivoro)mosso;
		}

		if(attaccante.aggCordinate(riga, colonna)==true) {
			//esegue il comattimento e stabilisce in vincitore in base al risultato del metodo
			//restituisce true se vince l'attaccante o false se vince l'attaccato (nemico)
			if(attaccante.combatti(destinazione.getDinosauro())==true) {
				destinazione.setDinosauro(attaccante);
			} else {
				destinazione.setDinosauro(attaccato);
			}
			if(attaccante.equals(destinazione.getDinosauro())){
				//ha vinto l'attaccante
				System.out.println("Rimosso dinosauro attaccato");
				giocatore.rimuoviDinosauro(attaccato,mappa[vecchiaRiga][vecchiaColonna]);
			} else { 
				//vince il dino che si trova sulla cella di destinazione prima del movimento
				System.out.println("Rimosso dinosauro attaccante");
				giocatore=this.partita.identificaDinosauro(attaccante);
				giocatore.rimuoviDinosauro(attaccante,mappa[vecchiaRiga][vecchiaColonna]);
			}
			return true;
		}
		else {
			//il dinosauro muore perche' non ha abbastanza energia per muoversi
			//il metodo rimuoviDinosauro lo cancella dalla lista dei dinosauri e anche dalla cella
			this.partita.identificaDinosauro(attaccante).rimuoviDinosauro(attaccante);
			return false; 					
		}
	}


	

	/**
	 * Metodo che esegue un semplice spostamento del Dinosauro su una cella della mappa
	 * costituita da terreno semplice, cioe' senza Dinosauri e/o Occupanti.
	 * @param mosso Dinosauro che esegue lo spostamento.
	 * @param riga  int che rappresenta la riga della mappa in cui spostarsi.
	 * @param colonna int che rappresenta la colonna della mappa in cui spostarsi.
	 * @return Un boolean: 'true' se lo spostamento ha avuto successo, 
	 * 			'false' se ci sono stati problemi.
	 */
	private boolean spostamentoSuTerreno(Dinosauro dinosauro, int riga, int colonna) {
		Cella[][] mappa = this.partita.getIsola().getMappa();
		Cella destinazione = mappa[riga][colonna];
		int vecchiaRiga = dinosauro.getRiga();
		int vecchiaColonna = dinosauro.getColonna();

		if(dinosauro.aggCordinate(riga, colonna)==true) {
			destinazione.setDinosauro(dinosauro);
			mappa[vecchiaRiga][vecchiaColonna].setDinosauro(null);
			return true;
		} else {
			this.partita.identificaDinosauro(dinosauro).rimuoviDinosauro(dinosauro);
			return false;					
		}
	}

	/**
	 * Metodo che si occupa di riposizionare gli Occupanti all'interno della mappa dopo che sono
	 * stati mangiati da altri Dinosauri.
	 * @param riga  int che rappresenta la riga della mappa in cui spostarsi.
	 * @param colonna int che rappresenta la colonna della mappa in cui spostarsi.
	 * @param elemento valore che rappresenta l'Occupante da riposizionare.
	 */
	private void riposizionaOccupante(int riga, int colonna, Occupante elemento) {
		Occupante occupante;
		Random random = new Random();
		int nuovaRiga, nuovaColonna;
		boolean stato=false;

		if(elemento instanceof Vegetale) {
			occupante = (Vegetale)elemento;
		} else {
			occupante = (Carogna)elemento;
		}
		do {
			nuovaRiga = random.nextInt(40);
			nuovaColonna = random.nextInt(40); 
			if((nuovaRiga!=riga || nuovaColonna!=colonna) 
					&& this.partita.getIsola().getMappa()[nuovaRiga][nuovaColonna]!=null &&
					this.partita.getIsola().getMappa()[nuovaRiga][nuovaColonna].getOccupante()==null) {
				this.partita.getIsola().getMappa()[nuovaRiga][nuovaColonna].setOccupante(occupante);
				stato=true;
			}
		} while(stato==false);
		System.out.println("Nuovo elemento in: " + nuovaRiga + "," + nuovaColonna);
	}
	
	//FIXME inserire questo metodo alla fine di ogni turno della partita e testarlo
	/**
	 * Metodo che scansiona tutta la mappa di gioco in cerca di Carogne
	 * con energia =0 per cancellarle e ricrearle nella mappa
	 * @param mappa Array bidimensionale per Celle che contiene la mappa del gioco.
	 */
	public void ricreaCarogne(Cella[][] mappa) {
		for(int i=0;i<40;i++) {
			for(int j=0;j<40;j++) {
				if(mappa[i][j]!=null && (mappa[i][j].getOccupante() instanceof Carogna)) {
					Carogna carogna = (Carogna)mappa[i][j].getOccupante();
					if(carogna.getEnergia()<=0) {
						System.out.println("carogna riposizionate");
						this.riposizionaOccupante(i, j, new Carogna());
						mappa[i][j].setOccupante(null);
					}
				}
			}
		}
	}

}
