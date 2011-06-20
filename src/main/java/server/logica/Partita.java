package server.logica;



import java.util.ArrayList;
import java.util.List;

import server.modellodati.Carnivoro;
import server.modellodati.Dinosauro;
import server.modellodati.Erbivoro;



/**
 * Classe Partita per la gestione dei giocatori,
 * per l'eta' e per far schiudere le uova
 */
public class Partita {

	private static final int MAX = 40;
	private static final int MAXGIOC = 8;
	private Isola isola;
	private Turno turnoCorrente; //Conserva solo il turno corrente
	private List<Giocatore> giocatori;
	private int contatoreTurni;

	/**
	 * Costruttore della classe Partita che inizializza i Giocatori e l'Isola.
	 * @param isola riferimento alla classe Isola contente la mappa di Cella[][].
	 */
	public Partita(Isola isola)
	{
		this.giocatori = new ArrayList<Giocatore>();
		this.isola = isola;
	}

	/**
	 * Metodo per aggiungere un Giocatore alla Partita.
	 * @param giocatore riferimento al Giocatore che deve essere aggiunto in Partita.
	 */
	public void aggiungiGiocatore(Giocatore giocatore) {
		if(this.giocatori.size()<MAXGIOC) {
			giocatori.add(giocatore);
		} else { 
			System.out.println("Impossibile accedere, sono concessi solo 8 giocatori online");
			//partita piena con 8 giocatori
		}
	}

	/**
	 * Metodo per rimuovere un giocatore dalla Partita.
	 * @param giocatore riferimento al Giocatore che deve essere rimosso dalla Partita.
	 */
	public void rimuoviGiocatore(Giocatore giocatore) {
		//ricevo giocatore, cioe' il giocatore che devo cancellare		
		//rimuovo tutti i dinosauri del giocatore dalla mappa
		Cella[][] mappa = this.getIsola().getMappa();
		for(int j=0;j<MAX;j++) {
			for(int i=0;i<MAX;i++) {
				if(mappa[i][j]!=null && mappa[i][j].getDinosauro()!=null && 
						giocatore.getDinosauri().contains(mappa[i][j].getDinosauro())) {
					mappa[i][j].setDinosauro(null);
				}		
			}
		}
		//rimuovo il giocatore dalla lista dei giocatori
		giocatori.remove(giocatore);
	}
	
	/**
	 * Metodo che incrementa l'eta' dei giocatori, turno dopo turno.
	 */
	public void incrementaEtaGiocatori() {
		for(int i=0;i<this.getGiocatori().size();i++) {
			this.getGiocatori().get(i).incrementaEtaAttuali();
		}
	}
	
	
	/**
	 * Metodo che restituisce il Giocatore che posside il Dinosauro dato in ingresso.
	 * @param dinosauro - Il Dinosauro che deve essere cercato tra quelli dei Giocatori.
	 * @return Il Giocatore che possiede il Dinosauro nella propria squadra.
	 */
	public Giocatore identificaDinosauro (Dinosauro dinosauro) {
		//gli do un dinosauro e lui lo cerca tra tutti quelli dei giocatori
		//quello che restituisce e' il giocatore che possiede quel dinosauro
		//nella propria arraylist di Dinosauri
		//se il dinosauro da cercare e' null blocco il metodo e ritorno null
		if(dinosauro==null) {
			return null;
		}

		for(int i=0; i<this.getGiocatori().size();i++) {
			for(int j=0; j<this.getGiocatore(i).getDinosauri().size();j++) {
				if(dinosauro.equals(this.getGiocatore(i).getDinosauri().get(j))) {
					return this.getGiocatore(i);
				}
			}
		}    	
		return null;
	}


	
	//************************************************************************************************************
	//************************************GESTONE UOVA E NASCITA DINOSAURI****************************************
	//************************************************************************************************************
	/**
	 * Metodo che scansiona tutti i Giocatori e all'interno di essi controlla tutte le Uova nell'apposito ArrayList. 
	 * Quindi, si preoccupare di chiamare il metodo SchiudiUova per far nascere i Dinosauri.
	 * @param turnoNascita - int che rappresenta il turno in cui sono nati i Dinosauri dalle uova.
	 */
	public void nascitaDinosauro (int turnoNascita) {
		String[] coordinate;
		int[] posizione;
		Dinosauro dinosauro = null;
		Integer riga = null;
		Integer colonna = null;
		String idDinosauro = null;
		List<String> listaUova;

		for(int i=0;i<this.getGiocatori().size();i++) {
			//ottengo la lista uova del giocatore i-esimo
			listaUova = this.getGiocatore(i).getUova();

			//scansiono la lista delle uova appena ottenuta
			for(int j=0; j<listaUova.size();j++) {
				//ottengo le coordinate dell'uovo in questione
				coordinate = listaUova.get(j).split("-");
				riga = Integer.parseInt(coordinate[0]);
				colonna = Integer.parseInt(coordinate[1]);
				idDinosauro = coordinate[2];

				posizione = this.generaCoordinateNascituro(riga,colonna);

				//NB: fare .getDinosauri().get(0) vuol dire  prendere il primo dino del giocatore
				//solo per vedere se e' carnivoro o erbivoro e quindi per capire il tipo della specie del dinosauro
				Dinosauro primoDinosauro = this.getGiocatore(i).getDinosauri().get(0); 

				if(primoDinosauro instanceof Carnivoro) {
					dinosauro = new Carnivoro(idDinosauro,posizione[0], posizione[1], turnoNascita);
				} else {
					if(primoDinosauro instanceof Erbivoro) {
						dinosauro = new Erbivoro(idDinosauro,posizione[0], posizione[1], turnoNascita);
					}
				}
				if(this.getGiocatore(i).aggiungiDinosauro(dinosauro)) {
					this.getIsola().getMappa()[posizione[0]][posizione[1]].setDinosauro(dinosauro);	
					this.getGiocatore(i).rimuoviUova();
				} else {
					//se non riesco ad aggiungere un dinosauro per quel preciso giocatore
					//rimuovo tutte le sue uova, perche' c'e' stato un problema e cosi' evito
					//un numero maggiore di 5 di dinosauri in squadra
					this.getGiocatore(i).rimuoviUova();
				}
			}
		}
	}


