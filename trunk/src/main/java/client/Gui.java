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

	private static final int MAX = 40;
	private JButton[][] mappaGui = new JButton[MAX][MAX];

	private JFrame frame;
	private JScrollPane mappaPanel;
	private JPanel datiPanel;
	private JPanel infoPanel;
	private JComboBox sceltaDinosauro = new JComboBox();
	private int indiceDino; 
	private String listaDinosauri;
	private int maxIndiceDinosauri;
	//	private int turnoNascita;
	//	private int turnoPartita;
	private JButton cresci;
	private JButton deponi;
	private JButton prossimoDinosauro;

	private String[][] mappaRicevuta;
	private DatiGui datiGui;
	private MappaGui mg;
	private ClientGui clientGui;
	private boolean[] movimento = {false,false,false,false,false};
	private boolean[] azione = {false,false,false,false,false};

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore della classe Gui che inizializza le classi clientGui, dayaGui e mappaGui.
	 */
	public Gui (ClientGui clientGui) {
		this.clientGui = clientGui;
		datiGui = new DatiGui(this);
		mg = new MappaGui(this,datiGui);
		this.maxIndiceDinosauri = 1;
	}

	public void disattivaAzioniGui() {
		this.cresci.setEnabled(false);
		this.deponi.setEnabled(false);
		this.prossimoDinosauro.setEnabled(false);
		mg.disattivaAzioniMappa();
	}

	public void attivaAzioniGui() {
		this.cresci.setEnabled(true);
		this.deponi.setEnabled(true);
		this.prossimoDinosauro.setEnabled(true);
		mg.attivaAzioniMappa();
	}

	/**
	 * Metodo che si occupa di preparare i dati per poi inizializzare la grafica.
	 * @param user String che rappresenta il nome dell'utente al momento del login.
	 * @param password String che rappresenta la password dell'utente al momento del login.
	 */
	public void preparaDati(String user, String password) {
		try {
			this.caricaMappa();

			this.getClientGui().listaDinosauri();
			this.listaDinosauri = this.getClientGui().getRisposta();
			System.out.println(this.listaDinosauri);

			this.getClientGui().statoDinosauro("11");
			int rigaDino = Integer.parseInt(this.getClientGui().getRisposta().split(",")[4].replace("{", ""));
			int colonnaDino = Integer.parseInt(this.getClientGui().getRisposta().split(",")[5].replace("}", ""));
			this.inizializzaGrafica();
			mg.setScrollBar(rigaDino, colonnaDino);
		} catch (IOException ecc) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ecc) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
	}

	public String impostaMappa() throws IOException, InterruptedException {
		this.getClientGui().vistaLocale("11");
		String answer = this.getClientGui().getRisposta();

		this.mappaPanel= mg.creaMappa(mg.creaMappaVisibilita(answer));
		this.setMappaGui(mg.getMappaGui());
		return answer;
	}

	public String ottieniStatoDinosauro() {
		String idDinosauro = this.getIdDinosauro(this.getIndiceDino());
		try {
			this.getClientGui().statoDinosauro(idDinosauro);
		} catch (IOException ecc) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ecc) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
		return this.getClientGui().getRisposta();
	}

	/**
	 * Metodo che inizializza la grafica, creando un frame ed aggiungendovi i vari JPanel e menu.
	 */
	private void inizializzaGrafica() {
		frame = new JFrame("Isola dei dinosauri BETA1");
		frame.setLayout(new FlowLayout());

		try {
			String answer = this.impostaMappa();
			frame.add(this.creaRiassuntoDati()); //aggiungo infoPanel
			frame.add(this.mappaPanel);

			MenuGui menuGui = new MenuGui(frame,this);		
			frame.setJMenuBar(menuGui.getMenu());

			mg.applicaVisiblita(answer);
			mg.applicaRaggiungibilita();
		} catch (IOException ecc) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ecc) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}

		frame.addWindowListener(
				new WindowListener() {
					@Override
					public void windowClosing(WindowEvent arg0) {
						try {
							clientGui.logout();
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						} catch (IOException ecc) {
							JOptionPane.showMessageDialog(null,"IOException");
						} catch (InterruptedException ecc) {
							JOptionPane.showMessageDialog(null,"InterruptedException");
						}								
					}
					@Override
					public void windowActivated(WindowEvent arg0) {
					}
					@Override
					public void windowClosed(WindowEvent arg0) {
						try {
							clientGui.uscitaPartita();
						} catch (IOException ecc) {
							JOptionPane.showMessageDialog(null,"IOException");
						} catch (InterruptedException ecc) {
							JOptionPane.showMessageDialog(null,"InterruptedException");
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
	private JPanel creaRiassuntoDati() {
		datiPanel = datiGui.creaDati("11");
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2,1));
		infoPanel.add(datiPanel);
		infoPanel.add(this.azioni());
		return infoPanel;
	}

	public void caricaMappa() throws IOException, InterruptedException {
		this.getClientGui().mappaGenerale();
		String risposta = this.getClientGui().getRisposta();

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

	public void ottieniClassifica() {
		try {
			this.getClientGui().classifica();
			String risposta = this.getClientGui().getRisposta();
			System.out.println("classifica: " + risposta);
		} catch (IOException ecc) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ecc) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
	}

	/**
	 * Metodo che crea il JPanel contenente i pulsanti per far crescere, deporre e selezionare un Dinosauro.
	 * @return Un JPanel contenente i pulsanti per far crescere, deporre e selezionare un Dinosauro.
	 */
	private JPanel azioni() {
		JPanel panelAzioni = new JPanel();
		panelAzioni.setLayout(new GridLayout(7,1));
		JLabel titolo = new JLabel("Azioni");
		cresci = new JButton("Cresci");
		deponi = new JButton("Deponi");

		//TODO
		this.ottieniClassifica();

		JPanel selezionePanel = new JPanel(new GridLayout(1,2));
		selezionePanel.add(new JLabel("   Seleziona: "));
		//		this.inizializzaSceltaDino();
		selezionePanel.add(sceltaDinosauro);

		JButton uscitaPartita = new JButton("Uscita Partita");
		prossimoDinosauro = new JButton("Prossimo dinosauro");


		panelAzioni.add(titolo);
		panelAzioni.add(cresci);
		panelAzioni.add(deponi);
		panelAzioni.add(new JLabel());
		panelAzioni.add(uscitaPartita);
		panelAzioni.add(prossimoDinosauro);

		prossimoDinosauro.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						System.out.println("Ho cliccato su prossimo dinosauri");
						prossimoDinosauro();
					}
				}
		);

		uscitaPartita.addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent event) {
						try {
							getClientGui().uscitaPartita();
							if(getClientGui().getRispostaServer().equals("@ok")) {
								frame.dispose();
							}
						} catch (IOException ecc) {
							JOptionPane.showMessageDialog(null,"IOException");
						} catch (InterruptedException ecc) {
							JOptionPane.showMessageDialog(null,"InterruptedException");
						}						
					}
				}
		);

		//				sceltaDinosauro.addItemListener(
		//						new ItemListener() {
		//							public void itemStateChanged(ItemEvent event) {
		//								String selezionato = (String) sceltaDinosauro.getSelectedItem();
		//								int i;
		//								for(i=0;i<giocatore.getDinosauri().size();i++) {
		//									if(giocatore.getDinosauri().get(i).getId().equals(selezionato)) {
		//										break;
		//									}
		//								}
		//								setIndiceDino(i);
		//								mg.setIndiceDino(i);
		//								datiGui.aggiornaDati();
		//							}
		//						}
		//				);

		cresci.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(!getAzione()[getIndiceDino()]) { // TODO indicedino
							try {
								String idDinosauro = getIdDinosauro(indiceDino);
								getClientGui().crescitaDinosauro(idDinosauro);
								String risposta = getClientGui().getRisposta();
								if(risposta.equals("@ok")) {
									getClientGui().vistaLocale(idDinosauro);
									mg.applicaVisiblita(getClientGui().getRisposta());
									//aggiorno il panel col riassunto dello stato del dinosauro
									datiGui.aggiornaDati(idDinosauro);
									prossimoDinosauro();
								} else {
									if(risposta.contains("@raggiuntaDimensioneMax")) {
										JOptionPane.showMessageDialog(null, "Raggiunta dimensione massima");
									} else {
										if(risposta.contains("@mortePerInedia")) {
											JOptionPane.showMessageDialog(null, "Il dinosauro e' morto perche' senza energia");
										}
									}
								}
								getAzione()[0] = true;
							} catch (IOException ecc) {
								JOptionPane.showMessageDialog(null,"IOException");
							} catch (InterruptedException ecc) {
								JOptionPane.showMessageDialog(null,"InterruptedException");
							}
						} else {
							//non si puo' eseguire il movimento
							JOptionPane.showMessageDialog(null, "Azione di crescita gia' eseguita!");
						}
					}

				}
		);

		deponi.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(!getAzione()[getIndiceDino()]) { 
							try {
								String idDinosauro = getIdDinosauro(indiceDino);
								getClientGui().deponiUovo(idDinosauro);
								String risposta = getClientGui().getRisposta(); //ottengo la risposta che contiene ok e l'id del Dinosauro
								idDinosauro = risposta.replace("@ok,", "");
								if(risposta.contains("@ok")) {
									getClientGui().statoDinosauro(idDinosauro);
									System.out.println("out" + getClientGui().getRisposta());
									getClientGui().vistaLocale(idDinosauro);
									mg.applicaVisiblita(getClientGui().getRisposta());
									//aggiorno il panel col riassunto dello stato del dinosauro
									datiGui.aggiornaDati(idDinosauro);

									//TODO per reinizializzae la lista dei dinosauri
									getClientGui().listaDinosauri();
									listaDinosauri = getClientGui().getRisposta();
									System.out.println(listaDinosauri);
									prossimoDinosauro();

								} else {
									if(risposta.contains("@raggiuntaDimensioneMax")) {
										JOptionPane.showMessageDialog(null, "Raggiunta dimensione massima");
									} else {
										if(risposta.contains("@mortePerInedia")) {
											JOptionPane.showMessageDialog(null, "Il dinosauro e' morto perche' senza energia");
										}
									}
								}
								getAzione()[0] = true;
							} catch (IOException ecc) {
								JOptionPane.showMessageDialog(null,"IOException");
							} catch (InterruptedException ecc) {
								JOptionPane.showMessageDialog(null,"InterruptedException");
							}
						} else {
							//non si puo' eseguire il movimento
							JOptionPane.showMessageDialog(null, "Azione di deposizione gia' eseguita!");
						}
					}
				}
		);
		return panelAzioni;
	}

	public void assegnaTurniDinosauri() {
//		if()
	}

	public String getIdDinosauro(int indiceDino) {
		//		try {
		String risposta = this.listaDinosauri;
		System.out.println("la risposta e': " + risposta);
		risposta = risposta.replace("@listaDinosauri,", "");
		String[] dinosauri = risposta.split(",");
		return dinosauri[indiceDino];
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		return null;
	}

	public void resetAzioniEMovimenti() {
		//ottengo la lista dei dinosauri del giocatore
		for(int i=0;i<5;i++) {
			this.getMovimento()[i] = false;
			this.getAzione()[i] = false;
		}
	}

	private void prossimoDinosauro(){

		//metodo per cambiare dinosauro e automaticamente setta a true (cioe' azione  e movimento eseguito) i 2 array
		String[] dinosauri = this.listaDinosauri.replace("@listaDinosauri,","").split(",");
		this.maxIndiceDinosauri = dinosauri.length;

		if(indiceDino + 1 < maxIndiceDinosauri) {
			this.getMovimento()[this.indiceDino] = true;
			this.getAzione()[this.indiceDino] = true;
			this.indiceDino++;
			System.out.println(this.indiceDino);
			String idDino = this.getIdDinosauro(indiceDino);
			System.out.println("id dino con quell'indice: " + idDino);
			mg.aggiornaStato(idDino);
		} else {
			//TODO il turno finisce per forza e viene inviato al server il messaggio

			//non tenerlo nella versione definitiva perche' dovra' essere fatto quando un metodi ricevera' un messaggio dal server 
			//che indica l'assegnazione del turno
			this.indiceDino = 0;
			for(int i=0;i<maxIndiceDinosauri;i++) {
				this.getMovimento()[i] = false;
				this.getAzione()[i] = false;
			}
			this.disattivaAzioniGui();
			mg.disattivaAzioniMappa();
			//invia messaggio 
		}
	}

	public void verificaTurno(int indiceDino) {
		if(this.getMovimento()[indiceDino] && this.getAzione()[indiceDino]) {
			this.prossimoDinosauro();
		}
	}


	//	public void prossimoDinosauro() {
	//		this.indiceDino++;
	//		if(this.turni[indiceDino]!=-1 && this.turni[indiceDino]<2) {
	//			
	//		}
	//	}

	//	public void eseguireAzione() {
	//		JFrame turnoFrame = new JFrame();
	//		turnoFrame.setLayout(new GridLayout(3,1));
	//		turnoFrame.add(new JLabel("Hai 30 secondi per fare una scelta:"));
	//		JButton pulsanteConferma = new JButton("Conferma");
	//		JButton pulsantePassa = new JButton("Passa");
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

	/**
	 * @return Un riferimento all'oggetto clientGui di tipo ClientGui, contenente tutti i metodi per richiedere i messaggi al server.
	 */
	public ClientGui getClientGui() {
		return clientGui;
	}

	/**
	 * @return Un array bidimensionale di String rappresentante la mappa ricevuta dal server.
	 */
	public String[][] getMappaRicevuta() {
		return mappaRicevuta;
	}

	public boolean[] getMovimento() {
		return movimento;
	}

	public void setMovimento(boolean[] movimento) {
		this.movimento = movimento.clone();
	}

	public boolean[] getAzione() {
		return azione;
	}

	public void setAzione(boolean[] azione) {
		this.azione = azione.clone();
	}
}