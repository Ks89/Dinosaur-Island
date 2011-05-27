package isoladinosauri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.FileReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
//import java.util.Locale;
import java.util.Formatter;
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
		StringTokenizer parametro = null;
		//		StringTokenizer stFile = null;
		String token;

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
				//				String par;

				if (comando == null) {
					System.out.println("Client closed connection.");
					break;
				} else if (comando.equals("@creaUtente")) {
					String nickname="";
					String password="";

					if(st.hasMoreTokens()){
						comando=st.nextToken();
						parametro = new StringTokenizer(comando,"=");
						if(parametro.hasMoreTokens()){
							String string=parametro.nextToken();
							if(string.equals("user")) {
								nickname=parametro.nextToken();
							}
							else { 
								bufferedWriter.write("errore! richiesto parametro 'user' ");
							}
						}
						//						else {
						//							bufferedWriter.write("errore! parametro mancante");
						//						}
						//	}
						//					else {
						//						bufferedWriter.write("errore! parametro mancante");
						//					}

						if(st.hasMoreTokens()){
							comando=st.nextToken();
							parametro = new StringTokenizer(comando,"=");
							if(parametro.hasMoreTokens()){
								String string=parametro.nextToken();
								if(string.equals("pass")) {
									password=parametro.nextToken();
								}
								else { 
									bufferedWriter.write("errore! richiesto parametro 'pass' ");
								}
							}
							//						else {
							//							bufferedWriter.write("errore! parametro mancante");
							//						}
						}
						//					else {
						//						bufferedWriter.write("errore! parametro mancante");
						//					}

						//cerco nel file Utenti.txt se l'utente e' gia' esistente

						FileReader fileReader = new FileReader("Utenti.txt");
						BufferedReader br;
						br = new BufferedReader(fileReader);
						String rigaFile = br.readLine();

						boolean trovato=false;
						while(rigaFile!=null) {
							if(rigaFile.split(" ")[0].equals(nickname)) {
								trovato=true;
								bufferedWriter.write("errore! l'utente esiste gia'");
								break;
							}
							rigaFile = br.readLine();
						}
						if(trovato==false) {
							bufferedWriter.write("@ok");
							FileWriter fileUtenti = new FileWriter ("Utenti.txt",true); //true=append
							fileUtenti.write(nickname+" "+password+"\n");
							fileUtenti.close();
						}

						//					bufferedWriter.write("nome = "+nickname+", pass = "+password);
						Utente utente = new Utente(nickname,password);
					}
					else {
						bufferedWriter.write("errore! parametri mancanti"); 
					}
				}

				else if (comando.equals("@login")) {


					String nickname="";
					String password="";

					if(st.hasMoreTokens()){
						comando=st.nextToken();
						parametro = new StringTokenizer(comando,"=");
						if(parametro.hasMoreTokens()){
							String string=parametro.nextToken();
							if(string.equals("user")) {
								nickname=parametro.nextToken();
							}
							else { 
								bufferedWriter.write("errore! richiesto parametro 'user' ");
							}
						}
						else {
							bufferedWriter.write("errore! parametro mancante");
						}
					}
					else {
						bufferedWriter.write("errore! parametro mancante");
					}

					if(st.hasMoreTokens()){
						comando=st.nextToken();
						parametro = new StringTokenizer(comando,"=");
						if(parametro.hasMoreTokens()){
							String string=parametro.nextToken();
							if(string.equals("pass")) {
								password=parametro.nextToken();
							}
							else { 
								bufferedWriter.write("errore! richiesto parametro 'pass' ");
							}
						}
						else {
							bufferedWriter.write("errore! parametro mancante");
						}
					}
					else {
						bufferedWriter.write("errore! parametro mancante");
					}

					FileReader fileReader = new FileReader("Utenti.txt");
					BufferedReader br;
					br = new BufferedReader(fileReader);
					String rigaFile = br.readLine();

					boolean trovato=false;
					while(rigaFile!=null) {
						if(rigaFile.split(" ")[0].equals(nickname) && rigaFile.split(" ")[1].equals(password) ) {
							trovato=true;
							token=nickname+password;
							bufferedWriter.write("@Ok - token = "+token);
							break;
						}
						rigaFile = br.readLine();
					}
					if(trovato==false) {
						bufferedWriter.write("Utente non trovato ");
					}
					br.close();

				}
				else
					bufferedWriter.write("@unknownCommand");

				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		}
		catch (IOException e) {
			System.out.println("Error in the connection with the client.");
		}
	}
}