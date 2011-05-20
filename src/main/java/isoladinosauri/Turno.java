package isoladinosauri;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;

public class Turno {

	//NB: questa classe deve anche chiamare, alla fine del Turno corrente del giocatore, il metodo incrementaEtaAttuali

	private Partita partita;
	private int contatoreTurno; //di dubbia utilita', perche' a me sembra di aver gia' gestito i turni

	public Turno(Partita partita) {
		this.partita = partita;
	}

	public void illuminaMappa (int posizioneX, int posizioneY) {

		Cella cella = this.partita.getIsola().getMappa()[posizioneX][posizioneY];

		//se e' terra con un dinosauro sopra...
		//qui mi assicuro che il quella posizione ci sia davvero un dinosauro
		if(cella!=null && cella.getDinosauro()!=null) {

			int[] vista = this.ottieniVisuale(posizioneX, posizioneY);
			//System.out.println("illumina -> " + "(" + vista[0] + "," + vista[1] + ")" + "(" + 
			//cella.getDinosauro().getriga() + "," + cella.getDinosauro().getcolonna() + ")" + "(" + vista[2] + "," + vista[3] + ")");

			Giocatore giocatore = this.partita.identificaDinosauro(cella.getDinosauro());
			boolean[][] mappaDaIlluminare = giocatore.getMappaVisibile();

			//vista[0] e vista[2] sono X origine e X fine
			//vista[1] e vista[3] sono Y origine e Y fine
			for(int j=vista[1];j<vista[3]+1;j++) { //scansiono le Y
				for(int i=vista[0];i<vista[2]+1;i++) { //scansiono le X
					mappaDaIlluminare[i][j] = true; //illumino
					giocatore.setMappaVisibile(mappaDaIlluminare);
				}
			}
		} else System.out.println("Eccezione: c'e' un problema perche' sto illuminando una zona in cui non c'e' un dinosauro");
	}

	public int[] ottieniVisuale (int riga, int colonna) {
		int[] vista = new int[4];
		Cella cella = this.partita.getIsola().getMappa()[riga][colonna];

		//se e' terra con un dinosauro sopra...
		//qui mi assicuro che il quella posizione ci sia davvero un dinosauro
		if(cella!=null && cella.getDinosauro()!=null) {

			//ottengo coordinate origine e fine vista tramite i 2 metodi privati
			int dimensione = cella.getDinosauro().getEnergiaMax() / 1000;
			int dimensioneStabilita = this.calcolaRaggioVisibilita(dimensione);

			//ottengo posizione dinosauro

			int[] origineVista = this.ottieniOrigineVisuale(riga, colonna, dimensioneStabilita);
			int[] fineVista = this.ottieniEstremoVisuale(riga, colonna, dimensioneStabilita);

			vista[0] = origineVista[0];
			vista[1] = origineVista[1];
			vista[2] = fineVista[0];
			vista[3] = fineVista[1];
		} else System.out.println("Eccezione: c'e' un problema perche' sto calcolando la visuale di una zona in cui non c'e' un dinosauro");	
		return vista;
	}

	private int calcolaRaggioVisibilita (int raggio) {
		//stabisce la porzione di mappa che il dinosauro deve vedere in base al raggio
		//metodo riciclato piu' volte:
		//1)passando come raggio la dimensione per illuminare la mappa
		//2)passando il numero di passi per il metodo movimento
		int raggioStabilito;
		if(raggio==1) raggioStabilito = 2;
		else if(raggio==2 || raggio==3) raggioStabilito = 3;
		else raggioStabilito = 4; // se il raggio==4 || raggio==5
		return raggioStabilito;
	}

	//metodo public per ottenere le coordinate di origine della vista del Dinosauro
	//e' fatto in modo che possa essere riutilizzato anche dal metodo sulla nascita del dino dall'uovo
	//e anche da quello per la gestione del movimento
	public int[] ottieniOrigineVisuale (int riga, int colonna, int raggio) {
		int[] coordinate = new int[2];
		int i=0, j=0;

		//calcolo in i e j il punto di origine della vista
		//inizio con i=0 nella posizione del dinosauro, dopo lo incremeneto, cioe' mi sto spostando
		//verso l'origine della dimensione del raggio
		//se arrivo a 0 termino subito, se no termino quando
		for(i=0;i<raggio;i++) if(riga - i <= 0) break;
		for(j=0;j<raggio;j++) if(colonna - j <= 0) break;

		coordinate[0] = riga - i;
		coordinate[1] = colonna - j;
		return coordinate;		
	}

