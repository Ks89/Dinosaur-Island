package isoladinosauri;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;

import java.util.ArrayList;
import java.util.List;

//import com.db4o.Db4o;
//import com.db4o.ObjectContainer;
//import com.db4o.ObjectSet;

public class Partita {

	private Isola isola;
	private Turno turnoCorrente; //Conserva solo il turno corrente
	private List<Giocatore> giocatori;
	private int contatoreTurni;

	//bug: se faccio crescere un dinosauro fino ad esaurire l'energia il programma crasha
	
	//aggiungere movimento dinosauro e richiamare illuminaMappa ogni passo fatto, sia la destinaz finale che i passi intermedi intrapresi per raggiungere
	//la destinazione
	
	//NB: fare l'illuminaMappa da zero, diversa, che riceve in ingresso la dimensione del dinosauro e lui illumina la zona, indipendentemente
	//dal fatto che ci sia o no un dinosauro sopra.
	
	//da fare: la vegetazione, una volta che viene mangiata deve rinascere e lo stesso anche la corogna
	//se la coragona arriva a 0 da solo cosa deve fare? Ricrearsi da un'altra parte?

	//simulare tutto con aggiunta di più giocatori
	//simulare tutto con JUNIT e mettere la Javadoc
	//riordinare tutto il codice
	private List<String> classifica;
	//private List<Tupla> classificaGiocatori;

	public Partita(Isola isola)
	{
		giocatori = new ArrayList<Giocatore>();
		classifica = new ArrayList<String>();
		this.isola = isola;
	}

	public Turno getTurnoCorrente() {
		return turnoCorrente;
	}

	public void setTurnoCorrente(Turno turnoCorrente) {
		this.turnoCorrente = turnoCorrente;
	}

	public Isola getIsola() {
		return isola;
	}

	public void setIsola(Isola isola) {
		this.isola = isola;
	}






	public List<Giocatore> getGiocatori() {
		return giocatori;
	}

	private Giocatore getGiocatore(int i) {
		return giocatori.get(i);
	}

	public void aggiungiGiocatore(Giocatore giocatore) {
		if(this.giocatori.size()<8) giocatori.add(giocatore);
		else { 
			//partita piena con 8 giocatori
		}
	}

	public void rimuoviGiocatore(Giocatore giocatore) {
		//ricevo gicatore, cioe' il giocatore che devo cancellare

		//rimuovo tutti i dinosauri del giocatore dalla mappa
		for(int j=0;j<40;j++) {
			for(int i=0;i<40;i++) {
				if(this.getIsola().getMappa()[i][j]!=null && this.getIsola().getMappa()[i][j].getDinosauro()!=null) {
					if(giocatore.getDinosauri().contains(this.getIsola().getMappa()[i][j].getDinosauro())==true) this.getIsola().getMappa()[i][j].setDinosauro(null);
				}		
			}
		}
		//rimuovo il giocatore dalla lista dei giocatori
		giocatori.remove(giocatore);
	}

	public int getContatoreTurni() {
		return contatoreTurni;
	}

	public void setContatoreTurni(int contatoreTurni) {
		this.contatoreTurni = contatoreTurni;
	}

	public Giocatore identificaDinosauro (Dinosauro dinosauro) {
		//gli do un dinosauro e lui lo cerca tra tutti quelli dei giocatori
		//quello che restituisce e' il giocatore che possiede quel dinosauro
		//nella propria arraylist di Dinosauri
		for(int i=0; i<this.getGiocatori().size();i++) {
			for(int j=0; j<this.getGiocatore(i).getDinosauri().size();j++) {
				if(dinosauro.equals(this.getGiocatore(i).getDinosauri().get(j))) return this.getGiocatore(i);
			}
		}    	
		return null;
	}

	public void nascitaDinosauro (int turnoNascita) {
		//scansiona tutti i giocatori e all'interno di essi scansiona tutte le arraylist 
		//delle uova. Quindi si preoccupa di chiamare il metodo SchiudiUova per far nascere i dinosauri

		String[] coordinate; //array con dentro
		int[] posizione;
		Dinosauro dinosauro = null;
		Integer riga = null;
		Integer colonna = null;
		List<String> listaUova;

		for(int i=0;i<this.getGiocatori().size();i++) {
			//ottengo la lista uova del giocatore i-esimo
			listaUova = this.getGiocatore(i).getUova();

			//scansiono la lista delle uova appena ottenuta
			for(int j=0; j<listaUova.size();j++) {

				//ottengo le coordinate dell'uovo in questione
				coordinate = listaUova.get(j).split("-");
				riga = Integer.parseInt(coordinate[0]);
				colonna = Integer.parseInt(coordinate[1]);

				posizione = this.generaCoordinateNascituro(riga,colonna);

				String idDinosauro = this.getGiocatore(i).generaIdDinosauro();
				System.out.println("id Dino:" + idDinosauro);

				//NB: fare .getDinosauri().get(0) vuol dire  prendere il primo dino del giocatore
				//solo per vedere se e' carnivoro o erbivoro e quindi per capire il tipo della specie del dinosauro
				if(this.getGiocatore(i).getDinosauri().get(0) instanceof Carnivoro) dinosauro = new Carnivoro(idDinosauro,posizione[0], posizione[1], turnoNascita);
				else if(this.getGiocatore(i).getDinosauri().get(0) instanceof Erbivoro) dinosauro = new Erbivoro(idDinosauro,posizione[0], posizione[1], turnoNascita);
				this.getGiocatore(i).aggiungiDinosauro(dinosauro);
				this.getIsola().getMappa()[posizione[0]][posizione[1]].setDinosauro(dinosauro);	
				this.getGiocatore(i).rimuoviUova();
			}
		}
	}

