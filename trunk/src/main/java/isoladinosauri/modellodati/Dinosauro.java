package isoladinosauri.modellodati;


import gestioneeccezioni.CrescitaException;
import gestioneeccezioni.MovimentoException;

import java.util.Random;



/**
 * Superclasse ASTRATTA di Carnivoro ed Erbivoro che
 * implementa i metodi astratti: calcolaForza, mangia e combatti.
 * Inoltre, possiede anche i metodi implentati per calcolare il
 * raggio della visibilita, per far crescere il Dinosauro e quello
 * per aggiornare le coordinate.
 */
public abstract class Dinosauro extends Organismo {

	private int etaAttualeDinosauro;
	private int durataVita;
	private int turnoNascita;
	private String id;

	/**
	 * @param id identificativo del Dinosauro composto da una String di 2 elementi "XY", dove 'X'=id giocatore e 'Y'=numero Dinosauro.
	 * @param riga int che rappresenta la riga della mappa in cui si trova il dinosauro.
	 * @param colonna int che rappresenta la colonna della mappa in cui si trova il Dinosauro.
	 * @param turnoNascita int che rappresenta il turno della partita in cui e' stato creato il Carnivoro.
	 * 			Lo scopo di questo valore e' quello di rendere molto semplice il calcolo dell'eta' del Dinosauro.
	 */
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

	/**
	 * @return Un int che rppresenta la forza del Dinosauro, calcolata in modo differente
	 * per Erbivori e Carnivoro. 
	 */
	public abstract int calcolaForza();
	
	
	/**
	 * @param occupante Occupante (Vegetazione o Carogna) che deve essere mangiato dal Dinosauro.
	 * @return Un boolean: 'true': Occupante mangiato completamente
	 * 		e quindi dovra' essere rimosso
	 * 		'false': Occupante mangiato solo in parte e non bisogna fare niente.
	 */
	public abstract boolean mangia(Occupante occupante);
	
	
	/**
	 * @param dinosauro Dinosauro (Erbivoro o Carnivoro) che deve essere attaccato
	 * @return restituisce un boolean: 'true': se il Dinosauro attaccante ha vinto il combattimento
	 * 		'false': se il Dinosauro attaccante ha perso il combattimento.
	 */
	public abstract boolean combatti(Dinosauro dinosauro);


	/**
	 * Metodo per calcolare il raggio di celle intorno al Dinosauro
	 * utilizzato in moltissimi metodi come la visibilita', la deposizione, il movimento ecc...
	 * @return Un int che indica il raggio di celle intorno al 
	 * 			Dinosauro in base alla sua dimensione.
	 */
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
	
	/**
	 * Metodo per aumentare la dimensione del Dinosauro dopo un'azione di crescita.
	 * @return Un int '1': crescita avvenuta correttamente,
	 * 			'0': non e' stato possibile far cresce il Dinosauro perche' ha gia' la dimensione massima,
	 * 			'-1': il Dinosauro non ha l'energia sufficiente per compiere l'azione e deve essere rimosso.
	 * @throws CrescitaException Eccezione che puo' essere causata dalla MORTE di un Dinosauro perche' non possiede energi
	 * 			sufficiente o dal raggiungimento della DIMENSIONEMASSIMA (cioe' 5).
	 */
	public void aumentaDimensione() throws CrescitaException {
		//nel caso in cui la dimensione sia gia' massima
		//ritorna false perche' non e' in grado di far crescere
		//la dimensione del dinosauro
		if(super.getEnergiaMax()/1000 < 5) {
			if(super.getEnergia()>(super.getEnergiaMax()/2)) {
				super.setEnergia(super.getEnergia() - super.getEnergiaMax()/2);
				super.setEnergiaMax(1000+super.getEnergiaMax());
			} else {
				throw new CrescitaException(CrescitaException.Causa.MORTE);
			}
		}
		else {
			throw new CrescitaException(CrescitaException.Causa.DIMENSIONEMASSIMA);
		}
	}

	/**
	 * Metodo per aggiornare le coordinate del Dinosauro dopo un'azione di spostamento
	 * in un'altra cella della mappa.
	 * @param riga int che indica la riga in cui dovra' andare il Dinosauro 
	 * 				dopo un movimento in un'altra cella della mappa.
	 * @param colonna int che indica la riga in cui dovra' andare il Dinosauro 
	 * 				dopo un movimento in un'altra cella della mappa.
	 * @throws MovimentoException Eccezione, il dinosauro muore perche' non ha sufficiente energia per muoversi.
	 */
	public void aggCordinate(int riga, int colonna) throws MovimentoException {
		//esegue il movimento nelle coordinate specificate ed e' chiamato dal metodo
		//del movimento nella classe Turno
		super.setEnergia(super.getEnergia() - 10 * (int)Math.pow(2, (double)super.getEnergiaMax()/1000));
		if(super.getEnergia()>0) { 
			//eseguo movimento correttamente
			super.setRiga(riga);
			super.setColonna(colonna);
		} else {
			//il dino deve essere cancellato dalla cella e dalla lista del giocatore
			//dal metodo che chiama aggCordinate (cioe' quello che si occupa del Movimento in Turno)
			//tramite rimuoviDinosauro() in Giocatore
			//quindi rilancio l'eccezione
			throw new MovimentoException(MovimentoException.Causa.MORTE);
		}
	}

	/**
	 * @return Un int che indica l'eta' attuale del Dinosauro.
	 */
	public int getEtaDinosauro() {
		return etaAttualeDinosauro;
	}

	/**
	 * @param etaAttualeDinosauro int per stabilire l'eta' attuale del Dinosauro.
	 */
	public void setEtaDinosauro(int etaAttualeDinosauro) {
		this.etaAttualeDinosauro = etaAttualeDinosauro;
	}

	/**
	 * Metodo senza parametri che incrementa di 1 il valore int etaAttualeDinosauro.
	 */
	public void incrementaEtaDinosauro() {
		this.etaAttualeDinosauro++;
	}

	/**
	 * @return Un int che indica il turno in cui e' nato il Dinosauro.
	 */
	public int getTurnoNascita() {
		return turnoNascita;
	}

	/**
	 * @param turnoNascita int per stabilire il turno in cui e' nato il Dinosauro.
	 */
	public void setTurnoNascita(int turnoNascita) {
		this.turnoNascita = turnoNascita;
	}

	/**
	 * @return Un int che indica la durata della vita del Dinosauro.
	 */
	public int getDurataVita() {
		return durataVita;
	}

	/**
	 * @param durataVita int per stabilire la durata della vita (espressa in turni) del Dinosauro.
	 */
	public void setDurataVita(int durataVita) {
		this.durataVita = durataVita;
	}
	
	/**
	 * @return Una String che indica l'identificativo del Dinosauro composto
	 * da una String di 2 elementi "XY", dove 'X'=id giocatore e 'Y'=numero dinosauro.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id int per stabilire l'indentificativo del Dinosauro.
	 */
	public void setId(String id) {
		this.id = id;
	}	
}
