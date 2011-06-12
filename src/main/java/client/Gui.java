package client;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.*;

/**
 *	Classe principale che si occupa di creare la grafica dell'applicazione (lato client).
 */
public class Gui {

	private static int MAX = 40;
	private JButton[][] mappaGui = new JButton[MAX][MAX];

//	private int turnoPartita;

	private JFrame frame;
	private JScrollPane mappaPanel;
	private JPanel datiPanel;
	private JPanel infoPanel;
//	private JComboBox sceltaDinosauro = new JComboBox();
	private int indiceDino; 
//	private int turnoNascita;

//	private Icon carnivoroIcona = new ImageIcon(this.getClass().getResource("/carnivoro.png"));
//	private Icon erbivoroIcona = new ImageIcon(this.getClass().getResource("/erbivoro.png"));
//	private Icon terraIcona = new ImageIcon(this.getClass().getResource("/terra.jpg"));

	private String[][] mappaRicevuta;
	private DatiGui datiGui;
	private MappaGui mg;
	private ClientGui clientGui;

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore della classe Gui che inizializza le classi clientGui, dayaGui e mappaGui.
	 */
	public Gui (ClientGui clientGui) {
		this.clientGui = clientGui;
		datiGui = new DatiGui(this);
		mg = new MappaGui(this,datiGui);
	}