	//metodo public per ottenere le coordinate dell'estremo della vista del Dinosauro
	//e' fatto in modo che possa essere riutilizzato anche dal metodo sulla nascita del dino dall'uovo
	//e anche da quello per la gestione del movimento
	public int[] ottieniEstremoVisuale (int riga, int colonna, int raggio) {
		int[] coordinate = new int[2];
		int i=0, j=0;

		//calcolo in i e j il punto estremo in alto a sinistra della vista (fine)
		for(i=0;i<raggio;i++) if(riga + i >=39) break;
		for(j=0;j<raggio;j++) if(colonna + j >=39) break;

		coordinate[0] = riga + i;
		coordinate[1] = colonna + j;
		return coordinate;
	}


	//*******************************************************************************************************************
	//*****************************************GESTIONE MOVIMENTO********************************************************
	//*******************************************************************************************************************

	public int [][] ottieniRaggiungibilita(int sorgRiga, int sorgColonna) {

		int i,j,riga,colonna,maxR,maxC,rigaSu,rigaGiu,colonnaSx,colonnaDx,passo,nPassi;
		int[] origineMappaMovimento = new int [2];
		int[] estremoMappaMovimento = new int [2];

		if(this.partita.getIsola().getMappa()[sorgRiga][sorgColonna].getDinosauro() instanceof Carnivoro){
			origineMappaMovimento = this.ottieniOrigineVisuale(sorgRiga, sorgColonna, 3); //in questo caso 3 e' il numero di passi non la dimensione
			estremoMappaMovimento = this.ottieniEstremoVisuale(sorgRiga, sorgColonna, 3);
			nPassi=3;
		}
		else{ //erbivoro
			origineMappaMovimento = this.ottieniOrigineVisuale(sorgRiga, sorgColonna, 2); //in questo caso 2 e' il numero di passi non la dimensione
			estremoMappaMovimento = this.ottieniEstremoVisuale(sorgRiga, sorgColonna, 2);
			nPassi=2;
		}
		
		//solo per test
//		nPassi=3;

		//estremi del movimento del dinosauro
		maxR = estremoMappaMovimento[0]-origineMappaMovimento[0]+1;
		maxC = estremoMappaMovimento[1]-origineMappaMovimento[1]+1;

		int[][] mappaMovimento = new int[maxR][maxC];

		// numeri convenzione: 
		//8 = ACQUA
		//9 = NON RAGGIUNGIBILE			

		// copio l'acqua della mappa nella sottomappa e metto non raggiungibile altrove

		for(i=0; i<maxR; i++) {
			for(j=0; j<maxC; j++) {
				if(this.partita.getIsola().getMappa()[origineMappaMovimento[0]+i][origineMappaMovimento[1]+j] == null)
					mappaMovimento[i][j] = 8; // ACQUA=8
				else mappaMovimento[i][j] = 9; // NON RAGGIUNGIBILE = 9
			}
		}
		mappaMovimento[sorgRiga-origineMappaMovimento[0]][sorgColonna-origineMappaMovimento[1]] = 0; // mi posiziono sul dinosauro e sono al passo 0

		// scandisco ora la mappaMovimento e per ogni passo stabilisco se posso muovermi sulla cella
		for(passo=1; passo<=nPassi; passo++) {
			for(riga=0; riga<maxR; riga++) {
				for(colonna=0; colonna<maxC; colonna++) {
					if(mappaMovimento[riga][colonna] == passo-1) {
						if(riga-1<0)rigaSu=riga;
						else rigaSu=riga-1;
						if(riga+1>=maxR) rigaGiu=riga;
						else rigaGiu=riga+1;

						for(i=rigaSu; i<=rigaGiu; i++) {
							if(colonna-1<0) colonnaSx=colonna;
							else colonnaSx=colonna-1;
							if(colonna+1>=maxC) colonnaDx=colonna;
							else colonnaDx=colonna+1;
							for(j=colonnaSx; j<=colonnaDx; j++) {
								// non c'e' acqua e non c'e' un numero di passi inferiori che mi permettono di raggiungere la cella
								if(mappaMovimento[i][j] == 9) mappaMovimento[i][j] = passo;
							}
						}
					}
				}
			}
		}
		// ora tutte le celle che contengono numeri diversi da 8 (acqua) e diversi da 9 (non raggiungibile) sono raggiungibili dal dinosauro con percorso 0->1->2->3	
		return mappaMovimento;
	}

