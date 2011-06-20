package server.logica;
//package isoladinosauri;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.Socket;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//import gestioneeccezioni.CrescitaException;
//import gestioneeccezioni.DeposizioneException;
//import gestioneeccezioni.MovimentoException;
//import isoladinosauri.modellodati.Carnivoro;
//import isoladinosauri.modellodati.Carogna;
//import isoladinosauri.modellodati.Dinosauro;
//import isoladinosauri.modellodati.Erbivoro;
//import isoladinosauri.modellodati.Vegetale;
//import isoladinosauri.timer.TimerServer;
//import isoladinosauri.GestioneServer;
//
//
///**
// * Classe che si occupa del protocollo di comunicazione tra Server e Client.
// */
//public class TurnoHandler extends Thread {
//
//	private Socket socket;
//	private ClientHandler ch;
//	private static final int MAX = 40;
//	private static final String ERRORE = "Errore lettura file";
//	private Partita partita;
//	private GestioneServer gestioneGiocatori;
//	private Classifica classifica;
//	private Timer timer;
//	private int indiceGiocatore = 0;
//
//	//variabili usate soltanto per l'ultimo dinosauro di ogni giocatore
//	private boolean eseguitaMossa = false;
//	private boolean eseguitaAzione = false;
//
//
////	//TODO metodo chiamato quando il client riceve il messaggio in broadcast di cambiaTurno
////	private void avviaTimerAzioni() {
////		TimerTask task = new TimerServer(this);
////		timer = new Timer();
////		timer.schedule(task, 2 * 60 * 1000);
////	}
//
//
//	
//
//
//
//	/**
//	 * Costruttore della classe ClientHandler che inizializza gli attributi socket, partita, gestioneGiocatori e classifica.
//	 * @param socket Riferimento al Socket.
//	 * @param partita Riferimento alla Partita.
//	 * @param gestioneGiocatori Riferimento a GestioneGiocatori contenenti le liste di utenti online e le razze create.
//	 * @param classifica Classifica dei giocatori.
//	 */
//	public TurnoHandler(Socket socket, ClientHandler ch,Partita partita, GestioneServer gestioneGiocatori,Classifica classifica) {
//		this.socket = socket;
//		this.partita = partita;
//		this.gestioneGiocatori = gestioneGiocatori;
//		this.classifica = classifica;
//		this.ch = ch;
//	}
//
//	
//
//	public void run() {
//		String comando = null;
//		String domanda;
//
//		try {
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//			while (true) {
//				String richiesta = bufferedReader.readLine();
//				if(richiesta!=null) {
//					comando = richiesta.split(",")[0];
//				}
//				if (comando.equals("@confermaTurno")) {
////					domanda = confermaTurno(richiesta);
////					bufferedWriter.write(domanda);
//				} else {
//					bufferedWriter.write("@unknownCommand");
//				}
//				bufferedWriter.newLine();
//				bufferedWriter.flush();
//			}
//		}
//		catch (IOException e) {
//			System.out.println("Errore durante la connessione col Client.");
//		}
//	}
//}