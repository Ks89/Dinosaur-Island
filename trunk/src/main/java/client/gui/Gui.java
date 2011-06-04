package client.gui;

import isoladinosauri.CaricamentoMappa;
import isoladinosauri.Cella;
import isoladinosauri.Giocatore;
import isoladinosauri.Isola;
import isoladinosauri.Partita;
import isoladinosauri.Turno;
import isoladinosauri.Utente;
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Vegetale;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Gui {

	/*
	 * TODO sistemare la javadoc
	 * FIXME il pannello dati viene aggiornato solo col dinosauro "0", se ne faccio nascere uno e lo seleziono
	 * 	non viene refreshato.
	 * FIXME se faccio nascere un dinosauro non gli viene applicata la visibilita senza fare un clic per poterlo muovere
	 * FIXME se clicco al di fuori dell'area raggiungibile in una zona gia esplorata mi setta le scrollbar lo stesso
	 * nonstante non sia raggiungibile
	*/
	
	private static final int MAX = 40;
	private JButton[][] mappaGui = new JButton[MAX][MAX];

	private int turnoPartita;
	private Cella[][] mappa;
	private Giocatore giocatore;

	private JFrame frame;
	private Dinosauro dino;
	private JMenuBar bar;
	private JScrollPane mappaPanel;
	private CaricamentoMappa cm;
	private JPanel datiPanel;
	private JPanel infoPanel;
	private JTextField selezioneField;
	private JButton confermaSelezione;
	private int indiceDino; 
	private int turnoNascita;

	private Partita partita;
	private Turno t;
	private Isola isola;
	private DatiGui datiGui;
	private MappaGui mg;

	
	private static final long serialVersionUID = 1L;

	public Gui () {
		this.cm = new CaricamentoMappa();
		this.mappa = cm.caricaDaFile();
		this.isola = new Isola(this.mappa);
		this.partita = new Partita(isola);
		this.t = new Turno(partita);
		partita.setTurnoCorrente(t);
		this.bar = new JMenuBar();
	}
	public JButton[][] getMappaGui() {
		return mappaGui;
	}

	public void setMappaGui(JButton[][] mappaGui) {
		this.mappaGui = mappaGui;
	}


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
	

	//**********************menu JFrame**********************
	private JMenuBar menuGrafica() {
		JMenu fileMenu = new JMenu("File"); 
		fileMenu.setMnemonic('F'); //imposta il mnemonic ad F

		//crea la voce About
		//Icon immagine4 = new ImageIcon(getClass().getResource("info_icon.png"));
		JMenuItem aboutItem = new JMenuItem("Info..."/*,immagine4*/);
		aboutItem.setMnemonic('I'); //imposta mnemonic
		fileMenu.add(aboutItem); //aggiunge la voce al menu file
		aboutItem.addActionListener(
				new ActionListener() //classe inner anonima
				{
					public void actionPerformed(ActionEvent event)
					{//mostra un messaggio quando l'untente sceglie about...
						JOptionPane.showMessageDialog(null,"Isola Dei Dinosauri BETA1\nCreato da 766172 e 7xxxxx\n(C) 2011","About",JOptionPane.PLAIN_MESSAGE);
					}
				}
		);
		//crea la voce exit
		//		Icon immagine5 = new ImageIcon(getClass().getResource("Exit_icon.jpg"));
		JMenuItem exitItem = new JMenuItem("Exit"/*,immagine5*/);
		exitItem.setMnemonic('E');
		fileMenu.add(exitItem);
		exitItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						frame.dispose();
					}
				}
		);

		//crea il nuovo menu Operazioni
		JMenu operazMenu = new JMenu("Operazioni");
		operazMenu.setMnemonic('O');
//		Icon refreshImmagine = new ImageIcon(ClassLoader.getSystemResource("refresh.png"));
		JMenuItem refreshElemento = new JMenuItem("Rigenera Mappa"/*,refreshImmagine*/);
		refreshElemento.setMnemonic('R'); //imposta mnemonic
		operazMenu.add(refreshElemento); //aggiunge la voce al menu Operazioni
