package client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class TestScroolPane {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		JFrame frameGiocatore = new JFrame("test");
		frameGiocatore.setLayout(new GridLayout(2,1));
		frameGiocatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameGiocatore.setPreferredSize(new Dimension(700,700));
		frameGiocatore.setResizable(false);

		JPanel panelMappa = new JPanel(new GridLayout(40,40));
		panelMappa.setPreferredSize(new Dimension(1000,1000));
		for(int i=0;i<40;i++) {
			for(int j=0;j<40;j++) {
				JButton button = new JButton("hcd");
				panelMappa.add(button);
			}
		}
		panelMappa.setVisible(true);
		
		JScrollPane panel = new JScrollPane(panelMappa);
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(1025,2000));
//		panel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		panel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollBar verticalScrollBar   = panel.getVerticalScrollBar();
		JScrollBar horizontalScrollBar = panel.getHorizontalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMaximum());
		horizontalScrollBar.setValue(horizontalScrollBar.getMaximum());
//		panel.setWheelScrollingEnabled(true);
		
		
		frameGiocatore.add(new JLabel("blabla"));
		frameGiocatore.add(panel);
		frameGiocatore.pack();
		frameGiocatore.setVisible(true);
		
		

	}

}
