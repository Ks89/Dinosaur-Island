package client.gui;

import isoladinosauri.Giocatore;
import isoladinosauri.modellodati.Dinosauro;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe che si occupa di inizializzare il Panel con il riassunto 
 * sullo stato del Dinosauro selezionato e del Giocatore.
 */
public class DatiGui {

	private JLabel nomeGiocatoreRis;
	private JLabel nomeSpecieRis;
	private JLabel etaRis;
	private JLabel tNascitaGiocatoreRis;
	private JLabel dinosauriRis;
	private JLabel uovaRis;
	private JLabel idDinosauroRis;
	private JLabel dimensioneRis;
	private JLabel forzaRis;
	private JLabel energiaRis;
	private JLabel energiaMaxRis;
	private JLabel tNascitaDinoRis;
	private JLabel posRis;
	private Dinosauro dino;
	private JPanel creaDatiPanel;
	
	/**
	 * Costruttore della classe DatiGui che si occupa di inizializzare tutte le JLabel
	 * contenenti le informazioni sul Giocatore e sui Dinosauri.
	 * @param giocatore Giocatore alla quale e' assegnato il Turno.
	 */
	public DatiGui() {
		this.nomeGiocatoreRis = new JLabel();
		this.nomeSpecieRis = new JLabel();
		this.etaRis = new JLabel();
		this.tNascitaGiocatoreRis = new JLabel();
		this.dinosauriRis = new JLabel();
		this.uovaRis = new JLabel();
		this.idDinosauroRis = new JLabel();
		this.dimensioneRis = new JLabel();
		this.forzaRis = new JLabel();
		this.energiaRis = new JLabel();
		this.energiaMaxRis = new JLabel();
		this.tNascitaDinoRis = new JLabel();
		this.posRis = new JLabel();
	}
	
	/**
	 * Metodo che aggiorna i dati nella JLabel del JPanel.
	 * @param indiceDino int che rappresenta il numero del Dinosauro selezionato.
	 */
	public void aggiornaDati(int indiceDino, Giocatore giocatore) {
		this.dino = giocatore.getDinosauri().get(indiceDino);
		this.nomeGiocatoreRis.setText(giocatore.getIdGiocatore() + "");
		this.nomeSpecieRis.setText(giocatore.getNomeSpecie());
		this.etaRis.setText(giocatore.getEtaAttuale() + "");
		this.tNascitaGiocatoreRis.setText(giocatore.getTurnoNascita() + "");
		this.dinosauriRis.setText(giocatore.getDinosauri().get(indiceDino).getId());
		this.uovaRis.setText("uova");
		this.idDinosauroRis.setText(dino.getId());
		this.dimensioneRis.setText((dino.getEnergiaMax() / 1000) + "");
		this.forzaRis.setText(dino.calcolaForza() + "");
		this.energiaRis.setText(dino.getEnergia() + "");
		this.energiaMaxRis.setText(dino.getEnergiaMax() + "");
		this.tNascitaDinoRis.setText(dino.getTurnoNascita() + "");
		this.posRis.setText(dino.getRiga() + "," + dino.getColonna());
	}

	/**
	 * Metodo per creare il JPanel contente le informazioni sul Giocatore e sui Dinosauri.
	 * @param indice int che rappresenta l'indice del Dinosauro selezionato.
	 * @return Un JPanel contente tutti gli elementi grafici sul Giocatore e sui Dinosauri.
	 */
	public JPanel creaDati(int indice,Giocatore giocatore) {

		this.aggiornaDati(indice,giocatore);
		creaDatiPanel = new JPanel();
		creaDatiPanel.setLayout(new GridLayout(15,2));

		JLabel nomeGiocatore = new JLabel(" ID: ");
		creaDatiPanel.add(nomeGiocatore);
		creaDatiPanel.add(nomeGiocatoreRis);

		JLabel nomeSpecie = new JLabel(" Specie: ");
		creaDatiPanel.add(nomeSpecie);
		creaDatiPanel.add(nomeSpecieRis);

		JLabel eta = new JLabel(" Eta: ");
		creaDatiPanel.add(eta);
		creaDatiPanel.add(etaRis);

		JLabel tNascitaGiocatore = new JLabel(" Nascita: ");
		creaDatiPanel.add(tNascitaGiocatore);
		creaDatiPanel.add(tNascitaGiocatoreRis);

		JLabel dinosauri = new JLabel(" Dinosauri: ");
		creaDatiPanel.add(dinosauri);
		creaDatiPanel.add(dinosauriRis);

		JLabel uova = new JLabel(" Uova: ");
		creaDatiPanel.add(uova);
		creaDatiPanel.add(uovaRis);

		JLabel idDinosauro = new JLabel(" Id: ");
		creaDatiPanel.add(idDinosauro);
		creaDatiPanel.add(idDinosauroRis);

		JLabel dimensione = new JLabel(" Dim: ");
		creaDatiPanel.add(dimensione);
		creaDatiPanel.add(dimensioneRis);

		JLabel forza = new JLabel(" Forza: ");
		creaDatiPanel.add(forza);
		creaDatiPanel.add(forzaRis);

		JLabel energia = new JLabel("Energia: ");
		creaDatiPanel.add(energia);
		creaDatiPanel.add(energiaRis);

		JLabel energiaMax = new JLabel(" EnergiaMax: ");
		creaDatiPanel.add(energiaMax);
		creaDatiPanel.add(energiaMaxRis);

		JLabel tNascitaDino = new JLabel(" Nascita: ");
		creaDatiPanel.add(tNascitaDino);
		creaDatiPanel.add(tNascitaDinoRis);

		JLabel pos = new JLabel(" Pos: ");
		creaDatiPanel.add(pos);
		creaDatiPanel.add(posRis);

		return creaDatiPanel;
	}	
}
