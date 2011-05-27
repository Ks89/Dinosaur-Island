package client;

import isoladinosauri.Cella;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;


public class Gui{

	private final int MAX = 40;
	private JButton cellaGui;
	private JButton[][] mappaGui = new JButton[MAX][MAX];

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void inizializzaGrafica(Cella[][] mappa) {

		Cella cella;
		//crea la finestra
		JFrame frame = new JFrame("Isola dei dinosauri BETA1");
		frame.setLayout(new GridLayout(41,41)); //imposto il Layout a griglia
		frame.setMinimumSize(new Dimension(1025,637));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);	

		for(int i=0;i<MAX;i++) {
			
			JLabel label = new JLabel();
			label.setText("" + (MAX-i-1));
			label.setVisible(true);
			//		label.setBounds(5,i*15,15,15);
			frame.add(label);
			
			for(int j=0;j<MAX;j++) {
				cella = mappa[i][j];
				mappaGui[i][j] = new JButton ();
				mappaGui[i][j].setText("   ");
				mappaGui[i][j].setFont(new Font("Brush Script MT",10, 12 ));
				//quadretto.setBorderPainted(false); //toglie i bordi
				mappaGui[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				mappaGui[i][j].setOpaque(true);
				//				mappaGui[i][j].setBounds((i*25)+25,(j*15)+15,25,15);
				if(cella==null) {
					mappaGui[i][j].setBackground(Color.BLUE);
				} else {
					mappaGui[i][j].setBackground(Color.GRAY);
				}
				cellaGui = mappaGui[i][j];
				cellaGui.addMouseListener(new GestioneMouse());

				frame.add(mappaGui[i][j]);
			}

		}

		for(int i=0;i<MAX;i++) {
			JLabel label = new JLabel();
			label.setText("" + i);
			label.setVisible(true);
			//		label.setBounds(5,i*15,15,15);
			frame.add(label);
		}

		frame.pack();
		frame.setVisible(true);

	}
	class GestioneMouse implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			JButton pulsante = (JButton)e.getSource();
			pulsante.setBackground(Color.RED);
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
		//		
		//		public void actionPerformed(ActionEvent e) {
		//			JButton pressed = (JButton)e.getSource();
		//			pressed.setBackground(Color.RED);
		//			
		//		}
		//		
		//		public void mouseEntered(MouseEvent arg0) {
		//			Object obj = arg0.getSource();
		//			if (obj instanceof JButton) {
		//				JButton button = (JButton) obj;
		//				button.setBorder(BorderFactory.createRaisedBevelBorder());		
		//			}
		//		}



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


