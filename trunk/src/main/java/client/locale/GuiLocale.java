package client.locale;

import gestioneeccezioni.CrescitaException;
import gestioneeccezioni.DeposizioneException;
import gestioneeccezioni.MovimentoException;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import server.logica.CaricamentoMappa;
import server.logica.Cella;
import server.logica.Giocatore;
import server.logica.Isola;
import server.logica.Partita;
import server.logica.Turno;
import server.logica.Utente;
import server.modellodati.Dinosauro;





/**
 *	Classe principale che si occupa di creare la grafica dell'applicazione (lato client).
 */
public class GuiLocale {

	/*
	 *  fare + immagini coi dinosauri con sovraimpresso nel png il livello con tutte le combinazioni
	 *  ridimensionare un pelo le celle e le immagini nelle celle per far si che l'area di spostamento del carnivoro sia visibile
	 *  se muovo un dinosauro appena nato dall'uovo il panel dati non si aggiorna
	 *  sarebbe bello che se il dinosauro va su una cella con u vegetale ed e' erbivoro. Facendo la crescita automaticamente mangia
	 * 		l'occupante e assorbe l'energia senza dover fare altre azioni di movimento ecc...
	 *  il menu a tendina non agiorna subito il panel laterale indicando il dinosauro
	 *  quando depongo e il nascituro va su una cella con carogna o vegetale la cella viene settata con solo il dinosauro
	 * 		perche' non ho fatto tutti gli if del caso
	 */

	private static final int MAX = 40;
	private static final int MAXDINO = 5;
	private JButton[][] mappaGui = new JButton[MAX][MAX];

	private int turnoPartita;
	private Cella[][] mappa;
	private Giocatore giocatore;

	private JFrame frame;
	private Dinosauro dino;
	private JScrollPane mappaPanel;
	private CaricamentoMappa cm;
	private JPanel datiPanel;
	private JPanel infoPanel;
	//	private JTextField selezioneField;
	//	private JButton confermaSelezione;
	private JComboBox sceltaDinosauro = new JComboBox();
	private int indiceDino; 
	private int turnoNascita;

	private Icon carnivoroIcona /*= new ImageIcon(this.getClass().getResource("/carnivoro.png"))*/;
	private Icon erbivoroIcona = new ImageIcon(this.getClass().getResource("/erbivoro.png"));
	private Icon terraIcona = new ImageIcon(this.getClass().getResource("/terra.jpg"));

	private Partita partita;
	private Turno t;
	private Isola isola;
	private DatiGuiLocale datiGui;
	private MappaGuiLocale mg;

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore della classe Gui che inizializza la mappa, l'Isola, la Partita ed il Turno.
	 */
	public GuiLocale () {
		this.cm = new CaricamentoMappa();
		this.mappa = cm.caricaDaFile("mappaTestAcquaUovo.txt");
		this.isola = new Isola(this.mappa);
		this.partita = new Partita(isola);
		this.t = new Turno(partita);
		partita.setTurnoCorrente(t);
	}

	/**
	 * Metodo che si occupa di impostare il giocatore.
	 * @param user String che rappresenta il nome dell'utente al momento del login.
	 * @param password String che rappresenta la password dell'utente al momento del login.
	 * @param specie String che rappresenta il nome della specie del Giocatore.
	 * @param tipo boolean che rappresenta in caso di 'true' un carnivoro, in caso di 'false' un erbivoro.
	 */
	public void setGiocatore(String user, String password, String specie, boolean tipo) {
		new Utente(user, password);
		String tipoDinosauro;
		if(tipo) {
			tipoDinosauro = "carnivoro";
		} else {
			tipoDinosauro = "erbivoro";
		}
		
	
		    giocatore = new Giocatore(turnoPartita, specie, tipoDinosauro);    
		    giocatore.setUtente(new Utente(user, password));

		
		giocatore = new Giocatore(turnoPartita, specie, tipoDinosauro);
		giocatore.aggiungiInPartita(partita);
		dino = giocatore.getDinosauri().get(indiceDino);
		datiGui = new DatiGuiLocale();
		this.inizializzaGrafica();
		mg.setScrollBar(dino.getRiga(), dino.getColonna());
	}

