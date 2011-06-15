///**
// * 
// */
//package isoladinosauri;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//public class ClassificaTest {
//
////////////////////////get da togliere	
////	/**
////	 * Test method for {@link isoladinosauri.Classifica#getClassifica()}.
////	 */
////	@Test
////	public void testGetClassifica() {
////		fail("Not yet implemented");
////	}
//
//	public Partita inizializzaPartita() {
//		CaricamentoMappa cm = new CaricamentoMappa();
//		Cella[][] mappaCelle;
//		mappaCelle = cm.caricaDaFile();
//		Isola i = new Isola(mappaCelle);
//		Partita p = new Partita(i);
//		return p;
//	}
//	
//	/**
//	 * Test method for {@link isoladinosauri.Classifica#aggiungiTuplaClassifica(isoladinosauri.Giocatore)}.
//	 */
//	@Test
//	public void testAggiungiTuplaClassifica() {
//		Partita p = inizializzaPartita();
//		Giocatore g = new Giocatore(1,"pippo","carnivoro");
//		Utente u = new Utente("nomeUtente","pass");
//		g.setUtente(u);
//		Classifica c = new Classifica(p);
//		c.aggiungiTuplaClassifica(g);
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Classifica#aggiornaClassificaStati()}.
//	 */
//	@Test
//	public void testAggiornaClassificaStati() {
//		Partita p = inizializzaPartita();
//		Giocatore g = new Giocatore(1,"pippo","carnivoro");
//		Utente u = new Utente("nomeUtente","pass");
//		g.setUtente(u);
//		Classifica c = new Classifica(p);
//		c.aggiornaClassificaStati();
//	}
//}
