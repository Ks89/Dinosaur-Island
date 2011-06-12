package isoladinosauri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;
import isoladinosauri.modellodati.Vegetale;
import isoladinosauri.GestioneGiocatori;

public class ClientHandler extends Thread {

	private Socket socket;
	private static final int MAX = 40;
	private static final String ERRORE = "Errore lettura file";
	private Partita partita;
//	private Utente utente;
	private GestioneGiocatori gestioneGiocatori;

	public ClientHandler(Socket socket, Partita partita, GestioneGiocatori gestioneGiocatori) {
		this.socket = socket;
		this.partita = partita;
		this.gestioneGiocatori = gestioneGiocatori;
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
	
	public String ottieniPassDaFile(String nomeFileFisico,String nomeUtente, String carSplit, int posSplit) {
	
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
	
	private boolean verificaIdDinosauro(String id) {
		if(Integer.parseInt(id)>=11 && Integer.parseInt(id)<=85) {
			for(int i=0;i<this.partita.getGiocatori().size();i++) {
				for(int j=0;j<this.partita.getGiocatori().size();j++) {
					if(this.partita.getGiocatori().get(i).getDinosauri().get(j).getId().equals(id)) {
						return true;
					}
				}
			}
		} 
		return false;
	}
	
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
	
	public String creaUtente(String request) {
		String answer;
		String nickname = request.split(",")[1].split("=")[1];
		String password = request.split(",")[2].split("=")[1];

		// verifico se ci sono gia' utenti registrati nel file e li creo nella classe utente
//		this.registraUtentiFile("Utenti.txt"," ",0);
		
		// verifico se l'utente si trova in partital'utente e' gia' esistente
		boolean trovato = cercaNelFile("Utenti.txt",nickname,null," ",0);
		if(trovato) {
			answer="@no,@usernameOccupato";
		}
		else {
			answer="@ok";
			scriviNelFile("Utenti.txt",nickname+" "+password+"\n");
//			System.out.println("Dati: " + nickname + "," + password);
//			this.utente = new Utente(nickname,password);
//			System.out.println("Dati: " + this.utente.getNomeUtente() + "," + this.utente.getPassword());
		}
		return answer;
	}

	public String login(String request) {
		String answer = null;
		String nickname = request.split(",")[1].split("=")[1];
		String password = request.split(",")[2].split("=")[1];
		String token = nickname+"-"+password;
		//verifico se c'e' nel file Utenti.txt
		boolean trovato = cercaNelFile("Utenti.txt",nickname,password," ",0);
		if(trovato) {
			boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
			if(!loggato) {
				//aggiungo l'utente in partita
				
				this.gestioneGiocatori.aggiungiTokenLoggati(nickname,token);
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
		String answer = null;
		String token = request.split(",")[1].split("=")[1];
		String nomeRazza = request.split(",")[2].split("=")[1];
		String tipoRazza = request.split(",")[3].split("=")[1];
		String nomeUtente = token.split("-")[0];

		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			int turnoCorrente = 1; //solo per test
			String password = this.ottieniPassDaFile("Utenti.txt", nomeUtente, " ", 0);
			Utente utente = new Utente(nomeUtente,password);
			Giocatore giocatore = new Giocatore(turnoCorrente, nomeRazza, tipoRazza);
			giocatore.setUtente(utente);
//			System.out.println("Utente: " + this.utente.getNomeUtente());
			this.gestioneGiocatori.aggiungiGiocatoreCreato(giocatore);
			answer = "@ok";
		}
		else if(!loggato) {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}

	public String accessoPartita(String request) {

		String answer = null;
		int k=0;

		String token = request.split(",")[1].split("=")[1];
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
//				this.partita.aggiungiGiocatore(giocatore);
				giocatore.aggiungiInPartita(this.partita);
				answer = "@ok";
			}
			else if(nGiocatori>=8) {
				answer = "@no,@troppiGiocatori";
			}
			else if(inPartita){
				answer = "@no,@ACCESSO_GIA_EFFETTUATO";
			}
		}
		else {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}

	public String uscitaPartita(String request) {
		String answer = null;
		String token = request.split(",")[1].split("=")[1];
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
			answer = "@ok";
		}
		else if(!inPartita) {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}

	public String listaGiocatori(String request) {
		String answer = null;

		String token = request.split(",")[1].split("=")[1];
		//verifico se e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);

		if(loggato) {
			//accedo alla lista dei giocatori per fornire la lista dei giocatori in partita
			String listaGiocatori = null;
			for(int i=0; i<this.partita.getGiocatori().size(); i++) {
				listaGiocatori = listaGiocatori.concat(",").concat(this.partita.getGiocatori().get(i).getUtente().getNomeUtente());
			}
			answer = "@listaGiocatori"+listaGiocatori;
		}
		else if(!loggato) {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}

	public String logout(String request) {
		String answer = null;
		String token = request.split(",")[1].split("=")[1];
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
				answer = "@ok";
			}
			else if(!loggato) {
				answer = "@no,@tokenNonValido";
			}
		return answer;
	}

	public String mappaGenerale(String request) {
		String token = request.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String answer;
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
				answer = "@mappaGenerale,{40,40},";
				//traduco la mappa di tipo Cella in tipo String
				for(int i=MAX-1; i>=0; i--) {
					for(int j=0; j<MAX; j++) {
						if(!giocatore.getMappaVisibile()[i][j]) {
							answer = answer.concat("[b]");
						} else {
							if(mappa[i][j]==null) {
								answer = answer.concat("[a]");
							} else {
								if(mappa[i][j].getOccupante() instanceof Carogna) {
									answer = answer.concat("[c]");
								} else {
									if(mappa[i][j].getOccupante() instanceof Vegetale) {
										answer = answer.concat("[v]");
									} else {
										answer = answer.concat("[t]");
									}
								}
							}
						}
					}
					answer = answer.concat(";");
				}
			}
			else {
				answer = "@no,@nonInPartita";
			}
		}
		else {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}
	
	public String listaDinosauri(String request) {

		String token;
		String answer = null;
		String listaDino = new String();

		token = request.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			// verifico se l'utente si trova in partita
			if(this.individuaGiocatore(nomeUtente)!=null) { //se il giocatore e' in partita
				Giocatore giocatore = this.individuaGiocatore(token.split("-")[0]);
				for(int k=0; k<giocatore.getDinosauri().size(); k++) {
					listaDino.concat(","+giocatore.getDinosauri().get(k).getId());
				}
			}
			else {
				//TODO: ?? dalle specifiche: ''..altrimenti non si e' in partita, e cio' e' definito dal simbolo +'' ??
			}
			answer = "@listaDinosauri"+listaDino;
		}
		else if(!loggato) {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}
	
	public String vistaLocale(String request) {
		String token = request.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String idDino = request.split(",")[2].split("=")[1];
		String answer = null;
		Cella[][] mappa = this.partita.getIsola().getMappa();

		//verifico se l'utente e' loggato
		boolean loggato = this.gestioneGiocatori.controlloSeLoggato(token);
		if(loggato) {
			
			if(this.individuaGiocatore(nomeUtente)!=null) { //se l'utente si trova in partita
//				Giocatore giocatore = this.individuaGiocatore(token.split("-")[0]);
				Dinosauro dinosauro = this.individuaDinosauro(idDino);
				if(dinosauro!=null) {
					int[] vista = partita.getTurnoCorrente().ottieniVisuale(dinosauro.getRiga(), dinosauro.getColonna(), dinosauro.calcolaRaggioVisibilita());
					
					answer = "@vistaLocale,{"+vista[0]+","+vista[1]+"}"+","+"{"+(vista[2]-vista[0]+1)+","+(vista[3]-vista[1]+1)+"},";
//					for(int i=39; i>=0; i--) {
//						for(int j=0; j<40; j++) {
					for(int i=vista[2]; i>=vista[0]; i--) {
						for(int j=vista[1]; j<vista[3]+1; j++) {
							if(mappa[i][j]==null) {
								answer = answer.concat("[a]");
							} else {
								if(mappa[i][j].getDinosauro()!=null) {
									answer = answer.concat("[d,").concat(mappa[i][j].getDinosauro().getEnergia()+"").concat("]");
								} else {	
									if(mappa[i][j].getOccupante() instanceof Carogna) {
										Carogna carogna = (Carogna)mappa[i][j].getOccupante();
										answer = answer.concat("[c,").concat(carogna.getEnergia()+"").concat("]");
									} else {
										if(mappa[i][j].getOccupante() instanceof Vegetale) {
											Vegetale vegetale = (Vegetale)mappa[i][j].getOccupante();
											answer = answer.concat("[v,").concat(vegetale.getEnergia()+"").concat("]");
										} else {
											answer = answer.concat("[t]");
										}
									}
								}
							}
						}
						answer = answer.concat(";");
					}
				}
				else {
					answer = "@no,@idNonValido";
				}
			}
			else {
				answer = "@no,@nonInPartita";
			}
		}
		else {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}
	
	public String statoDinosauro(String request) {
		String token = request.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String idDino = request.split(",")[2].split("=")[1];
		String answer = null;
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
						answer = "@statoDinosauro"+","+user+","+razza+","+tipo+","+"{"+riga+","+colonna+"}"+","+dimensione+","+energia+","+turni;
					}
					else {
						answer = "@statoDinosauro"+","+user+","+razza+","+tipo+","+"{"+riga+","+colonna+"}"+","+dimensione;
					}
				}
				else {
					answer = "@no,@idNonValido";
				}
			}
			else {
				answer = "@no,@nonInPartita";
			}
		}
		else {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}

