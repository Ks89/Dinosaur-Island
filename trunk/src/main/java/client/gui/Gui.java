package client.gui;

import isoladinosauri.CaricamentoMappa;
import isoladinosauri.Cella;
import isoladinosauri.Giocatore;
import isoladinosauri.Isola;
import isoladinosauri.Partita;
import isoladinosauri.Turno;
import isoladinosauri.Utente;
import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;
import isoladinosauri.modellodati.Vegetale;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 *	Classe principale che si occupa di creare la grafica dell'applicazione (lato client).
 */
public class Gui {

	/*
	 * FIXME se muovo un dinosauro appena nato dall'uovo il panel dati non si aggiorna
	 * FIXME un dinosauro puo' muoversi anche su se stesso o su uno della sua squadra mangiandolo, bisogna
	 * 		far si che mangia o cmq lo spostamento funzioni solo se nella destinazione ci sia un dinosauro di altri giocatori
	 * FIXME sarebbe bello che se il dinosauro va su una cella con u vegetale ed e' erbivoro. Facendo la crescita automaticamente mangia
	 * 		l'occupante e assorbe l'energia senza dover fare altre azioni di movimento ecc...
	 * TODO fare menu a tendina per selezionare i dinosauri e far si che nel menu appaiano gli id dei dinosauri e non i numeri
	 * 		direttamente.
	 */

	private static final int MAX = 40;
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
	private JTextField selezioneField;
	private JButton confermaSelezione;
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
		giocatore = new Giocatore(partita, turnoPartita, specie, tipoDinosauro);
		dino = giocatore.getDinosauri().get(indiceDino);
		datiGui = new DatiGui();
		this.inizializzaGrafica();
		mg.setScrollBar(dino.getRiga(), dino.getColonna());
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
		selezioneField = new JTextField();
		confermaSelezione = new JButton("Cambia Dino");
		selezionePanel.add(selezioneField);
		selezionePanel.add(confermaSelezione);

		panelAzioni.add(titolo);
		panelAzioni.add(cresci);
		panelAzioni.add(deponi);
		panelAzioni.add(selezionePanel);

		cresci.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(dino.aumentaDimensione()) {
							System.out.println("Il dinosauro " + dino.getId() + " e' ora di dimensione: " + (dino.getEnergiaMax()/1000));
							int raggio = dino.calcolaRaggioVisibilita();
							t.illuminaMappa(partita.getGiocatori().get(0), dino.getRiga(), dino.getColonna(), raggio);
							mg.applicaVisiblita(giocatore, t);
							datiGui.aggiornaDati(indiceDino, giocatore);
						}
						else {
							partita.getGiocatori().get(0).rimuoviDinosauro(dino);
							mappaGui[dino.getRiga()][dino.getColonna()].setIcon(terraIcona);
							JOptionPane.showMessageDialog(null, "Non e' stato possibile eseguire l'azione di crescita\nDinosauro rimosso!");
						}

					}
				}
		);

		deponi.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