	private int[] generaCoordinateNascituro (int riga, int colonna) {
		//passate le coordinate dell'uovo questo metodo genera le coordinate i cui POSSO
		//far nascere il dinosauro

		//funzionamento:
		//1) calcolo raggio visuale che parte da 1
		//2) cerco nella visuale uno spazio libero (terra senza dinosauro) (cioe' la cornice subito intorno al dinosauro)
		//3) se c'e' faccio return
		//4) incrementeo il raggio a 2 ecc...
		//5) fino a che non faccio un return

		//ottengo la vista del dinosauro
		int[] origine;  //origine vista
		int[] estremo; //estremo vista
		int[] coordinate = new int[2];  //coordinare in cui mettero' il nuovo dinosauro

		//TODO COSA STUPIDA: for fino a 39 perche' la cornice della mappa e' tutta di acqua (sembra cmq stupido)
		for(int i=1; i<39; i++) { //calcola il raggio della visuale
			origine = this.turnoCorrente.ottieniOrigineVisuale(riga, colonna, i);
			estremo = this.turnoCorrente.ottieniEstremoVisuale(riga, colonna, i);
			for(int w=origine[0]; w<estremo[0]+1; w++) { //mi muovo sulle righe 
				for(int j=origine[1]; j<estremo[1]+1; j++) { //mi muovo sulle colonne
					//					System.out.println(w + "," + j + " " + origine[0] + "," + origine[1] + " " + estremo[0] + "," + estremo[1]);
					if(this.getIsola().getMappa()[w][j]!=null && this.getIsola().getMappa()[w][j].getDinosauro()==null) {
						coordinate[0] = w;
						coordinate[1] = j;
						return coordinate;
					}
				}

			}
		}
		return coordinate; //se arrivo qui dovrei sollevare l'eccezione che il dinosauro non puo' nascere perche' nella
		//mappa non c'e spazio (follia pura)
	}


	//*******************************************************************************************************************
	//********************************************GESTIONE CLASSIFICA****************************************************
	//*******************************************************************************************************************
	public List<String> getClassifica() {
		return classifica;
	}
	
	//metodo che serve per aggiungere elementi nella classifica
	//DEVE ESSERE USATO SOLO PER INSERIRE NUOVI GIOCATORI E PER AGGIORNARE I PUNTEGGI
	//se passo un nuovo giocatore lo inserisce, se ne passo uno gia' presente gli aggiorna il punteggio
	//se voglio gestire lo stato online/offline chiamo AGGIORNACLASSIFICASTATI()
	public void aggiungiTuplaClassifica(Giocatore giocatore) {
		
		//ottengo il giocatore da aggiungere nella classifica e creo la sua tupla
		String tupla = ",{ " + giocatore.getNomeUtente() + "," + giocatore.getNomeSpecie() + "," + giocatore.calcolaPunti() + ",s}";
	
		//se la tupla non e' presente in classifica (cioe' risultato==-1) la aggiungo
		if(this.cercaInClassifica(giocatore)==-1) this.getClassifica().add(tupla);
		//altrimenti cerco la tupla nella classifica e la aggiorno col metodo apposta "aggiornaTupla"
		else this.aggiornaPuntiTupla(tupla,giocatore);
	}
	
	//metodo per aggiornare una tupla quando il giocatore, passato in aggiungiTuplaClassifica, si trova gia' nella classifica
	private void aggiornaPuntiTupla(String tupla, Giocatore giocatore) {
		String[] tuplaTagliata;
		String nuovaTupla;
		int punti = giocatore.calcolaPunti();		
		//ottengo la posizione nella classifica del giocatore, ovvero cerco la posizione della tupla associata al giocatore
		int posizione = this.cercaInClassifica(giocatore);

		//taglio la tupla ad ogni virgola e modifico solo il campo dei punti per poi settare l'elemento della classifica
		//in questo modo non aggiungo tuple alla classifica, semplicemente ne modifico una gia' esistente ed in particolare
		//modifico solo un piccolo campo di essa, ovvero la stringa che indica i punti del gicoatore
		//l'if serve nel caso in cui voglia aggiungere un giocatore che era giaì presente in classifica prima e che e' uscito
		//e ora sta rientrando nella partita. ovviamente gli resetto i punti e lo imposto su online
		tuplaTagliata = this.getClassifica().get(posizione).split(",");
		if(tuplaTagliata[4].equals("n}")) nuovaTupla = this.getClassifica().get(posizione).replace(tuplaTagliata[3] + "," + tuplaTagliata[4], "" + punti + ",s}");
		else nuovaTupla = this.getClassifica().get(posizione).replace(tuplaTagliata[3] + "," + tuplaTagliata[4], "" + punti + "," + tuplaTagliata[4]);
		this.getClassifica().set(posizione, nuovaTupla);
	}

