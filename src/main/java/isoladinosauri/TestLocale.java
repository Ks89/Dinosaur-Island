package isoladinosauri;

import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Vegetale;

import java.util.Scanner;

public class TestLocale {
	public static void main(String[] args) {

		System.out.println("Benvenuto in Isola dei Dinosauri Beta1");
		Scanner input = new Scanner(System.in);
		Isola i = new Isola();
		Partita p = new Partita(i);
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		i.caricaMappa();
		int scelta,scelta1;
		int turnoCorrente=1;
		int conteggioDinosauro=0;
		int conteggioGiocatori=0;

		do { //inzia la partita e termina solo quando resta un giocatore
			conteggioGiocatori=0;
			System.out.println("Menu:");
			System.out.println("[1]: Aggiunge un giocatore [OK]");
			System.out.println("[2]: Rimuove un giocatore [OK]");
			System.out.println("[3]: Esegue i turni dei giocatori, uno ad uno in sequenza [BETA - mancano un sacco di funzioni]");
			System.out.println("[4]: Stampa la classifica [OK]");
			System.out.println("[5]: Stampa tutte le mappe utili per i test[OK]");
			scelta1 = input.nextInt();
			conteggioDinosauro=0;
			switch(scelta1) {
			case 1 :
				//aggiungo un giocatore
				String nickname;
				String password;
				String nomeSpecie;
				int tipo;
				String tipoDinosauro;
				nickname = input.nextLine();
				System.out.println();
				System.out.println("Inserisci il nickname per il login: ");
				nickname = input.nextLine();
				System.out.println("Inserisci la password per il login: ");
				password = input.nextLine();
				System.out.println("Inserisci il nome della specie: ");
				nomeSpecie = input.nextLine();
				System.out.println("Inserisci 0 per carnivoro oppure 1 per erbivoro: ");
				tipo = input.nextInt();
				if(tipo==0) tipoDinosauro = "carnivoro";
				else tipoDinosauro = "erbivoro";

				Giocatore giocatore =  new Giocatore(p, nickname, password, turnoCorrente, nomeSpecie, tipoDinosauro);
				p.aggiungiTuplaClassifica(giocatore);
				break;
			case 2 :
				//rimuovo il giocatore
				System.out.println("Inserisci il nickname del giocatore da rimuovere: ");
				String rimozione = input.nextLine();
				rimozione = input.nextLine();
				p.rimuoviGiocatore(cercaGiocatore(rimozione,p));
				conteggioGiocatori=9999; //per far terminare i while e far ricominciare dall'inizio
				break;
			case 3 :
				//usa il giocatore successivo

				do {
					conteggioDinosauro=0;
					System.out.println();
					System.out.println("**********GIOCATORE************");
					System.out.println("ID:\t\t" + p.getGiocatori().get(conteggioGiocatori).getIdGiocatore());
					System.out.println("Nome:\t\t" + p.getGiocatori().get(conteggioGiocatori).getNomeUtente());
					System.out.println("Specie:\t\t" + p.getGiocatori().get(conteggioGiocatori).getNomeSpecie());
					System.out.println("Eta':\t\t" + p.getGiocatori().get(conteggioGiocatori).getEtaAttuale());
					System.out.println("Turno nascita:\t" + p.getGiocatori().get(conteggioGiocatori).getTurnoNascita());
					System.out.print("Dinosauri:\t");
					for(int j=0;j<p.getGiocatori().get(conteggioGiocatori).getDinosauri().size();j++) {
						System.out.print(p.getGiocatori().get(conteggioGiocatori).getDinosauri().get(j).getId() + "; ");
					}
					System.out.println();
					System.out.print("Uova:\t");
					for(int j=0;j<p.getGiocatori().get(conteggioGiocatori).getUova().size();j++) {
						System.out.print(p.getGiocatori().get(conteggioGiocatori).getUova().get(j) + "; ");
					}
					System.out.println();
					System.out.println("->ConteggioGiocatore: " + conteggioGiocatori);
					System.out.println("*******************************");
					System.out.println();
					do {
						if(p.getGiocatori().get(conteggioGiocatori).getDinosauri()!=null) {
							Dinosauro dino = p.getGiocatori().get(conteggioGiocatori).getDinosauri().get(conteggioDinosauro);
							//gestisce le azioni del singolo dinosauro
							System.out.println("Turno corrente: " + turnoCorrente);
							System.out.println();
							System.out.println("*********DINOSAURO*************");
							System.out.println("ID:\t\t" + dino.getId());
							System.out.println("Dimensione:\t" + dino.getEnergiaMax()/1000);
							System.out.println("Forza:\t\t" + dino.calcolaForza());
							System.out.println("Energia:\t" + dino.getEnergia());
							System.out.println("En Max:\t\t" + dino.getEnergiaMax());
							System.out.println("Eta':\t\t" + dino.getEtaDinosauro());
							System.out.println("TurnoNascita:\t" + dino.getTurnoNascita());
							System.out.println("Pos:\t\t(" + dino.getRiga() + "," + dino.getColonna() + ")");
							System.out.println("->ConteggioDinosauro: " + conteggioDinosauro);
							System.out.println("*******************************");
							System.out.println();

							System.out.println("[1]: Muovi");
							System.out.println("[2]: Non eseguire il movimento");
							scelta = input.nextInt();
							switch(scelta)  {
							case 1 :
								//muovi
								int[][] raggiungibile = t.ottieniRaggiungibilita(dino.getRiga(), dino.getColonna());
								int[][] stradaPercorsa;
								int[] coordinate = trovaDinosauro(raggiungibile);

								System.out.println("Coordinate: " +  coordinate[0] + " " + coordinate[1]);
								//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
								System.out.println("Posiz Dino: " + dino.getRiga() + "," + dino.getColonna());
								int origineRiga = dino.getRiga() - coordinate[0];
								int origineColonna = dino.getColonna() - coordinate[1];
								int fineRiga = dino.getRiga() + (raggiungibile.length - coordinate[0] - 1);
								int fineColonna = dino.getColonna() + (raggiungibile[0].length - coordinate[1] - 1);
								System.out.println("CoordinateMappa: " +  origineRiga + "," + origineColonna + "   " + fineRiga + "," + fineColonna);

								i.stampaMappaRaggiungibilita(origineRiga, origineColonna, fineRiga, fineColonna, raggiungibile);

								p.getGiocatori().get(conteggioGiocatori).stampaMappa();
								i.stampaMappaRidottaVisibilita(p.getGiocatori().get(conteggioGiocatori));
								String posMovimento;
								int riga, colonna;
								boolean spostDino=false;
								do {
									System.out.println("Inserisci coordinate come riga,colonna: ");

									posMovimento = input.nextLine();
									posMovimento = input.nextLine();
									System.out.println("s" + posMovimento + "s");
									System.out.println("-> Coordinare movimento ottenute: " + posMovimento.split(",")[0] + "," + posMovimento.split(",")[1]);
									riga = Integer.parseInt(posMovimento.split(",")[0]);
									colonna = Integer.parseInt(posMovimento.split(",")[1]);

									System.out.println("->Il dinosauro si muovera' da: (" + dino.getRiga() + "," + dino.getColonna() + ") a: (" + riga + "," + colonna + ")");

									stradaPercorsa = t.ottieniStradaPercorsa(dino.getRiga(), dino.getColonna(),riga, colonna);
									i.stampaMappaStradaPercorsa(origineRiga, origineColonna, fineRiga, fineColonna, stradaPercorsa);

									int[] coordinateStrada = trovaDinosauroStrada(stradaPercorsa);

									int origineRigaStrada = dino.getRiga() - coordinateStrada[0];
									int origineColonnaStrada = dino.getColonna() - coordinateStrada[1];
									int fineRigaStrada = dino.getRiga() + (stradaPercorsa.length - coordinateStrada[0] - 1);
									int fineColonnaStrada = dino.getColonna() + (stradaPercorsa[0].length - coordinateStrada[1] - 1);

									System.out.println("origine: " + origineRigaStrada+","+origineColonnaStrada + " fine: "+fineRigaStrada+","+fineColonnaStrada);

									int raggio = p.getTurnoCorrente().calcolaRaggioVisibilita(dino);
									//illumino la strada
									for(int w=0;w<40;w++) {
										for(int j=0;j<40;j++) {
											if((w>=origineRigaStrada && w<=fineRigaStrada) && (j>=origineColonnaStrada && j<=fineColonnaStrada)) {
												//System.out.println("coord: " + (w-origineRigaStrada) + "," + (origineColonnaStrada));
												if(stradaPercorsa[w-origineRigaStrada][j-origineColonnaStrada]<0) t.illuminaMappa(p.getGiocatori().get(conteggioGiocatori), w, j, raggio);
											}
										}
									}

									i.stampaMappaRidottaVisibilita(p.getGiocatori().get(conteggioGiocatori));

									if((riga!=dino.getRiga() || colonna!=dino.getColonna()) && raggiungibile[riga-origineRiga][colonna-origineColonna]!=9)
										spostDino = p.getTurnoCorrente().spostaDinosauro(dino, riga, colonna);
									else spostDino=false;

								}while(spostDino==false);

								System.out.println("->Il dinosauro e' ora in: (" + dino.getRiga() + "," + dino.getColonna() + ")");
								i.stampaMappaRidotta();
								break;
							default:
								break;
							}
							System.out.println("Azioni possibili:");
							System.out.println("[1]: Cresci");
							System.out.println("[2]: Deponi");
							System.out.println("[3]: Non eseguire l'azione");
							scelta = input.nextInt();
							switch(scelta)  {								
							case 1 :
								//cresci
								if(dino.aumentaDimensione(p.getGiocatori().get(conteggioGiocatori),i.getMappa()[dino.getRiga()][dino.getColonna()])==true) {
									System.out.println("Il dinosauro " + dino.getId() + " e' ora di dimensione: " + (dino.getEnergiaMax()/1000));
								}
								else {
									p.getGiocatori().get(conteggioGiocatori).rimuoviDinosauro(dino, i.getMappa()[dino.getRiga()][dino.getColonna()]);
									System.out.println("Non e' stato possibile far crescere il dinosauro: " + dino.getId());
								}
								break;
							case 2 :
								//deponi
								if(dino.deponi(i.getMappa()[dino.getRiga()][dino.getColonna()], p.getGiocatori().get(conteggioGiocatori))==false) 
									System.out.println("Errore deposizione, possibili motivi: energia insufficiente, squadra dei dinosauri completa");
								break;
							default : //passa l'azione per il dinosauro specificato
								break;
							}	
						}
						conteggioDinosauro++;
						System.out.println("Conteggio dinosauro: " + conteggioDinosauro);
						if(rimuoviGiocatoriSenzaDinosauri(p) == false) if(conteggioGiocatori>=p.getGiocatori().size()) conteggioGiocatori=p.getGiocatori().size()-1;

					}while(p.getGiocatori().get(conteggioGiocatori).getDinosauri().size() > conteggioDinosauro); //chiudo while della scansione dei dinosauri
					//qui si puo' anche stampare mappe
					p.aggiungiTuplaClassifica(p.getGiocatori().get(conteggioGiocatori));
					conteggioGiocatori++;
				}while(p.getGiocatori().size() > conteggioGiocatori);
				break;
			case 4 :
				//stampo la classifica
				p.stampaClassifica();
				break;
			case 5 :
				//stampo le mappe UTILI
				i.stampaMappa();
				i.stampaMappaRidotta();
				break;
			}
			if(scelta1==3) {
				turnoCorrente++;
				p.getTurnoCorrente().incrementaEtaGiocatori();
			}
			System.out.println("Classifica aggiornata e le uova sono state schiuse");
			p.nascitaDinosauro(turnoCorrente);
			p.aggiornaClassificaStati();
			p.stampaClassifica();
			cresciEconsuma(p);

		} while(p.getGiocatori().size()>0); //se esco da qui ho il vincitore
	}

