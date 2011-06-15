package client;

import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe che si occupa di inizializzare il Panel con il riassunto 
 * sullo stato del Dinosauro selezionato e del Giocatore.
 */
public class DatiGui {

	private JLabel userRis;
	private JLabel nomeSpecieRis;
//	private JLabel etaRis;
//	private JLabel tNascitaGiocatoreRis;
//	private JLabel idDinosauroRis;
	private JLabel dimensioneRis;
//	private JLabel forzaRis;
	private JLabel energiaRis;
	private JLabel energiaMaxRis;
	private JLabel tNascitaDinoRis;
	private JLabel posRis;
	private JPanel creaDatiPanel;
	private Gui gui;
	
	/**
	 * Costruttore della classe DatiGui che si occupa di inizializzare tutte le JLabel
	 * contenenti le informazioni sul Giocatore e sui Dinosauri.
	 * @param giocatore Giocatore alla quale e' assegnato il Turno.
	 */
	public DatiGui(Gui gui) {
		this.gui = gui;
		this.userRis = new JLabel();
		this.nomeSpecieRis = new JLabel();
//		this.etaRis = new JLabel();
//		this.tNascitaGiocatoreRis = new JLabel();
//		this.idDinosauroRis = new JLabel();
		this.dimensioneRis = new JLabel();
//		this.forzaRis = new JLabel();
		this.energiaRis = new JLabel();
		this.energiaMaxRis = new JLabel();
		this.tNascitaDinoRis = new JLabel();
		this.posRis = new JLabel();
	}
	
	/**
	 * Metodo che aggiorna i dati nella JLabel del JPanel.
	 * @param indiceDino int che rappresenta il numero del Dinosauro selezionato.
	 */
	public void aggiornaDati(String idDinosauro) {
		
		try {
			this.gui.getClientGui().statoDinosauro(idDinosauro);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] rispostaSplittata = this.gui.getClientGui().getRisposta().split(",");
		int rigaDino = Integer.parseInt(this.gui.getClientGui().getRisposta().split(",")[4].replace("{", ""));
		int colonnaDino = Integer.parseInt(this.gui.getClientGui().getRisposta().split(",")[5].replace("}", ""));
		
		this.userRis.setText(rispostaSplittata[1]);
		this.nomeSpecieRis.setText(rispostaSplittata[2]);
		
//		this.etaRis.setText(giocatore.getEtaAttuale() + "");
//		this.tNascitaGiocatoreRis.setText(giocatore.getTurnoNascita() + "");
//		
//		this.idDinosauroRis.setText(dino.getId());
		this.dimensioneRis.setText(rispostaSplittata[6]);
//		this.forzaRis.setText(dino.calcolaForza() + "");
		this.energiaRis.setText(rispostaSplittata[6]); //FIXME
		this.energiaMaxRis.setText(Integer.parseInt(rispostaSplittata[6])*1000 + "");
		this.tNascitaDinoRis.setText(rispostaSplittata[6]);
		this.posRis.setText(rigaDino + "," + colonnaDino);
	}

	/**
	 * Metodo per creare il JPanel contente le informazioni sul Giocatore e sui Dinosauri.
	 * @param indice int che rappresenta l'indice del Dinosauro selezionato.
	 * @return Un JPanel contente tutti gli elementi grafici sul Giocatore e sui Dinosauri.
	 */
	public JPanel creaDati(String idDinosauro) {
		
		this.aggiornaDati(idDinosauro);
		creaDatiPanel = new JPanel();
		creaDatiPanel.setLayout(new GridLayout(7,2));

		JLabel userGiocatore = new JLabel(" User: ");
		creaDatiPanel.add(userGiocatore);
		creaDatiPanel.add(userRis);

		JLabel nomeSpecie = new JLabel(" Specie: ");
		creaDatiPanel.add(nomeSpecie);
		creaDatiPanel.add(nomeSpecieRis);

//		JLabel eta = new JLabel(" Eta: ");
//		creaDatiPanel.add(eta);
//		creaDatiPanel.add(etaRis);

//		JLabel tNascitaGiocatore = new JLabel(" Nascita: ");
//		creaDatiPanel.add(tNascitaGiocatore);
//		creaDatiPanel.add(tNascitaGiocatoreRis);
//		
//		creaDatiPanel.add(new JLabel());
//		creaDatiPanel.add(new JLabel());
//
//		JLabel idDinosauro = new JLabel(" Id: ");
//		creaDatiPanel.add(idDinosauro);
//		creaDatiPanel.add(idDinosauroRis);

		JLabel dimensione = new JLabel(" Dim: ");
		creaDatiPanel.add(dimensione);
		creaDatiPanel.add(dimensioneRis);

//		JLabel forza = new JLabel(" Forza: ");
//		creaDatiPanel.add(forza);
//		creaDatiPanel.add(forzaRis);

		JLabel energia = new JLabel(" Energia: ");
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