	/**
	 * Metodo che riceve le coordinate dell'uovo e genere in uscita quelle in cui si puo' far nascere un Dinosauro.
	 * @param riga int che rappresenta la riga della mappa in cui si trova l'uovo.
	 * @param colonna int che rappresenta la colonna della mappa in cui si trova l'uovo.
	 * @return Un array con i seguenti valori:
	 * 		[0] - riga in cui si deve schiudere l'uovo, 
	 * 		[1] - colonna in cui si deve schiudere l'uovo.
	 */
	private int[] generaCoordinateNascituro (int riga, int colonna) {
		//funzionamento:
		//1) calcolo raggio visuale che parte da 1
		//2) cerco nella visuale uno spazio libero (terra senza dinosauro) (cioe' la cornice subito intorno al dinosauro)
		//3) se c'e' faccio return
		//4) incrementeo il raggio a 2 ecc... fino a che non faccio un return

		//ottengo la vista del dinosauro
		int[] origine;  //origine vista
		int[] estremo; //estremo vista
		int[] coordinate = new int[2];  //coordinare in cui mettero' il nuovo dinosauro

		for(int i=1; i<MAX; i++) { //calcola il raggio della visuale
			origine = this.turnoCorrente.ottieniOrigineVisuale(riga, colonna, i);
			estremo = this.turnoCorrente.ottieniEstremoVisuale(riga, colonna, i);
			for(int w=origine[0]; w<estremo[0]+1; w++) { //mi muovo sulle righe 
				for(int j=origine[1]; j<estremo[1]+1; j++) { //mi muovo sulle colonne
					if(this.getIsola().getMappa()[w][j]!=null && this.getIsola().getMappa()[w][j].getDinosauro()==null) {
						coordinate[0] = w;
						coordinate[1] = j;
						return coordinate;
					}
				}

			}
		}
		return coordinate; //FIXME se arrivo qui e' perche' nella mappa non c'e' spazio (impossibile arrivarci)
	}
	
	
	//************************************************************************************************************
	//***********************************************GETTER E SETTER**********************************************
	//************************************************************************************************************
	
	/**
	 * @return Un riferimento al turno corrente associato alla Partita.
	 */
	public Turno getTurnoCorrente() {
		return turnoCorrente;
	}

	/**
	 * @param turnoCorrente riferimento al Turno per impostare il turnoCorrente nella Partita.
	 */
	public void setTurnoCorrente(Turno turnoCorrente) {
		this.turnoCorrente = turnoCorrente;
	}

	/**
	 * @return L'Isola associata alla partita.
	 */
	public Isola getIsola() {
		return isola;
	}

	/**
	 * @param isola riferimento all'Isola che contiene la mappa di gioco.
	 */
	public void setIsola(Isola isola) {
		this.isola = isola;
	}

	/**
	 * @return Una lista dei Giocatori presenti in Partita.
	 */
	public List<Giocatore> getGiocatori() {
		return giocatori;
	}

	/**
	 * Metodo che permette di ottenere direttamente il Giocatore identificato da un certo indice 'i' nella Partita.
	 * @param i Posizione del Giocatore nell'arrayList di Giocatori.
	 * @return Un Giocatore online in Partita, nella i-esima posizione dell'ArrayList.
	 */
	private Giocatore getGiocatore(int i) {
		return giocatori.get(i);
	}

	/**
	 * @return Un int che rappresenta il contatore dei turni.
	 */
	public int getContatoreTurni() {
		return contatoreTurni;
	}

	/**
	 * @param contatoreTurni - int per impostare il contatore dei turni.
	 */
	public void setContatoreTurni(int contatoreTurni) {
		this.contatoreTurni = contatoreTurni;
	}
}
