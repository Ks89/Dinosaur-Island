package client.socket;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Classe che mostra l'interfaccia grafica di login e/o registrazione al gioco. Inoltre, permette
 * di accedere alla Partita inserendo il nome della specie e il tipo di dinosauro scelto.
 */
public class LoginGui { 

	private static final String ERRORE = "Devi inserire la password e il nome utente";

	private Gui gui;
	private static JFrame frameGiocatore = new JFrame("Aggiunta giocatore");
	private static CardLayout cardLayout = new CardLayout();
	private static Container contentPane = frameGiocatore.getContentPane();

	private JTextField user;
	private JTextField password;
	private JTextField specie;
	private String tipo;
	private JRadioButton carnivoroRButton = new JRadioButton ("carnivoro",false);
	private JRadioButton erbivoroRButton = new JRadioButton ("erbivoro",false);
	private JButton accedi;
	private JButton continua;
	private JButton creaRazza;


	/**
	 * Costruttore che viene inizializzato con la classe Gui.
	 * @param gui Riferimento alla classe Gui.
	 */
	public LoginGui(Gui gui) {
		this.gui = gui;
	}


	/**
	 * Metodo per chiamare l'interfaccia grafica di Login e/o registrazione dell'utente.
	 */
	public void aggiuntaGiocatore() {

		frameGiocatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameGiocatore.setMinimumSize(new Dimension(300,200));
		frameGiocatore.setResizable(false);

		JPanel homePanel = new JPanel(new GridLayout(4,2));
		JLabel userLabel = new JLabel("   User");
		user = new JTextField();
		JLabel passwordLabel = new JLabel("   Password");
		password = new JTextField();
		JButton login = createButton("Login");
		JButton registrati = new JButton("Registrati");
		continua = createButton("Continua");
		continua.setEnabled(false);
		homePanel.add(userLabel);
		homePanel.add(user);
		homePanel.add(passwordLabel);
		homePanel.add(password);
		homePanel.add(registrati);
		homePanel.add(login);
		homePanel.add(new JLabel("   Crea la razza: "));
		homePanel.add(continua);


		JPanel creazionePanel = new JPanel(new GridLayout(3,2));
		JLabel  specieLabel = new JLabel("   Specie");
		specie = new JTextField();

		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(carnivoroRButton);
		radioGroup.add(erbivoroRButton);
		carnivoroRButton.addItemListener(new RadioButtonHandler());
		erbivoroRButton.addItemListener(new RadioButtonHandler());

		JButton home = createButton("Indietro");
		creaRazza = createButton("Accesso");

		creazionePanel.add(specieLabel);
		creazionePanel.add(specie);
		creazionePanel.add(carnivoroRButton);
		creazionePanel.add(erbivoroRButton);
		creazionePanel.add(home);
		creazionePanel.add(creaRazza);

		JPanel accessoPanel = new JPanel(new GridLayout(2,1));
		accedi = new JButton("Accedi alla partita");
		accessoPanel.add(new JLabel("Clicca per accedere"));
		accessoPanel.add(accedi);

		contentPane.setLayout(cardLayout);
		contentPane.add(homePanel, "Indietro");
		contentPane.add(creazionePanel, "Continua");
		contentPane.add(accessoPanel, "Accesso");

		frameGiocatore.pack();
		frameGiocatore.setVisible(true);

		login.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(!(user.getText().isEmpty() || password.getText().isEmpty())) {
							try {
								gui.getClientGui().eseguiLogin(user.getText(), password.getText());
								continua.setEnabled(true);
							} catch (IOException ecc) {
								JOptionPane.showMessageDialog(null,"IOException");
							} catch (InterruptedException ecc) {
								JOptionPane.showMessageDialog(null,"InterruptedException");
							}
						} else {
							JOptionPane.showMessageDialog(null,ERRORE);
							continua.setEnabled(false);
						}
					}
				}
		);

		accedi.addActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(!(user.getText().isEmpty() || password.getText().isEmpty())) {
							try {
								System.out.println(user.getText() +"," +  password.getText());
								gui.getClientGui().accessoPartita(user.getText(), password.getText());			
								frameGiocatore.dispose();	
								gui.preparaDati(user.getText(), password.getText());
							} catch (IOException ecc) {
								JOptionPane.showMessageDialog(null,"IOException");
							} catch (InterruptedException ecc) {
								JOptionPane.showMessageDialog(null,"InterruptedException");
							}	
						} else {
							JOptionPane.showMessageDialog(null,ERRORE);
						}
					}
				}
		);


		creaRazza.addActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(!(user.getText().isEmpty() || password.getText().isEmpty())) {
							try {
								gui.getClientGui().creaRazza(user.getText(), password.getText(),specie.getText(),tipo);
							} catch (IOException ecc) {
								JOptionPane.showMessageDialog(null,"IOException");
							} catch (InterruptedException ecc) {
								JOptionPane.showMessageDialog(null,"InterruptedException");
							}
						} else {
							JOptionPane.showMessageDialog(null,ERRORE);
						}
					}
				}
		);
		registrati.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(!(user.getText().isEmpty() || password.getText().isEmpty())) {
							try {
								gui.getClientGui().creaUtente(user.getText(), password.getText());
							} catch (IOException ecc) {
								JOptionPane.showMessageDialog(null,"IOException");
							} catch (InterruptedException ecc) {
								JOptionPane.showMessageDialog(null,"InterruptedException");
							}
						} else {
							JOptionPane.showMessageDialog(null,ERRORE);
						}
					}
				}
		);
	}

	/**
	 * Classe per gestire gli eventi sui RadioButton, cioe' i pulsanti
	 * circolari per scegliere il tipo di Dinosauro
	 */
	private class RadioButtonHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			accedi.setEnabled(true);
			if(event.getSource() == carnivoroRButton) {
				tipo = "c";
			}
			if(event.getSource() == erbivoroRButton) {
				tipo = "e";
			}
		}
	}

	/**
	 * Metodo che creare i pulsanti associandoci l'azione per scorrere tra i vari Layout del CardLayout.
	 * @param action String che rappresenta il testo che deve avere il pulsante.
	 * @return Un JButton che rappresenta il pulsante appena creato.
	 */
	private static JButton createButton(String action) {
		JButton b = new JButton(action);
		b.setActionCommand(action);
		b.addActionListener(actionHandler);
		return b;
	}

	/**
	 * Metodo per mostrare i layer del CardLayout in base al pulsante premuto.
	 */
	private static ActionListener actionHandler = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(contentPane, e.getActionCommand());
		}
	};
}
