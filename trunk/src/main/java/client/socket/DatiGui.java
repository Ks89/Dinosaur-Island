package client.socket;

import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Classe che si occupa di inizializzare il Panel con il riassunto 
 * sullo stato del Dinosauro selezionato e del Giocatore.
 */
public class DatiGui {

	private JLabel userRis;
	private JLabel nomeSpecieRis;
	private JLabel dimensioneRis;
	private JLabel forzaRis;
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
	public void aggiornaDati(String idDinosauro) {
		System.out.println("ID DINOSAURO AGGIORNA DATI: " + idDinosauro);
		try {
			this.gui.getClientGui().statoDinosauro(idDinosauro);
		} catch (IOException ecc) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ecc) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
				
		String risp = this.gui.getClientGui().getRichiesta();

		System.out.println("stato Dinosauro: " + risp);

		String[] rispostaSplittata = risp.split(",");
		int rigaDino = Integer.parseInt(risp.split(",")[4].replace("{", ""));
		int colonnaDino = Integer.parseInt(risp.split(",")[5].replace("}", ""));
		
		this.userRis.setText(rispostaSplittata[1]);
		this.nomeSpecieRis.setText(rispostaSplittata[2]);
		
		this.dimensioneRis.setText(rispostaSplittata[6]);
		this.energiaRis.setText(rispostaSplittata[7]); //FIXME
		
//		this.energiaRis.setText("cc");
		this.energiaMaxRis.setText(Integer.parseInt(rispostaSplittata[6])*1000 + "");
		String tipo = rispostaSplittata[3];
//		if(tipo.equals("e")) {
//			this.forzaRis.setText((Integer.parseInt(rispostaSplittata[7]) * Integer.parseInt(rispostaSplittata[6])) + "");
//		} else {
//			this.forzaRis.setText((2 * Integer.parseInt(rispostaSplittata[7]) * Integer.parseInt(rispostaSplittata[6])) + "");
//		}
//		this.tNascitaDinoRis.setText(rispostaSplittata[8]);

		this.tNascitaDinoRis.setText("bb");
		this.forzaRis.setText("aa");
		
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
		creaDatiPanel.setLayout(new GridLayout(8,2));

		JLabel userGiocatore = new JLabel(" User: ");
		creaDatiPanel.add(userGiocatore);
		creaDatiPanel.add(userRis);

		JLabel nomeSpecie = new JLabel(" Specie: ");
		creaDatiPanel.add(nomeSpecie);
		creaDatiPanel.add(nomeSpecieRis);

		JLabel dimensione = new JLabel(" Dim: ");
		creaDatiPanel.add(dimensione);
		creaDatiPanel.add(dimensioneRis);

		JLabel forza = new JLabel(" Forza: ");
		creaDatiPanel.add(forza);
		creaDatiPanel.add(forzaRis);

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
