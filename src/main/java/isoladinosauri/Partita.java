package isoladinosauri;

import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;

import java.util.ArrayList;
import java.util.List;

//import com.db4o.Db4o;
//import com.db4o.ObjectContainer;
//import com.db4o.ObjectSet;

//FIXME GRANDE DUBBIO: l'erbivoro perde il combattimento e il carnivoro non fa nulla (anche se mi sembra strano)
//FIXME GRAVISSIMO: se c'e' un solo giocatore nella partita e il suo dinosauro muore il programma crasha SEMPRE (MOLTO GRAVE)
		//se invece cancello il giocatore direttamente dal menu, allora non succede nulla. Il bug sta nei cicli dei dinosauri, sugli indici
//FIXME una volta che il dinosauro erbivoro va su cella di un carnivoro e perde, il menu' delle azioni non devo apparire piu'
//FIXME se faccio crescere un dinosauro fino ad esaurire l'energia il programma crasha
//TODO mettere la Javadoc (inziato a farlo)

/**
 * Classe Partita per la gestione dei giocatori,
 * per l'eta' e per far schiudere le uova
 */
public class Partita {

	private Isola isola;
	private Turno turnoCorrente; //Conserva solo il turno corrente
	private List<Giocatore> giocatori;
	private int contatoreTurni;

	public Partita(Isola isola)
	{
		this.giocatori = new ArrayList<Giocatore>();
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
			System.out.println("Impossibile accedere, sono concessi solo 8 giocatori online");
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
	
	public void incrementaEtaGiocatori() {
		for(int i=0;i<this.getGiocatori().size();i++) this.getGiocatori().get(i).incrementaEtaAttuali();
	}

	public Giocatore identificaDinosauro (Dinosauro dinosauro) {
		//gli do un dinosauro e lui lo cerca tra tutti quelli dei giocatori
		//quello che restituisce e' il giocatore che possiede quel dinosauro
		//nella propria arraylist di Dinosauri
		//se il dinosauro da cercare e' null blocco il metodo e ritorno null
		if(dinosauro==null) return null;
		
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
				if(this.getGiocatore(i).aggiungiDinosauro(dinosauro)==true) {
					this.getIsola().getMappa()[posizione[0]][posizione[1]].setDinosauro(dinosauro);	
					this.getGiocatore(i).rimuoviUova();
				} else {
					//se non riesco ad aggiungere un dinosauro per quel preciso giocatore
					//rimuovo tutte le sue uova, perche' c'e' stato un problema e cosi' evito
					//un numero maggiore di 5 di dinosauri in squadra
					this.getGiocatore(i).rimuoviUova();
				}
			}
		}
	}

	//passate le coordinate dell'uovo questo metodo genera le coordinate i cui POSSO far nascere il dinosauro
	private int[] generaCoordinateNascituro (int riga, int colonna) {
		
		//funzionamento:
		//1) calcolo raggio visuale che parte da 1
		//2) cerco nella visuale uno spazio libero (terra senza dinosauro) (cioe' la cornice subito intorno al dinosauro)
		//3) se c'e' faccio return
		//4) incrementeo il raggio a 2 ecc... fino a che non faccio un return

		//ottengo la vista del dinosauro
		int[] origine;  //origine vista
		int[] estremo; //estremo vista
		int[] coordinate = new int[2];  //coordinare in cui mettero' il nuovo dinosauro

		//TODO corretto l'indice da 39 a 40, ma e' meglio tenerlo d'occhio, perche' non e' ancora stato ben testato questo cambiamento
		for(int i=1; i<40; i++) { //calcola il raggio della visuale
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
