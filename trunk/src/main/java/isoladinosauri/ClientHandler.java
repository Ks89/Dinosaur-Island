package isoladinosauri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler extends Thread {

	private Socket socket;
	private static final String ERRORE = "Errore lettura file";

	public ClientHandler(Socket socket) {
		this.socket = socket;
		
	}

	public String creaUtente(String comando, StringTokenizer st) {
		StringTokenizer string = null;
		String parametro;
		String nickname="";
		String password="";
		String risposta="";
//FIXME: tradurre tutte le StringTokenizer in .split()
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
				if(!trovato) { //inserisco il nuovo utente
					risposta="@ok";
					FileWriter fileUtenti = new FileWriter ("Utenti.txt",true); //true=append
					fileUtenti.write(nickname+" "+password+"\n");
					fileUtenti.close();
				}
//				Utente utente = new Utente(nickname,password);
			}
			catch(IOException ioException)
			{
				System.err.println(ERRORE);
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
						token=nickname+"-"+password;
						break;
					}
					rigaFile = br.readLine();
				}
				br.close();
				if(trovato) {
					//cerco se il token e' gia' loggato
					FileReader fileToken = new FileReader("Token.txt");
					br = new BufferedReader(fileToken);
					rigaFile = br.readLine();

					boolean trovatoToken=false;
					while(rigaFile!=null) {
						if(rigaFile.split(" ")[1].equals(token)) {
							trovatoToken=true;
							break;
						}
						rigaFile = br.readLine();
					}
					br.close();
					
					if(!trovatoToken) {
						//aggiorno il file Token.txt
						FileWriter fileTokenAgg = new FileWriter ("Token.txt",true); //true=append
						fileTokenAgg.write(nickname+" "+token+"\n");
						fileTokenAgg.close();
						risposta="@ok,"+token;
					}
					else {
						risposta="@no,@UTENTE_GIA_LOGGATO";
					}
				}
				else if(!trovato) {
					risposta="@no,@autenticazioneFallita";
				}
			}
			catch(IOException ioException)
			{
				System.err.println(ERRORE);
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
//			nomeRazza=string.nextToken();
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
				if(trovato) {
					//TODO: gestione creazione razza
				}
				else if(!trovato) {
					risposta="@no,@tokenNonValido";
				}
			}
			catch(IOException ioException)
			{
				System.err.println(ERRORE);
			}
		}
		return risposta;
	}
	
	public String accessoPartita(String comando, StringTokenizer st) {

		StringTokenizer string = null;
		String parametro;
		String nomeUtente="";
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
					nomeUtente=rigaFile.split(" ")[0];
					break;
				}
				rigaFile = br.readLine();
			}
			br.close();

			// verifico se il giocatore ha gia' effettuato l'accesso
			FileReader fileTokenInPartita = new FileReader("TokenInPartita.txt");
			br = new BufferedReader(fileTokenInPartita);
			rigaFile = br.readLine();

			boolean trovatoTokenInPartita=false;
			while(rigaFile!=null) {
				if(rigaFile.split(" ")[1].equals(token)) {
					trovatoTokenInPartita=true;
					break;
				}
				rigaFile = br.readLine();
			}
			br.close();

			if(!trovatoTokenInPartita) {

				//conto il numero di giocatori (token) nel file tokenInPartita
				FileReader fileTokenInPartitaRD = new FileReader("TokenInPartita.txt");
				br = new BufferedReader(fileTokenInPartitaRD);
				rigaFile = br.readLine();
				while(rigaFile!=null) {
					nGiocatori++;
					rigaFile = br.readLine();
				}
				br.close();

				if(trovato && nGiocatori<8) {
					//aggiorno il file TokenInPartita.txt
					FileWriter fileTokenInPartitaWR = new FileWriter ("TokenInPartita.txt",true); //true=append
					fileTokenInPartitaWR.write(nomeUtente+" "+token+"\n");
					fileTokenInPartitaWR.close();
					risposta="@ok";
				}
				else if(!trovato) {
					risposta="@no,@tokenNonValido";
				}
				else if(nGiocatori>=8) {
					risposta="@no,@troppiGiocatori";
				}
			}	
			else {
				risposta="@no,@ACCESSO_GIA_EFFETTUATO";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return risposta;
	}
	
	public String uscitaPartita(String comando, StringTokenizer st) {

		StringTokenizer string = null;
		String parametro;
		String token="";
		String risposta="";

		comando=st.nextToken();
		string = new StringTokenizer(comando,"=");
		parametro=string.nextToken();
		if(parametro.equals("token")) {
			token=string.nextToken();
		}
		try { // cerco se il token e' in partita
			FileReader fileTokenInPartita = new FileReader("TokenInPartita.txt");
			BufferedReader br;
			br = new BufferedReader(fileTokenInPartita);
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

			if(trovato) {
				//aggiorno il file TokenInPartita.txt cancellando il token
				//copio i token escluso quello che devo eliminare in un file tmp

				fileTokenInPartita = new FileReader("TokenInPartita.txt");
				FileWriter fileTmpToken = new FileWriter ("TmpToken.txt",true); //true=append
				
				br = new BufferedReader(fileTokenInPartita);
				rigaFile = br.readLine();

				while(rigaFile!=null) {
					if(rigaFile.split(" ")[1].equals(token)!=true) {
						fileTmpToken.write(rigaFile+"\n");
					}
					rigaFile = br.readLine();
				}
				br.close();
				fileTmpToken.close();

				//elimino il vecchio TokenInPartita.txt e poi rinomino il TmpToken.txt in TokenInPartita.txt
			
				File daEliminare = new File("TokenInPartita.txt");
				if(daEliminare.exists()) {
					daEliminare.delete();
				}
				
				File fileNomeVecchio = new File("TmpToken.txt");
				File fileNomeNuovo = new File("TokenInPartita.txt");
				fileNomeVecchio.renameTo(fileNomeNuovo);
				risposta="@ok";
			}
			else if(!trovato) {
				risposta="@no,@tokenNonValido";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return risposta;
	}
	
	public String listaGiocatori(String comando, StringTokenizer st) {
		StringTokenizer string = null;
		String parametro;
		String token="";
		String risposta="";

		comando=st.nextToken();
		string = new StringTokenizer(comando,"=");
		parametro=string.nextToken();
		if(parametro.equals("token")) {
			token=string.nextToken();
		}
		//verifico se c'e' nel file Token.txt
		try {
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
			if(trovato) {
				//accedo al file tokenInPartita.txt per fornire la lista dei giocatori in partita
				String listaGiocatori="";
				FileReader fileTokenInPartita = new FileReader("TokenInPartita.txt");
				br = new BufferedReader(fileTokenInPartita);
				rigaFile = br.readLine();
				while(rigaFile!=null) {
					listaGiocatori=listaGiocatori+","+rigaFile.split(" ")[0];
					rigaFile = br.readLine();
				}
				br.close();
				risposta="@listaGiocatori"+listaGiocatori;
			}
			else if(!trovato) {
				risposta="@no,@tokenNonValido";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return risposta;
	}
	
	public void run() {
		StringTokenizer st = null;
		String comando = new String("");
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				String request = bufferedReader.readLine();
				if(request!=null) {
					st = new StringTokenizer(request,",");
					comando = st.nextToken();
				}
				String risposta;
				
				//FIXME
//				if (comando == null) {
//					System.out.println("Client closed connection.");
//					break;
//				} else 
				if (comando.equals("@creaUtente")) {
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
				else if (comando.equals("@uscitaPartita")) {
					risposta=uscitaPartita(comando,st);
					bufferedWriter.write(risposta);
				}
				else if (comando.equals("@listaGiocatori")) {
					risposta=listaGiocatori(comando,st);
					bufferedWriter.write(risposta);
				}
				else {
					bufferedWriter.write("@unknownCommand");
				}

				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		}
		catch (IOException e) {
			System.out.println("Error in the connection with the client.");
		}
	}
}