package isoladinosauri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler extends Thread {

	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public String creaUtente(String comando, StringTokenizer st) {
		StringTokenizer string = null;
		String parametro;
		String nickname="";
		String password="";
		String risposta="";

		comando=st.nextToken(); // comando: user=U,pass=P
		string = new StringTokenizer(comando,"=");
		parametro=string.nextToken(); // string: user
		if(parametro.equals("user")) {
			nickname=string.nextToken();
		}
		comando=st.nextToken();
		string = new StringTokenizer(comando,"=");
		parametro=string.nextToken();
		if(parametro.equals("pass")) {
			password=string.nextToken();
			//cerco nel file Utenti.txt se l'utente e' gia' esistente
			try {
				FileReader fileReader = new FileReader("Utenti.txt");
				BufferedReader br;
				br = new BufferedReader(fileReader);
				String rigaFile = br.readLine();
				boolean trovato=false;
				while(rigaFile!=null) {
					if(rigaFile.split(" ")[0].equals(nickname)) {
						trovato=true;
						risposta="@no,@usernameOccupato";
						break;
					}
					rigaFile = br.readLine();
				}
				if(trovato==false) { //inserisco il nuovo utente
					risposta="@ok";
					FileWriter fileUtenti = new FileWriter ("Utenti.txt",true); //true=append
					fileUtenti.write(nickname+" "+password+"\n");
					fileUtenti.close();
				}
				Utente utente = new Utente(nickname,password);
			}
			catch(IOException ioException)
			{
				System.err.println("Errore lettura file");
			}
		}
		return risposta;
	}
	
	public String login(String comando, StringTokenizer st) {
		StringTokenizer string = null;
		String parametro;
		String nickname="";
		String password="";
		String token="";
		String risposta="";

		comando=st.nextToken();
		string = new StringTokenizer(comando,"=");
		parametro=string.nextToken();
		if(parametro.equals("user")) {
			nickname=string.nextToken();
		}
		comando=st.nextToken();
		string = new StringTokenizer(comando,"=");
		parametro=string.nextToken();
		if(parametro.equals("pass")) {
			password=string.nextToken();
			//verifico se c'e' nel file Utenti.txt
			try {
				FileReader fileUtenti = new FileReader("Utenti.txt");
				BufferedReader br;
				br = new BufferedReader(fileUtenti);
				String rigaFile = br.readLine();

				boolean trovato=false;
				while(rigaFile!=null) {
					if(rigaFile.split(" ")[0].equals(nickname) && rigaFile.split(" ")[1].equals(password) ) {
						trovato=true;
						token=nickname+password;
						risposta="@ok,"+token;
						break;
					}
					rigaFile = br.readLine();
				}
				br.close();
				if(trovato==true) {
					//aggiorno il file Token.txt
					FileWriter fileToken = new FileWriter ("Token.txt",true); //true=append
					fileToken.write(nickname+" "+token+"\n");
					fileToken.close();
				}
				else if(trovato==false) {
					risposta="@no,@autenticazioneFallita";
				}
			}
			catch(IOException ioException)
			{
				System.err.println("Errore lettura file");
			}
		}
		return risposta;
	}
	
	public String creaRazza(String comando, StringTokenizer st) {
		StringTokenizer string = null;
		String parametro;
		String token="";
		String nomeRazza="";
		String tipoRazza="";
		String risposta="";

		comando=st.nextToken();
		string = new StringTokenizer(comando,"=");
		parametro=string.nextToken();
		if(parametro.equals("token")) {
			token=string.nextToken();
		}
		comando=st.nextToken();
		string = new StringTokenizer(comando,"=");
		parametro=string.nextToken();
		if(parametro.equals("nome")) {
			nomeRazza=string.nextToken();
			//verifico se c'e' il token nel file Token.txt
			try {
				FileReader fileToken = new FileReader("Token.txt");
				BufferedReader br;
				br = new BufferedReader(fileToken);
				String rigaFile = br.readLine();

				boolean trovato=false;
				while(rigaFile!=null) {
					if(rigaFile.split(" ")[1].equals(token) ) {
						trovato=true;
						risposta="@ok";
						break;
					}
					rigaFile = br.readLine();
				}
				br.close();
				if(trovato==true) {
					//TODO: gestione creazione razza
				}
				else if(trovato==false) {
					risposta="@no,@tokenNonValido";
				}
			}
			catch(IOException ioException)
			{
				System.err.println("Errore lettura file");
			}
		}
		return risposta;
	}
	
	public String accessoPartita(String comando, StringTokenizer st) {

		StringTokenizer string = null;
		String parametro;
		String token="";
		String risposta="";
		int nGiocatori=0;

		comando=st.nextToken();
		string = new StringTokenizer(comando,"=");
		parametro=string.nextToken();
		if(parametro.equals("token")) {
			token=string.nextToken();
		}
		try { // cerco se il token e' valido
			FileReader fileToken = new FileReader("Token.txt");
			BufferedReader br;
			br = new BufferedReader(fileToken);
			String rigaFile = br.readLine();

			boolean trovato=false;
			while(rigaFile!=null) {
				if(rigaFile.split(" ")[1].equals(token)) {
					trovato=true;
					break;
				}
				rigaFile = br.readLine();
			}
			br.close();
			//conto il numero di giocatori (token) nel file tokenInPartita
			FileReader fileTokenInPartitaRD = new FileReader("TokenInPartita.txt");
			br = new BufferedReader(fileTokenInPartitaRD);
			rigaFile = br.readLine();
			while(rigaFile!=null) {
				nGiocatori++;
				rigaFile = br.readLine();
			}
			br.close();

			if(trovato==true && nGiocatori<8) {
				//aggiorno il file TokenInPartita.txt
				FileWriter fileTokenInPartitaWR = new FileWriter ("TokenInPartita.txt",true); //true=append
				fileTokenInPartitaWR.write(token+"\n");
				fileTokenInPartitaWR.close();
				risposta="@ok";
			}
			else if(trovato==false) {
				risposta="@no,@tokenNonValido";
			}
			else if(nGiocatori>=8) {
				risposta="@no,@troppiGiocatori";
			}
		}
		catch(IOException ioException)
		{
			System.err.println("Errore lettura file");
		}
		return risposta;
	}
	
	public void run() {
		StringTokenizer st = null;
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				String request = bufferedReader.readLine();
				st = new StringTokenizer(request,",");
				String comando = st.nextToken();
				String risposta;
				
				if (comando == null) {
					System.out.println("Client closed connection.");
					break;
				} else if (comando.equals("@creaUtente")) {
					risposta=creaUtente(comando,st);
					bufferedWriter.write(risposta);
				}
				else if (comando.equals("@login")) {
					risposta=login(comando,st);
					bufferedWriter.write(risposta);
				}
				else if (comando.equals("@creaRazza")) {
					risposta=creaRazza(comando,st);
					bufferedWriter.write(risposta);
				}
				else if (comando.equals("@accessoPartita")) {
					risposta=accessoPartita(comando,st);
					bufferedWriter.write(risposta);
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