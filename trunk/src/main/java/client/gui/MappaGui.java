package client.gui;

import isoladinosauri.Cella;
import isoladinosauri.Giocatore;
import isoladinosauri.Turno;
import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Erbivoro;
import isoladinosauri.modellodati.Vegetale;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	private Cella[][] mappa;
	private Giocatore giocatore;
	private int indiceDino;
	private DatiGui dg;
	private JScrollBar verticalScrollBar;
	private JScrollBar horizontalScrollBar;

	/**
	 * Costruttore che inzializza la mappa di JButton e la
	 * classe imposta il riferimento alla classe Gui.
	 * @param gui Riferimento all'oggetto Gui, necessario per richiamare molti dei metodi per
	 * 			il movimento e la raggiungibilita.
	 */
	public MappaGui(Gui gui, Giocatore giocatore, DatiGui dg) {
		mappaGui = new JButton[MAX][MAX];
		this.gui = gui;
		this.giocatore = giocatore;
		this.dg = dg;
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
	 * Metodo per creare il JScrollPane della mappa e le celle contenute in esso.
	 * @param mappa un array bidimensionale di Celle per leggere la mappa generata in Isola.
	 * @return Un JScrollPane contenente la mappa di gioco.
	 */
	public JScrollPane creaMappa(Cella[][] mappa) {
		this.mappa = mappa.clone();
		//crea la finestra
		JPanel panelMappa = new JPanel();
		panelMappa.setLayout(new GridLayout(41,41)); //imposto il Layout a griglia

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
	 * Metodo per applicare la visibilita alla mappa, cioe' "illuminarla".
	 * @param giocatore riferimento al Giocatore.
	 * @param t riferimento al Turno in corso.
	 */
	public void applicaVisiblita(Giocatore giocatore, Turno t) {

		boolean[][] visibilita = giocatore.getMappaVisibile();

		Icon erb_carIcona = new ImageIcon(this.getClass().getResource("/erb-car.png"));
		Icon erb_vegIcona = new ImageIcon(this.getClass().getResource("/erb-veg.png"));
		Icon carn_carIcona = new ImageIcon(this.getClass().getResource("/carn-car.png"));
		Icon carn_vegIcona = new ImageIcon(this.getClass().getResource("/carn-veg.png"));
		Icon carnivoroIcona = new ImageIcon(this.getClass().getResource("/carnivoro.png"));
		Icon erbivoroIcona = new ImageIcon(this.getClass().getResource("/erbivoro.png"));
		Icon carognaIcona = new ImageIcon(this.getClass().getResource("/car.png"));
		Icon vegetaleIcona = new ImageIcon(this.getClass().getResource("/veg.png"));
		Icon terraIcona = new ImageIcon(this.getClass().getResource("/terra.jpg"));
		Icon acquaIcona = new ImageIcon(this.getClass().getResource("/acqua.png"));

		for(int i=MAX-1;i>=0;i--) {
			for(int j=0;j<MAX;j++) {
				mappaGui[i][j].setPreferredSize(new Dimension(158,103));
				mappaGui[i][j].setMinimumSize(new Dimension(158,103));
				mappaGui[i][j].setMaximumSize(new Dimension(158,103));
				if(visibilita[i][j]) {
					if(mappa[i][j]==null) {
						mappaGui[i][j].setIcon(acquaIcona);
					} else {
						if(mappa[i][j].getDinosauro()!=null) {
							if(mappa[i][j].getDinosauro() instanceof Carnivoro) {
								if(mappa[i][j].getOccupante() instanceof Vegetale) {
									mappaGui[i][j].setIcon(carn_vegIcona);
								} else {
									if(mappa[i][j].getOccupante() instanceof Carogna) {
										mappaGui[i][j].setIcon(carn_carIcona);
									} else {
										mappaGui[i][j].setIcon(carnivoroIcona);
									}
								}
								
							} else {
								if(mappa[i][j].getDinosauro() instanceof Erbivoro) {
									if(mappa[i][j].getOccupante() instanceof Vegetale) {
										mappaGui[i][j].setIcon(erb_vegIcona);
									} else {
										if(mappa[i][j].getOccupante() instanceof Carogna) {
											mappaGui[i][j].setIcon(erb_carIcona);
										} else {
											mappaGui[i][j].setIcon(erbivoroIcona);
										}
									}
								}
							}
						} else {
							if(mappa[i][j].getOccupante() instanceof Carogna) {
								mappaGui[i][j].setIcon(carognaIcona);
							} else {
								if(mappa[i][j].getOccupante() instanceof Vegetale) {
									mappaGui[i][j].setIcon(vegetaleIcona);
								} else {
									mappaGui[i][j].setIcon(terraIcona);
								}
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
	 * Metodo per resettare la raggiungibilita' (contorno rosso delle celle)
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
	 * Metodo per applicare la raggiungibilita' alla mappa.
	 * @param indiceDino int che rappresenta il numero del Dinosauro del Giocatore.
	 * @param giocatore Giocatore a cui e' stato associato il Turno.
	 * @param t Turno corrente di gioco.
	 */
	public void applicaRaggiungibilita (int indiceDino, Giocatore giocatore, Turno t) {
		int rigaDino = giocatore.getDinosauri().get(indiceDino).getRiga();
		int colonnaDino = giocatore.getDinosauri().get(indiceDino).getColonna();
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
						mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.YELLOW,3));
					} 
					if(raggiungibile[i - inizioRiga][j - inizioColonna]==0) {
						mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.YELLOW,3));
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

	/**
	 * Classe che si occupa di gestire gli eventi di clic del mouse sui JButton della mappa.
	 */
	class GestioneMouse implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			JButton pulsante = (JButton)e.getSource();
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

			int[] datiRaggiungibilita = gui.getDatiRaggiungibilita();
			//FIXME
			if(mappa[rigaClic][colonnaClic]!=null && rigaClic>=datiRaggiungibilita[0] &&
					rigaClic <=datiRaggiungibilita[2] && colonnaClic>=datiRaggiungibilita[1] && colonnaClic<=datiRaggiungibilita[3]) {
//				if(rigaClic!=dino.getRiga() || colonnaClic!=dino.getColonna {
					gui.eseguiMovimento(rigaClic,colonnaClic);
					gui.assegnaTurni();
					System.out.println(rigaClic + "," + colonnaClic);
					setScrollBar(rigaClic,colonnaClic);
			} else {
				JOptionPane.showMessageDialog(null, "Errore! Posizione non concessa!");
			}
			dg.aggiornaDati(indiceDino, giocatore);
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

}
