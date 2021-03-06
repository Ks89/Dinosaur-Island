/*
Copyright 2011-2015 Stefano Cappa
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package client.socket;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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
	private JButton cresci;
	private JButton deponi;
	private JButton prossimoDinosauro;
	private Timer timer;

	private JButton conferma;
	private JButton passa;

	private String[][] mappaRicevuta;
	private DatiGui datiGui;
	private MappaGui mg;
	private Client clientGui;

	private boolean[] movimento = {false,false,false,false,false};
	private boolean[] azione = {false,false,false,false,false};

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore della classe Gui che inizializza le classi clientGui, dayaGui e mappaGui.
	 */
	public Gui (Client clientGui) {
		this.clientGui = clientGui;
		this.clientGui.setGui(this);
		datiGui = new DatiGui(this);
		mg = new MappaGui(this,datiGui);
		this.maxIndiceDinosauri = 1;
	}

	/**
	 * Metodo per ottenere un riferimento al Timer.
	 * @return Un Timer rappresentante il conteggio del tempo per i turni.
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Metodo per disattivare le azioni nell'inferfaccia grafica.
	 */
	public void disattivaAzioniGui() {
		this.cresci.setEnabled(false);
		this.deponi.setEnabled(false);
		this.prossimoDinosauro.setEnabled(false);
		this.conferma.setEnabled(false);
		this.passa.setEnabled(false);
		this.mg.disattivaAzioniMappa();
	}

	/**
	 * Metodo per disattivare le azioni nell'inferfaccia grafica.
	 */
	public void attivaAzioniGui() {
		this.timer.cancel();
		this.cresci.setEnabled(true);
		this.deponi.setEnabled(true);
		this.prossimoDinosauro.setEnabled(true);
		this.mg.attivaAzioniMappa();
		this.indiceDino=0;
		this.avviaTimer();
	}

	/**
	 * Metodo per avviare il timer di 2 minuti.
	 */
	private void avvioSecondoTimer() {
		//credo un nuovo timer da 2 minuti
		timer = new Timer();
		TimerTask task = new TimerClient(this);
		timer.schedule(task, 2 * 60 * 1000);
	}


	/**
	 * Metodo per avviare il primo timer di 30 secondi.
	 */
	private void avviaTimer() {
		TimerTask task = new TimerClient(this);
		timer = new Timer();
		timer.schedule(task, 30 * 1000);
	}


	/**
	 * Metodo richiamato dall'AscoltatoreCambioTurno per ottenere il turno dal server e rinizializzare la grafica.
	 */
	public void ottieniIlTurno() {
		conferma.setEnabled(true);
		passa.setEnabled(true);
		String idDino = this.getIdDinosauro(indiceDino);
		mg.aggiornaStato(idDino);
		try {
			this.getClientGui().vistaLocale(idDino);
			mg.applicaVisiblita(this.getClientGui().getRichiesta());
			mg.applicaRaggiungibilita();
			this.avviaTimer();
		} catch (IOException ecc) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ecc) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
	}

	/**
	 * Metodo per aggiornare la lista dei Dinsaorui e memorizzare in una variabile locale "listaDinosauri" 
	 * che viene usata da altri metodi.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void aggiornaListaDinosauri() throws IOException, InterruptedException {
		this.getClientGui().listaDinosauri();
		this.listaDinosauri = this.getClientGui().getRichiesta();
	}

	/**
	 * Metodo che si occupa di preparare i dati per poi inizializzare la grafica.
	 * @param user String che rappresenta il nome dell'utente al momento del login.
	 * @param password String che rappresenta la password dell'utente al momento del login.
	 */
	public void preparaDati(String user, String password) {
		try {
			this.caricaMappa();

			this.aggiornaListaDinosauri();
			String idDinosauro = this.listaDinosauri.split(",")[1];

			this.getClientGui().statoDinosauro(idDinosauro);
			int rigaDino = Integer.parseInt(this.getClientGui().getRichiesta().split(",")[4].replace("{", ""));
			int colonnaDino = Integer.parseInt(this.getClientGui().getRichiesta().split(",")[5].replace("}", ""));
			this.inizializzaGrafica();
			mg.setScrollBar(rigaDino, colonnaDino);
		} catch (IOException ecc) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ecc) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
	}

	/**
	 * Metodo per impostare la mappa.
	 * @return Una String rappresentante la vista locale.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String impostaMappa() throws IOException, InterruptedException {
		this.getClientGui().vistaLocale(this.getIdDinosauro(indiceDino));
		String answer = this.getClientGui().getRichiesta();

		this.mappaPanel= mg.creaMappa(mg.creaMappaVisibilita(answer));
		this.setMappaGui(mg.getMappaGui());
		return answer;
	}

	/**
	 * Metodo per ottenere lo stato del Dinosauro.
	 * @return Una String rappresentante lo stato del Dinosauro.
	 */
	public String ottieniStatoDinosauro() {
		String idDinosauro = this.getIdDinosauro(this.getIndiceDino());
		try {
			this.getClientGui().statoDinosauro(idDinosauro);
		} catch (IOException ecc) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ecc) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
		return this.getClientGui().getRichiesta();
	}

	/**
	 * Metodo che inizializza la grafica, creando un frame ed aggiungendovi i vari JPanel e menu.
	 */
	private void inizializzaGrafica() {
		frame = new JFrame("Isola dei dinosauri BETA1");
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

		this.disattivaAzioniGui();
		this.indiceDino=0;
		this.ottieniIlTurno();

		frame.addWindowListener(
				new WindowListener() {
					@Override
					public void windowClosing(WindowEvent arg0) {
						try {
							clientGui.uscitaPartita();
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
							clientGui.logout();
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
		String idDinosauro=null;
		try {
			this.aggiornaListaDinosauri();
			idDinosauro = this.listaDinosauri.split(",")[1];
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException ecc) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
		
		datiPanel = datiGui.creaDati(idDinosauro);
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2,1));
		infoPanel.add(datiPanel);
		infoPanel.add(this.azioni());
		return infoPanel;
	}

	/**
	 * Metodo per caricare la Mappa di gioco nel client, ricevendola dal server (quella generale).
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void caricaMappa() throws IOException, InterruptedException {
		this.getClientGui().mappaGenerale();
		String risposta = this.getClientGui().getRichiesta();

		if(risposta.contains("@mappaGenerale")) {
			int maxRighe = Integer.parseInt(risposta.split(",")[1].replace("{", ""));
			int maxColonne = Integer.parseInt(risposta.split(",")[2].replace("}", ""));
			this.mappaRicevuta = new String[maxRighe][maxColonne];
			String mappa = risposta.replace("@mappaGenerale"+","+"{"+maxRighe+","+maxColonne+"}"+",", "");
			mappa = mappa.replace("[", "");
			mappa = mappa.replace("]", "");
			String[] riga = mappa.split(";");

			for(int i=maxRighe-1;i>=0;i--) {
				for(int j=0;j<maxColonne;j++) {
					this.mappaRicevuta[i][j] = (riga[i].charAt(j)) + "";
				}
			}
		}
	}

	/**
	 * Metodo per ottenere la classifica dal server.
	 */
	public void ottieniClassifica() {
		try {
			this.getClientGui().classifica();
			this.getClientGui().getRichiesta();
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
		panelAzioni.setLayout(new GridLayout(9,1));
		JLabel titolo = new JLabel("Azioni");
		cresci = new JButton("Cresci");
		deponi = new JButton("Deponi");

		JPanel selezionePanel = new JPanel(new GridLayout(1,2));
		selezionePanel.add(new JLabel("   Seleziona: "));
		selezionePanel.add(sceltaDinosauro);

		prossimoDinosauro = new JButton("Prossimo dinosauro");

		conferma = new JButton("Conferma");
		passa = new JButton("Passa");

		panelAzioni.add(titolo);
		panelAzioni.add(cresci);
		panelAzioni.add(deponi);
		panelAzioni.add(new JLabel());
		panelAzioni.add(prossimoDinosauro);
		panelAzioni.add(new JLabel());
		panelAzioni.add(conferma);
		panelAzioni.add(passa);



		conferma.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						timer.cancel();
						try {
							getClientGui().confermaTurno();
							attivaAzioniGui();
							conferma.setEnabled(false);
							passa.setEnabled(false);
							avvioSecondoTimer();
							aggiornaListaDinosauri();
						} catch (IOException ec) {
							JOptionPane.showMessageDialog(null,"IOException");
						} catch (InterruptedException ec) {
							JOptionPane.showMessageDialog(null,"InterruptedException");
						}
					}
				}
		);

		passa.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						timer.cancel();
						try {
							getClientGui().passaTurno();
							disattivaAzioniGui();
						} catch (IOException ec) {
							JOptionPane.showMessageDialog(null,"IOException");
						} catch (InterruptedException ec) {
							JOptionPane.showMessageDialog(null,"InterruptedException");
						}
						conferma.setEnabled(false);
						passa.setEnabled(false);
					}
				}
		);

		prossimoDinosauro.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						prossimoDinosauro();
					}
				}
		);
		
		cresci.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(!azione[getIndiceDino()]) {
							try {
								String idDinosauro = getIdDinosauro(indiceDino);
								getClientGui().crescitaDinosauro(idDinosauro);
								String risposta = getClientGui().getRichiesta();
								if(risposta.equals("@ok")) {
									mg.resetToolTip();
									getClientGui().vistaLocale(idDinosauro);
									mg.applicaVisiblita(getClientGui().getRichiesta());
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
								azione[0] = true;
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
						if(!azione[getIndiceDino()]) { 
							try {
								String idDinosauro = getIdDinosauro(indiceDino);
								getClientGui().deponiUovo(idDinosauro);

								azione[0] = true;
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

	/**
	 * Metodo per ottenere l'id del Dinsoauro.
	 * @param indiceDino int rappresentanta il numero del Dinosauro per cui si vuole avere l'ID.
	 * @return Una String rappresentante l'id del Dinosauro.
	 */
	public String getIdDinosauro(int indiceDino) {
		String risposta = this.listaDinosauri;
		risposta = risposta.replace("@listaDinosauri,", "");
		String[] dinosauri = risposta.split(",");
		return dinosauri[indiceDino];
	}

	/**
	 * Metodo che reimposta le varibili azione e movimento per gestire i limiti sulle operazioni
	 * svolta dal giocatore quando ottiene il turno. Cioe' puo' eseguire solo un movimento e/o
	 * una azione di depozione o crescita.
	 */
	public void resetAzioniEMovimenti() {
		//ottengo la lista dei dinosauri del giocatore
		for(int i=0;i<5;i++) {
			this.movimento[i] = false;
			this.azione[i] = false;
		}
	}

	/**
	 * Metodo per selezionare il Dinosauro successivo. Se e' gia' l'ultimo procede con invio del messaggio PassaTurno
	 * al server e a reimpostare la gui del client.
	 */
	private void prossimoDinosauro(){

		//metodo per cambiare dinosauro e automaticamente setta a true (cioe' azione  e movimento eseguito) i 2 array
		String[] dinosauri = this.listaDinosauri.replace("@listaDinosauri,","").split(",");
		this.maxIndiceDinosauri = dinosauri.length;

		mg.resetToolTip();

		if(indiceDino + 1 < maxIndiceDinosauri) {
			this.movimento[this.indiceDino] = true;
			this.azione[this.indiceDino] = true;
			this.indiceDino++;
			String idDino = this.getIdDinosauro(indiceDino);
			mg.aggiornaStato(idDino);
		} else {
			//il turno finisce per forza e viene inviato al server il messaggio

			this.indiceDino = 0;
			for(int i=0;i<maxIndiceDinosauri;i++) {
				this.movimento[i] = false;
				this.azione[i] = false;
			}
			this.disattivaAzioniGui();
			mg.disattivaAzioniMappa();

			//invia messaggio per passare il turno
			try {
				this.getClientGui().passaTurno();
				timer.cancel();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"IOException");
			} catch (InterruptedException ecc) {
				JOptionPane.showMessageDialog(null,"InterruptedException");
			}
		}
	}

	/**
	 * Metodo per verificare il turno, cioe' se movimento e azione sono true viene richiamato automaticamete prossimoDinosauro.
	 * @param indiceDino
	 */
	public void verificaTurno(int indiceDino) {
		if(this.movimento[indiceDino] && this.azione[indiceDino]) {
			this.prossimoDinosauro();
		}
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
	public Client getClientGui() {
		return clientGui;
	}

	/**
	 * @return Un array bidimensionale di String rappresentante la mappa ricevuta dal server.
	 */
	public String[][] getMappaRicevuta() {
		return mappaRicevuta;
	}

	/**
	 * @param movimento array di 5 boolean per impostare che il Dinosauro ha eseguito il movimento.
	 */
	public void setMovimento(boolean[] movimento) {
		this.movimento = movimento.clone();
	}

	/**
	 * @return Un array di 5 boolean per indicare se il Dinosauro i-esimo ha compiuto un'azione di movimento.
	 */
	public boolean[] getMovimento() {
		return movimento;
	}
}