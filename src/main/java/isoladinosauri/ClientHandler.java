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

	public void run() {
		StringTokenizer st = null;
		StringTokenizer string = null;

		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				String request = bufferedReader.readLine();
				st = new StringTokenizer(request,",");
				String comando = st.nextToken();
				String parametro;

				if (comando == null) {
					System.out.println("Client closed connection.");
					break;
				} else if (comando.equals("@creaUtente")) {
					String nickname="";
					String password="";
	
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
						FileReader fileReader = new FileReader("Utenti.txt");
						BufferedReader br;
						br = new BufferedReader(fileReader);
						String rigaFile = br.readLine();
						boolean trovato=false;
						while(rigaFile!=null) {
							if(rigaFile.split(" ")[0].equals(nickname)) {
								trovato=true;
								bufferedWriter.write("@no,@usernameOccupato");
								break;
							}
							rigaFile = br.readLine();
						}
						if(trovato==false) { //inserisco il nuovo utente
							bufferedWriter.write("@ok");
							FileWriter fileUtenti = new FileWriter ("Utenti.txt",true); //true=append
							fileUtenti.write(nickname+" "+password+"\n");
							fileUtenti.close();
						}
						Utente utente = new Utente(nickname,password);
					}
				}
				else if (comando.equals("@login")) {
					String nickname="";
					String password="";
					String token="";

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
						FileReader fileReader = new FileReader("Utenti.txt");
						BufferedReader br;
						br = new BufferedReader(fileReader);
						String rigaFile = br.readLine();
	
						boolean trovato=false;
						while(rigaFile!=null) {
							if(rigaFile.split(" ")[0].equals(nickname) && rigaFile.split(" ")[1].equals(password) ) {
								trovato=true;
								token=nickname+password;
								bufferedWriter.write("@ok,"+token);
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
							bufferedWriter.write("@no,@autenticazioneFallita");
						}
					}
				}
				else if (comando.equals("@creaRazza")) {
					String token="";
					String nomeRazza="";
					String tipoRazza="";

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
						FileReader fileReader = new FileReader("Token.txt");
						BufferedReader br;
						br = new BufferedReader(fileReader);
						String rigaFile = br.readLine();
	
						boolean trovato=false;
						while(rigaFile!=null) {
							if(rigaFile.split(" ")[1].equals(token) ) {
								trovato=true;
								bufferedWriter.write("@ok");
								break;
							}
							rigaFile = br.readLine();
						}
						br.close();
						if(trovato==true) {
							//TODO: gestione creazione razza
						}
						else if(trovato==false) {
							bufferedWriter.write("@no,@tokenNonValido");
						}
					}
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