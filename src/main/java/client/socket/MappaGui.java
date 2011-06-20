package client.socket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Classe che inizializza il JScrollPane contenente la mappa di JButton.
 * Include anche la gestione dei clic sui pulsanti per eseguire il movimento
 * del Dinosauro (chiamando i metodi opportuni).
 */
public class MappaGui {

	private static final int MAX = 40;

	private JButton[][] mappaGui;
	private Gui gui;
	private DatiGui dg;
	private JScrollBar verticalScrollBar;
	private JScrollBar horizontalScrollBar;

	/**
	 * Costruttore che inzializza la mappa di JButton e la
	 * classe imposta il riferimento a Gui e a DatiGui.
	 * @param gui Riferimento all'oggetto Gui, necessario per richiamare molti dei metodi per
	 * 			il movimento e la raggiungibilita.	 
	 * @param dg Riferimento all'oggetto DatiGui, necessario per ottenere aggiornare il riassunto
	 * 			sulla stato del Dinosauro selezionato
	 */
	public MappaGui(Gui gui, DatiGui dg) {
		mappaGui = new JButton[MAX][MAX];
		this.gui = gui;
		this.dg = dg;
	}


	public void disattivaAzioniMappa() {
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				mappaGui[i][j].setEnabled(false);
			}
		}
	}

	public void attivaAzioniMappa() {
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				mappaGui[i][j].setEnabled(true);
			}
		}
	}



	/**
	 * Metodo per creare il JScrollPane della mappa e le celle contenute in esso.
	 * @param mappa un array bidimensionale di String per creare la Mappa.
	 * @return Un JScrollPane contenente la mappa di gioco.
	 */
	public JScrollPane creaMappa(String[][] mappa) {
		JPanel panelMappa = new JPanel();
		panelMappa.setLayout(new GridLayout(41,41));

		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				mappaGui[i][j] = new JButton ();
				mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				mappaGui[i][j].setOpaque(true);
				mappaGui[i][j].addMouseListener(new GestioneMouse());
			}
		}

		for(int i=MAX-1;i>=0;i--) {
			JLabel label = new JLabel();
			label.setText("" + (i));
			label.setVisible(true);
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
		scroll.setVisible(true);
		scroll.setPreferredSize(new Dimension(1025,637));
		verticalScrollBar   = scroll.getVerticalScrollBar();
		horizontalScrollBar = scroll.getHorizontalScrollBar();
		scroll.setWheelScrollingEnabled(false);
		return scroll;
	}

	/**
	 * Metodo per far spostare le ScrollBar e ottenere il Dinosauro sempre al centro della mappa.
	 * @param riga int che rappresenta la riga in cui si trova il Dinosauro.
	 * @param colonna int che rappresenta la colonna in cui si trova il Dinosauro.
	 */
	public void setScrollBar(int riga, int colonna) {
		verticalScrollBar.setValue(4305 - (riga * 103) - 463);
		horizontalScrollBar.setValue((colonna * 158) - 237);
	}

	/**
	 * @param answer
	 * @return
	 */
	public String[][] creaMappaVisibilita (String answer) {
		String[] datiVisibilita = answer.split(",");
		int rigaOrigine = Integer.parseInt(datiVisibilita[1].replace("{", ""));
		int colonnaOrigine = Integer.parseInt(datiVisibilita[2].replace("}", ""));
		int numeroRighe = Integer.parseInt(datiVisibilita[3].replace("{", ""));
		int numeroColonne = Integer.parseInt(datiVisibilita[4].replace("}", ""));
		String mappaVisibilita[][] = new String[numeroRighe][numeroColonne];

		String visibilita = answer.replace("@vistaLocale"+","+"{"+rigaOrigine+","+colonnaOrigine+"}"+","+"{"+numeroRighe+","+numeroColonne+"}"+",", "");

		System.out.println("visibilita: " + visibilita);

		String[] riga = visibilita.split(";");

		for(int i=0;i<numeroRighe;i++) {
			System.out.println(riga[i]);
			for(int j=0;j<numeroColonne;j++) {
				mappaVisibilita[i][j] = riga[i].split("]")[j].replace("[", "");
			}
		}
		System.out.println();
		System.out.println();
		System.out.println("Mappa risultante");
		for(int i=0;i<numeroRighe;i++) {
			for(int j=0;j<numeroColonne;j++) {
				System.out.print(mappaVisibilita[i][j] + "    ");
			}
			System.out.println();
		}
		return mappaVisibilita;
	}


	/**
	 * Metodo per applicare la visibilita alla mappa, cioe' "illuminare" le zone di buio.
	 * @param visibilita String rappresentante la mappa della visiblita ricevuta dal server.
	 */
	public void applicaVisiblita(String visibilita) {
		Icon carnivoroIcona = new ImageIcon(this.getClass().getResource("/carnivoro.png"));
		Icon erbivoroIcona = new ImageIcon(this.getClass().getResource("/erbivoro.png"));
		Icon carognaIcona = new ImageIcon(this.getClass().getResource("/car.png"));
		Icon vegetaleIcona = new ImageIcon(this.getClass().getResource("/veg.png"));
		Icon terraIcona = new ImageIcon(this.getClass().getResource("/terra.jpg"));
		Icon acquaIcona = new ImageIcon(this.getClass().getResource("/acqua.png"));

		String[][] mappaRicevuta = gui.getMappaRicevuta();
		String[][] mappaVisibilita = this.creaMappaVisibilita(visibilita);
		String[] datiVisibilita = visibilita.split(",");

		int rigaOrigine = Integer.parseInt(datiVisibilita[1].replace("{", ""));
		int colonnaOrigine = Integer.parseInt(datiVisibilita[2].replace("}", ""));
		int rigaFine = rigaOrigine + Integer.parseInt(datiVisibilita[3].replace("{", "")) - 1;
		int colonnaFine = colonnaOrigine + Integer.parseInt(datiVisibilita[4].replace("}", "")) - 1;

		System.out.println(rigaOrigine + "," + colonnaOrigine + "," + rigaFine + "," + colonnaFine);

		for(int i=rigaFine;i>=rigaOrigine;i--) {
			for(int j=colonnaOrigine;j<=colonnaFine;j++) {

				String cella = mappaVisibilita[i-rigaOrigine][j-colonnaOrigine];
				String daVisualizzare = new String();
				if(cella.contains("v") || cella.contains("c") || cella.contains("d")) {
					daVisualizzare = mappaVisibilita[i-rigaOrigine][j-colonnaOrigine].split(",")[1];
				}

				//ottengo lo stato della cella e la inserisco ina una String che poi usero' per metterla nel tooltip

				if(cella.contains("v")) {
					mappaRicevuta[i][j] = cella;
					mappaGui[i][j].setToolTipText("Energia Vegetale: " + daVisualizzare);
				} else {
					if (cella.contains("c")) {
						mappaRicevuta[i][j] = cella;
						mappaGui[i][j].setToolTipText("Energia Carogna: " + daVisualizzare);
					} else {
						if((cella.contains("a")) ||
								(cella.contains("t"))) {
							mappaRicevuta[i][j] = cella;
						} else {
							if(cella.contains("d")) {
								System.out.println(" - " + mappaVisibilita[i-rigaOrigine][j-colonnaOrigine]);
								try {
									String idDinosauro = gui.getIdDinosauro(gui.getIndiceDino());
									gui.getClientGui().statoDinosauro(idDinosauro);
									String risposta = gui.getClientGui().getRichiesta().split(",")[3];
									//ora ho aggiunto alla cella i,j col dinosauro il tipo, cioe' c o e concatenando.
									mappaRicevuta[i][j] = cella.concat(",").concat(risposta);
									mappaGui[i][j].setToolTipText("ID Dinosauro: " + daVisualizzare);
								} catch (IOException e) {
									JOptionPane.showMessageDialog(null,"IOException");
								} catch (InterruptedException e) {
									JOptionPane.showMessageDialog(null,"InterruptedException");
								}
							}
						}
					}
				}
			}
		}

		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				mappaGui[i][j].setPreferredSize(new Dimension(158,103));
				mappaGui[i][j].setMinimumSize(new Dimension(158,103));
				mappaGui[i][j].setMaximumSize(new Dimension(158,103));
				if(!(mappaRicevuta[i][j].equals("b"))) {
					if(mappaRicevuta[i][j].contains("a")) {
						mappaGui[i][j].setIcon(acquaIcona);
					} else {
						if(mappaRicevuta[i][j].contains("d")) {
							if(mappaRicevuta[i][j].contains(",c")) {
								mappaGui[i][j].setIcon(carnivoroIcona);
							} else {
								if(mappaRicevuta[i][j].contains("e")) {
									mappaGui[i][j].setIcon(erbivoroIcona);
								}
							}
						} else {
							if(mappaRicevuta[i][j].contains("c")) {
								mappaGui[i][j].setIcon(carognaIcona);
							}
							if(mappaRicevuta[i][j].contains("v")) {
								mappaGui[i][j].setIcon(vegetaleIcona);
							}
							if(mappaRicevuta[i][j].contains("t")) {
								mappaGui[i][j].setIcon(terraIcona);
							}
						}
					}
				} else {
					mappaGui[i][j].setBackground(Color.BLACK);
				}
			}
		}
	}

	/**
	 * Metodo che rimuove tutti i tooltip dalla celle.
	 */
	public void resetToolTip() {
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				mappaGui[i][j].setToolTipText(null);
			}
		}
	}

	
	
	/**
	 * Metodo per applicare la raggiungibilita' alla mappa.
	 */
	public void applicaRaggiungibilita () {
		String risposta = gui.ottieniStatoDinosauro();

		int rigaDino = Integer.parseInt(risposta.split(",")[4].replace("{", ""));
		int colonnaDino = Integer.parseInt(risposta.split(",")[5].replace("}", ""));
		int dimensione = Integer.parseInt(risposta.split(",")[6]);
		int raggio;
		if(dimensione==1) {
			raggio=2;
		} else {
			if(dimensione==2 || dimensione==3) {
				raggio=3;
			} else {
				raggio=4;
			}
		}

		//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
		int inizioRiga = rigaDino - raggio;
		int inizioColonna = colonnaDino - raggio;
		int fineRiga = rigaDino + raggio;
		int fineColonna = colonnaDino + raggio;

		for(int i=MAX-1;i>=0;i--) {
			for(int j=0;j<MAX;j++) {
				if((i>=inizioRiga && i<=fineRiga) && (j>=inizioColonna && j<=fineColonna))  {

					if(!(this.gui.getMappaRicevuta()[i][j].equals("a"))) {
						mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.YELLOW,3));
					} else {
						mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
					}
				} else {
					mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				}
			}
		}
	}


	/**
	 * Metodo per trovare un Dinosauro ("0") all'interno della mappa di raggiungibilita'.
	 * @param raggiungibile Mappa di raggiungibilita'.
	 * @return Un array di 2 elementi: 
	 * 			[0] - riga,
	 * 			[1] - colonna.
	 */
	public int[] trovaDinosauro (int[][] raggiungibile) {
		int j,w;
		int[] uscita = {0,0};
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

	/**
	 * Classe che si occupa di gestire gli eventi di clic del mouse sui JButton della mappa.
	 */
	class GestioneMouse implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			JButton pulsante = (JButton)e.getSource();
			if(pulsante.isEnabled()) {
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
				resetToolTip();

				System.out.println("cliccati" + rigaClic + "," + colonnaClic);
				String idDinosauro = gui.getIdDinosauro(gui.getIndiceDino());
				if(!gui.getMovimento()[gui.getIndiceDino()]) { // TODO indicedino
					try {	
						gui.getClientGui().muoviDinosauro(idDinosauro, rigaClic, colonnaClic);
						String risposta = gui.getClientGui().getRichiesta();
						if(risposta.contains("@ok")) {
							aggiornaStato(idDinosauro);
							gui.getMovimento()[gui.getIndiceDino()] = true; //TODO indiceDino
						}
					} catch (IOException ecc) {
						JOptionPane.showMessageDialog(null,"IOException");
					} catch (InterruptedException ecc) {
						JOptionPane.showMessageDialog(null,"InterruptedException");
					}
				} else {
					//non si puo' eseguire il movimento
					JOptionPane.showMessageDialog(null, "Azione di movimento gia' eseguita!");
				}
			}
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}

	}



	/**
	 * Metodo generale che riceve l'ID del Dinosauro e aggiorna la grafica caricando tutte le sue informazioni
	 * @param idDino String che rappresenta l'identificativo univoco del Dinosauro.
	 */
	public void aggiornaStato(String idDino) {
		try {
			//ricevo nuovamente la mappa con la vista locale				
			gui.getClientGui().vistaLocale(idDino);
			applicaVisiblita(gui.getClientGui().getRichiesta());
			applicaRaggiungibilita();

			gui.getClientGui().statoDinosauro(idDino);
			String[] risposta = gui.getClientGui().getRichiesta().split(",");
			int riga = Integer.parseInt(risposta[4].replace("{",""));
			int colonna = Integer.parseInt(risposta[5].replace("}",""));
			setScrollBar(riga,colonna);

			//aggiorno il panel col riassunto dello stato del dinosauro
			dg.aggiornaDati(idDino);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"IOException");
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null,"InterruptedException");
		}
	}

	/**
	 * @return Un array bidimensionale di JButton che rappresenta la mappa di gioco.
	 */
	public JButton[][] getMappaGui() {
		return mappaGui.clone();
	}

	/**
	 * @param mappaGui un array bidimensionale di JButton per impostare la mappa di gioco.
	 */
	public void setMappaGui(JButton[][] mappaGui) {
		this.mappaGui = mappaGui.clone();
	}

}
