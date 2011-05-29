package isoladinosauri;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Salvataggio {
	
	Partita partita;
	
	public Salvataggio(Partita partita) {
		this.partita = partita;
	}
	
	//*******************************************************************************************************************
	//************************************************SALVA PARTITA******************************************************
	//*******************************************************************************************************************
	public void salvaPartita() {
//
//			    Turno t = new Turno(this.partita);
//			    this.partita.setContatoreTurni((int)(Math.random()*100));
//			    this.partita.setTurnoCorrente(t);
//			    //Scrive
//		        ObjectContainer container = Db4o.openFile("isoladinosauri.db4o");
//		        container.store(this.partita);
//		        container.commit();
//		        container.close();
//			    
//		        //legge
//			    ObjectContainer container2 = Db4o.openFile("isoladinosauri.db4o");
//			    ObjectSet<Partita> partite = container2.query(Partita.class);
//			    for(Partita partita : partite) {
//			        System.out.println(partita.getTurnoCorrente());
//			    }
	}
}