	public int [][] ottieniStradaPercorsa(int sorgRiga, int sorgColonna, int destRiga, int destColonna) {

		int[][] mappaMovimento = this.ottieniRaggiungibilita(sorgRiga,sorgColonna);
		int i=0,j=0,maxR,maxC,riga,colonna,trovato,partenzaDinoRiga,partenzaDinoColonna,arrivoDinoRiga,arrivoDinoColonna,deltaRiga,deltaColonna,distMin,cont=0;
		int rigaSu,rigaGiu,colonnaSx,colonnaDx;

		deltaRiga = destRiga - sorgRiga;
		deltaColonna = destColonna - sorgColonna;

		maxR = mappaMovimento.length;
		maxC = mappaMovimento[0].length;

		//cerco le coordinate fittizie (partenzaDinoX,partenzaDinoY) del dinosauro nella sottomappa
		for(i=0,trovato=0; i<maxR && trovato==0; i++)
			for(j=0; j<maxC && trovato==0; j++)
				if(mappaMovimento[i][j]==0) trovato = 1;

		if(trovato==1){    	

			partenzaDinoRiga = i-1;
			partenzaDinoColonna = j-1;

			arrivoDinoRiga = partenzaDinoRiga + deltaRiga;
			arrivoDinoColonna = partenzaDinoColonna + deltaColonna;

			distMin = mappaMovimento[arrivoDinoRiga][arrivoDinoColonna];

			riga = arrivoDinoRiga;
			colonna = arrivoDinoColonna;

			mappaMovimento[riga][colonna] -= 7;

			for(cont=distMin; cont>0; cont--) {
				if(riga-1<0) rigaSu=riga;
				else rigaSu=riga-1;
				if(riga+1>=maxR) rigaGiu=riga;
				else rigaGiu=riga+1;
				for(i=rigaSu,trovato=0; i<=rigaGiu && trovato==0; i++) {
					if(colonna-1<0) colonnaSx=colonna;
					else colonnaSx=colonna-1;
					if(colonna+1>=maxC) colonnaDx=colonna;
					else colonnaDx=colonna+1;
					for(j=colonnaSx; j<=colonnaDx && trovato==0; j++) {
						if(mappaMovimento[i][j] == cont-1){
							trovato=1;
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


	public void spostaDinosauro(Dinosauro mosso, Cella destinazione, int riga, int colonna) {
		Giocatore giocatore = this.partita.identificaDinosauro(mosso);
		if(destinazione.getDinosauro()!=null) {
			//esegui combattimento
			if(mosso instanceof Carnivoro) {
				Carnivoro muovente = (Carnivoro)mosso;
				muovente.mangia(destinazione.getDinosauro(), destinazione);
			}
			else {
				Erbivoro muovente = (Erbivoro)mosso; 
				//Carnivoro dinoDestinazione = (Carnivoro)destinazione.getDinosauro();
				//destinazione.getDinosauro().mangia(muovente, this.partita);
			}

		} else {
			//sposta il dinosauro perche' nella cella non c'e' nessuno
			if(mosso.aggCordinate(riga, colonna)==true) {
				//movimento eseguito correttamente
			} else {
				//il dinosauro muore perche' non ha abbastanza energia per muoversi
				//il metodo rimuoviDinosauro lo cancella dalla lista dei dinosauri e anche dalla cella
				this.partita.identificaDinosauro(mosso).rimuoviDinosauro(mosso, this.partita.getIsola().getMappa()[mosso.getRiga()][mosso.getColonna()]);

			}
		}
	}

	public void incrementaEtaGiocatori() {
		for(int i=0;i<this.partita.getGiocatori().size();i++) this.partita.getGiocatori().get(i).incrementaEtaAttuali();
	}


	public int getContatoreTurno() {
		return contatoreTurno;
	}

	public void setContatoreTurno(int contatoreTurno) {
		this.contatoreTurno = contatoreTurno;
	}
}
