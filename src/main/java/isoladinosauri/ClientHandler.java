package isoladinosauri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
//import java.util.Locale;
import java.util.StringTokenizer;
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Vegetale;

import java.util.Scanner;

public class ClientHandler extends Thread {

	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
//		date = new Date();
//		dateFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ITALY);
	}

	public void run() {
		StringTokenizer st = null;
		
//		Isola i;
//		Partita p = new Partita(i);
//		Turno t = new Turno(p);
//		Classifica c = new Classifica(p);
//		p.setTurnoCorrente(t);

		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			while (true) {
				String request = bufferedReader.readLine();
				st = new StringTokenizer(request,",");
				String comando = st.nextToken();

				if (comando == null) {
					System.out.println("Client closed connection.");
					break;
				} else if (comando.equals("@creaUtente")) {

					String nickname="";
					String password="";

					if(st.hasMoreTokens()){
						nickname = st.nextToken();
					}
					else { 
						bufferedWriter.write("errore! parametro 'user' mancante");
					}
					if(st.hasMoreTokens()){
						password = st.nextToken();
						bufferedWriter.write("@ok");
					}
					else {
						bufferedWriter.write("errore! parametro 'pass' mancante");
					}

					bufferedWriter.write("nome= "+nickname+", pass= "+password);
					Utente utente;
//					Giocatore giocatore =  new Giocatore(p, nickname, password, turnoCorrente, nomeSpecie, tipoDinosauro);
//					c.aggiungiTuplaClassifica(giocatore);


				}
				else if (comando.equals("@Login")) {
					
				}
				else
					bufferedWriter.write("@unknownCommand");

				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		} catch (IOException e) {
			System.out.println("Error in the connection with the client.");
		}
	}
}