//		refreshItem.addActionListener(
//				new ActionListener() //classe inner anonima
//				{
//					public void actionPerformed(ActionEvent event)
//					{
//						//rigenera mappa
//						mappa = cm.caricaDaFile();
//						isola = new Isola(mappa);
//						partita = new Partita(isola);
//						t = new Turno(partita);
//						partita.setTurnoCorrente(t);
//						partita.getGiocatori().clear();
//						utente = new Utente(user.getText(), password.getText());
//						giocatore = new Giocatore(partita, turnoPartita, specie.getText(), tipo.getText());
//						dino = giocatore.getDinosauri().get(indiceDino);
//						//						mappaPanel= creaMappa(mappa);
//						//						applicaRaggiungibilita(0);
//						//						applicaVisibilita(0);
//						frame.removeAll();
//						bar.removeAll();
//						inizializzaGrafica();
//						//						frame.repaint();
//
//						//fare in modo che mappaGui cambi e si aggiorni e poi chiamare un repaint come fatto col JButton cresci
//
//					}
//				}
//		);
		//crea la voce Salva partita
		operazMenu.addSeparator();
		JMenuItem saveItem = new JMenuItem("Salva partita");
		saveItem.setMnemonic('S'); //imposta mnemonic
		operazMenu.add(saveItem); //aggiunge la voce al menu Operazioni
		saveItem.addActionListener(
				new ActionListener() //classe inner anonima
				{
					public void actionPerformed(ActionEvent event)
					{
						//Inserire salvataggio partita
					}
				}
		);
		JMenuItem loadItem = new JMenuItem("Carica...");
		loadItem.setMnemonic('C'); //imposta mnemoic
		operazMenu.add(loadItem);
		loadItem.addActionListener(
				new ActionListener() //classe inner anonima
				{
					public void actionPerformed(ActionEvent event)
					{
						//carica da file
					}
				}
		);
		//aggiunge il menu File alla barra appena creata
		bar.add(fileMenu);
		bar.add(operazMenu);
		return bar;

	}
	//********************************************************

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
		frame.setJMenuBar(this.menuGrafica());

		this.assegnaTurni();

		frame.pack();
		frame.setVisible(true);
	}
	
	private JPanel creaRiassuntoDati(int indiceDino) {
		datiPanel = datiGui.creaDati(indiceDino,giocatore);
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2,1));
		infoPanel.add(datiPanel);
		infoPanel.add(this.azioni());
		return infoPanel;
	}

	public void assegnaTurni() {
		mg.applicaRaggiungibilita(indiceDino,giocatore,t);
		mg.applicaVisiblita(giocatore, t);
	}


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
//							datiPanel = datiGui.ricreaDatiPanel(indiceDino);
//							datiGui.setPosizione(dino.getRiga(),dino.getColonna());
						}
						else {
							partita.getGiocatori().get(0).rimuoviDinosauro(dino);
							System.out.println("Non e' stato possibile far crescere il dinosauro: " + dino.getId());
						}

					}
				}
		);

		deponi.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						dino.setEnergia(5000);
						dino.setEnergiaMax(5000);
						if((giocatore.eseguiDeposizionedeponiUovo(dino))) {
							mg.applicaVisiblita(giocatore, t);
							partita.nascitaDinosauro(turnoNascita);
							for(int i=0;i<40;i++) {
								for(int j=0;j<40;j++) {
									if(mappa[i][j] !=null && mappa[i][j].getDinosauro()!=null) mappaGui[i][j].setBackground(Color.YELLOW);
								}
							}
							System.out.println("dino1 :" + giocatore.getDinosauri().get(0).getId());
							System.out.println("dino2 :" + giocatore.getDinosauri().get(1).getId());
//							frame.repaint();
						} else {
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
						System.out.println("slezionato: " + indiceDino);
					}
				}
		);
		return panelAzioni;
	}


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
				if((f>=origineRigaStrada && f<=fineRigaStrada) && (j>=origineColonnaStrada && j<=fineColonnaStrada) &&
						stradaPercorsa[f-origineRigaStrada][j-origineColonnaStrada]<0) {
					t.illuminaMappa(partita.getGiocatori().get(0), f, j, raggio);
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
	
	public Giocatore getGiocatore() {
		return giocatore;
	}
	
	public void setGiocatore(Giocatore giocatore) {
		this.giocatore = giocatore;
	}
	
	public int getIndiceDino() {
		return indiceDino;
	}
	
	public void setIndiceDino(int indiceDino) {
		this.indiceDino = indiceDino;
	}
	
	public Turno getT() {
		return t;
	}
	
	public void setT(Turno t) {
		this.t = t;
	}
	
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
}