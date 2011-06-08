package isoladinosauri;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import isoladinosauri.ServerMultiClient;
import isoladinosauri.ClientHandler;


public class ServerMultiClient {

	private int port;
	private Partita partita;

	public ServerMultiClient(int port) {
		this.port = port;
		GenerazioneMappa gm = new GenerazioneMappa();
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle = cm.caricaMappa(gm.creaMappaCasuale());
		this.partita = new Partita(new Isola(mappaCelle));
		Turno t = new Turno(partita);
		Classifica c = new Classifica(partita);
		partita.setTurnoCorrente(t);
	}

	public void runServer() throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server started. Waiting for connection...");
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("Client connected, creating new client handler.");
				new ClientHandler(socket, partita).start();
			} catch (IOException e) {
				System.out.println("Error while waiting for a connection.");
			}
			System.out.println("Waiting for a new connection...");
		}
	}
	public static void main(String[] args) {
		ServerMultiClient serverMultiClient = new ServerMultiClient(1234);
		try {
			serverMultiClient.runServer();
		} catch (IOException e) {
			System.out.println("Error opening the socket.");
			System.out.println("Terminating.");
		}

	}

}
