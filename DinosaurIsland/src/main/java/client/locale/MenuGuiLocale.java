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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import server.salvataggio.StatoGiocatoreDB;

/**
 * 	Classe che si occupa di creare il Menu del programma all'interno del JFrame.
 */
public class MenuGuiLocale {

	private JFrame frame;

	/**
	 * Costruttore della classe MenuGui che riceve il JFrame contiene tutti gli elementi dell'interfaccia.
	 * @param frame JFrame che contiene la grafica dell'applizazione.
	 */
	public MenuGuiLocale(JFrame frame) {
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
						JOptionPane.showMessageDialog(null,"Isola Dei Dinosauri\nCreato da Stefano Cappa\n(C) 2011","About",JOptionPane.PLAIN_MESSAGE);
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
						//ferma il server di db4o per salvare i giocatori
					    StatoGiocatoreDB.stopDBServer();
						frame.dispose();
					}
				}
		);

		//aggiunge il menu File alla barra appena creata
		JMenuBar bar = new JMenuBar();
		bar.add(fileMenu);
		return bar;
	}
	
}
