package client.gui;

import isoladinosauri.CaricamentoMappa;
import isoladinosauri.Cella;
import isoladinosauri.Giocatore;
import isoladinosauri.Isola;
import isoladinosauri.MovimentoException;
import isoladinosauri.Partita;
import isoladinosauri.Turno;
import isoladinosauri.Utente;
import isoladinosauri.modellodati.Dinosauro;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

/**
 *	Classe principale che si occupa di creare la grafica dell'applicazione (lato client).
 */
public class Gui {

	/*
	 * TODO fare + immagini coi dinosauri con sovraimpresso nel png il livello con tutte le combinazioni
	 * TODO ridimensionare un pelo le celle e le immagini nelle celle per far si che l'area di spostamento del carnivoro sia visibile
	 * FIXME se muovo un dinosauro appena nato dall'uovo il panel dati non si aggiorna
	 * FIXME sarebbe bello che se il dinosauro va su una cella con u vegetale ed e' erbivoro. Facendo la crescita automaticamente mangia
	 * 		l'occupante e assorbe l'energia senza dover fare altre azioni di movimento ecc...
	 * TODO il menu a tendina non agiorna subito il panel laterale indicando il dinosauro
	 * FIXME quando depongo e il nascituro va su una cella con carogna o vegetale la cella viene settata con solo il dinosauro
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

	private Icon carnivoroIcona = new ImageIcon(this.getClass().getResource("/carnivoro.png"));
	private Icon erbivoroIcona = new ImageIcon(this.getClass().getResource("/erbivoro.png"));
	private Icon terraIcona = new ImageIcon(this.getClass().getResource("/terra.jpg"));

	private Partita partita;
	private Turno t;
	private Isola isola;
	private DatiGui datiGui;
	private MappaGui mg;

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore della classe Gui che inizializza la mappa, l'Isola, la Partita ed il Turno.
	 */
	public Gui () {
		this.cm = new CaricamentoMappa();
		this.mappa = cm.caricaDaFile();
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
		giocatore.aggiungiInPartita(partita);
		dino = giocatore.getDinosauri().get(indiceDino);
		datiGui = new DatiGui();
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
		//		sceltaDinosauro = new JComboBox(listaDino); 
		sceltaDinosauro.setMaximumRowCount(MAXDINO);
	}

	/**
	 * Metodo che inizializza la grafica, creando un frame ed aggiungendovi i vari JPanel e menu.
	 */
	private void inizializzaGrafica() {
		frame = new JFrame("Isola dei dinosauri BETA1");
		frame.setLayout(new FlowLayout()); //imposto il Layout a griglia
		//		frame.setMinimumSize(new Dimension(1025,637));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//		frame.setResizable(false);
		mg = new MappaGui(this,giocatore,datiGui);
		mappaPanel= mg.creaMappa(mappa);
		this.setMappaGui(mg.getMappaGui());

		infoPanel = this.creaRiassuntoDati(indiceDino);

		frame.add(infoPanel);
		frame.add(mappaPanel);

		MenuGui menuGui = new MenuGui(frame);		
		frame.setJMenuBar(menuGui.getMenu());

		this.assegnaTurni();

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
	 * Metodo che si occupa di assegnare i Turni.
	 */
	public void assegnaTurni() {
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
						//						datiGui.aggiornaDati(i, giocatore);
					}
				}
		);

		cresci.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(dino.aumentaDimensione()==1) {
							int raggio = dino.calcolaRaggioVisibilita();
							t.illuminaMappa(partita.getGiocatori().get(0), dino.getRiga(), dino.getColonna(), raggio);
							mg.applicaVisiblita(giocatore, t);
							datiGui.aggiornaDati(indiceDino, giocatore);
						}
						else {
							if(dino.aumentaDimensione()==-1) {
								partita.getGiocatori().get(0).rimuoviDinosauro(dino);
								mappaGui[dino.getRiga()][dino.getColonna()].setIcon(terraIcona);
								JOptionPane.showMessageDialog(null, "Non e' stato possibile eseguire l'azione di crescita\nDinosauro rimosso!");
							}
						}

					}
				}
		);

		deponi.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						//						dino.setEnergia(5000);
						//						dino.setEnergiaMax(5000);
						int statoDeposizione = giocatore.eseguiDeposizionedeponiUovo(dino);
						if(statoDeposizione==1) {
							int raggio = dino.calcolaRaggioVisibilita();
							t.illuminaMappa(partita.getGiocatori().get(0), dino.getRiga(), dino.getColonna(), raggio);
							mg.applicaVisiblita(giocatore, t);
							partita.nascitaDinosauro(turnoNascita);
							inizializzaSceltaDino(giocatore);
							mg.rinizializzaMappa(carnivoroIcona, erbivoroIcona);
						} else {
							mappaGui[dino.getRiga()][dino.getColonna()].setIcon(terraIcona);
							if(statoDeposizione==-2) {
								JOptionPane.showMessageDialog(null, "Errore deposizione!\nIl dinosauro e' morto, " +
								"\nenergia insufficiente!");
							}
							if(statoDeposizione==0) {
								JOptionPane.showMessageDialog(null, "Errore deposizione!\nIl dinosauro e' morto, " +
								"squadra dei dinosauri completa!");
							}
						}
						datiGui.aggiornaDati(indiceDino, giocatore);
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

		int statoMovimento = partita.getTurnoCorrente().spostaDinosauro(dino, rigaCliccata, colonnaCliccata);
		switch(statoMovimento) {
		case -2:
			JOptionPane.showMessageDialog(null, "Il Dinosauro e' morto!");
			break;
		case -1:
			JOptionPane.showMessageDialog(null, "Scegliere un'altra destinazione!");
			break;
		case 0:
			JOptionPane.showMessageDialog(null, "Vince attaccato!");
			break;
		case 1:
			JOptionPane.showMessageDialog(null, "Tutto ok!");
			break;
		case 2:
			JOptionPane.showMessageDialog(null, "Vince attaccante!");
			break;
		case 3:
			JOptionPane.showMessageDialog(null, "Conbattimento eseguito e mangiato occupante!");
			break;
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