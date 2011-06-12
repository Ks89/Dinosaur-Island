package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * 	Classe che si occupa di creare il Menu del programma all'interno del JFrame.
 */
public class MenuGui {

	private JFrame frame;

	/**
	 * Costruttore della classe MenuGui che riceve il JFrame contiene tutti gli elementi dell'interfaccia.
	 * @param frame JFrame che contiene la grafica dell'applizazione.
	 */
	public MenuGui(JFrame frame) {
		this.frame = frame;
	}
	
	/**
	 * Metodo per generare il Menu
	 * @return Una JMenuBar contenente tutti gli elementi del menu,.
	 */
	public JMenuBar getMenu() {
		JMenu fileMenu = new JMenu("File"); 
		fileMenu.setMnemonic('F'); //imposta il mnemonic ad F

		//crea la voce About
		Icon aboutIcona = new ImageIcon(getClass().getResource("/info_icon.png"));
		JMenuItem aboutItem = new JMenuItem("Info...",aboutIcona);
		aboutItem.setMnemonic('I'); //imposta mnemonic
		fileMenu.add(aboutItem); //aggiunge la voce al menu file
		aboutItem.addActionListener(
				new ActionListener() //classe inner anonima
				{
					public void actionPerformed(ActionEvent event)
					{//mostra un messaggio quando l'untente sceglie about...
						JOptionPane.showMessageDialog(null,"Isola Dei Dinosauri BETA1\nCreato da 766172 e 7xxxxx\n(C) 2011","About",JOptionPane.PLAIN_MESSAGE);
					}
				}
		);
		//crea la voce exit
		Icon uscitaIcona = new ImageIcon(this.getClass().getResource("/Exit_icon.jpg"));
		JMenuItem exitItem = new JMenuItem("Exit",uscitaIcona);
		exitItem.setMnemonic('E');
		fileMenu.add(exitItem);
		exitItem.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						frame.dispose();
					}
				}
		);

		//crea il nuovo menu Operazioni
		JMenu operazMenu = new JMenu("Operazioni");
		operazMenu.setMnemonic('O');
		Icon refreshIcona = new ImageIcon(this.getClass().getResource("/refresh.png"));
		JMenuItem refreshElemento = new JMenuItem("Rigenera Mappa", refreshIcona);
		refreshElemento.setMnemonic('R'); //imposta mnemonic
		operazMenu.add(refreshElemento); //aggiunge la voce al menu Operazioni
		//		refreshItem.addActionListener(
		//				new ActionListener() //classe inner anonima
		//				{
		//					public void actionPerformed(ActionEvent event)
		//					{
		//						//rigenera mappa
		//						mappa = cm.caricaDaFile();
		//						isola = new Isola(mappa);
		//						partita = new Partita(isola);
		//						t = new Turno(partita);
		//						partita.setTurnoCorrente(t);
		//						partita.getGiocatori().clear();
		//						utente = new Utente(user.getText(), password.getText());
		//						giocatore = new Giocatore(partita, turnoPartita, specie.getText(), tipo.getText());
		//						dino = giocatore.getDinosauri().get(indiceDino);
		//						//						mappaPanel= creaMappa(mappa);
		//						//						applicaRaggiungibilita(0);
		//						//						applicaVisibilita(0);
		//						frame.removeAll();
		//						bar.removeAll();
		//						inizializzaGrafica();
		//						//						frame.repaint();
		//
		//						//fare in modo che mappaGui cambi e si aggiorni e poi chiamare un repaint come fatto col JButton cresci
		//
		//					}
		//				}
		//		);
		//crea la voce Salva partita
		operazMenu.addSeparator();
		JMenuItem saveItem = new JMenuItem("Salva partita");
		saveItem.setMnemonic('S'); //imposta mnemonic
		operazMenu.add(saveItem); //aggiunge la voce al menu Operazioni
		saveItem.addActionListener(
				new ActionListener() //classe inner anonima
				{
					public void actionPerformed(ActionEvent event)
					{
						//Inserire salvataggio partita
					}
				}
		);
		JMenuItem loadItem = new JMenuItem("Carica...");
		loadItem.setMnemonic('C'); //imposta mnemoic
		operazMenu.add(loadItem);
		loadItem.addActionListener(
				new ActionListener() //classe inner anonima
				{
					public void actionPerformed(ActionEvent event)
					{
						//carica da file
					}
				}
		);
		//aggiunge il menu File alla barra appena creata
		JMenuBar bar = new JMenuBar();
		bar.add(fileMenu);
		bar.add(operazMenu);
		return bar;
	}
	
}