	public void inizializzaSceltaDino(Giocatore giocatore) {
		String[] listaDino = new String[MAXDINO];
		sceltaDinosauro.removeAllItems();
		for(int i=0;i<giocatore.getDinosauri().size();i++) {
			listaDino[i] = giocatore.getDinosauri().get(i).getId();
			sceltaDinosauro.addItem(listaDino[i]);
		}
		sceltaDinosauro.setMaximumRowCount(MAXDINO);
	}

	/**
	 * Metodo che inizializza la grafica, creando un frame ed aggiungendovi i vari JPanel e menu.
	 */
	private void inizializzaGrafica() {
		frame = new JFrame("Isola dei dinosauri BETA1");
		frame.setLayout(new FlowLayout()); //imposto il Layout a griglia
		frame.setMinimumSize(new Dimension(1025,637));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		mg = new MappaGuiLocale(this,giocatore,datiGui);
		mappaPanel= mg.creaMappa(mappa);
		this.setMappaGui(mg.getMappaGui());

		infoPanel = this.creaRiassuntoDati(indiceDino);

		frame.add(infoPanel);
		frame.add(mappaPanel);

		MenuGuiLocale menuGui = new MenuGuiLocale(frame);		
		frame.setJMenuBar(menuGui.getMenu());

		this.aggiornaMappa();

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Metodo che crea il JPanel con le informazioni sul Dinosauro e sul Giocatore.
	 * @param indiceDino int che rappresenta il numero del Dinosauro.
	 * @return Un JPanel contenente il riassunto della situazione del Giocatore e del suo Dinosauro selezionato con indiceDino.
	 */
	private JPanel creaRiassuntoDati(int indiceDino) {
		datiPanel = datiGui.creaDati(indiceDino,giocatore);
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2,1));
		infoPanel.add(datiPanel);
		infoPanel.add(this.azioni());
		return infoPanel;
	}


	/**
	 * Metodo che si occupa di aggiornare la Mappa.
	 */
	public void aggiornaMappa() {
		mg.applicaRaggiungibilita(indiceDino,giocatore,t);
		mg.applicaVisiblita(giocatore, t);
	}

	/**
	 * Metodo che crea il JPanel contenente i pulsanti per far crescere, deporre e selezionare un Dinosauro.
	 * @return Un JPanel contenente i pulsanti per far crescere, deporre e selezionare un Dinosauro.
	 */
	private JPanel azioni() {
		JPanel panelAzioni = new JPanel();
		panelAzioni.setLayout(new GridLayout(4,1));
		JLabel titolo = new JLabel("Azioni");
		JButton cresci = new JButton("Cresci");
		JButton deponi = new JButton("Deponi");

		JPanel selezionePanel = new JPanel(new GridLayout(1,2));
		selezionePanel.add(new JLabel("   Seleziona: "));
		this.inizializzaSceltaDino(giocatore);
		selezionePanel.add(sceltaDinosauro);

		panelAzioni.add(titolo);
		panelAzioni.add(cresci);
		panelAzioni.add(deponi);
		panelAzioni.add(selezionePanel);

		sceltaDinosauro.addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent event) {
						String selezionato = (String) sceltaDinosauro.getSelectedItem();
						int i;
						for(i=0;i<giocatore.getDinosauri().size();i++) {
							if(giocatore.getDinosauri().get(i).getId().equals(selezionato)) {
								break;
							}
						}
						setIndiceDino(i);
						mg.setIndiceDino(i);
					}
				}
		);

		cresci.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						
						try {
							dino.aumentaDimensione();
							int raggio = dino.calcolaRaggioVisibilita();
							t.illuminaMappa(partita.getGiocatori().get(0), dino.getRiga(), dino.getColonna(), raggio);
							mg.applicaVisiblita(giocatore, t);
							datiGui.aggiornaDati(indiceDino, giocatore);
						} catch (CrescitaException ce){
							if(ce.getCausa()==CrescitaException.Causa.MORTE) {
								partita.getGiocatori().get(0).rimuoviDinosauro(dino);
								mappaGui[dino.getRiga()][dino.getColonna()].setIcon(terraIcona);
								JOptionPane.showMessageDialog(null, "Non e' stato possibile eseguire l'azione di crescita\nDinosauro rimosso!");
							}
							if(ce.getCausa()==CrescitaException.Causa.DIMENSIONEMASSIMA) {
								JOptionPane.showMessageDialog(null, "Non e' stato possibile eseguire l'azione di crescita\nDimensione massima!");
							}
						}
					}
				}
		);

		deponi.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 	
						try {
							String idNuovoDinosauro = giocatore.eseguiDeposizionedeponiUovo(dino);
							JOptionPane.showMessageDialog(null, "Id nuovo dinosauro: " + idNuovoDinosauro);
							int raggio = dino.calcolaRaggioVisibilita();
							t.illuminaMappa(partita.getGiocatori().get(0), dino.getRiga(), dino.getColonna(), raggio);
							mg.applicaVisiblita(giocatore, t);
							partita.nascitaDinosauro(turnoNascita);
							inizializzaSceltaDino(giocatore);
							mg.rinizializzaMappa(carnivoroIcona, erbivoroIcona);
						} catch (DeposizioneException de){
							if(de.getCausa()==DeposizioneException.Causa.MORTE) {
								mappaGui[dino.getRiga()][dino.getColonna()].setIcon(terraIcona);
								JOptionPane.showMessageDialog(null, "Errore deposizione!\nIl dinosauro e' morto, " +
								"\nenergia insufficiente!");
							}
							if(de.getCausa()==DeposizioneException.Causa.SQUADRACOMPLETA) {
								mappaGui[dino.getRiga()][dino.getColonna()].setIcon(terraIcona);
								JOptionPane.showMessageDialog(null, "Errore deposizione!\nIl dinosauro e' morto, " +
								"squadra dei dinosauri completa!");
							}
						} finally {
							datiGui.aggiornaDati(indiceDino, giocatore);
						}
					}
				}
		);
		return panelAzioni;
	}


	/**
	 * Metodo per trovare un Dinosauro all'interno della mappa 'stradaPercorsa'.
	 * @param stradaPercorsa array bidimensionale di int che rappresenta la strada percorsa tramite numeri negativi crescenti da -7.
	 * @return Un array di int con:
	 * 				[0] - la riga del Dinosauro trovato, 
	 * 				[1] - la colonna del Dinosauro trovato.	
	 */
	private static int[] trovaDinosauroStrada (int[][] stradaPercorsa) {
		int j,w;
		int[] uscita = {0,0};
		for(j=0;j<stradaPercorsa.length;j++) {
			for(w=0;w<stradaPercorsa[0].length;w++) {
				if(stradaPercorsa[j][w]==-7) {
					uscita[0] = j;
					uscita[1] = w;
					return uscita;
				}
			}
		}
		return uscita;
	}

	/**
	 * Metoco per eseguire il movimento di un Dinosauro.
	 * @param rigaCliccata int che rappreseta la riga della cella cliccata nella mappa del gioco.
	 * @param colonnaCliccata int che rappreseta la colonna della cella cliccata nella mappa del gioco.
	 */
	void eseguiMovimento(int rigaCliccata, int colonnaCliccata) {
		this.dino = giocatore.getDinosauri().get(this.indiceDino);
		//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
		int[][] stradaPercorsa = t.ottieniStradaPercorsa(dino.getRiga(), dino.getColonna(),rigaCliccata, colonnaCliccata);
		int[] coordinateStrada = trovaDinosauroStrada(stradaPercorsa);

		int origineRigaStrada = dino.getRiga() - coordinateStrada[0];
		int origineColonnaStrada = dino.getColonna() - coordinateStrada[1];
		int fineRigaStrada = dino.getRiga() + (stradaPercorsa.length - coordinateStrada[0] - 1);
		int fineColonnaStrada = dino.getColonna() + (stradaPercorsa[0].length - coordinateStrada[1] - 1);

		int raggio = dino.calcolaRaggioVisibilita();
		//illumino la strada
		for(int f=0;f<MAX;f++) {
			for(int j=0;j<MAX;j++) {
				if((f>=origineRigaStrada && f<=fineRigaStrada) && (j>=origineColonnaStrada && j<=fineColonnaStrada)) {
					if(stradaPercorsa[f-origineRigaStrada][j-origineColonnaStrada]<0) {
						t.illuminaMappa(partita.getGiocatori().get(0), f, j, raggio);
					}
				}
			}
		}

		
		try {
			boolean stato = partita.getTurnoCorrente().spostaDinosauro(dino, rigaCliccata, colonnaCliccata);
			//Salvataggio dello stato del giocatore ad ogni movimento
			if(stato) {
				System.out.println("Tutto ok");
			} else {
				System.out.println("Problema");
			}
		} catch (MovimentoException e){
//			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCATO) {
//				JOptionPane.showMessageDialog(null, "Vince attaccante!");
//			}
//			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCANTE) {
//				JOptionPane.showMessageDialog(null, "Vince attaccato!");
//			}
			if(e.getCausa()==MovimentoException.Causa.MORTE) {
				JOptionPane.showMessageDialog(null, "Il Dinosauro e' morto!");
			}
			if(e.getCausa()==MovimentoException.Causa.DESTINAZIONEERRATA) {
				JOptionPane.showMessageDialog(null, "Scegliere un'altra destinazione!");
			}
			if(e.getCausa()==MovimentoException.Causa.NESSUNVINCITORE) {
				JOptionPane.showMessageDialog(null, "Nessun vincitore!");
			}
			if(e.getCausa()==MovimentoException.Causa.ERRORE) {
				JOptionPane.showMessageDialog(null, "Errore!");
			}
		}
		mg.applicaVisiblita(giocatore, t);
		mg.applicaRaggiungibilita(indiceDino, giocatore, t);
	}

	/**
	 * Metodo per ottenere i dati nella vista di raggiungibilita'.
	 * @return un array di int con:
	 * 			[0] - riga d'inizio vista,
	 * 			[1] - colonna d'inizio vista,
	 * 			[2] - riga di fine vista,
	 * 			[3] - colonna di fine vista.
	 */
	public int[] getDatiRaggiungibilita () {
		int rigaDino = giocatore.getDinosauri().get(indiceDino).getRiga();
		int colonnaDino = giocatore.getDinosauri().get(indiceDino).getColonna();
		int[][] raggiungibile = t.ottieniRaggiungibilita(rigaDino, colonnaDino);
		int[] coordinate = mg.trovaDinosauro(raggiungibile);
		//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
		int[] datiRaggiungibilita = new int[4];
		datiRaggiungibilita[0] = rigaDino - coordinate[0]; //riga inzio
		datiRaggiungibilita[1] = colonnaDino - coordinate[1]; //colonna inizio
		datiRaggiungibilita[2] = rigaDino + (raggiungibile.length - coordinate[0] - 1); //riga fine
		datiRaggiungibilita[3] = colonnaDino + (raggiungibile[0].length - coordinate[1] - 1); //colonna fine
		return datiRaggiungibilita;
	}

	/**
	 * @return Il Giocatore.
	 */
	public Giocatore getGiocatore() {
		return giocatore;
	}

	/**
	 * @param giocatore Riferimento al Giocatore.
	 */
	public void setGiocatore(Giocatore giocatore) {
		this.giocatore = giocatore;
	}

	/**
	 * @return Un int che rappresenta il numero del Dinosauro nella propria squadra.
	 */
	public int getIndiceDino() {
		return indiceDino;
	}

	/**
	 * @param indiceDino int per impostare il numero del Dinosauro nella propria squadra.
	 */
	public void setIndiceDino(int indiceDino) {
		this.indiceDino = indiceDino;
	}

	/**
	 * @return Il Turno corrente.
	 */
	public Turno getT() {
		return t;
	}

	/**
	 * @param t riferimento al Tunro corrente.
	 */
	public void setT(Turno t) {
		this.t = t;
	}

	/**
	 * @return Un array bidimensionale contenente la mappa da gioco di JButton.
	 */
	public JButton[][] getMappaGui() {
		return mappaGui.clone();
	}

	/**
	 * @param mappaGui array bidimensionale per impostare la mappa di JButton.
	 */
	public void setMappaGui(JButton[][] mappaGui) {
		this.mappaGui = mappaGui.clone();
	}
}