	/**
	 * Metodo che si occupa di preparare i dati per poi inizializzare la grafica.
	 * @param user String che rappresenta il nome dell'utente al momento del login.
	 * @param password String che rappresenta la password dell'utente al momento del login.
	 */
	public void preparaDati(String user, String password) {
		try {
			this.caricaMappa();

			this.getClientGui().statoDinosauro("11");
			int rigaDino = Integer.parseInt(this.getClientGui().getAnswer().split(",")[4].replace("{", ""));
			int colonnaDino = Integer.parseInt(this.getClientGui().getAnswer().split(",")[5].replace("}", ""));
			System.out.println("Dino e' in: " + rigaDino + "," + colonnaDino);
			this.inizializzaGrafica();
			mg.setScrollBar(rigaDino, colonnaDino);

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	//	public void inizializzaSceltaDino(Giocatore giocatore) {
	//		String[] listaDino = new String[MAXDINO];
	//		sceltaDinosauro.removeAllItems();
	//		for(int i=0;i<giocatore.getDinosauri().size();i++) {
	//			listaDino[i] = giocatore.getDinosauri().get(i).getId();
	//			sceltaDinosauro.addItem(listaDino[i]);
	//		}
	//		//		sceltaDinosauro = new JComboBox(listaDino); 
	//		sceltaDinosauro.setMaximumRowCount(MAXDINO);
	//	}

	private String impostaMappa() throws IOException, InterruptedException {
		this.getClientGui().vistaLocale("11");
		String answer = this.getClientGui().getAnswer();

		this.mappaPanel= mg.creaMappa(mg.creaMappaVisibilita(answer));
		this.setMappaGui(mg.getMappaGui());
		return answer;
	}

	/**
	 * Metodo che inizializza la grafica, creando un frame ed aggiungendovi i vari JPanel e menu.
	 */
	private void inizializzaGrafica() {
		frame = new JFrame("Isola dei dinosauri BETA1");
		frame.setLayout(new FlowLayout());

		try {
			String answer = this.impostaMappa();
			frame.add(this.creaRiassuntoDati(indiceDino)); //aggiungo infoPanel
			frame.add(this.mappaPanel);

			MenuGui menuGui = new MenuGui(frame);		
			frame.setJMenuBar(menuGui.getMenu());

			mg.applicaRaggiungibilita();		
			mg.applicaVisiblita(answer);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		frame.addWindowListener(
				new WindowListener() {
					@Override
					public void windowClosing(WindowEvent arg0) {
						try {
							clientGui.logout();
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}									
					}
					@Override
					public void windowActivated(WindowEvent arg0) {
					}
					@Override
					public void windowClosed(WindowEvent arg0) {
						try {
							clientGui.uscitaPartita();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					@Override
					public void windowDeactivated(
							WindowEvent arg0) {
					}
					@Override
					public void windowDeiconified(
							WindowEvent arg0) {
					}
					@Override
					public void windowIconified(WindowEvent arg0) {
					}
					@Override
					public void windowOpened(WindowEvent arg0) {
					}
				}
		);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Metodo che crea il JPanel con le informazioni sul Dinosauro e sul Giocatore.
	 * @param indiceDino int che rappresenta il numero del Dinosauro.
	 * @return Un JPanel contenente il riassunto della situazione del Giocatore e del suo Dinosauro selezionato con indiceDino.
	 */
	private JPanel creaRiassuntoDati(int indiceDino) {
		datiPanel = datiGui.creaDati("11");
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2,1));
		infoPanel.add(datiPanel);
		infoPanel.add(this.azioni());
		return infoPanel;
	}

	public void caricaMappa() throws IOException, InterruptedException {
		this.getClientGui().mappaGenerale();
		String risposta = this.getClientGui().getAnswer();

		if(risposta.contains("@mappaGenerale")) {
			int maxRighe = Integer.parseInt(risposta.split(",")[1].replace("{", ""));
			int maxColonne = Integer.parseInt(risposta.split(",")[2].replace("}", ""));
			this.mappaRicevuta = new String[maxRighe][maxColonne];
			String mappa = risposta.replace("@mappaGenerale"+","+"{"+maxRighe+","+maxColonne+"}"+",", "");
			mappa = mappa.replace("[", "");
			mappa = mappa.replace("]", "");
			System.out.println(mappa);
			String[] riga = mappa.split(";");

			for(int i=maxRighe-1;i>=0;i--) {
				System.out.println(riga[i]);
				for(int j=0;j<maxColonne;j++) {
					this.mappaRicevuta[i][j] = (riga[i].charAt(j)) + "";
				}
			}

			for(int i=0;i<MAX;i++) {
				for(int j=0;j<MAX;j++) {
					System.out.print(this.mappaRicevuta[i][j] + " ");
				}
				System.out.println();
			}
		}
	}

	/**
	 * Metodo che crea il JPanel contenente i pulsanti per far crescere, deporre e selezionare un Dinosauro.
	 * @return Un JPanel contenente i pulsanti per far crescere, deporre e selezionare un Dinosauro.
	 */
	private JPanel azioni() {
		JPanel panelAzioni = new JPanel();
		panelAzioni.setLayout(new GridLayout(5,1));
		JLabel titolo = new JLabel("Azioni");
		JButton cresci = new JButton("Cresci");
		JButton deponi = new JButton("Deponi");

		//		JPanel selezionePanel = new JPanel(new GridLayout(1,2));
		//		selezionePanel.add(new JLabel("   Seleziona: "));
		//		this.inizializzaSceltaDino(giocatore);
		//		selezionePanel.add(sceltaDinosauro);

		JButton uscitaPartita = new JButton("Uscita Partita");

		panelAzioni.add(titolo);
		panelAzioni.add(cresci);
		panelAzioni.add(deponi);
		//		panelAzioni.add(selezionePanel);
		panelAzioni.add(new JLabel());
		panelAzioni.add(uscitaPartita);

		uscitaPartita.addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent event) {
						try {
							getClientGui().uscitaPartita();
							if(getClientGui().getRispostaServer().equals("@ok")) {
								//TODO fare un qualche cosa che fa ripartire il programma
								//da capo e bisogna per forza fare anche il login
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}
				}
		);

		//		sceltaDinosauro.addItemListener(
		//				new ItemListener() {
		//					public void itemStateChanged(ItemEvent event) {
		//						String selezionato = (String) sceltaDinosauro.getSelectedItem();
		//						int i;
		//						for(i=0;i<giocatore.getDinosauri().size();i++) {
		//							if(giocatore.getDinosauri().get(i).getId().equals(selezionato)) {
		//								break;
		//							}
		//						}
		//						setIndiceDino(i);
		//						mg.setIndiceDino(i);
		//						datiGui.aggiornaDati();
		//					}
		//				}
		//		);

		cresci.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						try {
							getClientGui().crescitaDinosauro("11");
							String risposta = getClientGui().getAnswer();
							if(risposta.equals("@ok")) {
								getClientGui().vistaLocale("11");
								mg.applicaVisiblita(getClientGui().getAnswer());
								//aggiorno il panel col riassunto dello stato del dinosauro
								datiGui.aggiornaDati("11");
							} else {
								if(risposta.contains("@raggiuntaDimensioneMax")) {
									JOptionPane.showMessageDialog(null, "Raggiunta dimensione massima");
								} else {
									if(risposta.contains("@mortePerInedia")) {
										JOptionPane.showMessageDialog(null, "Il dinosauro e' morto perche' senza energia");
									}
								}
							}
						} catch (IOException e2) {
							e2.printStackTrace();
						} catch (InterruptedException e2) {
							e2.printStackTrace();
						}
					}
				}
		);

		//		deponi.addActionListener(
		//				new ActionListener() {
		//					public void actionPerformed(ActionEvent e) { 
		//						//						dino.setEnergia(5000);
		//						//						dino.setEnergiaMax(5000);
		//						int statoDeposizione = giocatore.eseguiDeposizionedeponiUovo(dino);
		//						if(statoDeposizione==1) {
		//							int raggio = dino.calcolaRaggioVisibilita();
		//							t.illuminaMappa(partita.getGiocatori().get(0), dino.getRiga(), dino.getColonna(), raggio);
		//							mg.applicaVisiblita(giocatore, t);
		//							partita.nascitaDinosauro(turnoNascita);
		//							inizializzaSceltaDino(giocatore);
		//							mg.rinizializzaMappa(carnivoroIcona, erbivoroIcona);
		//						} else {
		//							mappaGui[dino.getRiga()][dino.getColonna()].setIcon(terraIcona);
		//							if(statoDeposizione==-2) {
		//								JOptionPane.showMessageDialog(null, "Errore deposizione!\nIl dinosauro e' morto, " +
		//								"\nenergia insufficiente!");
		//							}
		//							if(statoDeposizione==0) {
		//								JOptionPane.showMessageDialog(null, "Errore deposizione!\nIl dinosauro e' morto, " +
		//								"squadra dei dinosauri completa!");
		//							}
		//						}
		//						datiGui.aggiornaDati();
		//					}
		//				}
		//		);
		return panelAzioni;
	}


