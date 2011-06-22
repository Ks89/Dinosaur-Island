package server.salvataggio;

/**
 *	Dato da salvare. Contiene la chiave e l'oggetto.
 */
public class SalvataggioGiocatore {
    
    private String chiave;
    private Object oggetto;
    
    public String getChiave() {
        return chiave;
    }
    public void setChiave(String chiave) {
        this.chiave = chiave;
    }
    public Object getOggetto() {
        return oggetto;
    }
    public void setOggetto(Object oggetto) {
        this.oggetto = oggetto;
    }
    
    

}