	public String deponiUovo(String request) {
		String token = request.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String idDino = request.split(",")[2].split("=")[1];
		String idDinoNato = null;
		String answer = null;
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
					if(dinosauro.getEtaDinosauro()<dinosauro.getDurataVita()) {
						//FIXME: eta' attuale e' il numero dei turni e non delle mosse
						answer = "@no,@raggiuntoLimiteMosseDinosauro";
					} else {
						int stato = giocatore.eseguiDeposizionedeponiUovo(dinosauro);
						switch (stato) {
						case 0:
							answer = "@no,@raggiuntoNumeroMaxDinosauri";
							break;
						case 1:
							//FIXME: fornire l'id del nuovo dinosauro nato
							answer = "@ok,"+idDinoNato;
							break;
						case -2:
							answer = "@no,@mortePerInedia";
							break;
						}
					}
				}
				else {
					answer = "@no,@idNonValido";
				}
			}
			else {
				answer = "@no,@nonInPartita";
			}
		}
		else {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}

	public String cresciDinosauro(String request) {
		String answer = null;
		String token = request.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String idDino = request.split(",")[2].split("=")[1];

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
						int statoCrescita = dinosauro.aumentaDimensione();
						if(statoCrescita==1) {
							//azione di crescita eseguita correttamente
							answer = "@ok";
						} else {
							if(statoCrescita==0) {
								answer = "@no,@raggiuntaDimensioneMax";
							} else { //se stato crescita e' ==-1 il dinosauro deve essere rimosso perche' sebza energia
								answer = "@no,@mortePerInedia";
							}
						}
					} else {
						answer = "@no,@raggiuntoLimiteMosseDinosauro";
					}
				} else {
					answer = "@no,@idNonValido";
				}
			} else {
				answer = "@no,@nonInPartita";
			}
		} else {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}

	public String muoviDinosauro(String request) {
		String token = request.split(",")[1].split("=")[1];
		String nomeUtente = token.split("-")[0];
		String answer = null;
		String idDino = request.split(",")[2].split("=")[1];
		System.out.println(request);
		System.out.println(request.split("=")[3]);
		String destinazione = request.split("=")[3].replace("{","").replace("}", ""); //espressa come "X,Y"
		int riga = Integer.parseInt(destinazione.split(",")[0]);
		int colonna = Integer.parseInt(destinazione.split(",")[1]); 
		System.out.println("riga, colonna " + riga + "," + colonna);
		
		this.partita.getIsola().stampaMappaRidotta();
		
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
						int statoMovimento = partita.getTurnoCorrente().spostaDinosauro(dinosauro, riga, colonna);
						switch(statoMovimento) {
						case -2:
							answer = "@no,@mortePerInedia";
							break;
						case -1:
							answer = "@no,@destinazioneNonValida";
							break;
						case 0:
							answer = "@ok,@combattimento,p";	
							break;
						case 1:
							answer = "@ok";
							break;
						case 2:
							answer = "@ok,@combattimento,v";
							break;
						}
					} else {
						answer = "@no,@raggiuntoLimiteMosseDinosauro";
					}
				} else {
					answer = "@no,@idNonValido";
				}
			} else {
				answer = "@no,@nonInPartita";
			}
		} else {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}



	public void run() {

		String comando = null;
		String answer;

		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				String request = bufferedReader.readLine();
				if(request!=null) {
					comando = request.split(",")[0];
				}
//				FIXME: non si dovrebbe verificare mai, quindi si puo' togliere
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
				else if (comando.equals("@mappaGenerale")) {
					answer=mappaGenerale(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@listaDinosauri")) {
					answer = listaDinosauri(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@vistaLocale")) {
					answer = vistaLocale(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@statoDinosauro")) {
					answer = statoDinosauro(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@muoviDinosauro")) {
					answer = muoviDinosauro(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@cresciDinosauro")) {
					answer = cresciDinosauro(request);
					bufferedWriter.write(answer);
				}
				else if (comando.equals("@deponiUovo")) {
					answer = deponiUovo(request);
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