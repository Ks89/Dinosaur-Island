package client.gui;

class MainGui {

	public static void main (String[] args) {		
		Gui gui = new Gui();
		LoginGui loginGui = new LoginGui(gui);
		loginGui.aggiuntaGiocatore();
	}
}
