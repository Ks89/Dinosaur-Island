package client;

import isoladinosauri.Cella;
import isoladinosauri.Classifica;
import isoladinosauri.Giocatore;
import isoladinosauri.Isola;
import isoladinosauri.Partita;
import isoladinosauri.Turno;
import isoladinosauri.Utente;
import isoladinosauri.modellodati.Dinosauro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
	private JLabel posRis;	

	Partita partita;
	Turno t;
	Isola isola;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void inizializzaGrafica(Cella[][] mappa) {
		this.isola = new Isola(mappa);
		this.partita = new Partita(isola);
		this.t = new Turno(partita);
		partita.setTurnoCorrente(t);
		
		JFrame frame = new JFrame("Isola dei dinosauri BETA1");
		frame.setLayout(new FlowLayout()); //imposto il Layout a griglia
		frame.setMinimumSize(new Dimension(1025,637));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		JPanel infoPanel = this.creaInfo();
		JPanel mappaPanel= this.creaMappa(mappa);
		frame.add(infoPanel);
		frame.add(mappaPanel);
		frame.pack();
		frame.setVisible(true);
		this.evidenziaRagg(mappa);
	}

	public JPanel creaIntestazione() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,1));
		JLabel label1 = new JLabel("Isola Dinosauri");
		JLabel label2 = new JLabel("766172,7xxxxx");
		panel.add(label1);
		panel.add(label2);
		return panel;
	}

	private JPanel creaInfo() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(this.creaIntestazione(),BorderLayout.NORTH);
		panel.add(this.creaDati(),BorderLayout.CENTER);
		return panel;
	}

	private JPanel creaDati() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(14,2)); //imposto il Layout a griglia

		JLabel nomeGiocatore = new JLabel("Nome: ");
		panel.add(nomeGiocatore);
		JLabel nomeGiocatoreRis = new JLabel("1");
		panel.add(nomeGiocatoreRis);

		JLabel nomeSpecie = new JLabel("Specie: ");
		panel.add(nomeSpecie);
		JLabel nomeSpecieRis = new JLabel("ds");
		panel.add(nomeSpecieRis);

		JLabel eta = new JLabel("Eta: ");
		panel.add(eta);
		JLabel etaRis = new JLabel("3");
		panel.add(etaRis);

		JLabel tNascitaGiocatore = new JLabel("Nascita: ");
		panel.add(tNascitaGiocatore);
		JLabel tNascitaGiocatoreRis = new JLabel("4");
		panel.add(tNascitaGiocatoreRis);

		JLabel dinosauri = new JLabel("Dinosauri: ");
		panel.add(dinosauri);
		JLabel dinosauriRis = new JLabel("11,12");
		panel.add(dinosauriRis);

		JLabel uova = new JLabel("Uova: ");
		panel.add(uova);
		JLabel uovaRis = new JLabel("23-12");
		panel.add(uovaRis);

		JLabel idDinosauro = new JLabel("Id: ");
		panel.add(idDinosauro);
		idDinosauroRis = new JLabel("11,12,13");
		panel.add(idDinosauroRis);

		JLabel dimensione = new JLabel("Dim: ");
		panel.add(dimensione);
		dimensioneRis = new JLabel("4");
		panel.add(dimensioneRis);

		JLabel forza = new JLabel("Forza: ");
		panel.add(forza);
		forzaRis = new JLabel("1");
		panel.add(forzaRis);

		JLabel energia = new JLabel("Energia: ");
		panel.add(energia);
		energiaRis = new JLabel("5000");
		panel.add(energiaRis);

		JLabel energiaMax = new JLabel("EnergiaMax: ");
		panel.add(energiaMax);
		energiaMaxRis = new JLabel("5000");
		panel.add(energiaMaxRis);

		JLabel tNascitaDino = new JLabel("Nascita: ");
		panel.add(tNascitaDino);
		tNascitaDinoRis = new JLabel("2");
		panel.add(tNascitaDinoRis);

		JLabel pos = new JLabel("Pos: ");
		panel.add(pos);
		posRis = new JLabel("12,34");
		panel.add(posRis);

		return panel;
	}

	private JPanel creaMappa(Cella[][] mappa) {

		Cella cella;
		//crea la finestra
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(41,41)); //imposto il Layout a griglia
		panel.setMinimumSize(new Dimension(1025,637));

		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				cella = mappa[i][j];
				mappaGui[i][j] = new JButton ();
				mappaGui[i][j].setText("     ");
				//				mappaGui[i][j].setText(i + "-" + j);
				mappaGui[i][j].setFont(new Font("Brush Script MT",0, 4 ));
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
			panel.add(label);

			//aggiungo le 40 celle che costituiscono la riga una in fila
			//all'altra e dopo passo alla riga sotto col for ("i")
			for(int j=0;j<MAX;j++) {
				panel.add(mappaGui[i][j]);
			}
		}

		//creo una label vuota e la inserisco nell'estremo inferiore a sx della mappa
		//dove non c deve andare nessun numero di riga/colonna
		JLabel label = new JLabel();
		label.setText(" ");
		label.setVisible(true);
		panel.add(label);

		//inserisco le label per le colonne in basso
		for(int i=0;i<MAX;i++) {
			label = new JLabel();
			label.setText("  " + i);
			label.setVisible(true);
			panel.add(label);
		}

		panel.setVisible(true);
		return panel;

	}


	public void evidenziaRagg (Cella[][] mappa) {

		Utente utente = new Utente("1","1");
		
		Classifica c = new Classifica(partita);
		Giocatore giocatore = new Giocatore(partita, utente, 0, "1", "carnivoro");
		c.aggiungiTuplaClassifica(giocatore);

		System.out.println(giocatore.getDinosauri().get(0).getColonna());
		Dinosauro dino = giocatore.getDinosauri().get(0);

		this.idDinosauroRis.setText(dino.getId());
		this.dimensioneRis.setText((dino.getEnergiaMax()/1000) + "");
		this.energiaRis.setText(dino.getEnergia() + "");
		this.energiaMaxRis.setText(dino.getEnergiaMax() + "");
		this.posRis.setText(dino.getRiga() + "," + dino.getColonna());
		this.forzaRis.setText("...");
		this.tNascitaDinoRis.setText(dino.getTurnoNascita() + "");
		mappaGui[dino.getRiga()][dino.getColonna()].setBackground(Color.YELLOW);


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
			
			System.out.println(rigaClic + "-" + colonnaClic);			
			int[][] raggiungibile = t.ottieniRaggiungibilita(rigaClic, colonnaClic);
			int[] coordinate = trovaDinosauro(raggiungibile);
			//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
			int inizioRiga = rigaClic - coordinate[0];
			int inizioColonna = colonnaClic - coordinate[1];
			int fineRiga = rigaClic + (raggiungibile.length - coordinate[0] - 1);
			int fineColonna = colonnaClic + (raggiungibile[0].length - coordinate[1] - 1);

			for(int i=MAX-1;i>=0;i--) {
				for(int j=0;j<MAX;j++) {
					if((i>=inizioRiga && i<=fineRiga) && (j>=inizioColonna && j<=fineColonna))  {
						if((raggiungibile[i - inizioRiga][j - inizioColonna]!=9) &&
								(raggiungibile[i - inizioRiga][j - inizioColonna]!=8)) {
							mappaGui[i][j].setBackground(Color.WHITE);
						} else {
							if(raggiungibile[i - inizioRiga][j - inizioColonna]==0) {
								mappaGui[i][j].setBackground(Color.YELLOW);
							}
						}
					}
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			JButton pulsante = (JButton)e.getSource();
			pulsante.setBorder(BorderFactory.createRaisedBevelBorder());		
		}
		public void mouseExited(MouseEvent e) {
			JButton pulsante = (JButton)e.getSource();
			pulsante.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		}

		public void mousePressed(MouseEvent e) {
			JButton pulsante = (JButton)e.getSource();
			pulsante.setBackground(Color.GREEN);
		}

		public void mouseReleased(MouseEvent e) {
			JButton pulsante = (JButton)e.getSource();
			pulsante.setBackground(Color.YELLOW);
		}
	}

	//	class ButtonHandler implements ActionListener{
	//
	//		public void actionPerformed(ActionEvent e) {
	//			JButton pressed = (JButton)e.getSource();
	//			pressed.setBackground(Color.RED);
	//		}
	//
	//	}

}




