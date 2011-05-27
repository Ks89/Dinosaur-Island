package client;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
//for debug
//import java.util.Enumeration;

/**
 * ToolBarEditor.java is a toolbar implementation
 * 
 * @author		Andrea 	Manneschi
 * @since		1.0		21/08/2003
 * @version		2.0		16/09/2003	 
 *
 */
public class TestMouseRollover extends JToolBar implements MouseListener {

	public TestMouseRollover() {		
		JFrame frame = new JFrame();
		this.setRollover(true);			

		JButton b1 = new JButton("Bottone1");
		b1.addMouseListener(this);		
		b1.setMargin(new Insets(1, 1, 1, 1));

		JButton b2 = new JButton("Bottone2");
		b2.addMouseListener(this);	
		b2.setMargin(new Insets(1, 1, 1, 1));

		JButton b3 = new JButton("Bottone3");
		b3.addMouseListener(this);
		b3.setMargin(new Insets(1, 1, 1, 1));


		JButton b4 = new JButton("Bottone4");
		b4.addMouseListener(this);
		b4.setMargin(new Insets(1, 1, 1, 1));

	    JPanel panel = new JPanel(new GridLayout(1, 4, 4, 4));
		panel.add(b1);
		panel.add(b2);
		panel.add(b3);
		panel.add(b4);
		this.add(panel);
                
                /*
		this.add(b1);
		this.add(b2);
		this.addSeparator();
		this.add(b3);
		this.addSeparator();
		this.add(b4);
                */

		this.setVisible(true);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
		Object obj = arg0.getSource();
		if (obj instanceof JButton) {
			JButton button = (JButton) obj;
			button.setBorder(BorderFactory.createRaisedBevelBorder());		
		}
	}

	public void mouseExited(MouseEvent arg0) {
		Object obj = arg0.getSource();
		if (obj instanceof JButton) {
			JButton button = (JButton) obj;
			button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		}		
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {

	}

	public static void main (String[] args) {
		new TestMouseRollover();
	}

}