	private static boolean rimuoviGiocatoriSenzaDinosauri(Partita p) {
		boolean stato=false;
		for(int i=0;i<p.getGiocatori().size();i++)
			if(p.getGiocatori().get(i).getDinosauri().isEmpty()) {
				p.getGiocatori().remove(p.getGiocatori().get(i));
				stato=true;
			}
		return stato;
	}

	private static void cresciEconsuma(Partita p) {
		for(int i=0;i<40;i++) {
			for(int j=0;j<40;j++) {
				if(p.getIsola().getMappa()[i][j]!=null && p.getIsola().getMappa()[i][j].getOccupante()!=null) {
					if(p.getIsola().getMappa()[i][j].getOccupante() instanceof Vegetale) {
						Vegetale vegetale = (Vegetale)(p.getIsola().getMappa()[i][j].getOccupante());
						vegetale.cresci();
					} else if(p.getIsola().getMappa()[i][j].getOccupante() instanceof Carogna) {
						Carogna carogna = (Carogna)(p.getIsola().getMappa()[i][j].getOccupante());
						carogna.consuma();
					}
				} 

			}
		}
	}

	private static Giocatore cercaGiocatore (String nickname, Partita p) {
		for(int i=0;i<p.getGiocatori().size();i++) if((p.getGiocatori().get(i).getNomeUtente()).equals(nickname)) return p.getGiocatori().get(i);
		return null;
	}

	private static int[] trovaDinosauroStrada (int[][] stradaPercorsa) {
		int j,w;
		int[] uscita = {0,0};
		for(j=0;j<stradaPercorsa.length;j++) {
			for(w=0;w<stradaPercorsa[0].length;w++) {
				if(stradaPercorsa[j][w]==-7) {
					uscita[0] = j;
					uscita[1] = w;
					return uscita;
				}
			}
		}
		return uscita;
	}

	private static int[] trovaDinosauro (int[][] raggiungibile) {
		int j,w;
		int[] uscita = {0,0};
		for(j=0;j<raggiungibile.length;j++) {
			for(w=0;w<raggiungibile[0].length;w++) {
				System.out.print(raggiungibile[j][w] + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();

		for(j=0;j<raggiungibile.length;j++) {
			for(w=0;w<raggiungibile[0].length;w++) {
				if(raggiungibile[j][w]==0) {
					uscita[0] = j;
					uscita[1] = w;
					return uscita;
				}
			}
		}
		return uscita;
	}
}
