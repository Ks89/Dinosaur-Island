package client;

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
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Classe che inizializza il JScrollPane contenente la mappa di JButton.
 * Include anche la gestione dei clic sui pulsanti per eseguire il movimento
 * del Dinosauro (chiamando i metodi opportuni).
 */
public class MappaGui {

	private static int MAX = 40;

	private JButton[][] mappaGui;
	private Gui gui;
	private int indiceDino;
	private JScrollBar verticalScrollBar;
	private JScrollBar horizontalScrollBar;

	/**
	 * Costruttore che inzializza la mappa di JButton e la
	 * classe imposta il riferimento alla classe Gui.
	 * @param gui Riferimento all'oggetto Gui, necessario per richiamare molti dei metodi per
	 * 			il movimento e la raggiungibilita.
	 */
	public MappaGui(Gui gui, DatiGui dg) {
		mappaGui = new JButton[MAX][MAX];
		this.gui = gui;
	}

	/**
	 * Metodo per creare il JScrollPane della mappa e le celle contenute in esso.
	 * @param mappa un array bidimensionale di Celle per leggere la mappa generata in Isola.
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
//		Icon erbcarIcona = new ImageIcon(this.getClass().getResource("/erb-car.png"));
//		Icon erbvegIcona = new ImageIcon(this.getClass().getResource("/erb-veg.png"));
//		Icon carncarIcona = new ImageIcon(this.getClass().getResource("/carn-car.png"));
//		Icon carnvegIcona = new ImageIcon(this.getClass().getResource("/carn-veg.png"));
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
				if(mappaVisibilita[i-rigaOrigine][j-colonnaOrigine].contains("v") || mappaVisibilita[i-rigaOrigine][j-colonnaOrigine].contains("c")
						|| mappaVisibilita[i-rigaOrigine][j-colonnaOrigine].contains("t") || mappaVisibilita[i-rigaOrigine][j-colonnaOrigine].contains("a")) {
					mappaRicevuta[i][j] = mappaVisibilita[i-rigaOrigine][j-colonnaOrigine];
				} else {
					if(mappaVisibilita[i-rigaOrigine][j-colonnaOrigine].contains("d")) {
						System.out.println(" - " + mappaVisibilita[i-rigaOrigine][j-colonnaOrigine]);
						try {
							gui.getClientGui().statoDinosauro("11");
							String risposta = gui.getClientGui().getAnswer().split(",")[3];
							//ora ho aggiunto alla cella i,j col dinosauro il tipo, cio c o e concatenando.
							mappaRicevuta[i][j] = mappaVisibilita[i-rigaOrigine][j-colonnaOrigine].concat(",").concat(risposta);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
	 * Metodo per resettare la raggiungibilita' (contorno delle celle)
	 * rimpostandolo su "grigio chiaro", come predefinito.
	 */
	public void resetRaggiungibilita (){
		for(int i=MAX-1;i>=0;i--) {
			for(int j=0;j<MAX;j++) {
				mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			}
		}
	}

	/**
	 * Metodo per rinizializzare la mappa dopo aver deposto un uovo, in pratica aggiorna
	 * la mappa di gioco andando a verificare dove ci sono nuovi dinosauri nati.
	 * @param carnivoroIcona Icon che rappresenta l'icona del Dinosauro Carnivoro.
	 * @param erbivoroIcona Icon che rappresenta l'icona del Dinosauro Erbivoro.
	 */
//	public void rinizializzaMappa(Icon carnivoroIcona, Icon erbivoroIcona) {
//		for(int i=0;i<40;i++) {
//			for(int j=0;j<40;j++) {
//				if(mappa[i][j] !=null && mappa[i][j].getDinosauro()!=null) {
//					if(mappa[i][j].getDinosauro() instanceof Carnivoro) {
//						mappaGui[i][j].setIcon(carnivoroIcona);
//					} else {
//						if(mappa[i][j].getDinosauro() instanceof Erbivoro) {
//							mappaGui[i][j].setIcon(erbivoroIcona);
//						}
//					}
//				}
//			}
//		}
//	}

	
	/**
	 * Metodo per applicare la raggiungibilita' alla mappa.
	 */
	public void applicaRaggiungibilita () {
		try {
			this.gui.getClientGui().statoDinosauro("11");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String risposta = this.gui.getClientGui().getAnswer();
		
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
					mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.YELLOW,3));
//					if((raggiungibile[i - inizioRiga][j - inizioColonna]!=NONRAGG) &&
//							(raggiungibile[i - inizioRiga][j - inizioColonna]!=ACQUA)) {
//						mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.YELLOW,3));
//					} 
//					if(raggiungibile[i - inizioRiga][j - inizioColonna]==0) {
//						mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.YELLOW,3));
//					}
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
//			JButton pulsante = (JButton)e.getSource();
//			int q, w = 0;
//			int rigaClic = 0, colonnaClic = 0;
//			for(q=0;q<MAX;q++) {
//				for(w=0;w<MAX;w++) {
//					if(pulsante.equals(mappaGui[q][w])) {
//						rigaClic =  q;
//						colonnaClic = w;  
//					}
//				}
//			}
//
//			int[] datiRaggiungibilita = gui.getDatiRaggiungibilita();
//
//			if(mappa[rigaClic][colonnaClic]!=null && rigaClic>=datiRaggiungibilita[0] &&
//					rigaClic <=datiRaggiungibilita[2] && colonnaClic>=datiRaggiungibilita[1] && colonnaClic<=datiRaggiungibilita[3]) {
//				gui.setIndiceDino(indiceDino);
//				gui.eseguiMovimento(rigaClic,colonnaClic);
//				gui.assegnaTurni();
//				setScrollBar(rigaClic,colonnaClic);
//			} else {
//				JOptionPane.showMessageDialog(null, "Errore! Posizione non concessa!");
//			}
//			dg.aggiornaDati(indiceDino, giocatore);
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

	/**
	 * @return Un int che rappresenta l'indice del Dinosauro selezionato.
	 */
	public int getIndiceDino() {
		return indiceDino;
	}

	/**
	 * @param indiceDino int per impostare l'indice del Dinosauro selezionato.
	 */
	public void setIndiceDino(int indiceDino) {
		this.indiceDino = indiceDino;
	}

}
