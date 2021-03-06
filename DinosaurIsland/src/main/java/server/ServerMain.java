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

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.ServerMain;
import server.logica.CaricamentoMappa;
import server.logica.Cella;
import server.logica.Classifica;
import server.logica.GenerazioneMappa;
import server.logica.Isola;
import server.logica.Partita;
import server.logica.Turno;
import server.rmi.Implementazione;


/**
 * Classe per la gestione del Server.
 */
public class ServerMain {

	private int porta;
	private int portaRmi;
	private Partita partita;
	private GestioneGiocatori gestioneGiocatori;
	private Classifica classifica;

	
	/**
	 * Costruttore della classe ServerMultiClient che inizializza la mappa, la Partita, la Classifica ed il Turno.
	 * @param porta int che rappresenta la porta di comunicazione.
	 */
	public ServerMain(int porta, int portaRmi) {
		this.porta = porta;
		this.portaRmi = portaRmi;
		GenerazioneMappa gm = new GenerazioneMappa();
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle = cm.caricaMappa(gm.creaMappaCasuale());

//		Cella[][] mappaCelle = cm.caricaDaFile("mappaTestAcquaUovo.txt");
		this.partita = new Partita(new Isola(mappaCelle));
		Turno t = new Turno(partita);
		partita.setTurnoCorrente(t);
		this.gestioneGiocatori = new GestioneGiocatori();
		this.classifica = new Classifica(this.partita);
	}

	
	/**
	 * Metodo per la gestione delle connessioni.
	 * @throws IOException Eccezione duvuta ad un errore nell'apertura di una nuova connessione con un Client.
	 */
	public void runServer() throws IOException {	
		
		//RMI
		//LocateRegistry viene usato per creare un riferimento al registro remoto su un host.
		//createRegistry crea ed esporta un'istaza del registro sul client (specificando la porta)
		//registry e' un'interfaccia remota usata in rmi
	    Registry reg = LocateRegistry.createRegistry(portaRmi);
	    reg.rebind("isola-rmi", new Implementazione()); 
	    //rebind: sostiuisce il legame per il nome specificato nel registro col riferimento remoto.
	    //La classe con l'implementazione viene registrata sul server RMI
	    System.out.println("RMI avviato su porta " + portaRmi);
	    
	    
	    
		//socket
		ServerSocket serverSocket = new ServerSocket(porta);
		ServerSocket serverSocketTurno = new ServerSocket(5678);
		System.out.println("Server avviato. In attesa di connessioni...");
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				Socket socketTurno = serverSocketTurno.accept();
				System.out.println("Client connesso, creazione di un nuovo client handler.");
				new GestoreClient(socket, socketTurno, partita,gestioneGiocatori,classifica).start();
			} catch (IOException e) {
				System.out.println("Errore durante l'apertura di una connessione.");
			}
			System.out.println("In attesa di una nuova connessione...");
		}
	}
	
	
	/**
	 * Metodo che avvia il Server.
	 */
	public static void main(String[] args) {
		ServerMain serverMultiClient = new ServerMain(1234,1099);
		try {
			serverMultiClient.runServer();
		} catch (IOException e) {
			System.out.println("Errore durante l'apertuta di socket.");
			System.out.println("Terminazione.");
		}

	}

}
