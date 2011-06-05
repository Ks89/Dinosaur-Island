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
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Vegetale;
import isoladinosauri.CaricamentoMappa;

public class ClientHandler extends Thread {

	private Socket socket;
	private static final String ERRORE = "Errore lettura file";

	public ClientHandler(Socket socket) {
		this.socket = socket;
		
	}
	
	public boolean cercaNelFile(String nomeFileFisico, String parametro1, String parametro2, String carSplit, int posSplit) {
		boolean trovato = false;
		try {
			FileReader fileReader = new FileReader(nomeFileFisico);
			BufferedReader br;
			br = new BufferedReader(fileReader);
			String rigaFile = br.readLine();

			while(rigaFile!=null) {
				if(parametro2!=null) {
					if(rigaFile.split(carSplit)[posSplit].equals(parametro1) && rigaFile.split(carSplit)[posSplit+1].equals(parametro2)) {
						trovato=true;
						break;
					}
				}
				else {
					if(rigaFile.split(carSplit)[posSplit].equals(parametro1)) {
						trovato=true;
						break;
					}
				}
				rigaFile = br.readLine();
			}
			br.close();
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}

		return trovato;
	}
	
	public void scriviNelFile(String nomeFileFisico, String riga) {
		try {
			FileWriter fileWriter = new FileWriter (nomeFileFisico,true); //true=append
			fileWriter.write(riga);
			fileWriter.close();
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
	}

	public String creaUtente(String request) {
		String nickname = new String("");
		String password = new String("");
		String answer = new String("");

		nickname = request.split(",")[1].split("=")[1];
		password = request.split(",")[2].split("=")[1];
		//cerco nel file Utenti.txt se l'utente e' gia' esistente
		boolean trovato = false;
		trovato = cercaNelFile("Utenti.txt",nickname,null," ",0);
		if(trovato) {
			answer="@no,@usernameOccupato";
		}
		else {
			answer="@ok";
			scriviNelFile("Utenti.txt",nickname+" "+password+"\n");
			Utente utente = new Utente(nickname,password);
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
		boolean trovato = false;
		trovato = cercaNelFile("Utenti.txt",nickname,password," ",0);
			if(trovato) {
				//cerco se il token e' gia' loggato
				boolean trovatoToken = false;
				trovatoToken = cercaNelFile("Token.txt",nickname,null," ",0);
				if(!trovatoToken) {
					//aggiorno il file Token.txt
					token = nickname+"-"+password;
					scriviNelFile("Token.txt",nickname+" "+token+"\n");
					answer = "@ok,"+token;
				}
				else {
					answer = "@no,@UTENTE_GIA_LOGGATO";
				}
			}
			else if(!trovato) {
				answer = "@no,@autenticazioneFallita";
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
		boolean trovato = false;
		trovato = cercaNelFile("Token.txt",token,null," ",1);
		if(trovato) {
			//FIXME: mancano p e turnoCorrente
			CaricamentoMappa cm = new CaricamentoMappa(); 	//solo x test
			Cella[][] mappaCelle;							//solo x test
			mappaCelle = cm.caricaDaFile();					//solo x test
			Isola i = new Isola(mappaCelle);				//solo x test
			Partita p = new Partita(i);						//solo x test
			Turno t = new Turno(p);							//solo x test
			int turnoCorrente = 1;							//solo x test
			Giocatore giocatore = new Giocatore(p, turnoCorrente, nomeRazza, tipoRazza);
			answer = "@ok";
		}
		else if(!trovato) {
			answer = "@no,@tokenNonValido";
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
			boolean trovatoTokenInPartita = false;
			trovatoTokenInPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
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
					scriviNelFile("TokenInPartita.txt",nomeUtente+" "+token+"\n");
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
		 // cerco se il token e' in partita
		boolean trovato = false;
		trovato = cercaNelFile("TokenInPartita.txt",token,null," ",1);
		try {
			if(trovato) {
				//aggiorno il file TokenInPartita.txt cancellando il token
				//copio i token escluso quello che devo eliminare in un file tmp
				
				FileReader fileTokenInPartita = new FileReader("TokenInPartita.txt");
				FileWriter fileTmpToken = new FileWriter ("TmpToken.txt",true); //true=append

				BufferedReader br = new BufferedReader(fileTokenInPartita);
				String rigaFile = br.readLine();

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
		boolean trovato = false;
		trovato = cercaNelFile("Token.txt",token,null," ",1);
		try {
			if(trovato) {
				//accedo al file tokenInPartita.txt per fornire la lista dei giocatori in partita
				String listaGiocatori = new String ("");
				FileReader fileTokenInPartita = new FileReader("TokenInPartita.txt");
				BufferedReader br = new BufferedReader(fileTokenInPartita);
				String rigaFile = br.readLine();
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

		boolean trovato = false;
		trovato = cercaNelFile("Token.txt",token,null," ",1);
		try {
			if(trovato) {
				//aggiorno il file Token.txt cancellando il token che vuole fare il logout
				//copio i token escluso quello che devo eliminare in un file tmp

				FileReader fileToken = new FileReader("Token.txt");
				FileWriter fileTmpTokenLogout = new FileWriter ("TmpTokenLogout.txt",true); //true=append

				BufferedReader br = new BufferedReader(fileToken);
				String rigaFile = br.readLine();

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
/*	
	public String mappaGenerale(String request) {

		String token = new String("");
		String answer = new String("");

		token = request.split(",")[1].split("=")[1];
		//verifico se c'e' nel file Token.txt

		boolean trovatoToken = false;
		trovatoToken = cercaNelFile("Token.txt",token,null," ",1);
		try {
			if(trovatoToken) {
				//accedo al file TokenInPartita.txt per verificare se e' in partita
				boolean trovatoInPartita = false;
				trovatoInPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
				if(trovatoInPartita) {
					CaricamentoMappa cm = new CaricamentoMappa();
					//					GenerazioneMappa gm = new GenerazioneMappa();
					Cella[][] mappaCelle;

					String answerMappa = new String ("");

					mappaCelle = cm.caricaDaFile();
					String[][] mappaStringhe = new String[mappaCelle.length][mappaCelle[0].length];
					//traduco la mappa di tipo Cella in tipo String
					for(int i=0; i<mappaCelle.length; i++) {
						for(int j=0; j<mappaCelle[0].length; j++) {
							//FIXME: manca giocatore
							if(giocatore.getMappaVisibile[i][j]) {

								if (mappaCelle[i][j] == null) { //e' acqua
									mappaStringhe[i][j] = new String("a");
								}
								else { //se e' terra puo' essere carogna o vegetale

									if(mappaCelle[i][j].getOccupante() instanceof Carogna)
									{
										mappaStringhe[i][j] = new String("c");
									}
									if(mappaCelle[i][j].getOccupante() instanceof Vegetale)
									{
										mappaStringhe[i][j] = new String("v");
									}
									if(!(mappaCelle[i][j].getOccupante() instanceof Vegetale) &&
											!(mappaCelle[i][j].getOccupante() instanceof Carogna)) {
										mappaStringhe[i][j] = new String("t");
									}
								}
							} else {
								mappaStringhe[i][j] = new String("b"); //buio
							}

							answerMappa += "["+mappaStringhe[i][j]+"]";
						}
						answerMappa += ";";
					}
					answer = "@mappaGenerale,{"+mappaStringhe.length+","+mappaStringhe[0].length+"}"+","+answerMappa;
				}
				else {
					answer = "@no,@nonInPartita";
				}
			}
			else {
				answer = "@no,@tokenNonValido";
			}
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return answer;
	}
	
	public String listaDinosauri(String request) {

		String token = new String("");
		String answer = new String("");
		String listaDino = new String("");

		token = request.split(",")[1].split("=")[1];
		//verifico se c'e' nel file Token.txt
		try {
			boolean trovato = false;
			trovato = cercaNelFile("Token.txt",token,null," ",1);
			if(trovato) {
				//accedo al file tokenInPartita.txt per verificare se l'utente si trova in partita
				boolean inPartita = false;
				inPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
				if(inPartita) { //FIXME: manca giocatore
					for(int k=0; k<giocatore.getDinosauri().dinosauri.size(); k++) {
						listaDino += ","+giocatore.getDinosauri().dinosauri.get(k).getId();
					}
				}
				else {
					//TODO: ?? dalle specifiche: ''..altrimenti non si e' in partita, e cio' e' definito dal simbolo +'' ??
				}
				answer = "@listaDinosauri"+listaDino;
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
*/
	
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
//				else if (comando.equals("@mappaGenerale")) {
//					answer=mappaGenerale(request);
//					bufferedWriter.write(answer);
//				}
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