package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import client.ClientGui;


public class ClientGui {

	private String host;
	private int port;
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String richiesta;
	private String risposta;
	
	public String getRisposta() {
		return risposta;
	}

	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}
	private String token;

	public ClientGui(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void inizializzaClient() throws UnknownHostException, IOException {
		this.socket = new Socket(host, port);
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public String getRispostaServer() throws IOException, InterruptedException{
		risposta = bufferedReader.readLine();
		if (risposta != null) {
			System.out.println("Risposta server: " + risposta);
		}
		return risposta;
	}

	public void creaUtente(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@creaUtente,user="+nomeUtente+",pass="+password;
		this.inviaAlServer();
	}

	public void eseguiLogin(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@login,user="+nomeUtente+",pass="+password;
		this.inviaAlServer();
	}

	public void creaRazza(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		this.token = nomeUtente + "-" + password;
		richiesta="@creaRazza,token="+this.token+",nome="+nomeUtente+",tipo="+password;
		this.inviaAlServer();
	}

	public void logout() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@logout,token="+this.token;
		this.inviaAlServer();
	}

	public void classifica() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@classifica,token="+this.token;
		this.inviaAlServer();
	}
	
	public void uscitaPartita() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@uscitaPartita,token="+this.token;
		this.inviaAlServer();
	}

	public void mappaGenerale() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@mappaGenerale,token="+this.token;
		this.inviaAlServer();
	}

	public void statoDinosauro(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@statoDinosauro,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}	

	public void accessoPartita(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@accessoPartita,token="+token;
		this.inviaAlServer();
	}

	public void vistaLocale(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@vistaLocale,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}

	public void crescitaDinosauro(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@cresciDinosauro,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}
	
	public void deponiUovo(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@deponiUovo,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}
	
	public void muoviDinosauro(String idDino,int riga, int colonna) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@muoviDinosauro,token=" + token + ",idDino=" + idDino + ",dest={" + riga + "," + colonna + "}";
		this.inviaAlServer();
	}
	
	public void listaDinosauri() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@listaDinosauri,token="+token;
		this.inviaAlServer();
	}

	public void inviaAlServer() throws IOException, InterruptedException{
		System.out.println("Invio richiesta al server: " + richiesta);
		bufferedWriter.write(richiesta);
		bufferedWriter.newLine();
		bufferedWriter.flush();
		this.getRispostaServer();	
	}

	public static void main(String[] args) {
		ClientGui client = new ClientGui("localhost", 1234);

		Gui gui = new Gui(client);
		LoginGui loginGui = new LoginGui(gui);
		loginGui.aggiuntaGiocatore();
	}

}
