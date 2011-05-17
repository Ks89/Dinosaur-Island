package isoladinosauri;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Giocatore extends Utente {

	private Partita partita;
	private int idGiocatore; //serve per identificare il giocatore quando creo gli id dei suoi dinosauri
	
	private int etaAttualeGiocatore; //da quanto e' in vita. Se arriva a 120 il giocatore "muore"
	private int turnoNascita; //turno di nascita del giocatore
	
	private String nomeSpecie;
	private List<Dinosauro> dinosauri; //squadra di dinosauri del giocatore
	private boolean[][] mappaVisibile; //gestisce visuale giocatore con buio (se è false)

	private List<String> uova; //e' un array di uova del giocatore che viene svuotato alla fine di ogni giro dei giocatori

	//ATTENZIONE: SOLO IL PRIMO DINOSAURO DEL GIOCATORE E' POSIZIONATO A CASO NELLA MAPPA
	//GLI ALTRI NASCONO DALLE UOVA VICINO AL DINOSAURO CHE LE GENERA
	//PER QUESTO IL METODO POSIZIONADINOSAURO() CHIAMATO NEL COSTRUTTORE
	//VIENE USATO SOLO NELLA CREAZIONE DEL GIOCATORE (E DI CONSEGUENZA DEL SUO PRIMO DINO)
	//DOPO NON SARA' PIU' USATO

	public Giocatore(Partita partita, String login, String password, int turnoNascita, String nomeSpecie, String tipoSpecie) {
		this.partita = partita;
		super.setNomeUtente(login);
		super.setPassword(password);
		this.etaAttualeGiocatore=0;
		this.turnoNascita = turnoNascita;
		this.nomeSpecie = nomeSpecie;
		dinosauri = new ArrayList<Dinosauro>();

		//ottengo posX e posY in un vettore di interi di 2 elementi
		int[] pos = this.posizionaDinosauro();

		this.idGiocatore = this.generaIdGiocatore();
		String idDinosauro = this.generaIdDinosauro();

		//creo il dinosauro con ID, POSIZIONE e TURNO NASCITA
		if(tipoSpecie.equals("carnivoro")) {
			Dinosauro dinosauro = new Carnivoro(idDinosauro,pos[0], pos[1], turnoNascita);
			dinosauri.add((Carnivoro)dinosauro);
			this.partita.getIsola().getMappa()[pos[0]][pos[1]].setDinosauro(dinosauro); 
		}
		if(tipoSpecie.equals("erbivoro")) {
			Dinosauro dinosauro = new Erbivoro(idDinosauro,pos[0], pos[1], turnoNascita);
			dinosauri.add((Erbivoro)dinosauro);
			this.partita.getIsola().getMappa()[pos[0]][pos[1]].setDinosauro(dinosauro);
		}
		
		this.mappaVisibile = new boolean[40][40];
		this.inizializzaMappaBuia();

		//TODO fare illuminazione mappa appena viene creato il dinosauro
		//		//illumino la mappa dove c'e' il dinosauro appena creato
		//		System.out.println(pos[0] + " " + pos[1]);
		//		this.partita.getTurnoCorrente().illuminaMappa(pos[0], pos[1]);

		//inizializzo l'array per le uova, ovviamente parte da vuoto perche' non ho uova all'inizio
		this.uova = new ArrayList<String>();
	}
	
	//***********************************************************************************************************************
	//**************************************************POSIZIONAMENTO*******************************************************
	//***********************************************************************************************************************
	private int[] posizionaDinosauro () {
		//metodo che fornisce le coordinate in cui andare
		//a mettere in dinosauro, stando attento a non
		//metterlo dove c'e' gia' un altro o dove c'e'
		//acqua. Il metodo mette in un array con 2 interi
		//posX e posY e fa il return
		int[] coordinate = new int[2];
		Cella cella;
		Random random = new Random();
		do {
			coordinate[0] = random.nextInt(40);
			coordinate[1] = random.nextInt(40);
			//			coordX=23;
			//			coordY=14;
			cella = this.partita.getIsola().getMappa()[coordinate[0]][coordinate[1]];
		} while(cella==null || cella.getDinosauro()!=null);
		return coordinate;
	}

	//***********************************************************************************************************************
	//*****************************************************GENERO ID*********************************************************
	//***********************************************************************************************************************
	private int generaIdGiocatore() {
		//array che indica quali id dei giocatori sono disponibili nella partita
		int[] posizioni = {1,2,3,4,5,6,7,8};
		//se non ci sono giocatori di certo quello nuovo sara' il primo
		if(this.partita.getGiocatori().size()==0) return 1;
		else { 
			for(int j=0;j<this.partita.getGiocatori().size();j++) {
				for(int i=0;i<8;i++) {
					//mette a '0' gli id gia' occupati nell'array posizioni[]
					if(this.partita.getGiocatori().get(j).getIdGiocatore()==posizioni[i]) posizioni[i]=0;
				}
			} //cerco il primo che non e' '0' nell'array e ritorno l'indice + 1 
			for(int i=0;i<8;i++) if(posizioni[i]!=0) return i+1;
		}
		return -1; //TODO se c'e' un problema fa il return con -1
	}
	
	private int generaIdParziale() {
		//uguale a generaIdGiocatore() ma sta volta lavoro sui char, perche' l'id del dinosauro
		//e' il carattere [1] nel campo della classe Dinosauro
		char[] posizioni = {'1','2','3','4','5'};
		if(this.getDinosauri().size()==0) return 1;
		else {
			for(int j=0;j<this.getDinosauri().size();j++) {
				for(int i=0;i<5;i++) if(this.getDinosauri().get(j).getId().charAt(1)==posizioni[i]) posizioni[i]='0';
			}
			for(int i=0;i<5;i++) if(posizioni[i]!='0') return i+1;
		}
		return -1; //TODO se c'e' un problema fa il return con -1
	}
	
	public String generaIdDinosauro() {
		return "" + this.idGiocatore + this.generaIdParziale();
	}
	
	public int getIdGiocatore() {
		return idGiocatore;
	}

	public void setIdGiocatore(int idGiocatore) {
		this.idGiocatore = idGiocatore;
	}
	
	
	//***********************************************************************************************************************
	//*************************************************GESTIONE VISIBILITA'**************************************************
	//***********************************************************************************************************************
	
	public void inizializzaMappaBuia() {
		//Inizializza la mappa tutta a buio e viene chiamato solo alla creazione del giocatore
		for(int j=0;j<40;j++) {
			for(int i=0;i<40;i++) mappaVisibile[i][j]=false;
		}
	}

	
	//***********************************************************************************************************************
	//**************************************************GESTIONE CLASSIFICA**************************************************
	//***********************************************************************************************************************
	public int calcolaPunti() {
		int punti=0;
		for(int i=0;i<this.getDinosauri().size();i++) punti += (this.getDinosauri().get(i).getEnergiaMax()/1000) + 1;
		return punti;
	}

	
	//***********************************************************************************************************************
	//***************************************************GESTIONE DINOSAURO**************************************************
	//***********************************************************************************************************************
	public void aggiungiDinosauro(Dinosauro dinosauro) {
		if(dinosauri.size()<8) dinosauri.add(dinosauro);
		else { 
			//squadra piena con 5 dinosauri
		}
	}

	public void rimuoviDinosauro(Dinosauro dinosauro, Cella cella) {
		boolean stato = this.dinosauri.remove(dinosauro);
		if(stato==true) {
			//dinosauro rimosso correttamente
			//lo cancello anche dalla mappa
			cella.setDinosauro(null);
		}
		else {
			//dinosauro non trovato 
		}
	}

	
	//***********************************************************************************************************************
	//******************************************************GESTIONE UOVA****************************************************
	//***********************************************************************************************************************
	public List<String> getUova() {
		return uova;
	}

	public void aggiungiUovo(int posX, int posY) {
		this.uova.add(posX + "-" + posY);
		//System.out.println(uova.get(0));
	}

	public void rimuoviUovo(Cella cella) {
		boolean stato = uova.remove(cella);
		if(stato==true) {
			//uovo rimosso correttamente
		}
		else {
			//uovo non trovato 
		}
	}
	
	
	//***********************************************************************************************************************
	//**************************************************GESTIONE TURNI*******************************************************
	//***********************************************************************************************************************
	//viene richiamato da Turno, alla fine di ogni turno eseguito dal giocatore
	public void incrementaEtaAttuali() {
		this.etaAttualeGiocatore++;
		for(int i=0;i<this.getDinosauri().size();i++) this.getDinosauri().get(i).incrementaEtaDinosauro();
	}
	
	public int getTurnoNascita() {
		return turnoNascita;
	}

	public void setTurnoNascita(int turnoNascita) {
		this.turnoNascita = turnoNascita;
	}
	
	public int getEtaAttuale() {
		return etaAttualeGiocatore;
	}

	public void setEtaAttuale(int etaAttualeGiocatore) {
		this.etaAttualeGiocatore = etaAttualeGiocatore;
	}
	
	
	//***********************************************************************************************************************
	//**************************************************GET e SET************************************************************
	//***********************************************************************************************************************
	public List<Dinosauro> getDinosauri() {
		return dinosauri;
	}
	
	public boolean[][] getMappaVisibile() {
		return mappaVisibile;
	}

	public void setMappaVisibile(boolean[][] mappaVisibile) {
		this.mappaVisibile = mappaVisibile;
	}
	
	public String getNomeSpecie() {
		return nomeSpecie;
	}

	public void setNomeSpecie(String nomeSpecie) {
		this.nomeSpecie = nomeSpecie;
	}
	
	public void stampaMappa() {
		for(int i=0;i<40;i++) {
			for(int j=0;j<40;j++) {
				if(this.mappaVisibile[i][j]==true) System.out.print(1 + " ");
				if(this.mappaVisibile[i][j]==false) System.out.print(0 + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
