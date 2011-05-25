package isoladinosauri.modellodati;

import java.util.Random;

import isoladinosauri.Cella;

/**
 * Superclasse ASTRATTA di Carnivoro e Erbivoro che
 * implementa i metodi comuni per gestire le azioni
 */
public abstract class Dinosauro extends Organismo implements Animale {

	private int etaAttualeDinosauro;
	private int durataVita;
	private int turnoNascita;
	private String id;

	protected Dinosauro(String id, int riga, int colonna, int turnoNascita) {
		this.setId(id);
		super.setEnergia(750);
		super.setEnergiaMax(1000);
		super.setRiga(riga);
		super.setColonna(colonna);
 		Random random = new Random();
		this.durataVita = random.nextInt(13) + 24;
		this.turnoNascita = turnoNascita;
		this.setEtaDinosauro(0);
	}

	public abstract int calcolaForza();
	public abstract void mangia(Cella cella);
	public abstract void combatti(Cella cella);

	//restituisce il raggio stabilito in base alla dimensione del dinosauro
	public int calcolaRaggioVisibilita () {
		//secondo le specifiche della sezione Visibilita'
		int dimensione = super.getEnergiaMax()/1000;
		int raggioStabilito;
		if(dimensione==1) {
			raggioStabilito = 2;
		} else {
			if(dimensione==2 || dimensione==3) {
				raggioStabilito = 3;
			} else {
				raggioStabilito = 4; // se il raggio==4 || raggio==5
			}
		}	
		return raggioStabilito;
	}
	
	public boolean aumentaDimensione() {
		//nel caso in cui la dimensione sia gia' massima
		//ritorna false perche' non e' in grado di far crescere
		//la dimensione del dinosauro
		if(super.getEnergiaMax()/1000 < 5) {
			if(super.getEnergia()>(super.getEnergiaMax()/2)) {
				super.setEnergia(super.getEnergia() - super.getEnergiaMax()/2);

				super.setEnergiaMax(1000+super.getEnergiaMax());
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
		super.setEnergia(super.getEnergia() - 10 * (int)Math.pow(2, (double)super.getEnergiaMax()/1000));
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

	public int getEtaDinosauro() {
		return etaAttualeDinosauro;
	}

	public void setEtaDinosauro(int etaAttualeDinosauro) {
		this.etaAttualeDinosauro = etaAttualeDinosauro;
	}

	public void incrementaEtaDinosauro() {
		this.etaAttualeDinosauro++;
	}

	public int getTurnoNascita() {
		return turnoNascita;
	}

	public void setTurnoNascita(int turnoNascita) {
		this.turnoNascita = turnoNascita;
	}


	public int getEtaAttualeDinosauro() {
		return etaAttualeDinosauro;
	}

	public void setEtaAttualeDinosauro(int etaAttualeDinosauro) {
		this.etaAttualeDinosauro = etaAttualeDinosauro;
	}

	public int getDurataVita() {
		return durataVita;
	}

	public void setDurataVita(int durataVita) {
		this.durataVita = durataVita;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
}
