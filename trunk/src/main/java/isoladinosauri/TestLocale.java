package isoladinosauri;

import isoladinosauri.modellodati.Dinosauro;

import java.util.Scanner;

public class TestLocale {




	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Benvenuto in Isola dei Dinosauri Beta1");
		Scanner input = new Scanner(System.in);
		Isola i = new Isola();
		Partita p = new Partita(i);
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		i.caricaMappa();
		int scelta;
		int turnoCorrente=0;
		int conteggioDinosauro=0;
		int conteggioGiocatori=0;

		do { //inzia la partita e termina solo quando resta un giocatore
			turnoCorrente++;
			conteggioGiocatori=0;
			Giocatore giocatore = null;
			System.out.println("Menu:");
			System.out.println("[1]: Aggiunge un giocatore");
			System.out.println("[2]: Rimuove un giocatore");
			System.out.println("[3]: Esegue i turni dei giocatori, uno ad uno in sequenza");
			scelta = input.nextInt();
			conteggioDinosauro=0;
			switch(scelta) {
			case 1 :
				//aggiungo un giocatore
				String nickname = new String();
				String password = new String();
				String nomeSpecie = new String();
				int tipo;
				String tipoDinosauro = new String();
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

				giocatore = new Giocatore(p, nickname, password, turnoCorrente, nomeSpecie, tipoDinosauro);
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
						Dinosauro dino = p.getGiocatori().get(conteggioGiocatori).getDinosauri().get(conteggioDinosauro);
						//gestisce le azioni del singolo dinosauro
						System.out.println("Turno corrente: " + turnoCorrente);
						System.out.println();
						System.out.println("*********DINOSAURO*************");
						System.out.println("ID:\t\t" + dino.getId());
						System.out.println("Energia:\t" + dino.getEnergia());
						System.out.println("En Max:\t\t" + dino.getEnergiaMax());
						System.out.println("Eta':\t\t" + dino.getEtaDinosauro());
						System.out.println("Pos:\t\t(" + dino.getRiga() + "," + dino.getColonna() + ")");
						System.out.println("->ConteggioDinosauro: " + conteggioDinosauro);
						System.out.println("*******************************");
						System.out.println();
						System.out.println("[1]: Muovi");
						System.out.println("[2]: Cresci");
						System.out.println("[3]: Deponi");
						scelta = input.nextInt();
						switch(scelta)  {
						case 1 :
							//muovi
							int[][] raggiungibile = t.ottieniRaggiungibilita(dino.getRiga(), dino.getColonna());
							int[][] stradaPercorsa = t.ottieniStradaPercorsa(dino.getRiga(), dino.getColonna(),dino.getRiga()+3, dino.getColonna()+2);
							int[] coordinate = trovaDinosauro(raggiungibile);

							System.out.println("Coordinate: " +  coordinate[0] + " " + coordinate[1]);
							//ottengo la riga e la colonna di dove si trova il dinosauro nella vista di raggiungibilita'
							System.out.println("Posiz Dino: " + dino.getRiga() + "," + dino.getColonna());
							int origineRiga = dino.getRiga() - coordinate[0];
							int origineColonna = dino.getColonna() - coordinate[1];
							int fineRiga = dino.getRiga() + (raggiungibile.length - coordinate[0] - 1);
							int fineColonna = dino.getColonna() + (raggiungibile[0].length - coordinate[1] - 1) ;
							System.out.println("CoordinateMappa: " +  origineRiga + "," + origineColonna + "   " + fineRiga + "," + fineColonna);
							i.stampaMappaRaggiungibilita(origineRiga, origineColonna, fineRiga, fineColonna, raggiungibile);
							i.stampaMappaStradaPercorsa(origineRiga, origineColonna, fineRiga, fineColonna, stradaPercorsa);
							break;
						case 2 :
							//cresci
							if(dino.aumentaDimensione()==true) System.out.println("Il dinosauro " + dino.getId() + " e' ora di dimensione: " + (dino.getEnergiaMax()/1000));
							else System.out.println("Non e' stato possibile far crescere il dinosauro: " + dino.getId());
							break;
						case 3 :
							//deponi
							dino.deponi(i.getMappa()[dino.getRiga()][dino.getColonna()], p.getGiocatori().get(conteggioGiocatori));
							break;
						default : //passa l'azione per il dinosauro specificato
							break;
						}		
						conteggioDinosauro++;
					}while(p.getGiocatori().get(conteggioGiocatori).getDinosauri().size() > conteggioDinosauro); //chiudo while della scansione dei dinosauri
//					i.stampaMappa();
					i.stampaMappaRidotta();
					p.getGiocatori().get(conteggioGiocatori).stampaMappa();
					i.stampaMappaRidottaVisibilita(p.getGiocatori().get(conteggioGiocatori));
					conteggioGiocatori++;
				}while(p.getGiocatori().size() > conteggioGiocatori);
				break;
			}
			p.nascitaDinosauro(turnoCorrente);
		} while(p.getGiocatori().size()>0); //se esco da qui ho il vincitore
	}


	private static Giocatore cercaGiocatore (String nickname, Partita p) {
		for(int i=0;i<p.getGiocatori().size();i++) if((p.getGiocatori().get(i).getNomeUtente()).equals(nickname)) return p.getGiocatori().get(i);
		return null;
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
