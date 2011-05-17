package isoladinosauri;

import isoladinosauri.modellodati.Dinosauro;

import java.util.Scanner;

public class TestLocale {

	
	//Questa classe e' estrememamente in beta e funziona ancora male, ci vorra' del tempo :)
	
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
				p.aggiungiGiocatore(giocatore);
				t.illuminaMappa(giocatore.getDinosauri().get(0).getPosX(),giocatore.getDinosauri().get(0).getPosY());
				break;
			case 2 :
				//rimuovo il giocatore
				//TODO fare il coso che chiede il login del giocatore per cercarlo e toglierlo
				p.rimuoviGiocatore(giocatore);
				break;
			case 3 :
				//usa il giocatore successivo
				do {
					conteggioDinosauro=0;
					System.out.println("Giocatore: " + p.getGiocatori().get(conteggioGiocatori).getIdGiocatore() + ", login: " + p.getGiocatori().get(conteggioGiocatori).getNomeUtente() + ",numero: " + conteggioGiocatori);
					do {
						Dinosauro dino = p.getGiocatori().get(conteggioGiocatori).getDinosauri().get(conteggioDinosauro);
						//gestisce le azioni del singolo dinosauro
						System.out.println("contGioc: " + conteggioGiocatori);
						System.out.println("contDino: " + conteggioDinosauro);

						System.out.println("Dinosauro: " + p.getGiocatori().get(conteggioGiocatori).getDinosauri().get(conteggioDinosauro).getId() + " numero: " + conteggioDinosauro);
						System.out.println("Turno corrente: " + turnoCorrente);
						System.out.println("[1]: Muovi");
						System.out.println("[2]: Cresci");
						System.out.println("[3]: Deponi");
						scelta = input.nextInt();
						switch(scelta)  {
						case 1 :
							//muovi
							break;
						case 2 :
							//cresci
							if(dino.aumentaDimensione()==true) System.out.println("Il dinosauro " + dino.getId() + " e' ora di dimensione: " + (dino.getEnergiaMax()/1000));
							else System.out.println("Non e' stato possibile far crescere il dinosauro: " + dino.getId());
							break;
						case 3 :
							//deponi
							dino.deponi(i.getMappa()[dino.getPosX()][dino.getPosY()], p.getGiocatori().get(conteggioGiocatori));
							break;
						default : //passa l'azione per il dinosauro specificato
							break;
						}		
						conteggioDinosauro++;
					}while(p.getGiocatori().get(conteggioGiocatori).getDinosauri().size() > conteggioDinosauro); //chiudo while della scansione dei dinosauri
					i.stampaMappa();
					conteggioGiocatori++;
				}while(p.getGiocatori().size() > conteggioGiocatori);
				break;
			}
			p.nascitaDinosauro(turnoCorrente);
		} while(p.getGiocatori().size()>0); //se esco da qui ho il vincitore
	}
}
