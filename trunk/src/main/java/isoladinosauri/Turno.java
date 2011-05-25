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
 * gestire l'illuminazione e la vista
 * movimento, combattimento ecc.. dei dinosauri
 * riposizionamento vegetazione e carogna dopo che sono
 * state mangiate.
 */
public class Turno {

	private Partita partita;

	public Turno(Partita partita) {
		this.partita = partita;
	}

	//*******************************************************************************************************************
	//****************************************GESTIONE VISTA E ILLUMINAZIONE*********************************************
	//*******************************************************************************************************************

	//esegue l'illuminazione della mappa con un certo raggio
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

	//ottengo nell'array vista le coodinare (riga, colonna) della vista;
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

	//per ottenere le coordinate di origine della vista del Dinosauro
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
	//*****************************************GESTIONE MOVIMENTO********************************************************
	//*******************************************************************************************************************
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
					if(mappaMovimento[riga][colonna] == passo-1) {
						if(riga-1<0) {
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
							if(colonna-1<0) {
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

	public int [][] ottieniStradaPercorsa(int sorgRiga, int sorgColonna, int destRiga, int destColonna) {

		int[][] mappaMovimento = this.ottieniRaggiungibilita(sorgRiga,sorgColonna);
		int i=0,j=0,maxR,maxC,riga,colonna,partenzaDinoRiga,partenzaDinoColonna;
		int arrivoDinoRiga,arrivoDinoColonna,deltaRiga,deltaColonna,distMin,cont=0;
		int rigaSu,rigaGiu,colonnaSx,colonnaDx;
		boolean trovato=false;

		deltaRiga = destRiga - sorgRiga;
		deltaColonna = destColonna - sorgColonna;

		maxR = mappaMovimento.length;
		maxC = mappaMovimento[0].length;

		//cerco le coordinate fittizie (partenzaDinoX,partenzaDinoY) del dinosauro nella sottomappa
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

			arrivoDinoRiga = partenzaDinoRiga + deltaRiga;
			arrivoDinoColonna = partenzaDinoColonna + deltaColonna;

			distMin = mappaMovimento[arrivoDinoRiga][arrivoDinoColonna];

			riga = arrivoDinoRiga;
			colonna = arrivoDinoColonna;

			mappaMovimento[riga][colonna] -= 7;

			for(cont=distMin; cont>0; cont--) {
				if(riga-1<0) {
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
					if(colonna-1<0) {
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
						if(mappaMovimento[i][j] == cont-1) {
							trovato=true;
							mappaMovimento[i][j] -= 7;
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
	 * @param mosso - Dinosauro che esegue lo spostamento
	 * @param riga - riga della mappa in cui spostarsi
	 * @param colonna - colonna della mappa in cui spostarsi
	 * @return - 'true' se lo spostamento ha avuto successo, 'false' se ci sono stati problemi
	 */
	public boolean spostaDinosauro(Dinosauro mosso, int riga, int colonna) {
		//riga e colonna sono le coordinate della destinazione
		//restituisce true se il movimento e' corretto o false se c'e' stato un problema

		//ottengo cella di destinazione, individuata da (riga,colonna)
		Cella destinazione = this.partita.getIsola().getMappa()[riga][colonna];

		//se nella destinazione c'e' acqua blocca subito il metodo con return false;
		if(destinazione==null) return false;
		else {
			//TODO dubbio su cosa mangiare nel caso ci siano piu' cose assieme su una cella
			if(destinazione.getDinosauro()!=null) {
				//combatto col dinosauro presente nella cella di destinazione
				return this.combatti(mosso, riga, colonna);
			} else 			
				if(destinazione.getOccupante()!=null) {
					//mangio l'occupante, distinguendo se vegetazione o carogna
					return this.mangiaOccupante(mosso, riga, colonna);
				} else {
					//mi sto muovendo su terra semplice
					return this.spostamentoSuTerreno(mosso, riga, colonna);
				}
		}
	}

	private boolean spostamentoConOccupante(Dinosauro mosso, int riga, int colonna) {
		Cella[][] mappa = this.partita.getIsola().getMappa();
		Cella destinazione = mappa[riga][colonna];
		int vecchiaRiga = mosso.getRiga();
		int vecchiaColonna = mosso.getColonna();
		if(mosso.aggCordinate(riga, colonna)==true) {
			mosso.mangia(destinazione);
			destinazione.setDinosauro(mosso);
			mappa[vecchiaRiga][vecchiaColonna].setDinosauro(null);
			this.riposizionaOccupante(riga, colonna, new Carogna());
			return true;
		} else {
			this.partita.identificaDinosauro(mosso).rimuoviDinosauro(mosso);
			return false;	
		}
	}

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
				//eseguo il movimento in una cella in cui c'e' una carogna e io mi muovo con un erbivoro o una vegetazione con un carnivoro
				if(dinosauro.aggCordinate(riga, colonna)==true) {
					destinazione.setDinosauro(dinosauro);
					mappa[vecchiaRiga][vecchiaColonna].setDinosauro(null);
					return true;
				} else {
					this.partita.identificaDinosauro(dinosauro).rimuoviDinosauro(dinosauro);
					return false;					
				}
	}

	private boolean spostamentoConDinosauro(Dinosauro mosso, int riga, int colonna) {
		Cella[][] mappa = this.partita.getIsola().getMappa();
		Cella destinazione = mappa[riga][colonna];
		int vecchiaRiga = mosso.getRiga();
		int vecchiaColonna = mosso.getColonna();
		Dinosauro attaccato = destinazione.getDinosauro();

		//ottengo il gicoatore che possiede il dinosauro attaccato
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
			attaccante.combatti(destinazione); //TODO potrei rifare il metodo per far restituire il dinosauro vincitore e senza far acquisire la cella per poi settare in questo metodo il vincitore nella cella
			//il metodo mangia mette il vincitore direttamente nella cella di destinazione
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

}
