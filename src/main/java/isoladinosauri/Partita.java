package isoladinosauri;

import java.util.List;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Partita {
	
	private Cella[][] mappa;
	private Turno turnoCorrente; //Conserva solo il turno corrente
	private List<Giocatore> giocatori;
	private int contatoreTurni;
	
	
	
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


    
    public static void main(String[] args) {
	    Partita p = new Partita();
	    Turno t = new Turno(p);
	    t.setContatoreTurno((int)(Math.random()*100));
	    p.setTurnoCorrente(t);
	    
	    //Scrive
        ObjectContainer container = Db4o.openFile("isoladinosauri.db4o");
        container.store(p);
        container.commit();
        container.close();
	    
        //legge
	    ObjectContainer container2 = Db4o.openFile("isoladinosauri.db4o");
	    ObjectSet<Partita> partite = container2.query(Partita.class);
	    for(Partita partita : partite) {
	        System.out.println(partita.getTurnoCorrente().getContatoreTurno());
	    }
	    
	    
        
    }
	
	

}
