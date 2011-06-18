package client;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;


/**
 *	Classe main che si occupa di lanciare il Client.
 */
class MainClient {

	/**
	 * Metodo main per eseguire la grafica del Client
	 */
	public static void main (String[] args) {		
		Client client = new Client("localhost", 1234);
		try {
			client.inizializzaClient();
			Gui gui = new Gui(client);
			new AscoltatoreCambioTurno(gui).start();
			LoginGui loginGui = new LoginGui(gui);
			loginGui.aggiuntaGiocatore();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "UnknownHostException: Host sconosciuto!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IOException: Server non raggiungibile!");
		}	
	}
}
