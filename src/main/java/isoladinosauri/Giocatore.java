package isoladinosauri;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;

import java.util.ArrayList;
import java.util.List;

public class Giocatore extends Utente {
	
	private Partita partita;
	private int vitaTurni;

	private int turnoNascita;
	private String nomeSpecie;
	private List<Dinosauro> dinosauri; //squadra di dinosauri del giocatore
	private boolean[][] mappaVisibile; //gestisce visuale giocatore con buio (se è false)
	
	//ATTENZIONE: SOLO IL PRIMO DINOSAURO DEL GICOATORE E' POSIZIONATO A CASO NELLA MAPPA
	//GLI ALTRI NASCONO DALLE UOVA VICINO AL DINOSAURO CHE LE GENERA
	//PER QUESTO IL METODO POSIZIONADINOSAURO() CHIAMATO NEL COSTRUTTORE
	//VIENE USATO SOLO NELLA CREAZIONE DEL GIOCATORE (E DI CONSEGUENZA DEL SUO PRIMO DINO)
	//DOPO NON SARA' PIU' USATO
	
	public Giocatore(Partita partita, int vitaTurni, int turnoNascita, String nomeSpecie, String tipoSpecie) {
		this.partita = partita;
		this.vitaTurni = vitaTurni;
		this.turnoNascita = turnoNascita;
		this.nomeSpecie = nomeSpecie;
		dinosauri = new ArrayList<Dinosauro>();
		
		//ottengo posX e posY in un vettore di interi di 2 elementi
		int[] pos = this.posizionaDinosauro();
		
		if(tipoSpecie.equals("carnivoro")) {
			Dinosauro dinosauro = new Carnivoro(pos[0], pos[1], turnoNascita);
			dinosauri.add((Carnivoro)dinosauro);
		}
		if(tipoSpecie.equals("erbivoro")) {
			Dinosauro dinosauro = new Erbivoro(pos[0], pos[1], turnoNascita);
			dinosauri.add((Erbivoro)dinosauro);
		}

		mappaVisibile = new boolean[40][40];
		this.inizializzaMappaBuia();
		
		//TODO nb: la mappa non deve essere tutta buia, ma un'area intorno al dinosauro
		//appena creato deve essere visibile, ma non l'ho ancora implementato
	}
	
	private int[] posizionaDinosauro () {
		int[] coordinate = new int[2];
		//metodo che fornisce le coordinate in cui andare
		//a mettere in dinosauro, stando attento a non
		//metterlo dove c'e' gia' un altro o dove c'e'
		//acqua. Il metodo mette in un array con 2 interi
		//posX e posY
		return coordinate;
	}
	
	private void inizializzaMappaBuia() {
		//TODO manca il fattto che la mappa non deve essere inzializzata tutta a buio
		// ma dipende dalla posizione del dinosauro di partenza
		for(int j=0;j<40;j++) {
			for(int i=0;i<40;i++) {
				mappaVisibile[i][j]=false;
			}
		}
	}
	
	public int getVitaTurni() {
		return vitaTurni;
	}

	public void setVitaTurni(int vitaTurni) {
		this.vitaTurni = vitaTurni;
	}

	public int getTurnoNascita() {
		return turnoNascita;
	}

	public void setTurnoNascita(int turnoNascita) {
		this.turnoNascita = turnoNascita;
	}

	public String getNomeSpecie() {
		return nomeSpecie;
	}

	public void setNomeSpecie(String nomeSpecie) {
		this.nomeSpecie = nomeSpecie;
	}
	
	public List<Dinosauro> getDinosauri() {
		return dinosauri;
	}

	public void aggiungiDinosauro(Dinosauro dinosauro) {
    	if(dinosauri.size()<8) dinosauri.add(dinosauro);
    	else { 
    		//squadra piena con 5 dinosauri
    	}
	}

	public void rimuoviDinosauro(Dinosauro dinosauro) {
    	boolean stato = dinosauri.remove(dinosauro);
    	if(stato==true) {
    		//dinosauro rimosso correttamente
    	}
    	else {
    		//dinosauro non trovato 
    	}
	}
	
	public boolean[][] getMappaVisibile() {
		return mappaVisibile;
	}

	public void setMappaVisibile(boolean[][] mappaVisibile) {
		this.mappaVisibile = mappaVisibile;
	}

	void niente() {
		if (dinosauri.get(0) instanceof Carnivoro) {
//			((Carnivoro)dinosauri.get(0)). bla bla
			//TODO
		}
	}

}