	//	/**
	//	 * Metoco per eseguire il movimento di un Dinosauro.
	//	 * @param rigaCliccata int che rappreseta la riga della cella cliccata nella mappa del gioco.
	//	 * @param colonnaCliccata int che rappreseta la colonna della cella cliccata nella mappa del gioco.
	//	 */
	//	void eseguiMovimento(int rigaCliccata, int colonnaCliccata) {
	//		this.dino = giocatore.getDinosauri().get(this.indiceDino);
	//		//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
	//		int[][] stradaPercorsa = t.ottieniStradaPercorsa(dino.getRiga(), dino.getColonna(),rigaCliccata, colonnaCliccata);
	//		int[] coordinateStrada = trovaDinosauroStrada(stradaPercorsa);
	//
	//		int origineRigaStrada = dino.getRiga() - coordinateStrada[0];
	//		int origineColonnaStrada = dino.getColonna() - coordinateStrada[1];
	//		int fineRigaStrada = dino.getRiga() + (stradaPercorsa.length - coordinateStrada[0] - 1);
	//		int fineColonnaStrada = dino.getColonna() + (stradaPercorsa[0].length - coordinateStrada[1] - 1);
	//
	//		int raggio = dino.calcolaRaggioVisibilita();
	//		//illumino la strada
	//		for(int f=0;f<MAX;f++) {
	//			for(int j=0;j<MAX;j++) {
	//				if((f>=origineRigaStrada && f<=fineRigaStrada) && (j>=origineColonnaStrada && j<=fineColonnaStrada)) {
	//					if(stradaPercorsa[f-origineRigaStrada][j-origineColonnaStrada]<0) {
	//						t.illuminaMappa(partita.getGiocatori().get(0), f, j, raggio);
	//					}
	//				}
	//			}
	//		}
	//		int stato = partita.getTurnoCorrente().spostaDinosauro(dino, rigaCliccata, colonnaCliccata);
	//		if(stato==-2) {
	//			JOptionPane.showMessageDialog(null, "Il Dinosauro e' morto!");
	//		} else {
	//			if(stato==-1) {
	//				JOptionPane.showMessageDialog(null, "Scegliere un'altra destinazione!");
	//			} else {
	//				if(stato==1) {
	//					JOptionPane.showMessageDialog(null, "Tutto ok!");
	//				} else {
	//					if(stato==0) {
	//						JOptionPane.showMessageDialog(null, "Vince attaccato!");
	//					} else {
	//						if(stato==2) {
	//							JOptionPane.showMessageDialog(null, "Vince attaccante!");
	//						} else {
	//							if(stato==3) {
	//								JOptionPane.showMessageDialog(null, "Conbattimento eseguito e mangiato occupante!");
	//							}
	//						}
	//					}
	//				}
	//			}
	//		}
	//
	//		mg.applicaVisiblita(giocatore, t);
	//		mg.applicaRaggiungibilita(indiceDino, giocatore, t);
	//	}
	//
	//	/**
	//	 * Metodo per ottenere i dati nella vista di raggiungibilita'.
	//	 * @return un array di int con:
	//	 * 			[0] - riga d'inizio vista,
	//	 * 			[1] - colonna d'inizio vista,
	//	 * 			[2] - riga di fine vista,
	//	 * 			[3] - colonna di fine vista.
	//	 */
	//	public int[] getDatiRaggiungibilita () {
	//		int rigaDino = giocatore.getDinosauri().get(indiceDino).getRiga();
	//		int colonnaDino = giocatore.getDinosauri().get(indiceDino).getColonna();
	//		int[][] raggiungibile = t.ottieniRaggiungibilita(rigaDino, colonnaDino);
	//		int[] coordinate = mg.trovaDinosauro(raggiungibile);
	//		//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
	//		int[] datiRaggiungibilita = new int[4];
	//		datiRaggiungibilita[0] = rigaDino - coordinate[0]; //riga inzio
	//		datiRaggiungibilita[1] = colonnaDino - coordinate[1]; //colonna inizio
	//		datiRaggiungibilita[2] = rigaDino + (raggiungibile.length - coordinate[0] - 1); //riga fine
	//		datiRaggiungibilita[3] = colonnaDino + (raggiungibile[0].length - coordinate[1] - 1); //colonna fine
	//		return datiRaggiungibilita;
	//	}

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

	public ClientGui getClientGui() {
		return clientGui;
	}

	public void setClientGui(ClientGui clientGui) {
		this.clientGui = clientGui;
	}

	public String[][] getMappaRicevuta() {
		return mappaRicevuta;
	}

	public void setMappaRicevuta(String[][] mappaRicevuta) {
		this.mappaRicevuta = mappaRicevuta;
	}

}