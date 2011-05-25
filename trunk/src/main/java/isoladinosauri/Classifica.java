package isoladinosauri;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che contiene un ArrayList di Tupla (anch'essa una classe)
 * Costituita da metodi per inserire elementi nella classifica,
 * aggiornarne i punteggi e rimuovere giocatori, mantenendo il tutto
 * ordinato in modo decrescente tramite bubblesort
 */

public class Classifica {

	private Partita partita;
	private List<Tupla> classificaGiocatori;

	public Classifica(Partita partita) {
		this.classificaGiocatori = new ArrayList<Tupla>();
		this.partita = partita;
	}


	public List<Tupla> getClassifica() {
		return this.classificaGiocatori;
	}

	private int calcolaPunti(Giocatore giocatore) {
		int punti=0;
		for(int i=0;i<giocatore.getDinosauri().size();i++) 
			punti += (giocatore.getDinosauri().get(i).getEnergiaMax()/1000) + 1;
		return punti;
	}


	public void aggiungiTuplaClassifica(Giocatore giocatore) {
		//metodo che serve per aggiungere elementi nella classifica
		//DEVE ESSERE USATO SOLO PER INSERIRE NUOVI GIOCATORI E PER AGGIORNARE I PUNTEGGI
		//se passo un nuovo giocatore lo inserisce, se ne passo uno gia' presente gli aggiorna il punteggio
		//se voglio gestire lo stato online/offline chiamo AGGIORNACLASSIFICASTATI()

		//creo la Tupla da inserire in classifica
		Tupla tupla = new Tupla();
		tupla.setNomeUtente(giocatore.getNomeUtente());
		tupla.setNomeSpecie(giocatore.getNomeSpecie());
		tupla.setPunti(this.calcolaPunti(giocatore));
		tupla.setStato("s");

		//se la tupla non e' presente in classifica (cioe' risultato==-1) l'aggiungo
		//altrimenti cerco la tupla nella classifica e la aggiorno col metodo apposta "aggiornaTupla"
		if(this.cercaInClassifica(giocatore)==-1) this.getClassifica().add(tupla);
		else this.aggiornaPuntiTupla(tupla,giocatore);
	}

	private void aggiornaPuntiTupla(Tupla tupla, Giocatore giocatore) {
		//metodo per aggiornare una tupla quando il giocatore, passato in aggiungiTuplaClassifica, si trova gia' nella classifica

		//se la tupla e' riferita ad un gicoatore offline lo rendo online
		if(tupla.getStato().equals("n")) tupla.setStato("s");
		//aggiorno il punteggio del giocatore e lo inserisco nella tupla
		tupla.setPunti(this.calcolaPunti(giocatore));
	}

	public void aggiornaClassificaStati() {
		//metodo che viene richiamata alla fine di ogni giro dei turni dei gicoatori (cioe' ad ogni turno della partita)

		Giocatore giocatore;
		for(int i=0;i<this.getClassifica().size();i++) {
			//per ogni tupla della classifica controllo se il giocatore ad esso associata e' ancora
			//presente in partita, tramite l'apposito metodo
			giocatore = this.cercaInGiocatori(this.getClassifica().get(i));

			//se giocatore==null vuol dire che non e' stato trovato da cercaInGiocatori e quindi non e' piu' presente nella
			//partita. Di conseguenza, aggiorno la tupla modificando lo stato in offline "n"
			if(giocatore==null) this.getClassifica().get(i).setStato("n");
			else this.aggiornaPuntiTupla(this.getClassifica().get(i), giocatore);
		}
		//eseguo l'ordinamento decrescente della classifica per punteggio
		this.ordinaClassifica();
	}

	private int cercaInClassifica(Giocatore giocatore) {
		//scansiona la classifica e verifica se la tupla corrente e' associata al giocatore ricevuto in ingresso
		//nel caso positivo restituisce la posizione del giocatore nella classifica
		//in caso negativo restituisce sempre -1
		Tupla tupla;

		for(int i=0;i<this.getClassifica().size();i++) {
			tupla = this.getClassifica().get(i);
			if(tupla.getNomeUtente().equals(giocatore.getNomeUtente()) &&
					(tupla.getNomeSpecie().equals(giocatore.getNomeSpecie()))) return i;
		}
		return -1;
	}

	private Giocatore cercaInGiocatori(Tupla tupla) {
		//scansiona i giocatori e verifica se il giocatore corrente e' associato alla tupla ricevuto in ingresso
		//in caso positivo restituisce il giocatore associato all tupla
		//in caso negativo restituisce null
		for(int j=0;j<this.partita.getGiocatori().size();j++) {
			if(tupla.getNomeUtente().equals(this.partita.getGiocatori().get(j).getNomeUtente()) &&
					(tupla.getNomeSpecie().equals(this.partita.getGiocatori().get(j).getNomeSpecie()))) return this.partita.getGiocatori().get(j);
		}
		return null;
	}

	private void ordinaClassifica() {
		//si occupa dell'ordinamento della classifica tramite bubblesort
		//E' un metodo privato chiamato da aggiornaClassificaStati()
		Tupla temp;
		for(int j=0;j<this.getClassifica().size();j++) {
			for(int i=j;i<this.getClassifica().size();i++) {
				if(this.getClassifica().get(j).getPunti()<this.getClassifica().get(i).getPunti()) {
					temp = this.getClassifica().get(j);
					this.getClassifica().set(j, this.getClassifica().get(i));
					this.getClassifica().set(i, temp);
				}
			}
		}
	}

	public void stampaClassifica() {
		for(int i=0;i<this.getClassifica().size();i++) 
			System.out.println(this.classificaGiocatori.get(i).getNomeUtente() + "," +
					this.classificaGiocatori.get(i).getNomeSpecie() + "," +
					this.classificaGiocatori.get(i).getPunti() + "," + 
					this.classificaGiocatori.get(i).getStato());
	}


}
