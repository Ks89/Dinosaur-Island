package client.gui;

/**
 *	Classe main che si occupa di lanciare l'interfaccia grafica sul Client.
 */
class MainGui {

	public static void main (String[] args) {		
		Gui gui = new Gui();
		LoginGui loginGui = new LoginGui(gui);
		loginGui.aggiuntaGiocatore();
	}
}
