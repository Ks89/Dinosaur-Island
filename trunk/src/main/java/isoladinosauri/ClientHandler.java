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

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;
import isoladinosauri.modellodati.Vegetale;

public class ClientHandler extends Thread {

	private Socket socket;
	private static final int MAX = 40;
	private static final String ERRORE = "Errore lettura file";
	private Partita partita;
	private Utente utente;

	public ClientHandler(Socket socket, Partita partita) {
		this.socket = socket;
		this.partita = partita;
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
		String answer;
		String nickname = request.split(",")[1].split("=")[1];
		String password = request.split(",")[2].split("=")[1];

		//cerco nel file Utenti.txt se l'utente e' gia' esistente
		boolean trovato = cercaNelFile("Utenti.txt",nickname,null," ",0);
		if(trovato) {
			answer="@no,@usernameOccupato";
		}
		else {
			answer="@ok";
			scriviNelFile("Utenti.txt",nickname+" "+password+"\n");
			System.out.println("Dati: " + nickname + "," + password);
			this.utente = new Utente(nickname,password);
			System.out.println("Dati: " + this.utente.getNomeUtente() + "," + this.utente.getPassword());
		}
		return answer;
	}

	public String login(String request) {
		String token;
		String answer = new String();	 
		String nickname = request.split(",")[1].split("=")[1];
		String password = request.split(",")[2].split("=")[1];

		//verifico se c'e' nel file Utenti.txt
		boolean trovato = cercaNelFile("Utenti.txt",nickname,password," ",0);
		if(trovato) {
			//cerco se il token e' gia' loggato
			boolean trovatoToken = cercaNelFile("Token.txt",nickname,null," ",0);
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
		String answer = new String();
		String token = request.split(",")[1].split("=")[1];
		String nomeRazza = request.split(",")[2].split("=")[1];
		String tipoRazza = request.split(",")[3].split("=")[1];

		//verifico se c'e' il token nel file Token.txt
		boolean trovato = cercaNelFile("Token.txt",token,null," ",1);
		if(trovato) {
			int turnoCorrente = 1; //solo per test
			Giocatore giocatore = new Giocatore(this.partita, turnoCorrente, nomeRazza, tipoRazza);
			giocatore.setUtente(this.utente);
			System.out.println("Utente: " + this.utente.getNomeUtente());
			this.partita.aggiungiGiocatore(giocatore);
			answer = "@ok";
		}
		else if(!trovato) {
			answer = "@no,@tokenNonValido";
		}
		return answer;
	}

	public String accessoPartita(String request) {

		String nomeUtente = new String();
		String answer = new String();
		int nGiocatori = 0;

		String token = request.split(",")[1].split("=")[1];
		try { // cerco se il token e' valido
			FileReader fileToken = new FileReader("Token.txt");
			BufferedReader br = new BufferedReader(fileToken);
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
			boolean trovatoTokenInPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
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
		String answer = new String();
		String token = request.split(",")[1].split("=")[1];

		// cerco se il token e' in partita
		boolean trovato = cercaNelFile("TokenInPartita.txt",token,null," ",1);
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
		String answer = new String();

		String token = request.split(",")[1].split("=")[1];
		//verifico se c'e' nel file Token.txt
		boolean trovato = false;
		trovato = cercaNelFile("Token.txt",token,null," ",1);
		try {
			if(trovato) {
				//accedo al file tokenInPartita.txt per fornire la lista dei giocatori in partita
				String listaGiocatori = new String ();
				FileReader fileTokenInPartita = new FileReader("TokenInPartita.txt");
				BufferedReader br = new BufferedReader(fileTokenInPartita);
				String rigaFile = br.readLine();
				while(rigaFile!=null) {
					listaGiocatori = listaGiocatori.concat(",").concat(rigaFile.split(" ")[0]); //FIXME nn so se va
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
		String answer = new String();
		String token = request.split(",")[1].split("=")[1];

		//verifico se c'e' nel file Token.txt
		boolean trovato = cercaNelFile("Token.txt",token,null," ",1);
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

	private Giocatore individuaGiocatore (String nomeUtente) {
		int i=0;
		if(this.partita.getGiocatori().isEmpty()) {
			return null;
		} else {
			for(i=0; i<this.partita.getGiocatori().size();i++) {
				System.out.println(partita.getGiocatori());
				System.out.println(partita.getGiocatori().get(i));
				System.out.println(partita.getGiocatori().get(i).getUtente());
				System.out.println(partita.getGiocatori().get(i).getUtente().getNomeUtente());

				if(partita.getGiocatori().get(i).getUtente().getNomeUtente().equals(nomeUtente)) {
					break;
				}
			}
		}
		return this.partita.getGiocatori().get(i);
	}

	public String mappaGenerale(String request) {
		String token = request.split(",")[1].split("=")[1];
		String answer;
		Cella[][] mappa = this.partita.getIsola().getMappa();

		//verifico se c'e' nel file Token.txt
		boolean trovatoToken = cercaNelFile("Token.txt",token,null," ",1);
		if(trovatoToken) {
			//accedo al file TokenInPartita.txt per verificare se e' in partita
			boolean trovatoInPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
			if(trovatoInPartita) {

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
	
	private boolean verificaIdDinosauro(String id) {
		if(Integer.parseInt(id)>=11 && Integer.parseInt(id)<=85) {
			for(int i=0;i<partita.getGiocatori().size();i++) {
				for(int j=0;j<partita.getGiocatori().size();j++) {
					if(partita.getGiocatori().get(i).getDinosauri().get(j).getId().equals(id)) {
						return true;
					}
				}
			}
		} 
		return false;
	}
	
	public String listaDinosauri(String request) {

		String token;
		String answer = new String();
		String listaDino = new String();

		token = request.split(",")[1].split("=")[1];
		//verifico se c'e' nel file Token.txt
		boolean trovato = cercaNelFile("Token.txt",token,null," ",1);
		if(trovato) {
			//accedo al file tokenInPartita.txt per verificare se l'utente si trova in partita
			boolean inPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
			if(inPartita) {
				Giocatore giocatore = this.individuaGiocatore(token.split("-")[0]);
				for(int k=0; k<giocatore.getDinosauri().size(); k++) {
					listaDino += ","+giocatore.getDinosauri().get(k).getId();
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
		return answer;
	}
	
	public String vistaLocale(String request) {
		String token = request.split(",")[1].split("=")[1];
		String idDino = request.split(",")[2].split("=")[1];
		String answer = new String();
		Cella[][] mappa = this.partita.getIsola().getMappa();

		//verifico se c'e' nel file Token.txt
		boolean trovatoToken = cercaNelFile("Token.txt",token,null," ",1);
		if(trovatoToken) {
			//accedo al file TokenInPartita.txt per verificare se e' in partita
			boolean trovatoInPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
			if(trovatoInPartita) {

				Giocatore giocatore = this.individuaGiocatore(token.split("-")[0]);
				Dinosauro dinosauro = this.individuaDinosauro(idDino);
				if(dinosauro!=null) {
					int[] vista = partita.getTurnoCorrente().ottieniVisuale(dinosauro.getRiga(), dinosauro.getColonna(), dinosauro.calcolaRaggioVisibilita());
					int maxR = vista[2]-vista[0];
					int maxC = vista[3]-vista[1];
					answer = "@vistaLocale,{"+vista[0]+","+vista[1]+"}"+","+"{"+vista[2]+","+vista[3]+"}";
					for(int i=maxR-1; i>vista[0]; i--) {
						for(int j=vista[1]; j<maxC; j++) {
							if(mappa[i][j]==null) {
								answer = answer.concat("[a]");
							} else {
								if(mappa[i][j].getOccupante() instanceof Dinosauro) {
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
		String idDino = request.split(",")[2].split("=")[1];
		String answer = new String();
//		Cella[][] mappa = this.partita.getIsola().getMappa();

		//verifico se c'e' nel file Token.txt
		boolean trovatoToken = cercaNelFile("Token.txt",token,null," ",1);
		if(trovatoToken) {
			//accedo al file TokenInPartita.txt per verificare se e' in partita
			boolean trovatoInPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
			if(trovatoInPartita) {
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
					String tipo = new String();
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
		String idDino = request.split(",")[2].split("=")[1];
		String idDinoNato =  new String();
		String answer = new String();
		//		Cella[][] mappa = this.partita.getIsola().getMappa();

		//verifico se c'e' nel file Token.txt
		boolean trovatoToken = cercaNelFile("Token.txt",token,null," ",1);
		if(trovatoToken) {
			//accedo al file TokenInPartita.txt per verificare se e' in partita
			boolean trovatoInPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
			if(trovatoInPartita) {
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

	public String cresciDinosauro(String request) {
		String answer = new String();
		String token = request.split(",")[1].split("=")[1];
		String idDino = request.split(",")[2].split("=")[1];

		//verifico se c'e' nel file Token.txt
		boolean trovatoToken = cercaNelFile("Token.txt",token,null," ",1);
		boolean trovatoInPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
		if(trovatoToken) {
			if(trovatoInPartita) {
				if(this.verificaIdDinosauro(idDino)) {
					Dinosauro dinosauro = this.individuaDinosauro(idDino);

					if(dinosauro.getEtaDinosauro()>=dinosauro.getDurataVita()) {
						int statoCrescita = dinosauro.aumentaDimensione();
						if(statoCrescita==1) {
							//azione di crescita eseguita correttamente
							answer = "@ok";
						} else {
							if(statoCrescita==0) {
								answer = "@no[,@raggiuntaDimensioneMax]";
							} else { //se stato crescita e' ==-1 il dinosauro deve essere rimosso perche' sebza energia
								answer = "@no[,@mortePerInedia]";
							}
						}
					} else {
						answer = "@no[,@raggiuntoLimiteMosseDinosauro]";
					}
				} else {
					answer = "@no[,@idNonValido]";
				}
			} else {
				answer = "@no[,@nonInPartita]";
			}
		} else {
			answer = "@no[,@tokenNonValido]";
		}
		return answer;
	}

	public String muoviDinosauro(String request) {
		String token = request.split(",")[1].split("=")[1];
		String answer = new String();
		String idDino = request.split(",")[2].split("=")[1];
		String destinazione = request.split("{")[1].split("}")[0]; //espressa come "X,Y"
		int riga = Integer.parseInt(destinazione.split(",")[0]);
		int colonna = Integer.parseInt(destinazione.split(",")[1]); 

		//verifico se c'e' nel file Token.txt
		boolean trovatoToken = cercaNelFile("Token.txt",token,null," ",1);
		boolean trovatoInPartita = cercaNelFile("TokenInPartita.txt",token,null," ",1);
		if(trovatoToken) {
			if(trovatoInPartita) {
				if(this.verificaIdDinosauro(idDino)) {
					Dinosauro dinosauro = this.individuaDinosauro(idDino);
					if(dinosauro.getEtaDinosauro()>=dinosauro.getDurataVita()) {
						int statoMovimento = partita.getTurnoCorrente().spostaDinosauro(dinosauro, riga, colonna);
						switch(statoMovimento) {
						case -2:
							answer = "@no[,@mortePerInedia]";
							break;
						case -1:
							answer = "@no[,@destinazioneNonValida]";
							break;
						case 0:
							answer = "@ok[,@combattimento,p]";	
							break;
						case 2:
							answer = "@ok[,@combattimento,v]";
							break;
						}
					} else {
						answer = "@no[,@raggiuntoLimiteMosseDinosauro]";
					}
				} else {
					answer = "@no[,@idNonValido]";
				}
			} else {
				answer = "@no[,@nonInPartita]";
			}
		} else {
			answer = "@no[,@tokenNonValido]";
		}
		return answer;
	}



	public void run() {

		String comando = new String();
		String answer;

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