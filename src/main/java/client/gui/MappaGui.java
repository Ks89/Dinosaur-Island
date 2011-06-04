package client.gui;

import isoladinosauri.Cella;
import isoladinosauri.Giocatore;
import isoladinosauri.Turno;
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Vegetale;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
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
		this.mappa = mappa;
		//crea la finestra
		JPanel panelMappa = new JPanel();
		//		panel.setLayout(null);
		panelMappa.setLayout(new GridLayout(41,41)); //imposto il Layout a griglia
		//		panel.setMinimumSize(new Dimension(3025,2637));

		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				mappaGui[i][j] = new JButton ();
				mappaGui[i][j].setText("       ");
				mappaGui[i][j].setFont(new Font("Brush Script MT",0, 80 ));
				mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				mappaGui[i][j].setOpaque(true);
				if(mappa[i][j]==null) {
					mappaGui[i][j].setBackground(Color.BLUE);
				} else {
					mappaGui[i][j].setBackground(Color.GRAY);
				}
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
		verticalScrollBar.setValue(4305 - (riga * 103) - 51 - 206 - 206);
		horizontalScrollBar.setValue((colonna * 158) - 158 - 79);
	}

	/**
	 * Metodo per applicare la visibilita alla mappa, cioe' "illuminarla".
	 * @param giocatore riferimento al Giocatore.
	 * @param t riferimento al Turno in corso.
	 */
	public void applicaVisiblita(Giocatore giocatore, Turno t) {
		boolean[][] visibilita = giocatore.getMappaVisibile();
		
//		Icon vegetaleIcona = new ImageIcon(getClass().getResource("vegetale.png"));
		
		for(int i=MAX-1;i>=0;i--) {
			for(int j=0;j<MAX;j++) {
				if(visibilita[i][j]) {
					if(mappa[i][j]==null) {
						mappaGui[i][j].setBackground(Color.BLUE);
					} else {
						if(mappa[i][j].getDinosauro()!=null) {
							mappaGui[i][j].setBackground(Color.YELLOW);
						} else {
							if(mappa[i][j].getOccupante() instanceof Carogna) {
								mappaGui[i][j].setBackground(Color.PINK);
							} else {
								if(mappa[i][j].getOccupante() instanceof Vegetale) {
									mappaGui[i][j].setBackground(Color.GREEN);
								} else {
									mappaGui[i][j].setBackground(Color.GRAY);
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
						mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.RED,3));
					} 
					if(raggiungibile[i - inizioRiga][j - inizioColonna]==0) {
						mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.RED,3));
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
