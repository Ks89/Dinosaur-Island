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

	public String creaUtente(String request) {
		String nickname = new String("");
		String password = new String("");
		String answer = new String("");

		nickname = request.split(",")[1].split("=")[1];
		password = request.split(",")[2].split("=")[1];
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
					answer="@no,@usernameOccupato";
					break;
				}
				rigaFile = br.readLine();
			}
			if(!trovato) { //inserisco il nuovo utente
				answer="@ok";
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
		return answer;
	}
	
	public String login(String request) {
		String nickname = new String("");
		String password = new String("");
		String token = new String("");
		String answer = new String("");

		nickname = request.split(",")[1].split("=")[1];
		password = request.split(",")[2].split("=")[1];

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
					answer="@ok,"+token;
				}
				else {
					answer="@no,@UTENTE_GIA_LOGGATO";
				}
			}
			else if(!trovato) {
				answer="@no,@autenticazioneFallita";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return answer;
	}
	
	public String creaRazza(String request) {

		String token = new String("");
		String nomeRazza = new String("");
		String tipoRazza = new String("");
		String answer = new String("");

		token = request.split(",")[1].split("=")[1];
		nomeRazza = request.split(",")[2].split("=")[1];
		tipoRazza = request.split(",")[3].split("=")[1];
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
					answer = "@ok";
					break;
				}
				rigaFile = br.readLine();
			}
			br.close();
			if(trovato) {
				//TODO: gestione creazione razza, bisogna creare prima un giocatore dato che la razza (o specie) appartiene a lui
			}
			else if(!trovato) {
				answer = "@no,@tokenNonValido";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return answer;
	}
	
	public String accessoPartita(String request) {

		String nomeUtente = new String("");
		String token = new String("");
		String answer = new String("");
		int nGiocatori = 0;

		token = request.split(",")[1].split("=")[1];
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
					answer = "@ok";
				}
				else if(!trovato) {
					answer = "@no,@tokenNonValido";
				}
				else if(nGiocatori>=8) {
					answer = "@no,@troppiGiocatori";
				}
			}	
			else {
				answer = "@no,@ACCESSO_GIA_EFFETTUATO";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return answer;
	}
	
	public String uscitaPartita(String request) {

		String token = new String("");
		String answer = new String("");

		token = request.split(",")[1].split("=")[1];
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
				answer = "@ok";
			}
			else if(!trovato) {
				answer = "@no,@tokenNonValido";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return answer;
	}
	
	public String listaGiocatori(String request) {

		String token = new String("");
		String answer = new String("");

		token = request.split(",")[1].split("=")[1];
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
				answer = "@listaGiocatori"+listaGiocatori;
			}
			else if(!trovato) {
				answer = "@no,@tokenNonValido";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return answer;
	}
	
	public String logout(String request) {
		
		String token = new String("");
		String answer = new String("");

		token = request.split(",")[1].split("=")[1];

		//verifico se c'e' nel file Token.txt
		try {
			FileReader fileToken = new FileReader("Token.txt");
			BufferedReader br;
			br = new BufferedReader(fileToken);
			String rigaFile = br.readLine();

			boolean trovato=false;
			while(rigaFile!=null) {
				if(rigaFile.split(" ")[1].equals(token) ) {
					trovato=true;
					break;
				}
				rigaFile = br.readLine();
			}
			br.close();
			if(trovato) {
				//aggiorno il file Token.txt cancellando il token che vuole fare il logout
				//copio i token escluso quello che devo eliminare in un file tmp

				fileToken = new FileReader("Token.txt");
				FileWriter fileTmpTokenLogout = new FileWriter ("TmpTokenLogout.txt",true); //true=append

				br = new BufferedReader(fileToken);
				rigaFile = br.readLine();

				while(rigaFile!=null) {
					if(rigaFile.split(" ")[1].equals(token)!=true) {
						fileTmpTokenLogout.write(rigaFile+"\n");
					}
					rigaFile = br.readLine();
				}
				br.close();
				fileTmpTokenLogout.close();

				//elimino il vecchio Token.txt e poi rinomino il TmpTokenLogout.txt in Token.txt

				File daEliminare = new File("Token.txt");
				if(daEliminare.exists()) {
					daEliminare.delete();
				}

				File fileNomeVecchio = new File("TmpTokenLogout.txt");
				File fileNomeNuovo = new File("Token.txt");
				fileNomeVecchio.renameTo(fileNomeNuovo);
				answer = "@ok";
			}
			else if(!trovato) {
				answer = "@no,@tokenNonValido";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return answer;
	}
	
	public void run() {
		
		String comando = new String("");
		String answer = new String("");
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				String request = bufferedReader.readLine();
				if(request!=null) {
					comando = request.split(",")[0];
				}
				//FIXME
//				if (comando == null) {
//					System.out.println("Client closed connection.");
//					break;
//				} else 
				if (comando.equals("@creaUtente")) {
					answer = creaUtente(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@login")) {
					answer=login(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@creaRazza")) {
					answer=creaRazza(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@accessoPartita")) {
					answer=accessoPartita(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@uscitaPartita")) {
					answer=uscitaPartita(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@listaGiocatori")) {
					answer=listaGiocatori(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@logout")) {
					answer=logout(request);
					bufferedWriter.write(answer);
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