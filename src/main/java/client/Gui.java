package client;

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class Gui {

	private static final int MAX = 40;
	private JButton[][] mappaGui = new JButton[MAX][MAX];

	private JLabel idDinosauroRis;
	private JLabel dimensioneRis;
	private JLabel forzaRis;
	private JLabel energiaRis;
	private JLabel energiaMaxRis;
	private JLabel tNascitaDinoRis;
	private int turnoPartita;
	private Cella[][] mappa;
	private Utente utente;
	private Giocatore giocatore;
	private JFrame frameGiocatore;
	private JFrame frame;
	private Dinosauro dino;
	private JPanel creaDatiPanel;
	private JPanel panelMappa;
	private JMenuBar bar;
	private JScrollPane mappaPanel;
	private CaricamentoMappa cm;

	private JTextField rigaField;
	private JTextField colonnaField;

	private JTextField user;
	private JTextField password;
	private JTextField specie;
	private JTextField tipo;

	private Partita partita;
	private Turno t;
	private Isola isola;

	private int rigaSelezionata = 99;
	private int colonnaSelezionata = 99;

	/**
	 * 
	 */
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
	public void aggiuntaGiocatore() {

		frameGiocatore = new JFrame("Aggiunta giocatore");
		frameGiocatore.setLayout(new GridLayout(5,2));
		frameGiocatore.setMinimumSize(new Dimension(400,300));

		JLabel userLabel = new JLabel("user");
		user = new JTextField();
		JLabel passwordLabel = new JLabel("password");
		password = new JTextField();
		JLabel  specieLabel = new JLabel("specie");
		specie = new JTextField();
		JLabel tipoLabel = new JLabel("tipo");
		tipo = new JTextField();

		frameGiocatore.add(userLabel);
		frameGiocatore.add(user);
		frameGiocatore.add(passwordLabel);
		frameGiocatore.add(password);
		frameGiocatore.add(specieLabel);
		frameGiocatore.add(specie);
		frameGiocatore.add(tipoLabel);
		frameGiocatore.add(tipo);

		JLabel aggLabel = new JLabel();
		JButton aggiungi = new JButton("aggiungi");
		frameGiocatore.add(aggLabel);
		frameGiocatore.add(aggiungi);

		aggiungi.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						frameGiocatore.dispose();
						utente = new Utente(user.getText(), password.getText());
						giocatore = new Giocatore(partita, utente, turnoPartita, specie.getText(), tipo.getText());
						dino = giocatore.getDinosauri().get(0);
						inizializzaGrafica();
					}
				}
		);
		frameGiocatore.pack();
		frameGiocatore.setVisible(true);
	}

	private JMenuBar menuGrafica() {
		JMenu fileMenu = new JMenu("File"); 
		fileMenu.setMnemonic('F'); //imposta il mnemonic ad F

		//crea la voce About
		//		Icon immagine4 = new ImageIcon(getClass().getResource("info_icon.png"));
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
		//		Icon immagine3 = new ImageIcon(getClass().getResource("refresh.png"));
		JMenuItem refreshItem = new JMenuItem("Rigenera Mappa"/*, immagine3*/);
		refreshItem.setMnemonic('R'); //imposta mnemonic
		operazMenu.add(refreshItem); //aggiunge la voce al menu Operazioni
		refreshItem.addActionListener(
				new ActionListener() //classe inner anonima
				{
					public void actionPerformed(ActionEvent event)
					{
						//rigenera mappa
						mappa = cm.caricaDaFile();
						isola = new Isola(mappa);
						partita = new Partita(isola);
						t = new Turno(partita);
						partita.setTurnoCorrente(t);
						partita.getGiocatori().clear();
						utente = new Utente(user.getText(), password.getText());
						giocatore = new Giocatore(partita, utente, turnoPartita, specie.getText(), tipo.getText());
						dino = giocatore.getDinosauri().get(0);
						//						mappaPanel= creaMappa(mappa);
						//						applicaRaggiungibilita(0);
						//						applicaVisibilita(0);
						frame.removeAll();
						bar.removeAll();
						inizializzaGrafica();
						//						frame.repaint();

						//fare in modo che mappaGui cambi e si aggiorni e poi chiamare un repaint come fatto col JButton cresci

					}
				}
		);
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
		//********************************************************

	}
	//	
	//	private void generaGrafica() {
	//		frame.removeAll();
	//	}

	private void inizializzaGrafica() {

		frame = new JFrame("Isola dei dinosauri BETA1");
		frame.setLayout(new FlowLayout()); //imposto il Layout a griglia
		//		frame.setMinimumSize(new Dimension(1025,637));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		mappaPanel= this.creaMappa(mappa);
		JPanel infoPanel = this.creaInfo(mappa);
		frame.add(infoPanel);
		frame.add(mappaPanel);
		frame.setJMenuBar(this.menuGrafica());
		frame.pack();
		frame.setVisible(true);
		this.applicaRaggiungibilita(0);
		this.applicaVisibilita(0);
		//		this.evidenziaRagg(mappa);
	}


	private JPanel creaInfo(Cella[][] mappa) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel panelInt = new JPanel();
		panelInt.setLayout(new GridLayout(2,1));
		JLabel label1 = new JLabel("Isola Dinosauri");
		JLabel label2 = new JLabel("766172,7xxxxx");
		panelInt.add(label1);
		panelInt.add(label2);

		panel.add(panelInt,BorderLayout.NORTH);
		JPanel creaDatiPanel = this.creaDati(mappa);
		panel.add(creaDatiPanel,BorderLayout.CENTER);
		panel.add(this.azioni(), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel azioni() {
		JPanel panelAzioni = new JPanel();
		panelAzioni.setLayout(new GridLayout(5,1));
		JLabel titolo = new JLabel("Azioni");
		JButton cresci = new JButton("cresci");
		JButton muovi = new JButton("muovi");

		JPanel coordinate = new JPanel();
		coordinate.setLayout(new GridLayout(1,2));
		rigaField = new JTextField();
		colonnaField = new JTextField();
		coordinate.add(rigaField);
		coordinate.add(colonnaField);

		JButton deponi = new JButton("deponi");
		panelAzioni.add(titolo);
		panelAzioni.add(cresci);
		panelAzioni.add(muovi);
		panelAzioni.add(coordinate);
		panelAzioni.add(deponi);

		cresci.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(dino.aumentaDimensione()) {
							System.out.println("Il dinosauro " + dino.getId() + " e' ora di dimensione: " + (dino.getEnergiaMax()/1000));
							int raggio = dino.calcolaRaggioVisibilita();
							t.illuminaMappa(partita.getGiocatori().get(0), dino.getRiga(), dino.getColonna(), raggio);
							applicaVisibilita(0);
							frame.repaint();
						}
						else {
							partita.getGiocatori().get(0).rimuoviDinosauro(dino);
							System.out.println("Non e' stato possibile far crescere il dinosauro: " + dino.getId());
						}
						energiaRis.setText(dino.getEnergia() + "");
						energiaMaxRis.setText(dino.getEnergiaMax() + "");
						dimensioneRis.setText((dino.getEnergiaMax()/1000)  + "");
						forzaRis.setText(dino.calcolaForza() + "");
					}
				}
		);

		muovi.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						//muovi
						int[][] raggiungibile = t.ottieniRaggiungibilita(dino.getRiga(), dino.getColonna());
						int[][] stradaPercorsa;
						int[] coordinate = trovaDinosauro(raggiungibile);
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

						//						JButton pulsante = (JButton)e.getSource();
						//						int q, w = 0;
						//						int rigaClic = 0, colonnaClic = 0;
						//						for(q=0;q<MAX;q++) {
						//							for(w=0;w<MAX;w++) {
						//								if(pulsante.equals(mappaGui[q][w])) {
						//									rigaClic =  q;
						//									colonnaClic = w;  
						//								}
						//							}
						//						}
						riga = Integer.parseInt(rigaField.getText());
						colonna = Integer.parseInt(colonnaField.getText());

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

						boolean spostDino = false;
						if((riga!=dino.getRiga() || colonna!=dino.getColonna()) && raggiungibile[riga-origineRiga][colonna-origineColonna]!=9) {
							spostDino = partita.getTurnoCorrente().spostaDinosauro(dino, riga, colonna);
						} else {
							spostDino=false;
						}

						//													spostDino = p.getTurnoCorrente().spostaDinosauro(dino, riga, colonna);

						System.out.println("->Il dinosauro e' ora in: (" + dino.getRiga() + "," + dino.getColonna() + ")");
						isola.stampaMappaRidotta();
						if(mappa[vecchiaRiga][vecchiaColonna]!=null) {
							if(mappa[vecchiaRiga][vecchiaColonna].getOccupante() instanceof Vegetale) {
								mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.GREEN);
							} else {
								mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.RED);
							}
							if(mappa[vecchiaRiga][vecchiaColonna].getDinosauro()!=null) {
								mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.WHITE);
							}
						}

						//						mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.WHITE);
						applicaVisibilita(0);
						mappaGui[dino.getRiga()][dino.getColonna()].setBackground(Color.YELLOW);
						frame.repaint();
					}
				}
		);

		//		deponi.addActionListener(
		//				new ActionListener() {
		//					public void actionPerformed(ActionEvent e) { 
		//						if((partita.getGiocatori().get(0).eseguiDeposizionedeponiUovo(dino))==false) {
		//							System.out.println("Errore deposizione, possibili motivi: energia insufficiente, squadra dei dinosauri completa");
		//									if(p.getGiocatori().get(conteggioGiocatori).getDinosauri().isEmpty()) {
		//										conteggioDinosauro=0;
		//										conteggioGiocatori--;
		//									}
		//						}
		//					}
		//				}
		//		);
		return panelAzioni;
	}

	private JPanel creaDati(Cella[][] mappa) {

		mappaGui[dino.getRiga()][dino.getColonna()].setBackground(Color.YELLOW);

		dino = giocatore.getDinosauri().get(0);
		creaDatiPanel = new JPanel();
		creaDatiPanel.setLayout(new GridLayout(15,2)); //imposto il Layout a griglia

		JLabel nomeGiocatore = new JLabel("Nome: ");
		creaDatiPanel.add(nomeGiocatore);
		JLabel nomeGiocatoreRis = new JLabel(giocatore.getIdGiocatore() + "");
		creaDatiPanel.add(nomeGiocatoreRis);

		JLabel nomeSpecie = new JLabel("Specie: ");
		creaDatiPanel.add(nomeSpecie);
		JLabel nomeSpecieRis = new JLabel(giocatore.getNomeSpecie());
		creaDatiPanel.add(nomeSpecieRis);

		JLabel eta = new JLabel("Eta: ");
		creaDatiPanel.add(eta);
		JLabel etaRis = new JLabel(giocatore.getEtaAttuale() + "");
		creaDatiPanel.add(etaRis);

		JLabel tNascitaGiocatore = new JLabel("Nascita: ");
		creaDatiPanel.add(tNascitaGiocatore);
		JLabel tNascitaGiocatoreRis = new JLabel(giocatore.getTurnoNascita() + "");
		creaDatiPanel.add(tNascitaGiocatoreRis);

		JLabel dinosauri = new JLabel("Dinosauri: ");
		creaDatiPanel.add(dinosauri);
		JLabel dinosauriRis = new JLabel(giocatore.getDinosauri().get(0).getId());
		creaDatiPanel.add(dinosauriRis);

		JLabel uova = new JLabel("Uova: ");
		creaDatiPanel.add(uova);
		JLabel uovaRis = new JLabel("uova");
		creaDatiPanel.add(uovaRis);

		JLabel idDinosauro = new JLabel("Id: ");
		creaDatiPanel.add(idDinosauro);
		idDinosauroRis = new JLabel(dino.getId());
		creaDatiPanel.add(idDinosauroRis);

		JLabel dimensione = new JLabel("Dim: ");
		creaDatiPanel.add(dimensione);
		dimensioneRis = new JLabel((dino.getEnergiaMax() / 1000) + "");
		creaDatiPanel.add(dimensioneRis);

		JLabel forza = new JLabel("Forza: ");
		creaDatiPanel.add(forza);
		forzaRis = new JLabel(dino.calcolaForza() + "");
		creaDatiPanel.add(forzaRis);

		JLabel energia = new JLabel("Energia: ");
		creaDatiPanel.add(energia);
		energiaRis = new JLabel(dino.getEnergia() + "");
		creaDatiPanel.add(energiaRis);

		System.out.println("nuova energia" + dino.getEnergiaMax());

		JLabel energiaMax = new JLabel("EnergiaMax: ");
		creaDatiPanel.add(energiaMax);
		energiaMaxRis = new JLabel(dino.getEnergiaMax() + "");
		creaDatiPanel.add(energiaMaxRis);

		JLabel tNascitaDino = new JLabel("Nascita: ");
		creaDatiPanel.add(tNascitaDino);
		tNascitaDinoRis = new JLabel(dino.getTurnoNascita() + "");
		creaDatiPanel.add(tNascitaDinoRis);

		JLabel pos = new JLabel("Pos: ");
		creaDatiPanel.add(pos);
		creaDatiPanel.add(new JLabel(dino.getRiga() + "," + dino.getColonna()));

		creaDatiPanel.repaint();
		return creaDatiPanel;
	}


	private JScrollPane creaMappa(Cella[][] mappa) {

		Cella cella;
		//crea la finestra
		panelMappa = new JPanel();
		//		panel.setLayout(null);
		panelMappa.setLayout(new GridLayout(41,41)); //imposto il Layout a griglia
		//		panel.setMinimumSize(new Dimension(3025,2637));

		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				cella = mappa[i][j];
				mappaGui[i][j] = new JButton ();
				mappaGui[i][j].setBounds(90*i, 70*j, 90, 70);
				mappaGui[i][j].setText("       ");
				//								mappaGui[i][j].setText(i + "-" + j);
				mappaGui[i][j].setFont(new Font("Brush Script MT",0, 80 ));
				mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				mappaGui[i][j].setOpaque(true);
				if(cella==null) {
					mappaGui[i][j].setBackground(Color.BLUE);
				} else {
					mappaGui[i][j].setBackground(Color.GRAY);
				}
				mappaGui[i][j].addMouseListener(new GestioneMouse());
			}

		}

		for(int i=MAX-1;i>=0;i--) {
			//creo le label 
			JLabel label = new JLabel();
			label.setText("" + (i));
			label.setVisible(true);
			//aggiungo la label all'inzio di ogni riga
			panelMappa.add(label);

			//aggiungo le 40 celle che costituiscono la riga una in fila
			//all'altra e dopo passo alla riga sotto col for ("i")
			for(int j=0;j<MAX;j++) {
				panelMappa.add(mappaGui[i][j]);
			}
		}

		//creo una label vuota e la inserisco nell'estremo inferiore a sx della mappa
		//dove non ci deve andare nessun numero di riga/colonna
		JLabel label = new JLabel();
		label.setText(" ");
		label.setVisible(true);
		panelMappa.add(label);

		//inserisco le label per le colonne in basso
		for(int i=0;i<MAX;i++) {
			label = new JLabel();
			label.setText("  " + i);
			label.setVisible(true);
			panelMappa.add(label);
		}

		panelMappa.setVisible(true);
		JScrollPane scroll = new JScrollPane(panelMappa);
		//		scroll.setAutoscrolls(true);
		scroll.setVisible(true);
		scroll.setMinimumSize(new Dimension(1025,637));
		scroll.setPreferredSize(new Dimension(1025,637));
		//		scroll.setDoubleBuffered(true);
		//		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollBar verticalScrollBar   = scroll.getVerticalScrollBar();
		JScrollBar horizontalScrollBar = scroll.getHorizontalScrollBar();
		//FIXME problema nella riga sotto
		verticalScrollBar.setValue(verticalScrollBar.getMaximum());
		horizontalScrollBar.setValue(horizontalScrollBar.getMaximum());
		//		scroll.getVerticalScrollBar().setValue(verticalScrollBar.getMinimum();
		//		scroll.getHorizontalScrollBar().setValue(5000);
		//		scroll.setLocation(500, 500);
		scroll.setWheelScrollingEnabled(false);
		//		scroll.setSize(new Dimension(1025,637));
		return scroll;

	}


	public void applicaVisibilita (int indice) {
		System.out.println();
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				if(giocatore.getMappaVisibile()[i][j]) {
					if (mappa[i][j] == null) { //e' acqua
						mappaGui[i][j].setBackground(Color.BLUE);
					} else { //se e' terra puo' essere carogna o vegetale
						Cella cella = mappa[i][j];
						if(cella.getDinosauro()!=null) {
							mappaGui[i][j].setBackground(Color.YELLOW);
						} else {
							if(cella.getOccupante() instanceof Carogna) {
								mappaGui[i][j].setBackground(Color.RED);
							}
							if(cella.getOccupante() instanceof Vegetale) {
								mappaGui[i][j].setBackground(Color.GREEN);
							}
							if(!(cella.getOccupante() instanceof Vegetale) &&
									!(cella.getOccupante() instanceof Carogna)) {
								mappaGui[i][j].setBackground(Color.GRAY);
							}
						}

					}
				} else {
					mappaGui[i][j].setBackground(Color.BLACK);
				}
			}
		}
	}

	public void applicaRaggiungibilita (int indice) {
		int rigaDino = giocatore.getDinosauri().get(indice).getRiga();
		int colonnaDino = giocatore.getDinosauri().get(indice).getColonna();
		int[][] raggiungibile = t.ottieniRaggiungibilita(rigaDino, colonnaDino);
		int[] coordinate = trovaDinosauro(raggiungibile);
		//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
		int inizioRiga = rigaDino - coordinate[0];
		int inizioColonna = colonnaDino - coordinate[1];
		int fineRiga = rigaDino + (raggiungibile.length - coordinate[0] - 1);
		int fineColonna = colonnaDino + (raggiungibile[0].length - coordinate[1] - 1);

		for(int i=MAX-1;i>=0;i--) {
			for(int j=0;j<MAX;j++) {
				if((i>=inizioRiga && i<=fineRiga) && (j>=inizioColonna && j<=fineColonna))  {
					if((raggiungibile[i - inizioRiga][j - inizioColonna]!=9) &&
							(raggiungibile[i - inizioRiga][j - inizioColonna]!=8)) {
						mappaGui[i][j].setBackground(Color.WHITE);
					} 
					if(raggiungibile[i - inizioRiga][j - inizioColonna]==0) {
						mappaGui[i][j].setBackground(Color.YELLOW);
						mappaGui[i][j].setEnabled(false);
					}
				} else {
					mappaGui[i][j].setBackground(Color.BLACK);
					mappaGui[i][j].setEnabled(false);
				}
			}
		}
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


	private int[] trovaDinosauro (int[][] raggiungibile) {
		int j,w;
		int[] uscita = {0,0};
		for(j=0;j<raggiungibile.length;j++) {
			for(w=0;w<raggiungibile[0].length;w++) {
				System.out.print(raggiungibile[j][w] + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();

		for(j=0;j<raggiungibile.length;j++) {
			for(w=0;w<raggiungibile[0].length;w++) {
				if(raggiungibile[j][w]==0) {
					uscita[0] = j;
					uscita[1] = w;
					return uscita;
				}
			}
		}
		return uscita;
	}


	private void eseguiMovimento(int rigaCliccata, int colonnaCliccata) {

		int[][] raggiungibile = t.ottieniRaggiungibilita(dino.getRiga(), dino.getColonna());
		int[][] stradaPercorsa;
		int[] coordinate = trovaDinosauro(raggiungibile);
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

		boolean spostDino = false;
		if((riga!=dino.getRiga() || colonna!=dino.getColonna()) && raggiungibile[riga-origineRiga][colonna-origineColonna]!=9) {
			spostDino = partita.getTurnoCorrente().spostaDinosauro(dino, riga, colonna);
		} else {
			spostDino=false;
		}

		System.out.println("->Il dinosauro e' ora in: (" + dino.getRiga() + "," + dino.getColonna() + ")");
		isola.stampaMappaRidotta();
		if(mappa[vecchiaRiga][vecchiaColonna]!=null) {
			if(mappa[vecchiaRiga][vecchiaColonna].getOccupante() instanceof Vegetale) {
				mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.GREEN);
			} else {
				mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.RED);
			}
			if(mappa[vecchiaRiga][vecchiaColonna].getDinosauro()!=null) {
				mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.WHITE);
			}
		}

		//						mappaGui[vecchiaRiga][vecchiaColonna].setBackground(Color.WHITE);
		applicaVisibilita(0);
		mappaGui[dino.getRiga()][dino.getColonna()].setBackground(Color.YELLOW);
		frame.repaint();
	}

	class GestioneMouse implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			JButton pulsante = (JButton)e.getSource();
			pulsante.setBackground(Color.RED);
			int q, w = 0;
			int rigaClic = 0, colonnaClic = 0;
			for(q=0;q<MAX;q++) {
				for(w=0;w<MAX;w++) {
					if(pulsante.equals(mappaGui[q][w])) {
						rigaClic =  q;
						colonnaClic = w;  
					}
				}
			}
			eseguiMovimento(rigaClic,colonnaClic);
		}

		public void mouseEntered(MouseEvent e) {
			//			JButton pulsante = (JButton)e.getSource();
			//			pulsante.setBorder(BorderFactory.createLineBorder(Color.RED));
			//			pulsante.setBorder(BorderFactory.createRaisedBevelBorder());		
		}
		public void mouseExited(MouseEvent e) {
			//			JButton pulsante = (JButton)e.getSource();
			//			pulsante.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		}

		public void mousePressed(MouseEvent e) {
			//			JButton pulsante = (JButton)e.getSource();
			//			pulsante.setBackground(Color.GREEN);
		}

		public void mouseReleased(MouseEvent e) {
			//			JButton pulsante = (JButton)e.getSource();
			//			pulsante.setBackground(Color.YELLOW);
		}
	}


	//	class ButtonHandler implements ActionListener{
	//		public void actionPerformed(ActionEvent e) {
	//			Giocatore giocatore = new Giocatore(partita, null, turnoPartita, , null);
	//			JButton pressed = (JButton)e.getSource();
	//			pressed.setBackground(Color.RED);
	//		}
	//	}

}




