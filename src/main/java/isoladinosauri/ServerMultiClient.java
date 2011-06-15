package isoladinosauri;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import isoladinosauri.ServerMultiClient;
import isoladinosauri.ClientHandler;


/**
 * Classe per la gestione del Server.
 */
public class ServerMultiClient {

	private int porta;
	private Partita partita;
	private GestioneGiocatori gestioneGiocatori;
	private Classifica classifica;

	
	/**
	 * Costruttore della classe ServerMultiClient che inizializza la mappa, la Partita, la Classifica ed il Turno.
	 * @param porta int che rappresenta la porta di comunicazione.
	 */
	public ServerMultiClient(int porta) {
		this.porta = porta;
//		GenerazioneMappa gm = new GenerazioneMappa();
		CaricamentoMappa cm = new CaricamentoMappa();
		
		Cella[][] mappaCelle = cm.caricaDaFile();
//		Cella[][] mappaCelle = cm.caricaMappa(gm.creaMappaCasuale());
		this.partita = new Partita(new Isola(mappaCelle));
		Turno t = new Turno(partita);
		partita.setTurnoCorrente(t);
		this.gestioneGiocatori = new GestioneGiocatori();
		this.classifica = new Classifica(this.partita);
	}

	
	/**
	 * Metodo per la gestione delle connessioni.
	 * @throws IOException Eccezione duvuta ad un errore nell'apertura di una nuova connessione con un Client.
	 */
	public void runServer() throws IOException {
		ServerSocket serverSocket = new ServerSocket(porta);
		System.out.println("Server avviato. In attesa di connessioni...");
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("Client connesso, creazione di un nuovo client handler.");
				new ClientHandler(socket, partita,gestioneGiocatori,classifica).start();
			} catch (IOException e) {
				System.out.println("Errore durante l'apertura di una connessione.");
			}
			System.out.println("In attesa di una nuova connessione...");
		}
	}
	
	
	/**
	 * Metodo che avvia il Server.
	 */
	public static void main(String[] args) {
		ServerMultiClient serverMultiClient = new ServerMultiClient(1234);
		try {
			serverMultiClient.runServer();
		} catch (IOException e) {
			System.out.println("Errore durante l'apertuta di socket.");
			System.out.println("Terminazione.");
		}

	}

}
