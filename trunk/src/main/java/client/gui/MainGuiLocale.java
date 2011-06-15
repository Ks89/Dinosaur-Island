package client.gui;

/**
 *	Classe main che si occupa di lanciare l'interfaccia grafica sul Client.
 */
class MainGuiLocale {

	/**
	 * Metodo main per eseguire la grafica il locale SOLO per scopi di test
	 */
	public static void main (String[] args) {		
		GuiLocale gui = new GuiLocale();
		LoginGuiLocale loginGui = new LoginGuiLocale(gui);
		loginGui.aggiuntaGiocatore();
	}
}
