package isoladinosauri;

public class Turno {
	
	private Partita partita;
	private int contatoreTurno;
	
	public Turno(Partita partita) {
		this.partita = partita;
	}

	public Cella[][] getVisuale(String idDinosauro){
		//TODO
		return null;
	}
	
	public void muovi() { //gestione movimento
		//TODO
	}

    public int getContatoreTurno() {
        return contatoreTurno;
    }

    public void setContatoreTurno(int contatoreTurno) {
        this.contatoreTurno = contatoreTurno;
    }
	
	

}
