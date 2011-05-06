package isoladinosauri;

import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Vegetale;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Partita {
	
	private Cella[][] mappa;
	private Turno turnoCorrente; //Conserva solo il turno corrente
	private List<Giocatore> giocatori;
	private int contatoreTurni;
	
	//da gestire la classifica, per ora la metto cosi, ma deve tenere a mente diverse cose
	//e magari conviene farla solo con delle stringhe
	private List<Giocatore> classifica;
	
	
	public Cella[][] getMappa() {
        return mappa;
    }



    public void setMappa(Cella[][] mappa) {
        this.mappa = mappa;
    }



    public Turno getTurnoCorrente() {
        return turnoCorrente;
    }



    public void setTurnoCorrente(Turno turnoCorrente) {
        this.turnoCorrente = turnoCorrente;
    }



    public List<Giocatore> getGiocatori() {
        return giocatori;
    }



    public void setGiocatori(List<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }



    public int getContatoreTurni() {
        return contatoreTurni;
    }



    public void setContatoreTurni(int contatoreTurni) {
        this.contatoreTurni = contatoreTurni;
    }


    public void caricaMappa() {
    	//esegue il caricamento della Mappa come file di testo (.txt)
    	//e riempie l'array Mappa con gli elementi letti dal file
    	//richiama anche i metodi predefiniti per inizializzare l'energiaMax al momento
    	//della creazione di un Occupante (vegetazione e/o carogna)
		try
		{  	
			BufferedReader br = new BufferedReader(new FileReader("mappa.txt"));
			String riga = br.readLine();
			StringTokenizer st = null;	
			String cellaLetta;
            
			mappa = new Cella[40][40];
			
            for(int i=0;i<40;i++) {
            	st = new StringTokenizer(riga);
            	
            	for(int j=0;j<40;j++) {
            		cellaLetta = st.nextToken();
            	
            		if(cellaLetta.equals("a"))	{
            			//se acqua mette null senza fare la cella
                		mappa[i][j]=null;
            		}
            		if(cellaLetta.equals("t")) {
            			//se terra crea cella ma vuota
            			Cella cella=new Cella();
                		mappa[i][j]=cella;
            		}
            		if(cellaLetta.equals("v")) {
            			//se vegetale crea cella e mette come occupante il vegetale
            			Cella cella=new Cella();
            			//chiamo costruttore predef che inzializza energiaMax random
            			cella.setOccupante(new Vegetale()); 
            			mappa[i][j]=cella;
                	}
            		if(cellaLetta.equals("c")) {
            			//se carogna crea cella e mette come occupante la carogna
            			Cella cella=new Cella();
            			//chiamo costruttore predef che inzializza energiaMax random
                		cella.setOccupante(new Carogna());
                		mappa[i][j]=cella;
            		}
            	}
                riga = br.readLine();
                //System.out.println();
            }
            br.close();
		}
		catch(IOException ioException)
		{
			System.err.println("Errore lettura file.");
		}
	}
    
    
    public void stampaMappa() {
    	//metodo che esiste solo per testare il caricamente
    	//presto sarˆ rimosso e trasformato in test junit
    	for(int i=0;i<40;i++) {
    		for(int j=0;j<40;j++) {
    			if (mappa[i][j] == null) { //e' acqua
    				System.out.print("a:     ");
    			}
    			else { //se e' terra puo' essere carogna o vegetale
    				if (mappa[i][j] instanceof Cella) {
    					Cella cella = mappa[i][j];
    					if(cella.getOccupante() instanceof Carogna)
    					{
    						Carogna carogna = (Carogna)cella.getOccupante();
    						System.out.print("c: " + carogna.getEnergiaMax() + " ");
    					}
    					if(cella.getOccupante() instanceof Vegetale)
    					{
    						Vegetale vegetale = (Vegetale)cella.getOccupante();
    						System.out.print("c: " + vegetale.getEnergiaMax() + " ");
    					}
    					if(!(cella.getOccupante() instanceof Vegetale) &&
    							!(cella.getOccupante() instanceof Carogna)) {
    						System.out.print("t:     ");
    					}
        			}
    			}
    		}
    		System.out.println();
    	}
    }	
    
    
    public static void main(String[] args) {
    	
  	    Partita p = new Partita();
	    
  	    //carica mappa e la stampa mostrando l'energiaMax generata a caso sui vari
  	    //occupanti, cioe carogne o vegetali
	    p.caricaMappa();
	    p.stampaMappa();
	    
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
