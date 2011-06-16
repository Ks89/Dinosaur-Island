package isoladinosauri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import Eccezioni.CrescitaException;
import Eccezioni.DeposizioneException;
import Eccezioni.MovimentoException;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;
import isoladinosauri.modellodati.Vegetale;
import isoladinosauri.GestioneGiocatori;

/**
 * Classe che si occupa del protocollo di comunicazione tra Server e Client.
 */
public class ClientHandler extends Thread {

	private Socket socket;
	private static final int MAX = 40;
	private static final String ERRORE = "Errore lettura file";
	private Partita partita;
	private GestioneGiocatori gestioneGiocatori;
	private Classifica classifica;
	
	//variabili usate soltanto per l'ultimo dinosauro di ogni giocatore
	private boolean eseguitaMossa = false;
	private boolean eseguitaAzione = false;

	/**
	 * Costruttore della classe ClientHandler che inizializza gli attributi socket, partita, gestioneGiocatori e classifica.
	 * @param socket Riferimento al Socket.
	 * @param partita Riferimento alla Partita.
	 * @param gestioneGiocatori Riferimento a GestioneGiocatori contenenti le liste di utenti online e le razze create.
	 * @param classifica Classifica dei giocatori.
	 */
	public ClientHandler(Socket socket, Partita partita, GestioneGiocatori gestioneGiocatori,Classifica classifica) {
		this.socket = socket;
		this.partita = partita;
		this.gestioneGiocatori = gestioneGiocatori;
		this.classifica = classifica;
	}

