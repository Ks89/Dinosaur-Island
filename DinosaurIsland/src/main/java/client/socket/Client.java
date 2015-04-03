/*
Copyright 2011-2015 Stefano Cappa
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package client.socket;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import comune.InterfacciaRmi;

import client.socket.Client;


/**
 *	Classe che si occupa di inviare i messaggi
 *	al server e di ricevere la risposta.
 */
public class Client {

	private String host;
	private int porta;
	private int portaTurno; //porta per la gestione del socket col broadcast
	private Socket socket;
	private Socket socketTurno;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private BufferedReader bufferedReaderTurno;
	private String richiesta;
	private String rispostaTurno;
	private String risposta;
	private String token;
	private String nomeUtente;
	private Gui gui;


	/**
	 * Costruttore della classe Client.
	 * @param host String che rappresenta l'indirizzo IP del server.
	 * @param porta int che rappresenta la porta di comunicazione col server.
	 * @param portaTurno int che rappresente la porta di comunicazione per i messaggi in broadcast col server.
	 */
	public Client (String host, int porta, int portaTurno) {
		this.host = host;
		this.porta = porta;
		this.portaTurno = portaTurno;

		///////////esempio di chiamata da client a RMI Server//////////////
		try {
			//Ottengo l'oggetto remoto (il client vede solo l'interfaccia)
			//Per ora host e porta sono scritti direttamente nel codice
			InterfacciaRmi interfaccia = (InterfacciaRmi)Naming.lookup("rmi://localhost:1099/isola-rmi"); 

			//Eseguo i metodi sull'interfaccia, come se fosse locale, in realta' viene chiamato il server
			System.out.println("somma: " + interfaccia.somma(5, 2));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param gui Riferimento alla classe Gui.
	 */
	public void setGui(Gui gui) {
		this.gui = gui;
	}

	/**
	 * @return Una string rappresentanta la risposta del Server.
	 */
	public String getRichiesta() {
		return risposta;
	}

	/**
	 * Metodo per inizializzare il Client.
	 * @throws UnknownHostException Eccezione sollevata se l'host non e' riconosciuto.
	 * @throws IOException
	 */
	public void inizializzaClient() throws UnknownHostException, IOException {
		this.socket = new Socket(host, porta);
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		this.socketTurno = new Socket(host, portaTurno);
		bufferedReaderTurno = new BufferedReader(new InputStreamReader(socketTurno.getInputStream()));
	}

	/**
	 * Metodo per ottenere la risposta dal Server.
	 * @return Una String contenente la risposta dal server.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String getRispostaServer() throws IOException, InterruptedException{
		risposta = bufferedReader.readLine();
		return risposta;
	}

	/**
	 * Metodo per ottenere i "@cambioTurno" dal server.
	 * @return Una String contenente il messaggio di cambioturno dal server.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String getBroadcastServerTurno() throws IOException, InterruptedException{
		rispostaTurno = bufferedReaderTurno.readLine();
		return rispostaTurno;
	}

	/**
	 * Metodo per registrarsi.
	 * @param nomeUtente String che rappresenta il nome dell'utente.
	 * @param password String che rappresenta la password.
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
	 * @param nomeUtente String che rappresenta il nome dell'utente.
	 * @param password String che rappresenta la password.
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
	 * @param nomeUtente String che rappresenta il nome dell'utente.
	 * @param password String che rappresenta la password.
	 * @param nome String che rappresenta il nome della specie.
	 * @param tipo String che rappresenta il tipo della razza, puo' essere "c" oppure "e".
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void creaRazza(String nomeUtente, String password,String nome, String tipo) throws IOException, InterruptedException{
		if(tipo.equals("c") || tipo.equals("e")) {
			bufferedWriter.flush();
			this.token = nomeUtente + "-" + password;
			richiesta = "@creaRazza,token=" + this.token + ",nome=" + nome + ",tipo=" + tipo;
			this.inviaAlServer();
		}
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
	 * @param idDino String che rappresenta l'id del Dinosauro.
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
	 * @param nomeUtente String che rappresenta il nome dell'utente.
	 * @param password String che rappresenta la password.
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
	 * @param idDino String che rappresenta l'id del Dinosauro.
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
	 * @param idDino String che rappresenta l'id del Dinosauro.
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
	 * @param idDino String che rappresenta l'id del Dinosauro.
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
	 * @param idDino String che rappresenta l'id del Dinosauro.
	 * @param riga int che indica la riga di destinazione.
	 * @param colonna int che indica la colonna di destinazione.
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
		this.gui.getTimer().cancel();
		bufferedWriter.flush();
		richiesta="@passaTurno,token="+token;
		this.inviaAlServer();
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

	/**
	 * @return Un String che rappresenta il nome dell'utente.
	 */
	public String getNomeUtente() {
		return nomeUtente;
	}

	/**
	 * @param nomeUtente String per impostare il nome dell'utente.
	 */
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
}
