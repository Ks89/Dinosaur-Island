/*
Copyright 2011-2015 Stefano Cappa
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package client.locale;


import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Classe che mostra l'interfaccia grafica di login e/o registrazione al gioco. Inoltre, permette
 * di accedere alla Partita inserendo il nome della specie e il tipo di dinosauro scelto.
 */
public class LoginGuiLocale { 
	private GuiLocale gui;
	private static JFrame frameGiocatore = new JFrame("Aggiunta giocatore");
	private static CardLayout cardLayout = new CardLayout();
	private static Container contentPane = frameGiocatore.getContentPane();

	private JTextField user;
	private JTextField password;
	private JTextField specie;
	private boolean tipo;
	private JRadioButton carnivoroRButton;
	private JRadioButton erbivoroRButton;
	private JButton accedi;
	

	/**
	 * Costruttore che viene inizializzato con la classe Gui.
	 * @param gui GuiLocale caricata subito dopo la fase di login.
	 */
	public LoginGuiLocale(GuiLocale gui) {
		this.gui = gui;
		carnivoroRButton = new JRadioButton ("carnivoro",false);
		erbivoroRButton = new JRadioButton ("erbivoro",false);
	}


	/**
	 * Metodo per chiamare l'interfaccia grafica di Login e/o registrazione dell'utente.
	 */
	public void aggiuntaGiocatore() {
		
		frameGiocatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameGiocatore.setMinimumSize(new Dimension(300,200));
		frameGiocatore.setResizable(false);

		JPanel homePanel = new JPanel(new GridLayout(3,2));
		JLabel userLabel = new JLabel("   User");
		user = new JTextField();
		JLabel passwordLabel = new JLabel("   Password");
		password = new JTextField();
		JButton login = createButton("Login");
		JButton registrati = new JButton("Registrati");
		homePanel.add(userLabel);
		homePanel.add(user);
		homePanel.add(passwordLabel);
		homePanel.add(password);
		homePanel.add(registrati);
		homePanel.add(login);

		JPanel loginPanel = new JPanel(new GridLayout(3,2));
		JLabel  specieLabel = new JLabel("   Specie");
		specie = new JTextField();
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(carnivoroRButton);
		radioGroup.add(erbivoroRButton);
		carnivoroRButton.addItemListener(new RadioButtonHandler());
		erbivoroRButton.addItemListener(new RadioButtonHandler());
		
		JButton home = createButton("Indietro");
		accedi = new JButton("Accedi");
		accedi.setEnabled(false);
		
		loginPanel.add(specieLabel);
		loginPanel.add(specie);
		loginPanel.add(carnivoroRButton);
		loginPanel.add(erbivoroRButton);
		loginPanel.add(home);
		loginPanel.add(accedi);

		contentPane.setLayout(cardLayout);
		contentPane.add(homePanel, "Indietro");
		contentPane.add(loginPanel, "Login");

		frameGiocatore.pack();
		frameGiocatore.setVisible(true);

		accedi.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						if(accedi.isEnabled()) {
							frameGiocatore.dispose();
							gui.setGiocatore(user.getText(), password.getText(), specie.getText(), tipo);
						}
					}
				}
		);
		registrati.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						//inserisce l'utente nella lista di quelli registrati sul server, 
						//ovviamente non serve implementarlo il locale
					}
				}
		);
	}
	
	/**
	 * Classe per gestire gli eventi sui RadioButton (erbivoro e carnivoro).
	 */
	private class RadioButtonHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			accedi.setEnabled(true);
			if(event.getSource() == carnivoroRButton) {
				tipo = true;
			}
			if(event.getSource() == erbivoroRButton) {
				tipo = false;
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