	//metodo che viene richiamata alla fine di ogni giro dei turni dei gicoatori (cioe' ad ogni turno della partita)
	public void aggiornaClassificaStati() {
		String nuovaTupla;
		Giocatore giocatore;
		for(int i=0;i<this.getClassifica().size();i++) {
			//per ogni tupla della classifica controllo se il giocatore ad esso associata e' ancora
			//presente in partita, tramite l'apposito metodo
			giocatore = this.cercaInGiocatori(this.getClassifica().get(i));
			
			//se giocatore==null vuol dire che non e' stato trovato da cercaInGiocatori e quindi non e' piu' presente nella
			//partita. Di conseguenza aggiorno la tupla modificando lo stato in offline (,n})
			if(giocatore==null) {
				nuovaTupla = this.getClassifica().get(i).replace(",s}", ",n}");
				this.getClassifica().set(i, nuovaTupla);
			}	
		}
		//qui chiamo l'ordinamento della classifica
		this.ordinaClassifica();
	}

	//dato un giocatore restituisce la posizione nella classifica
	private int cercaInClassifica(Giocatore giocatore) {
		String[] separa;
		//scansiona la classifica e verifica se la tupla corrente e' associata al giocatore ricevuto in ingresso
		//nel caso positivo restituisce la posizione del giocatore nella classifica
		//in caso negativo restituisce sempre -1
		for(int i=0;i<this.getClassifica().size();i++) {
			separa = this.getClassifica().get(i).split(",");
			if(separa[1].substring(2).equals(giocatore.getNomeUtente()) && (separa[2].equals(giocatore.getNomeSpecie()))) return i;
		}
		return -1;
	}

	//data una tupla della classifica dica a quale giocatore appartiene
	private Giocatore cercaInGiocatori(String tupla) {
		String[] separa = tupla.split(",");
		//scansiona i giocatori e verifica se il giocatore corrente e' associato alla tupla ricevuto in ingresso
		//nel caso positivo restituisce il giocatore associato all tupla
		//in caso negativo restituisce null
		for(int j=0;j<this.getGiocatori().size();j++) {
			if(separa[1].substring(2).equals(this.getGiocatore(j).getNomeUtente()) && (separa[2].equals(this.getGiocatore(j).getNomeSpecie()))) return this.getGiocatore(j);
		}
		return null;
	}
	
	//si occupa dell'ordinamento della classifica tramite bubblesort
	//E' un metodo privato chiamato da aggiornaClassificaStati()
	private void ordinaClassifica() {
		String temp;
		for(int j=0;j<this.getClassifica().size();j++) {
			for(int i=j;i<this.getClassifica().size();i++) {
				if(this.ottieniPuntiTupla(this.getClassifica().get(j))<this.ottieniPuntiTupla(this.getClassifica().get(i))) {
					temp = this.getClassifica().get(j);
					this.getClassifica().set(j, this.getClassifica().get(i));
					this.getClassifica().set(i, temp);
				}
			}
		}
	}
	
	//usato in ordinaClassifica() per ottenere in punteggi su cui fare l'if
	private int ottieniPuntiTupla(String tupla) {
		String[] separa = tupla.split(",");
		int punti = Integer.parseInt(separa[3]);
		return punti;
	}
	
	public void stampaClassifica() {
		for(int i=0;i<this.getClassifica().size();i++) System.out.println(classifica.get(i));
	}


	//*******************************************************************************************************************
	//************************************************SALVA PARTITA******************************************************
	//*******************************************************************************************************************
	public void salvaPartita() {

		//	    Turno t = new Turno(p);
		//	    t.setContatoreTurno((int)(Math.random()*100));
		//	    p.setTurnoCorrente(t);
		//	    //Scrive
		//        ObjectContainer container = Db4o.openFile("isoladinosauri.db4o");
		//        container.store(p);
		//        container.commit();
		//        container.close();
		//	    
		//        //legge
		//	    ObjectContainer container2 = Db4o.openFile("isoladinosauri.db4o");
		//	    ObjectSet<Partita> partite = container2.query(Partita.class);
		//	    for(Partita partita : partite) {
		//	        System.out.println(partita.getTurnoCorrente().getContatoreTurno());
		//	    }
	}
}
