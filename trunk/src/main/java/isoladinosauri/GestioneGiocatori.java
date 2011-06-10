package isoladinosauri;
import java.util.ArrayList;
import java.util.List;

public class GestioneGiocatori {
	private List<String> tokenLoggati;
	private List<Giocatore> giocatoriCreati;

	public GestioneGiocatori() {
		this.giocatoriCreati = new ArrayList<Giocatore>();
		this.tokenLoggati = new ArrayList<String>();
	}
	
	public List<Giocatore> ottieniGiocatoriCreati() {
		return this.giocatoriCreati;
	}
	
	public void aggiungiGiocatoreCreato(Giocatore giocatore) {
		this.giocatoriCreati.add(giocatore);
	}
	
	public List<String> ottieniTokenLoggati() {
		return this.tokenLoggati;
	}
	
	public void aggiungiTokenLoggati(String user, String token) {
		this.tokenLoggati.add(user+" "+token);
	}
	
	public void rimuoviTokenLoggati(String user, String token) {
		this.tokenLoggati.remove(user+" "+token);
	}
	
	public int indiceGiocatoreCreato(String nomeUtente) {
		int k=0;
		if(this.ottieniGiocatoriCreati().isEmpty()) {
			return -1;
		}
		//cerco l'indice del giocatore nell'array dei giocatoriCreati
		for(k=0; k<this.ottieniGiocatoriCreati().size(); k++) {
			if(this.ottieniGiocatoriCreati().get(k).getUtente().getNomeUtente()!=null) {
				if(this.ottieniGiocatoriCreati().get(k).getUtente().getNomeUtente().equals(nomeUtente)) {
					break;
				}
			}
		}
		return k;
	}
	
	public int indiceTokenLoggato(String nomeUtente, String token) {
		int k=0;
		if(this.ottieniTokenLoggati().isEmpty()) {
			return -1;
		}
		
		for(k=0; k<this.ottieniTokenLoggati().size(); k++) {
			if(this.ottieniTokenLoggati().get(k)!=null) {
				if(this.ottieniTokenLoggati().get(k).equals(nomeUtente+" "+token)) {
					break;
				}
			}
		}
		return k;
	}
	
	public boolean controlloSeLoggato(String token) {
		if(this.ottieniTokenLoggati().isEmpty()) {
			return false;
		}
		//cerco se l'utente e' gia' loggato
		boolean loggato = false;
		for(int i=0; i<this.ottieniTokenLoggati().size(); i++) {
			if(this.ottieniTokenLoggati().get(i)!=null) {
				if(this.ottieniTokenLoggati().get(i).split(" ")[1].equals(token)) {
					loggato = true;
					break;
				}
			}
		}
		return loggato;
	}
}