//						dino.setEnergia(5000);
//						dino.setEnergiaMax(5000);
						if((giocatore.eseguiDeposizionedeponiUovo(dino))) {
							int raggio = dino.calcolaRaggioVisibilita();
							t.illuminaMappa(partita.getGiocatori().get(0), dino.getRiga(), dino.getColonna(), raggio);
							mg.applicaVisiblita(giocatore, t);
							partita.nascitaDinosauro(turnoNascita);
							for(int i=0;i<40;i++) {
								for(int j=0;j<40;j++) {
									if(mappa[i][j] !=null && mappa[i][j].getDinosauro()!=null) {
										if(mappa[i][j].getDinosauro() instanceof Carnivoro) {
											mappaGui[i][j].setIcon(carnivoroIcona);
										} else {
											if(mappa[i][j].getDinosauro() instanceof Erbivoro) {
												mappaGui[i][j].setIcon(erbivoroIcona);
											}
										}
									}
								}
							}
						} else {
							mappaGui[dino.getRiga()][dino.getColonna()].setIcon(terraIcona);
							JOptionPane.showMessageDialog(null, "Errore deposizione!\nIl dinosauro e' morto, " +
							"possibili motivi: \nenergia insufficiente\nsquadra dei dinosauri completa!");
						}
						datiGui.aggiornaDati(indiceDino, giocatore);
					}
				}
		);

		confermaSelezione.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						indiceDino = Integer.parseInt(selezioneField.getText());
						dino = giocatore.getDinosauri().get(indiceDino);
						mg.resetRaggiungibilita();
						mg.applicaRaggiungibilita(indiceDino, giocatore, t);
						mg.setScrollBar(dino.getRiga(), dino.getColonna());
						mg.applicaVisiblita(giocatore, t);
						datiGui.aggiornaDati(indiceDino, giocatore);
						System.out.println("slezionato: " + indiceDino);
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

		int[][] raggiungibile = t.ottieniRaggiungibilita(dino.getRiga(), dino.getColonna());
		int[][] stradaPercorsa;
		int[] coordinate = mg.trovaDinosauro(raggiungibile);
		int vecchiaRiga = dino.getRiga();
		int vecchiaColonna = dino.getColonna();
		System.out.println("Coordinate: " +  coordinate[0] + " " + coordinate[1]);
		//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
		System.out.println("Posiz Dino: " + dino.getRiga() + "," + dino.getColonna());
		int origineRiga = dino.getRiga() - coordinate[0];
		int origineColonna = dino.getColonna() - coordinate[1];
		int fineRiga = dino.getRiga() + (raggiungibile.length - coordinate[0] - 1);
		int fineColonna = dino.getColonna() + (raggiungibile[0].length - coordinate[1] - 1);
		System.out.println("CoordinateMappa: " +  origineRiga + "," + origineColonna + "   " + fineRiga + "," + fineColonna);

		isola.stampaMappaRaggiungibilita(origineRiga, origineColonna, fineRiga, fineColonna, raggiungibile);

		isola.stampaMappaRidottaVisibilita(partita.getGiocatori().get(0));
		int riga, colonna;

		System.out.println("Inserisci coordinate come: riga,colonna: ");

		riga = rigaCliccata;
		colonna = colonnaCliccata;

		System.out.println("->Il dinosauro si muovera' da: (" + dino.getRiga() + "," + dino.getColonna() + ") a: (" + riga + "," + colonna + ")");

		stradaPercorsa = t.ottieniStradaPercorsa(dino.getRiga(), dino.getColonna(),riga, colonna);
		isola.stampaMappaStradaPercorsa(origineRiga, origineColonna, fineRiga, fineColonna, stradaPercorsa);

		int[] coordinateStrada = trovaDinosauroStrada(stradaPercorsa);

		int origineRigaStrada = dino.getRiga() - coordinateStrada[0];
		int origineColonnaStrada = dino.getColonna() - coordinateStrada[1];
		int fineRigaStrada = dino.getRiga() + (stradaPercorsa.length - coordinateStrada[0] - 1);
		int fineColonnaStrada = dino.getColonna() + (stradaPercorsa[0].length - coordinateStrada[1] - 1);

		System.out.println("origine: " + origineRigaStrada+","+origineColonnaStrada + " fine: "+fineRigaStrada+","+fineColonnaStrada);

		int raggio = dino.calcolaRaggioVisibilita();
		//illumino la strada
		for(int f=0;f<40;f++) {
			for(int j=0;j<40;j++) {
				if((f>=origineRigaStrada && f<=fineRigaStrada) && (j>=origineColonnaStrada && j<=fineColonnaStrada)) {
					if(stradaPercorsa[f-origineRigaStrada][j-origineColonnaStrada]<0) {
						t.illuminaMappa(partita.getGiocatori().get(0), f, j, raggio);
					}
				}
			}
		}

		isola.stampaMappaRidottaVisibilita(partita.getGiocatori().get(0));

		partita.getTurnoCorrente().spostaDinosauro(dino, riga, colonna);

		System.out.println("->Il dinosauro e' ora in: (" + dino.getRiga() + "," + dino.getColonna() + ")");
		isola.stampaMappaRidotta();

		//		mg.resetRaggiungibilita();
		mg.applicaVisiblita(giocatore, t);
		mg.applicaRaggiungibilita(indiceDino, giocatore, t);


		if(mappa[vecchiaRiga][vecchiaColonna]!=null) {
			if(mappa[vecchiaRiga][vecchiaColonna].getOccupante() instanceof Vegetale) {
				mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.GREEN);
			} else {
				if(mappa[vecchiaRiga][vecchiaColonna].getOccupante() instanceof Carogna) {
					mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.PINK);
				}
			}
		} else {
			mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.BLUE);
		}
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