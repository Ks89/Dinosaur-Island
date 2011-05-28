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
		StringTokenizer parametro = null;
		String token;

		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				String request = bufferedReader.readLine();
				st = new StringTokenizer(request,",");
				String comando = st.nextToken();
				String string;

				if (comando == null) {
					System.out.println("Client closed connection.");
					break;
				} else if (comando.equals("@creaUtente")) {
					String nickname="";
					String password="";
	
					comando=st.nextToken(); // comando: user=U,pass=P
					parametro = new StringTokenizer(comando,"=");
					string=parametro.nextToken(); // string: user
					if(string.equals("user")) {
						nickname=parametro.nextToken();
					}
					comando=st.nextToken();
					parametro = new StringTokenizer(comando,"=");
					string=parametro.nextToken();
					if(string.equals("pass")) {
						password=parametro.nextToken();
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
						Utente utente = new Utente(nickname,password);
					}
				}
			
				else if (comando.equals("@login")) {
					String nickname="";
					String password="";

					comando=st.nextToken();
					parametro = new StringTokenizer(comando,"=");
					string=parametro.nextToken();
					if(string.equals("user")) {
						nickname=parametro.nextToken();
					}

					comando=st.nextToken();
					parametro = new StringTokenizer(comando,"=");
					string=parametro.nextToken();
					if(string.equals("pass")) {
						password=parametro.nextToken();
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