	/**
	 * Metodo per eseguire ricerche nel file degli utenti.
	 * @param nomeFileFisico String che rappresenta il nome del file.
	 * @param parametro1
	 * @param parametro2
	 * @param carSplit
	 * @param posSplit
	 * @return Un boolean: 'true' - l'elemento e' stato trovato, 'false' - non e' stato trovato.
	 */
	private boolean cercaNelFile(String nomeFileFisico, String parametro1, String parametro2, String carSplit, int posSplit) {
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

	/**
	 * @param nomeFileFisico
	 * @param nomeUtente
	 * @param carSplit
	 * @param posSplit
	 * @return
	 */
	private String ottieniPassDaFile(String nomeFileFisico,String nomeUtente, String carSplit, int posSplit) {

		String password = null;
		try {
			FileReader fileReader = new FileReader(nomeFileFisico);
			BufferedReader br;
			br = new BufferedReader(fileReader);
			String rigaFile = br.readLine();
			rigaFile = br.readLine();
			while(rigaFile!=null) {
				if(rigaFile.split(carSplit)[posSplit].equals(nomeUtente)) {
					password = rigaFile.split(carSplit)[posSplit+1];
					break;				
				}
				rigaFile = br.readLine();
			}
			br.close();
		}
		catch(IOException ioException)
		{
			System.err.println(ERRORE);
		}
		return password;
	}

	
	/**
	 * @param nomeFileFisico
	 * @param riga
	 */
	private void scriviNelFile(String nomeFileFisico, String riga) {
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

	/**
	 * Metodo per individuare il Giocatore da un nome dell'utente.
	 * @param nomeUtente String che rappresenta il nome dell'utente.
	 * @return Un Giocatore che possiede lo stesso nome dell'Utente ad esso associato. 
	 * 		Se il Gicoatore non viene individuato restituisce null.
	 */
	private Giocatore individuaGiocatore (String nomeUtente) {
		int i=0;
		if(this.partita.getGiocatori().isEmpty()) {
			return null;
		} else {
			for(i=0; i<this.partita.getGiocatori().size();i++) {
				if(this.partita.getGiocatori().get(i).getUtente().getNomeUtente().equals(nomeUtente)) {
					return this.partita.getGiocatori().get(i);
				}
			}
		}
		return null;
	}

	/**
	 * Metodo per verificare se il Dinosauro (tramite il suo id) e' veramente in Partita.
	 * @param id String che rappresenta l'id del Dinosauro.
	 * @return Un boolean: 'true' - il Dinosauro e' in Partita, 'false' - il Dinosauro non e' in Partita.
	 */
	private boolean verificaIdDinosauro(String id) {
		if(Integer.parseInt(id)>=11 && Integer.parseInt(id)<=85) {
			for(int i=0;i<this.partita.getGiocatori().size();i++) {
				for(int j=0;j<this.partita.getGiocatori().get(i).getDinosauri().size();j++) {
					if(this.partita.getGiocatori().get(i).getDinosauri().get(j).getId().equals(id)) {
						return true;
					}
				}
			}
		} 
		return false;
	}

	/**
	 * Metodo per individuare un Dinosauro dal suo id.
	 * @param id String che rappresenta il l'id del Dinosauro.
	 * @return Un Dinosauro che possiede lo stesso id passato come parametro in ingresso. 
	 * 		Se il Dinosauro non viene individuato restituisce null.
	 */
	private Dinosauro individuaDinosauro (String id) {
		if(this.partita.getGiocatori().isEmpty()) {
			return null;
		} else {
			for(int i=0; i<this.partita.getGiocatori().size();i++) {
				for(int j=0; j<this.partita.getGiocatori().get(i).getDinosauri().size();j++) {
					if(this.partita.getGiocatori().get(i).getDinosauri().get(j).getId().equals(id)) {
						return this.partita.getGiocatori().get(i).getDinosauri().get(j);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Metodo per creare un Utente.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String creaUtente(String richiesta) {
		String domanda;
		String nickname = richiesta.split(",")[1].split("=")[1];
		String password = richiesta.split(",")[2].split("=")[1];
		
		// verifico se l'utente si trova in partital'utente e' gia' esistente
		boolean trovato = cercaNelFile("Utenti.txt",nickname,null," ",0);
		if(trovato) {
			domanda="@no,@usernameOccupato";
		}
		else {
			domanda="@ok";
			scriviNelFile("Utenti.txt",nickname+" "+password+"\n");
		}
		return domanda;
	}

	/**
	 * Metodo per eseguire il login.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String login(String richiesta) {
		String domanda = null;
		String nickname = richiesta.split(",")[1].split("=")[1];
		String password = richiesta.split(",")[2].split("=")[1];
		String token = nickname+"-"+password;
		//verifico se c'e' nel file Utenti.txt
		boolean trovato = cercaNelFile("Utenti.txt",nickname,password," ",0);
		if(trovato) {
			boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
			if(!loggato) {
				this.gestioneGiocatori.aggiungiTokenLoggati(nickname,token);
				domanda = "@ok,"+token;
			}
			else {
				domanda = "@no,@UTENTE_GIA_LOGGATO";
			}
		}
		else if(!trovato) {
			domanda = "@no,@autenticazioneFallita";
		}
		return domanda;
	}

	/**
	 * Metodo per creare una razza, cioe' un Giocatore.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String creaRazza(String richiesta) {
		String domanda = null;
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeRazza = richiesta.split(",")[2].split("=")[1];
		String tipoRazza = richiesta.split(",")[3].split("=")[1];
		String nomeUtente = token.split("-")[0];

		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			int turnoCorrente = 1;
			String password = this.ottieniPassDaFile("Utenti.txt", nomeUtente, " ", 0);
			Utente utente = new Utente(nomeUtente,password);
			Giocatore giocatore = new Giocatore(turnoCorrente, nomeRazza, tipoRazza);
			giocatore.setUtente(utente);
			this.gestioneGiocatori.aggiungiGiocatoreCreato(giocatore);
			domanda = "@ok";
		}
		else if(!loggato) {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	
	/**
	 * Metodo per accedere alla Partita.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String accessoPartita(String richiesta) {

		String domanda = null;
		int k=0;

		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		// cerco se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			// verifico se il giocatore ha gia' effettuato l'accesso alla partita
			boolean inPartita = false;
			if(this.individuaGiocatore(nomeUtente)!=null) {
				inPartita = true;
			}
			int nGiocatori = this.partita.getGiocatori().size();
			if(!inPartita && nGiocatori<8) {
				k = this.gestioneGiocatori.indiceGiocatoreCreato(nomeUtente);
				//aggiungo il giocatore alla partita
				Giocatore giocatore = this.gestioneGiocatori.ottieniGiocatoriCreati().get(k);
				giocatore.aggiungiInPartita(this.partita);
				this.classifica.aggiungiTuplaClassifica(giocatore);
				domanda = "@ok";
			}
			else if(nGiocatori>=8) {
				domanda = "@no,@troppiGiocatori";
			}
			else if(inPartita){
				domanda = "@no,@ACCESSO_GIA_EFFETTUATO";
			}
		}
		else {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per uscire dalla Partita.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String uscitaPartita(String richiesta) {
		String domanda = null;
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		int k=0;

		// cerco se il giocatore e' in partita
		boolean inPartita = false;
		if(this.individuaGiocatore(nomeUtente)!=null) {
			inPartita = true;
		}
		if(inPartita) {
			//cerco l'indice del giocatore nell'array dei giocatoriCreati
			k = this.gestioneGiocatori.indiceGiocatoreCreato(nomeUtente);
			//rimuovo il giocatore dalla partita
			Giocatore giocatore = this.gestioneGiocatori.ottieniGiocatoriCreati().get(k);
			this.partita.rimuoviGiocatore(giocatore);
			domanda = "@ok";
		}
		else if(!inPartita) {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per ottenere la lista dei Giocatori.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String listaGiocatori(String richiesta) {
		String domanda = null;

		String token = richiesta.split(",")[1].split("=")[1];
		//verifico se e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);

		if(loggato) {
			//accedo alla lista dei giocatori per fornire la lista dei giocatori in partita
			String listaGiocatori = new String();
			for(int i=0; i<this.partita.getGiocatori().size(); i++) {
				listaGiocatori = listaGiocatori.concat(",").concat(this.partita.getGiocatori().get(i).getUtente().getNomeUtente());
			}
			domanda = "@listaGiocatori"+listaGiocatori;
		}
		else if(!loggato) {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per eseguire il logout.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String logout(String richiesta) {
		String domanda = null;
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		int k;

		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);

		if(loggato) {
			//rimuovo il giocatore dalla lista login
			this.gestioneGiocatori.rimuoviTokenLoggati(nomeUtente,token);
			//cerco l'indice del giocatore nell'array dei giocatoriCreati
			k = this.gestioneGiocatori.indiceGiocatoreCreato(nomeUtente);
			//rimuovo il giocatore dalla partita
			Giocatore giocatore = this.gestioneGiocatori.ottieniGiocatoriCreati().get(k);
			this.partita.rimuoviGiocatore(giocatore);
			domanda = "@ok";
		}
		else if(!loggato) {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per ottenere la mappa generale (40x40).
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String mappaGenerale(String richiesta) {
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String domanda;
		Cella[][] mappa = this.partita.getIsola().getMappa();

		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			//verifico se e' in partita
			boolean inPartita = false;
			if(this.individuaGiocatore(nomeUtente)!=null) {
				inPartita = true;
			}
			if(inPartita) {
				Giocatore giocatore = this.individuaGiocatore(token.split("-")[0]);
				domanda = "@mappaGenerale,{40,40},";
				//traduco la mappa di tipo Cella in tipo String
				for(int i=MAX-1; i>=0; i--) {
					for(int j=0; j<MAX; j++) {
						if(!giocatore.getMappaVisibile()[i][j]) {
							domanda = domanda.concat("[b]");
						} else {
							if(mappa[i][j]==null) {
								domanda = domanda.concat("[a]");
							} else {
								if(mappa[i][j].getOccupante() instanceof Carogna) {
									domanda = domanda.concat("[c]");
								} else {
									if(mappa[i][j].getOccupante() instanceof Vegetale) {
										domanda = domanda.concat("[v]");
									} else {
										domanda = domanda.concat("[t]");
									}
								}
							}
						}
					}
					domanda = domanda.concat(";");
				}
			}
			else {
				domanda = "@no,@nonInPartita";
			}
		}
		else {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per ottenere la lista dei Dinosauro del Giocatore richiedente.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String listaDinosauri(String richiesta) {

		String token;
		String domanda = null;
		String listaDino=new String();

		token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			// verifico se l'utente si trova in partita
			Giocatore giocatore = this.individuaGiocatore(nomeUtente);
			if(giocatore!=null) { //se il giocatore e' in partita
				for(int k=0; k<giocatore.getDinosauri().size(); k++) {
					listaDino = listaDino.concat(",").concat(giocatore.getDinosauri().get(k).getId());
				}
			}
			else {
				//TODO: ?? dalle specifiche: ''..altrimenti non si e' in partita, e cio' e' definito dal simbolo +'' ??
			}
			domanda = "@listaDinosauri"+listaDino;
		}
		else if(!loggato) {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per ottenere la Classifica.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String classifica(String richiesta) {
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String domanda;
		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			//verifico se e' in partita
			boolean inPartita = false;
			if(this.individuaGiocatore(nomeUtente)!=null) {
				inPartita = true;
			}
			if(inPartita) {
				domanda = "@classifica" + classifica.ottieniClassifica();

			} else {
				domanda = "@no,@nonInPartita";
			}	
		} else {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per ottenere la vista locale del Dinosauro.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String vistaLocale(String richiesta) {
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String idDino = richiesta.split(",")[2].split("=")[1];
		String domanda = null;
		Cella[][] mappa = this.partita.getIsola().getMappa();

		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {

			if(this.individuaGiocatore(nomeUtente)!=null) { //se l'utente si trova in partita
				//				Giocatore giocatore = this.individuaGiocatore(token.split("-")[0]);
				Dinosauro dinosauro = this.individuaDinosauro(idDino);
				if(dinosauro!=null) {
					int[] vista = partita.getTurnoCorrente().ottieniVisuale(dinosauro.getRiga(), dinosauro.getColonna(), dinosauro.calcolaRaggioVisibilita());

					domanda = "@vistaLocale,{"+vista[0]+","+vista[1]+"}"+","+"{"+(vista[2]-vista[0]+1)+","+(vista[3]-vista[1]+1)+"},";
					//					for(int i=39; i>=0; i--) {
					//						for(int j=0; j<40; j++) {
					for(int i=vista[0]; i<vista[2]+1; i++) {
						for(int j=vista[1]; j<vista[3]+1; j++) {
							if(mappa[i][j]==null) {
								domanda = domanda.concat("[a]");
							} else {
								if(mappa[i][j].getDinosauro()!=null) {
									domanda = domanda.concat("[d,").concat(mappa[i][j].getDinosauro().getEnergia()+"").concat("]");
								} else {	
									if(mappa[i][j].getOccupante() instanceof Carogna) {
										Carogna carogna = (Carogna)mappa[i][j].getOccupante();
										domanda = domanda.concat("[c,").concat(carogna.getEnergia()+"").concat("]");
									} else {
										if(mappa[i][j].getOccupante() instanceof Vegetale) {
											Vegetale vegetale = (Vegetale)mappa[i][j].getOccupante();
											domanda = domanda.concat("[v,").concat(vegetale.getEnergia()+"").concat("]");
										} else {
											domanda = domanda.concat("[t]");
										}
									}
								}
							}
						}
						domanda = domanda.concat(";");
					}
				}
				else {
					domanda = "@no,@idNonValido";
				}
			}
			else {
				domanda = "@no,@nonInPartita";
			}
		}
		else {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per ottenere lo stato di un Dinosauro.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String statoDinosauro(String richiesta) {
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String idDino = richiesta.split(",")[2].split("=")[1];
		String domanda = null;
		//		Cella[][] mappa = this.partita.getIsola().getMappa();

		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			// verifico se l'utente si trova in partita
			boolean inPartita = false;
			if(this.individuaGiocatore(nomeUtente)!=null) {
				inPartita = true;
			}
			if(inPartita) {
				Giocatore giocatore = this.individuaGiocatore(token.split("-")[0]);
				Dinosauro dinosauro = this.individuaDinosauro(idDino);
				if(dinosauro!=null) {
					//cerco se il dinosauro appartiene al giocatore
					boolean proprioDino = false;
					for(int i=0; i<giocatore.getDinosauri().size(); i++) {
						if(giocatore.getDinosauri().get(i).getId().equals(idDino)) {
							proprioDino = true;
						}
					}
					String user = token.split("-")[0];
					String razza = giocatore.getNomeSpecie();
					String tipo = null;
					if(dinosauro instanceof Carnivoro) {
						tipo = "c";
					} else {
						if(dinosauro instanceof Erbivoro) {
							tipo = "e";
						}
					}
					int riga = dinosauro.getRiga();
					int colonna = dinosauro.getColonna();
					int dimensione = dinosauro.getEnergiaMax()/1000;
					if(proprioDino) {
						int energia = dinosauro.getEnergia();
						//FIXME: turni vissuti
						int turni = 1;//turni vissuti ??
						domanda = "@statoDinosauro"+","+user+","+razza+","+tipo+","+"{"+riga+","+colonna+"}"+","+dimensione+","+energia+","+turni;
					}
					else {
						domanda = "@statoDinosauro"+","+user+","+razza+","+tipo+","+"{"+riga+","+colonna+"}"+","+dimensione;
					}
				}
				else {
					domanda = "@no,@idNonValido";
				}
			}
			else {
				domanda = "@no,@nonInPartita";
			}
		}
		else {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per deporre un uovo.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String deponiUovo(String richiesta) {
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String idDino = richiesta.split(",")[2].split("=")[1];
		String domanda = null;

		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			// verifico se l'utente si trova in partita
			boolean inPartita = false;
			if(this.individuaGiocatore(nomeUtente)!=null) {
				inPartita = true;
			}
			if(inPartita) {
				Giocatore giocatore = this.individuaGiocatore(token.split("-")[0]);
				Dinosauro dinosauro = this.individuaDinosauro(idDino);
				if(dinosauro!=null) {
//					if(dinosauro.getEtaDinosauro()<dinosauro.getDurataVita()) {
//						//FIXME: eta' attuale e' il numero dei turni e non delle mosse
//						domanda = "@no,@raggiuntoLimiteMosseDinosauro";
//					} else {
						try {
							String idNuovoDinosauro = giocatore.eseguiDeposizionedeponiUovo(dinosauro);
							domanda = "@ok,"+idNuovoDinosauro;
							partita.nascitaDinosauro(1);
							
							if(this.verificaUltimoDinosauro(dinosauro)) {
								//se il dinosauro che ha compiuto l'azione e' l'ultimo nell'arrayList dei Dinosauri del Giocatore
								this.eseguitaAzione=true;
								//invia una notifica a tutti di cambiaTurno con lo username del giocatore che puo' cmpiere le sue mosse
							}
							
						} catch (DeposizioneException de){
							if(de.getCausa()==DeposizioneException.Causa.MORTE) {
								domanda = "@no,@mortePerInedia";							}
							if(de.getCausa()==DeposizioneException.Causa.SQUADRACOMPLETA) {
								domanda = "@no,@raggiuntoNumeroMaxDinosauri";
							}
						}
//					}
				}
				else {
					domanda = "@no,@idNonValido";
				}
			}
			else {
				domanda = "@no,@nonInPartita";
			}
		}
		else {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per far crescere un Dinosauro.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String cresciDinosauro(String richiesta) {
		String domanda = null;
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String idDino = richiesta.split(",")[2].split("=")[1];

		// verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		// verifico se l'utente si trova in partita
		boolean inPartita = false;
		if(this.individuaGiocatore(nomeUtente)!=null) {
			inPartita = true;
		}
		if(loggato) {
			if(inPartita) {
				if(this.verificaIdDinosauro(idDino)) {
					Dinosauro dinosauro = this.individuaDinosauro(idDino);

					if(dinosauro.getEtaDinosauro()<dinosauro.getDurataVita()) {						
						try {
							dinosauro.aumentaDimensione();
							domanda = "@ok";
							
							if(this.verificaUltimoDinosauro(dinosauro)) {
								//se il dinosauro che ha compiuto l'azione e' l'ultimo nell'arrayList dei Dinosauri del Giocatore
								this.eseguitaAzione=true;
								//invia una notifica a tutti di cambiaTurno con lo username del giocatore che puo' cmpiere le sue mosse
							}
							
						} catch (CrescitaException ce){
							if(ce.getCausa()==CrescitaException.Causa.MORTE) {
								domanda = "@no,@mortePerInedia";
							}
							if(ce.getCausa()==CrescitaException.Causa.DIMENSIONEMASSIMA) {
								domanda = "@no,@raggiuntaDimensioneMax";
							}
						}
					} else {
						domanda = "@no,@raggiuntoLimiteMosseDinosauro";
					}
				} else {
					domanda = "@no,@idNonValido";
				}
			} else {
				domanda = "@no,@nonInPartita";
			}
		} else {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}

	/**
	 * Metodo per muovere un Dinosauro.
	 * @param richiesta String che rappresenta la richiesta ottenuta dal Client.
	 * @return Una String con la risposta del Server.
	 */
	private String muoviDinosauro(String richiesta) {
		String token = richiesta.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String domanda = null;
		String idDino = richiesta.split(",")[2].split("=")[1];
		System.out.println(richiesta);
		System.out.println(richiesta.split("=")[3]);
		String destinazione = richiesta.split("=")[3].replace("{","").replace("}", ""); //espressa come "X,Y"
		int riga = Integer.parseInt(destinazione.split(",")[0]);
		int colonna = Integer.parseInt(destinazione.split(",")[1]); 
		System.out.println("riga, colonna " + riga + "," + colonna);

		// verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			// verifico se l'utente si trova in partita
			boolean inPartita = false;
			if(this.individuaGiocatore(nomeUtente)!=null) {
				inPartita = true;
			}
			if(inPartita) {
				if(this.verificaIdDinosauro(idDino)) {
					Dinosauro dinosauro = this.individuaDinosauro(idDino);
					if(dinosauro.getEtaDinosauro()<=dinosauro.getDurataVita()) {

						try {
							boolean statoMovimento = partita.getTurnoCorrente().spostaDinosauro(dinosauro, riga, colonna);
							if(statoMovimento) {
								domanda = "@ok";

								if(this.verificaUltimoDinosauro(dinosauro)) {
									//se il dinosauro mosso e' l'ultimo nell'arrayList dei Dinosauri del Giocatore
									this.eseguitaMossa=true;
									//invia una notifica a tutti di cambiaTurno con lo username del giocatore che puo' cmpiere le sue mosse
								}
			
							} else {
								System.out.println("Problema");
							}
						} catch (MovimentoException e){
							if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCATO) {
								domanda = "@ok,@combattimento,v";
							}
							if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCANTE) {
								domanda = "@ok,@combattimento,p";
							}
							if(e.getCausa()==MovimentoException.Causa.MORTE) {
								domanda = "@no,@mortePerInedia";
							}
							if(e.getCausa()==MovimentoException.Causa.DESTINAZIONEERRATA) {
								domanda = "@no,@destinazioneNonValida";
							}
						}
					} else {
						domanda = "@no,@raggiuntoLimiteMosseDinosauro";
					}
				} else {
					domanda = "@no,@idNonValido";
				}
			} else {
				domanda = "@no,@nonInPartita";
			}
		} else {
			domanda = "@no,@tokenNonValido";
		}
		return domanda;
	}
	
	
//	public String confermaTurno(String richiesta) {
//		String token = richiesta.split(",")[1].split("=")[1];
//		System.out.println(richiesta);
//
//		// verifico se l'utente e' loggato
//		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
//		if(loggato) {
//			// verifico se l'utente si trova in partita
//			boolean inPartita = false;
//			if(this.individuaGiocatore(nomeUtente)!=null) {
//				inPartita = true;
//			}
//			if(inPartita) {
//	
//				
//				
//			} else {
//				domanda = "@no,@nonInPartita";
//			}
//		} else {
//			domanda = "@no,@tokenNonValido";
//		}
//		return domanda;
//	}
	
	private boolean verificaUltimoDinosauro (Dinosauro dinosauro) {
		for(int i=0;i<this.partita.getGiocatori().size();i++) {
			for(int j=0;j<this.partita.getGiocatori().get(i).getDinosauri().size();j++) {
				if(dinosauro.equals(this.partita.getGiocatori().get(i).getDinosauri().get(j)) &&
						j==this.partita.getGiocatori().get(i).getDinosauri().size()-1) {
					return true;
				}
			}
		}
		return false;
	}
	

	public void run() {

		String comando = null;
		String domanda;

		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				String richiesta = bufferedReader.readLine();
				if(richiesta!=null) {
					comando = richiesta.split(",")[0];
				}
				if (comando.equals("@creaUtente")) {
					domanda = creaUtente(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@login")) {
					domanda=login(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@creaRazza")) {
					domanda=creaRazza(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@accessoPartita")) {
					domanda=accessoPartita(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@uscitaPartita")) {
					domanda=uscitaPartita(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@listaGiocatori")) {
					domanda=listaGiocatori(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@logout")) {
					domanda=logout(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@mappaGenerale")) {
					domanda=mappaGenerale(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@listaDinosauri")) {
					domanda = listaDinosauri(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@classifica")) {
					domanda = classifica(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@vistaLocale")) {
					domanda = vistaLocale(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@statoDinosauro")) {
					domanda = statoDinosauro(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@muoviDinosauro")) {
					domanda = muoviDinosauro(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@cresciDinosauro")) {
					domanda = cresciDinosauro(richiesta);
					bufferedWriter.write(domanda);
				} else if (comando.equals("@deponiUovo")) {
					domanda = deponiUovo(richiesta);
					bufferedWriter.write(domanda);
				} else {
					bufferedWriter.write("@unknownCommand");
				}
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		}
		catch (IOException e) {
			System.out.println("Errore durante la connessione col Client.");
		}
	}
}