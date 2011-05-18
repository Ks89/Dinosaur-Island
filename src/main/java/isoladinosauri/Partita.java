package isoladinosauri;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;

import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Partita {

	private Isola isola;
	private Turno turnoCorrente; //Conserva solo il turno corrente
	private List<Giocatore> giocatori;
	private int contatoreTurni;

	//creare metdo in giocatore per creare il suo punteggio da usare in creaclassifica

	//aggiungere movimento dinosauro e richiamare illuminaMappa ogni passo fatto, sia la destinaz finale che i passi intermedi intrapresi per raggiungere
	//la destinazione

	//simulare tutto con aggiunta di più giocatori
	//simulare tutto con JUNIT e mettere la Javadoc
	//riordinare tutto il codice

	//da gestire la classifica, per ora la metto cosi, ma deve tenere a mente diverse cose
	//e magari conviene farla solo con delle stringhe
	private List<String> classifica;

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
		int[] posizione = new int[2];
		Dinosauro dinosauro = null;
		Integer posX = null;
		Integer posY = null;
		List<String> listaUova;

		for(int i=0;i<this.getGiocatori().size();i++) {
			//ottengo la lista uova del giocatore i-esimo
			listaUova = this.getGiocatore(i).getUova();

			//scansiono la lista delle uova appena ottenuta
			for(int j=0; j<listaUova.size();j++) {

				//ottengo le coordinate dell'uovo in questione
				coordinate = listaUova.get(j).split("-");
				posX = Integer.parseInt(coordinate[0]);
				posY = Integer.parseInt(coordinate[1]);

				posizione = this.generaCoordinateNascituro(posX,posY);

				String idDinosauro = this.getGiocatore(i).generaIdDinosauro();
				System.out.println("id Dino:" + idDinosauro);

				//NB: fare .getDinosauri().get(0) vuol dire  prendere il primo dino del giocatore
				//solo per vedere se e' carnivoro o erbivoro e quindi per capire il tipo della specie del dinosauro
				if(this.getGiocatore(i).getDinosauri().get(0) instanceof Carnivoro) dinosauro = new Carnivoro(idDinosauro,posizione[0], posizione[1], turnoNascita);
				else if(this.getGiocatore(i).getDinosauri().get(0) instanceof Erbivoro) dinosauro = new Erbivoro(idDinosauro,posizione[0], posizione[1], turnoNascita);
				this.getGiocatore(i).aggiungiDinosauro(dinosauro);
				this.getIsola().getMappa()[posizione[0]][posizione[1]].setDinosauro(dinosauro);	
			}
		}
	}

	private int[] generaCoordinateNascituro (int posX, int posY) {
		//passate le coordinate dell'uovo questo metodo genera le coordinate i cui POSSO
		//far nascere il dinosauro

		//funzionamento:
		//1) calcolo raggio visuale che parte da 1
		//2) cerco nella visuale uno spazio libero (terra senza dinosauro) (cioe' la cornice subito intorno al dinosauro)
		//3) se c'e' faccio return
		//4) incrementeo il raggio a 2 ecc...
		//5) fino a che non faccio un return

		//ottengo la vista del dinosauro
		int[] origine = new int[2];  //origine vista
		int[] estremo = new int[2]; //estremo vista
		int[] coordinate = new int[2];  //coordinare in cui mettero' il nuovo dinosauro

		//TODO COSA STUPIDA: for fino a 39 perche' la cornice della mappa e' tutta di acqua (sembra cmq stupido)
		for(int i=1; i<39; i++) { //calcola il raggio della visuale
			origine = this.turnoCorrente.ottieniOrigineVisuale(posX, posY, i);
			estremo = this.turnoCorrente.ottieniEstremoVisuale(posX, posY, i);
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


	public List<String> getClassifica() {
		return classifica;
	}

	private Giocatore cercaInClassifica(String tupla) {
		String[] separa = tupla.substring(3, tupla.length()).split(",");

		for(int i=0;i<this.getGiocatori().size();i++) {
			//System.out.println(separa[0] + "," + this.getGiocatore(i).getNomeUtente() + "-" + separa[1] + "," + this.getGiocatore(i).getNomeSpecie());
			if(separa[0].equals(this.getGiocatore(i).getNomeUtente()) && (separa[1].equals(this.getGiocatore(i).getNomeSpecie()))) return this.getGiocatore(i);
		}
		return null; //indica che l'elemento non e' stato trovato (viene gestino in aggiornaClassifica())
	}


	public void aggiornaClassifica() {
		String tupla; //elemento della classifica (cioe' la riga)
		String[] tuplaTagliata;
		String nuovaTupla; //quella che uso per modificare la tupla della classifica
		Giocatore giocatore;

		if(this.getClassifica().size()==0) {
			//la classifica e' vuota e quindi di certo devo aggiungere tutti i giocatori della parita
			for(int i=0; i<this.getGiocatori().size();i++) {
				tupla = ",{ " + this.getGiocatore(i).getNomeUtente() + "," + this.getGiocatore(i).getNomeSpecie() + "," + this.getGiocatore(i).calcolaPunti() + ",s}";
				this.getClassifica().add(tupla);
			}
		} else {
			for(int i=0; i<this.getClassifica().size();i++) {
				tupla = this.getClassifica().get(i);
				
				//cerco il giocatore associato a questa tupla
				giocatore = this.cercaInClassifica(tupla);
				//System.out.println("giocatore: "+ giocatore);
				
				if(giocatore!=null) {
					//vuol dire che la tupla e' riferita ad un giocatore online 
					//quindi devo aggiornare la sua tupla
					tuplaTagliata = this.getClassifica().get(i).split(",");
					nuovaTupla = this.getClassifica().get(i).replace(tuplaTagliata[3], "" + giocatore.calcolaPunti());
					this.getClassifica().set(i, nuovaTupla);
				} else {
					//vuol dire che la tupla e' riferita ad un gicoatore offline 
					//allora aggiorno la sua tupla mettendolo ad offline
					nuovaTupla = this.getClassifica().get(i).replace(",s}", ",n}");
					this.getClassifica().set(i, nuovaTupla);
				}
			}
		}
		//TODO ora bisogna fare un metodo per ordinare la classifica
	}

	//solo per test
	public void stampaClassifica() {
		for(int i=0;i<this.getClassifica().size();i++) System.out.println(classifica.get(i));
	}

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
