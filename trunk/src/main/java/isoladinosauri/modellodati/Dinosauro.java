package isoladinosauri.modellodati;

import java.util.Random;

import isoladinosauri.Cella;
import isoladinosauri.Giocatore;


public abstract class Dinosauro extends Organismo implements Animale {

	protected int dimensione;
	protected int etaAttualeDinosauro;
	protected int durataVita;
	protected int turnoNascita;
	protected String id;

	protected Dinosauro(String id, int riga, int colonna, int turnoNascita) {
		this.setId(id);
		super.setEnergia(750);
		super.energiaMax=1000;
		super.riga = riga;
		super.colonna = colonna;
		this.dimensione=1;
		Random random = new Random();
		this.durataVita = random.nextInt(13) + 24;
		this.turnoNascita = turnoNascita;
		this.setEtaDinosauro(0);
	}
	
	public abstract int calcolaForza();

	public boolean aumentaDimensione() {
		//nel caso in cui la dimensione sia gia' massima
		//ritorna false perche' non e' in grado di far crescere
		//la dimensione del dinosauro
		if(dimensione < 5) {
			dimensione++;
			super.energia -= super.energiaMax / 2;
			super.energiaMax = 1000 * this.dimensione;
			return true;
		}
		else return false; 
	}

	public boolean aggCordinate(int riga, int colonna) {
		//esegue il movimento nelle coordinate specificate ed e' chiamato dal metodo
		//del movimento nella classe Turno
		super.energia -= 10 * (int)Math.pow(2, (double)this.dimensione);
		if(super.getEnergia()>0) { 
			//eseguo movimento correttamente
			super.setRiga(riga);
			super.setColonna(colonna);
			return true;
		} else {
			//TODO il dino deve essere cancellato dalla cella e dalla lista del giocatore
			//dal metodo che chiama aggCordinate (cioe' quello che si occupa del Movimento in Turno)
			//tramite rimuoviDinosauro() in Giocatore
			return false;
		}
	}

	//metodo per far deporre un Uovo al dinosauro
	public void deponi(Cella cella, Giocatore giocatore) {
		//la cella deve essere quella in cui c'e' il dinosauro che depone
		super.energia -= 1500;

		if(super.getEnergia()>0) {
			//il dinosauro pu' compiere l'azione di deposizione
			if(giocatore.getDinosauri().size()<5) giocatore.aggiungiUovo(super.getRiga(),super.getColonna());
			else {
				System.out.println("Errore squadra completa");
				//errore squadra completa e non posso creare altri dinosauri
			}
		}
		//il dinosauro muore perche' non ha sufficiente energia
		else giocatore.rimuoviDinosauro(this, cella);		
	}

	public int getEtaDinosauro() {
		return etaAttualeDinosauro;
	}

	public void setEtaDinosauro(int etaAttualeDinosauro) {
		this.etaAttualeDinosauro = etaAttualeDinosauro;
	}

	public void incrementaEtaDinosauro() {
		this.etaAttualeDinosauro++;;
	}

	public int getTurnoNascita() {
		return turnoNascita;
	}

	public void setTurnoNascita(int turnoNascita) {
		this.turnoNascita = turnoNascita;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
}
