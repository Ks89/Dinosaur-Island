//package isoladinosauri;
//
//import isoladinosauri.modellodati.Carogna;
//import isoladinosauri.modellodati.Dinosauro;
//import isoladinosauri.modellodati.Vegetale;
//
//import java.util.Scanner;
//
//import Eccezioni.CrescitaException;
//import Eccezioni.DeposizioneException;
//import Eccezioni.MovimentoException;
//
///**
// * Classe richiamata dal main per simulare il gioco in Locale con grafica
// */
//public class LocaleConsole {
//
//	private static final int MAX = 40;
//
//	public void avviaLineaDiComando () {
//		boolean uscita=false;
//		int scelta,scelta1;
//		int turnoCorrente=1;
//		int conteggioDinosauro=0;
//		int conteggioGiocatori=0;
//		System.out.println("Benvenuto in Isola dei Dinosauri Beta1");
//		Scanner input = new Scanner(System.in);
//
//		CaricamentoMappa cm = new CaricamentoMappa();
//		GenerazioneMappa gm = new GenerazioneMappa();
//		Isola i;
//		Cella[][] mappaCelle;
//
//		System.out.println("Scegli come inzializzare la mappa di gioco");
//		System.out.println("[1]: Carica una mappa da file [OK]");
//		System.out.println("[2]: Genera a caso una mappa [OK]");
//		scelta = input.nextInt();
//		switch(scelta) {
//		case 1 :
//			mappaCelle = cm.caricaDaFile();
//			i = new Isola(mappaCelle);		
//			break;
//		default :
//			String[][] mappaStringhe = gm.creaMappaCasuale();
//			mappaCelle = cm.caricaMappa(mappaStringhe);
//			i = new Isola(mappaCelle);
//			break;
//		}
//
//		Partita p = new Partita(i);
//		Turno t = new Turno(p);
//		Classifica c = new Classifica(p);
//		p.setTurnoCorrente(t);
//
//
//
//		do { //inzia la partita
//			conteggioGiocatori=0;
//			System.out.println("Menu:");
//			System.out.println("[1]: Aggiunge un giocatore [OK]");
//			System.out.println("[2]: Rimuove un giocatore [OK]");
//			System.out.println("[3]: Esegue i turni dei giocatori, uno ad uno in sequenza [OK]");
//			System.out.println("[4]: Stampa la classifica [OK]");
//			System.out.println("[5]: Stampa tutte le mappe utili per i test[OK]");
//			scelta1 = input.nextInt();
//			conteggioDinosauro=0;
//			t.ricreaCarogne(i.getMappa());
//			switch(scelta1) {
//			case 1 :
//				//aggiungo un giocatore
//				String nickname;
//				String password;
//				String nomeSpecie;
//				String tipoDinosauro;
//				nickname = input.nextLine();
//				System.out.println();
//				System.out.println("Inserisci il nickname per il login: ");
//				nickname = input.nextLine();
//				System.out.println("Inserisci la password per il login: ");
//				password = input.nextLine();
//				System.out.println("Inserisci il nome della specie: ");
//				nomeSpecie = input.nextLine();
//				System.out.println("Inserisci 0 per carnivoro oppure 1 per erbivoro: ");
//				String tipo = input.nextLine();
//				if(tipo.equals("0")) {
//					tipoDinosauro = "carnivoro";
//				} else {
//					tipoDinosauro = "erbivoro";
//				}
//				Utente utente = new Utente(nickname,password);
//				Giocatore giocatore =  new Giocatore(turnoCorrente, nomeSpecie, tipoDinosauro);
//				giocatore.setUtente(utente);
//				giocatore.aggiungiInPartita(p);
//				c.aggiungiTuplaClassifica(giocatore);
//				break;
//			case 2 :
//				//rimuovo il giocatore
//				System.out.println("Inserisci il nickname del giocatore da rimuovere: ");
//				String rimozione = input.nextLine();
//				rimozione = input.nextLine();
//				p.rimuoviGiocatore(cercaGiocatore(rimozione,p));
//				conteggioGiocatori=9999; //per far terminare i while e far ricominciare dall'inizio
//				break;
//			case 3 :
//				//usa il giocatore successivo
//
//				for(conteggioGiocatori=0;conteggioGiocatori<p.getGiocatori().size();conteggioGiocatori++) {
//					System.out.println();
//					System.out.println("**********GIOCATORE************");
//					System.out.println("ID:\t\t" + p.getGiocatori().get(conteggioGiocatori).getIdGiocatore());
//					System.out.println("Nome:\t\t" + p.getGiocatori().get(conteggioGiocatori).getUtente().getNomeUtente());
//					System.out.println("Specie:\t\t" + p.getGiocatori().get(conteggioGiocatori).getNomeSpecie());
//					System.out.println("Eta':\t\t" + p.getGiocatori().get(conteggioGiocatori).getEtaAttuale());
//					System.out.println("Turno nascita:\t" + p.getGiocatori().get(conteggioGiocatori).getTurnoNascita());
//					System.out.print("Dinosauri:\t");
//					for(int j=0;j<p.getGiocatori().get(conteggioGiocatori).getDinosauri().size();j++) {
//						System.out.print(p.getGiocatori().get(conteggioGiocatori).getDinosauri().get(j).getId() + "; ");
//					}
//					System.out.println();
//					System.out.print("Uova:\t");
//					for(int j=0;j<p.getGiocatori().get(conteggioGiocatori).getUova().size();j++) {
//						System.out.print(p.getGiocatori().get(conteggioGiocatori).getUova().get(j) + "; ");
//					}
//					System.out.println();
//					System.out.println("->ConteggioGiocatore: " + conteggioGiocatori);
//					System.out.println("*******************************");
//					System.out.println();
//					for(conteggioDinosauro=0;conteggioDinosauro<p.getGiocatori().get(conteggioGiocatori).getDinosauri().size();conteggioDinosauro++) {
//						giocatore = p.getGiocatori().get(conteggioGiocatori);
//						if(p.getGiocatori().get(conteggioGiocatori).getDinosauri()!=null) {
//							Dinosauro dino = p.getGiocatori().get(conteggioGiocatori).getDinosauri().get(conteggioDinosauro);
//							//gestisce le azioni del singolo dinosauro
//							System.out.println("Turno corrente: " + turnoCorrente);
//							System.out.println();
//							System.out.println("*********DINOSAURO*************");
//							System.out.println("ID:\t\t" + dino.getId());
//							System.out.println("Dimensione:\t" + dino.getEnergiaMax()/1000);
//							System.out.println("Forza:\t\t" + dino.calcolaForza());
//							System.out.println("Energia:\t" + dino.getEnergia());
//							System.out.println("En Max:\t\t" + dino.getEnergiaMax());
//							System.out.println("Eta':\t\t" + dino.getEtaDinosauro());
//							System.out.println("TurnoNascita:\t" + dino.getTurnoNascita());
//							System.out.println("Pos:\t\t(" + dino.getRiga() + "," + dino.getColonna() + ")");
//							System.out.println("->ConteggioDinosauro: " + conteggioDinosauro);
//							System.out.println("*******************************");
//							System.out.println();
//
//							System.out.println("[1]: Muovi");
//							System.out.println("[2]: Non eseguire il movimento");
//							scelta = input.nextInt();
//							switch(scelta)  {
//							case 1 :
//								//muovi
//
//								int[][] raggiungibile = t.ottieniRaggiungibilita(dino.getRiga(), dino.getColonna());
//								int[][] stradaPercorsa;
//								int[] coordinate = trovaDinosauro(raggiungibile);
//
//								System.out.println("Coordinate: " +  coordinate[0] + " " + coordinate[1]);
//								//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita
//								System.out.println("Posiz Dino: " + dino.getRiga() + "," + dino.getColonna());
//								int origineRiga = dino.getRiga() - coordinate[0];
//								int origineColonna = dino.getColonna() - coordinate[1];
//								int fineRiga = dino.getRiga() + (raggiungibile.length - coordinate[0] - 1);
//								int fineColonna = dino.getColonna() + (raggiungibile[0].length - coordinate[1] - 1);
//								System.out.println("CoordinateMappa: " +  origineRiga + "," + origineColonna + "   " + fineRiga + "," + fineColonna);
//
//								i.stampaMappaRaggiungibilita(origineRiga, origineColonna, fineRiga, fineColonna, raggiungibile);
//
//								i.stampaMappaRidottaVisibilita(p.getGiocatori().get(conteggioGiocatori));
//								String posMovimento;
//								int riga, colonna;
//								boolean statoSpostamento=false;
//								do {
//									System.out.println("Inserisci coordinate come: riga,colonna: ");
//
//									posMovimento = input.nextLine();
//									posMovimento = input.nextLine();
//									System.out.println("s" + posMovimento + "s");
//									System.out.println("-> Coordinare movimento ottenute: " + posMovimento.split(",")[0] + "," + posMovimento.split(",")[1]);
//									riga = Integer.parseInt(posMovimento.split(",")[0]);
//									colonna = Integer.parseInt(posMovimento.split(",")[1]);
//
//									System.out.println("->Il dinosauro si muovera' da: (" + dino.getRiga() + "," + dino.getColonna() + ") a: (" + riga + "," + colonna + ")");
//
//									stradaPercorsa = t.ottieniStradaPercorsa(dino.getRiga(), dino.getColonna(),riga, colonna);
//									i.stampaMappaStradaPercorsa(origineRiga, origineColonna, fineRiga, fineColonna, stradaPercorsa);
//
//									int[] coordinateStrada = trovaDinosauroStrada(stradaPercorsa);
//
//									int origineRigaStrada = dino.getRiga() - coordinateStrada[0];
//									int origineColonnaStrada = dino.getColonna() - coordinateStrada[1];
//									int fineRigaStrada = dino.getRiga() + (stradaPercorsa.length - coordinateStrada[0] - 1);
//									int fineColonnaStrada = dino.getColonna() + (stradaPercorsa[0].length - coordinateStrada[1] - 1);
//
//									System.out.println("origine: " + origineRigaStrada+","+origineColonnaStrada + " fine: "+fineRigaStrada+","+fineColonnaStrada);
//
//									int raggio = dino.calcolaRaggioVisibilita();
//									//illumino la strada
//									for(int w=0;w<MAX;w++) {
//										for(int j=0;j<MAX;j++) {
//											if((w>=origineRigaStrada && w<=fineRigaStrada) && (j>=origineColonnaStrada && j<=fineColonnaStrada)) {
//												if(stradaPercorsa[w-origineRigaStrada][j-origineColonnaStrada]<0) {
//													t.illuminaMappa(p.getGiocatori().get(conteggioGiocatori), w, j, raggio);
//												}
//											}
//										}
//									}
//
//									i.stampaMappaRidottaVisibilita(p.getGiocatori().get(conteggioGiocatori));
//
//									try {
//										boolean stato = p.getTurnoCorrente().spostaDinosauro(dino, riga, colonna);
//										if(stato) {
//											System.out.println("Tutto ok");
//										} else {
//											System.out.println("Problema");
//										}
//									} catch (MovimentoException e){
//										if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCATO) {
//											System.out.println("Vince attaccante");
//											statoSpostamento=true;
//										}
//										if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCANTE) {
//											System.out.println("Vince attaccato");
//											statoSpostamento=true;
//										}
//										if(e.getCausa()==MovimentoException.Causa.MORTE) {
//											System.out.println("Dinosauro morto");
//											statoSpostamento=false;
//										}
//										if(e.getCausa()==MovimentoException.Causa.DESTINAZIONEERRATA) {
//											System.out.println("Scegliere un'altra destinazione!");
//											statoSpostamento=false;
//										}
//										if(e.getCausa()==MovimentoException.Causa.NESSUNVINCITORE) {
//											System.out.println("Nessun vincitore!");
//											statoSpostamento=true; //se arriva qui c'e' un bug non gestibile
//										}
//										if(e.getCausa()==MovimentoException.Causa.ERRORE) {
//											System.out.println("Errore!");
//											statoSpostamento=true; //se arriva qui c'e' un bug non gestibile
//										}
//									}
//								}while(!statoSpostamento);
//
//								System.out.println("->Il dinosauro e' ora in: (" + dino.getRiga() + "," + dino.getColonna() + ")");
//								i.stampaMappaRidotta();
//								break;
//							default:
//								break;
//							}
//
//							System.out.println("Azioni possibili:");
//							System.out.println("[1]: Cresci");
//							System.out.println("[2]: Deponi");
//							System.out.println("[3]: Non eseguire l'azione");
//							scelta = input.nextInt();
//							switch(scelta)  {								
//							case 1 :
//								//cresci
//								
//								try {
//									dino.aumentaDimensione();
//									System.out.println("Il dinosauro " + dino.getId() + " e' ora di dimensione: " + (dino.getEnergiaMax()/1000));
//									int raggio = dino.calcolaRaggioVisibilita();
//									t.illuminaMappa(p.getGiocatori().get(conteggioGiocatori), dino.getRiga(), dino.getColonna(), raggio);
//								} catch (CrescitaException ce){
//									if(ce.getCausa()==CrescitaException.Causa.MORTE) {
//										p.getGiocatori().get(conteggioGiocatori).rimuoviDinosauro(dino);
//										System.out.println("Non e' stato possibile far crescere il dinosauro: " + dino.getId());
//										conteggioDinosauro--;
//									}
//									if(ce.getCausa()==CrescitaException.Causa.DIMENSIONEMASSIMA) {
//										System.out.println("Non e' stato possibile eseguire l'azione di crescita\nDimensione massima!");
//									}
//								}
//
//								break;
//							case 2 :
//								//deponi
//								
//								try {
//									p.getGiocatori().get(conteggioGiocatori).eseguiDeposizionedeponiUovo(dino);
//								} catch (DeposizioneException de){
//									if(de.getCausa()==DeposizioneException.Causa.MORTE) {
//										System.out.println("Errore deposizione, energia insufficiente");
//									}
//									if(de.getCausa()==DeposizioneException.Causa.SQUADRACOMPLETA) {
//										System.out.println("Non e' stato possibile eseguire l'azione di deposizione\nDimensione massima!");
//									}
//								}
//
//								break;
//							default : //passa l'azione per il dinosauro specificato
//								break;
//							}	
//						}
//						System.out.println("Conteggio dinosauro: " + conteggioDinosauro);
//						System.out.println("Conteggio giocatori: " + conteggioGiocatori);
//
//						for(int w=0;w<p.getGiocatori().size();w++) {
//							if(p.getGiocatori().get(w).getDinosauri().isEmpty()) {
//								p.getGiocatori().remove(p.getGiocatori().get(w));
//								if(conteggioGiocatori>0) {
//									conteggioGiocatori--;
//								}
//								conteggioDinosauro=0;
//							}
//						}
//						System.out.println("Conteggio dinosauro: " + conteggioDinosauro);
//						System.out.println("Conteggio giocatori: " + conteggioGiocatori);
//					}//finisce il for con la scansione dei dinosauri di un certo giocatore
//
//
//
//
//					//qui si puo' anche stampare mappe
//					c.aggiungiTuplaClassifica(p.getGiocatori().get(conteggioGiocatori));
//				}
//				break;
//			case 4 :
//				//stampo la classifica
//				c.stampaClassifica();
//				break;
//			case 5 :
//				//stampo le mappe UTILI
//				i.stampaMappa();
//				i.stampaMappaRidotta();
//				break;
//			default:
//				//termina il ciclo while ed esce dal programma
//				uscita=true;
//				break;
//			}
//			if(scelta1==3) {
//				turnoCorrente++;
//				p.incrementaEtaGiocatori();
//				cresciEconsuma(p);
//			}
//			System.out.println("Classifica aggiornata e le uova sono state schiuse");
//			p.nascitaDinosauro(turnoCorrente);
//			c.aggiornaClassificaStati();
//			c.stampaClassifica();
//
//		} while(!uscita);
//	}
//
//	private static void cresciEconsuma(Partita p) {
//		for(int i=0;i<MAX;i++) {
//			for(int j=0;j<MAX;j++) {
//				if(p.getIsola().getMappa()[i][j]!=null && p.getIsola().getMappa()[i][j].getOccupante()!=null) {
//					if(p.getIsola().getMappa()[i][j].getOccupante() instanceof Vegetale) {
//						Vegetale vegetale = (Vegetale)(p.getIsola().getMappa()[i][j].getOccupante());
//						vegetale.cresci();
//					} else { 
//						if(p.getIsola().getMappa()[i][j].getOccupante() instanceof Carogna) {
//							Carogna carogna = (Carogna)(p.getIsola().getMappa()[i][j].getOccupante());
//							carogna.consuma();
//						}
//					}
//				} 
//			}
//		}
//	}
//
//	private static Giocatore cercaGiocatore (String nickname, Partita p) {
//		for(int i=0;i<p.getGiocatori().size();i++) {
//			if((p.getGiocatori().get(i).getUtente().getNomeUtente()).equals(nickname)) {
//				return p.getGiocatori().get(i);
//			}
//		}
//		return null;
//	}
//
//	private static int[] trovaDinosauroStrada (int[][] stradaPercorsa) {
//		int j,w;
//		int[] uscita = {0,0};
//		for(j=0;j<stradaPercorsa.length;j++) {
//			for(w=0;w<stradaPercorsa[0].length;w++) {
//				if(stradaPercorsa[j][w]==-7) {
//					uscita[0] = j;
//					uscita[1] = w;
//					return uscita;
//				}
//			}
//		}
//		return uscita;
//	}
//
//	private static int[] trovaDinosauro (int[][] raggiungibile) {
//		int j,w;
//		int[] uscita = {0,0};
//		for(j=0;j<raggiungibile.length;j++) {
//			for(w=0;w<raggiungibile[0].length;w++) {
//				System.out.print(raggiungibile[j][w] + " ");
//			}
//			System.out.println();
//		}
//		System.out.println();
//		System.out.println();
//
//		for(j=0;j<raggiungibile.length;j++) {
//			for(w=0;w<raggiungibile[0].length;w++) {
//				if(raggiungibile[j][w]==0) {
//					uscita[0] = j;
//					uscita[1] = w;
//					return uscita;
//				}
//			}
//		}
//		return uscita;
//	}
//
//	public static void main(String[] args) {
//		LocaleConsole cl = new LocaleConsole();
//		cl.avviaLineaDiComando();
//	}
//}
