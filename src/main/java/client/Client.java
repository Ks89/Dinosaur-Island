package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import client.Client;


/**
 *	Classe che si occupa di inviare i messaggi
 *	al server e di ricevere la risposta.
 */
public class Client {

	private String host;
	private int porta;
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String richiesta;
	private String risposta;
	private String token;
	private String nomeUtente;


	public Client (String host, int porta) {
		this.host = host;
		this.porta = porta;
	}
	
	
	/**
	 * @return Una string rappresentanta la risposta del Server.
	 */
	public String getRichiesta() {
		return risposta;
	}

	/**
	 * Metodo per inizializzare il Client.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void inizializzaClient() throws UnknownHostException, IOException {
		this.socket = new Socket(host, porta);
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	/**
	 * Metodo per ottenere la risposta dal Server.
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String getRispostaServer() throws IOException, InterruptedException{
		risposta = bufferedReader.readLine();
		if (risposta != null) {
			System.out.println("Risposta server: " + risposta);
		}
		return risposta;
	}

	/**
	 * Metodo per registrarsi.
	 * @param nomeUtente
	 * @param password
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void creaUtente(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@creaUtente,user="+nomeUtente+",pass="+password;
		this.inviaAlServer();
	}

	/**
	 * Metodo per eseguire il login.
	 * @param nomeUtente
	 * @param password
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void eseguiLogin(String nomeUtente, String password) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@login,user="+nomeUtente+",pass="+password;
		this.inviaAlServer();
	}

	/**
	 * Metodo per creare la razza.
	 * @param nomeUtente
	 * @param password
	 * @param nome
	 * @param tipo
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void creaRazza(String nomeUtente, String password,String nome, String tipo) throws IOException, InterruptedException{
		bufferedWriter.flush();
		this.token = nomeUtente + "-" + password;
		richiesta = "@creaRazza,token=" + this.token + ",nome=" + nome + ",tipo=" + tipo;
		this.inviaAlServer();
	}

	/**
	 * Metodo per eseguire il logout.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void logout() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@logout,token="+this.token;
		this.inviaAlServer();
	}

	/**
	 * Metodo per richiedere la classifica.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void classifica() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@classifica,token="+this.token;
		this.inviaAlServer();
	}

	/**
	 * Metodo per uscire dalla Partita.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void uscitaPartita() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@uscitaPartita,token="+this.token;
		this.inviaAlServer();
	}

	/**
	 * Metodo per ottenere la mappa generale.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void mappaGenerale() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@mappaGenerale,token="+this.token;
		this.inviaAlServer();
	}

	/**
	 * Metodo per ottenere lo stato del Dinosauro.
	 * @param idDino
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void statoDinosauro(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@statoDinosauro,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}	

	/**
	 * Metodo per accedere in Partita.
	 * @param nomeUtente
	 * @param password
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void accessoPartita(String nomeUtente, String password) throws IOException, InterruptedException{
		this.nomeUtente = nomeUtente;
		bufferedWriter.flush();
		richiesta="@accessoPartita,token="+token;
		this.inviaAlServer();
	}

	/**
	 * Metodo per ottenere la vista locale.
	 * @param idDino
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void vistaLocale(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@vistaLocale,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}

	/**
	 * Metodo per far crescere un Dinosauro.
	 * @param idDino
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void crescitaDinosauro(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@cresciDinosauro,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}

	/**
	 * Metodo per deporre un uovo.
	 * @param idDino
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void deponiUovo(String idDino) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@deponiUovo,token="+token+",idDino="+idDino;
		this.inviaAlServer();
	}

	/**
	 * Metodo per eseguire un movimento.
	 * @param idDino
	 * @param riga
	 * @param colonna
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void muoviDinosauro(String idDino,int riga, int colonna) throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@muoviDinosauro,token=" + token + ",idDino=" + idDino + ",dest={" + riga + "," + colonna + "}";
		this.inviaAlServer();
	}

	/**
	 * Metodo per ottenere la lista dei Dinosauri.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void listaDinosauri() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@listaDinosauri,token="+token;
		this.inviaAlServer();
	}

	/**
	 * Metodo per confermare il Turno.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void confermaTurno() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@confermaTurno,token="+token;
		this.inviaAlServer();
	}

	/**
	 * Metodo per passare il turno.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void passaTurno() throws IOException, InterruptedException{
		bufferedWriter.flush();
		richiesta="@passaTurno,token="+token;
		this.inviaAlServer();
	}

	//ricevi il cambio turno
	public void cambioTurno(String nomeUtente) throws IOException, InterruptedException{
		if(this.risposta.contains("@cambioTurno")) {
			String utente = this.risposta.split(",")[1];
			//controllo se il nomeUtente delo giocatore sul client e' uguale a quello mandato dal server
			//in tal caso vuol dire che ho ottenuto il turno
			if(utente.equals(nomeUtente)) {
//				this.avviaPrimoTimer();
			}
			//ricevo il cambio turno
		}
	}


	/**
	 * Metodo per inviare al Server la richiesta effettuata.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void inviaAlServer() throws IOException, InterruptedException{
		System.out.println("Invio richiesta al server: " + richiesta);
		bufferedWriter.write(richiesta);
		bufferedWriter.newLine();
		bufferedWriter.flush();
		this.getRispostaServer();	
	}
	
	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
}
