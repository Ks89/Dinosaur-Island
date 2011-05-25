package isoladinosauri.modellodati;

import java.util.Random;

import isoladinosauri.Cella;
import isoladinosauri.Giocatore;


/**
 * Superclasse ASTRATTA di Carnivoro e Erbivoro che
 * implementa i metodi comuni per gestire le azioni
 */
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
	public abstract void mangia(Cella cella);
	public abstract void combatti(Cella cella);

	public boolean aumentaDimensione(Giocatore giocatore) {
		//nel caso in cui la dimensione sia gia' massima
		//ritorna false perche' non e' in grado di far crescere
		//la dimensione del dinosauro
		if(this.dimensione < 5) {
			if(super.getEnergia()>(super.getEnergiaMax()/2)) {
				this.dimensione++;
				super.energia -= super.energiaMax / 2;
				super.energiaMax = 1000 * this.dimensione;
				return true;
			} else {
				return false;
			}
		}
		else {
			System.out.println("Raggiunta la dimensiona massima, impossibile completare l'operazione");
			return false; 
		}
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
			//il dino deve essere cancellato dalla cella e dalla lista del giocatore
			//dal metodo che chiama aggCordinate (cioe' quello che si occupa del Movimento in Turno)
			//tramite rimuoviDinosauro() in Giocatore
			return false;
		}
	}

	//metodo per far deporre un Uovo al dinosauro
	public boolean deponi(Cella cella, Giocatore giocatore) {
		//la cella deve essere quella in cui c'e' il dinosauro che depone
		super.energia -= 1500;
		
		if(super.getEnergia()>0) {
			//il dinosauro pu' compiere l'azione di deposizione, ma solo se il num di dino
			//sommati a quello delle uova (perche' in futuro saranno anch'essi dino) sia <5
			if((giocatore.getDinosauri().size() + giocatore.getUova().size()) < 5 ) {
				giocatore.aggiungiUovo(super.getRiga(),super.getColonna());
				return true;
			} else return false; //squadra completa e non posso creare altri dinosauri deponendo uova	
		} else { //il dinosauro muore perche' non ha sufficiente energia
			giocatore.rimuoviDinosauro(this, cella);	
			return false;
		}
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
