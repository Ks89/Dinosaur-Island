///**
// * 
// */
//package isoladinosauri;
//
//import static org.junit.Assert.*;
//import isoladinosauri.modellodati.Carnivoro;
//import isoladinosauri.modellodati.Dinosauro;
//
//import org.junit.Test;
//
//public class GiocatoreTest {
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#aggiungiInPartita(isoladinosauri.Partita)}.
//	 */
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
//	@Test //FIXME: da' errore di nullPointerException
//	public void testAggiungiInPartita() {
//		Partita p = inizializzaPartita();
//		Giocatore g = new Giocatore(1,"pippo","carnivoro");
//		assertEquals(0, p.getGiocatori().size());
//		g.aggiungiInPartita(p);
//		assertEquals(1, p.getGiocatori().size());
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#generaIdDinosauro()}.
//	 */
//	@Test //TODO: controllare
//	public void testGeneraIdDinosauro() {
//		Partita p = inizializzaPartita();
//		Giocatore g = new Giocatore(1,"pippo","carnivoro");
////		Dinosauro d = new Carnivoro("11",1,1,1);
//		p.aggiungiGiocatore(g);
////		g.aggiungiDinosauro(d);
//		assertEquals("11", g.generaIdDinosauro());
//	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#getIdGiocatore()}.
////	 */
////	@Test
////	public void testGetIdGiocatore() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#setIdGiocatore(int)}.
////	 */
////	@Test
////	public void testSetIdGiocatore() {
////		fail("Not yet implemented");
////	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#aggiungiDinosauro(isoladinosauri.modellodati.Dinosauro)}.
//	 */
//	@Test //TODO: aspettare x le uova
//	public void testAggiungiDinosauro() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#rimuoviDinosauro(isoladinosauri.modellodati.Dinosauro)}.
//	 */
//	@Test
//	public void testRimuoviDinosauro() {
//		Dinosauro d = new Carnivoro("11",1,1,1);
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#getUova()}.
//	 */
//	@Test
//	public void testGetUova() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#aggiungiUovo(int, int)}.
//	 */
//	@Test
//	public void testAggiungiUovo() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#rimuoviUova()}.
//	 */
//	@Test
//	public void testRimuoviUova() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#eseguiDeposizionedeponiUovo(isoladinosauri.modellodati.Dinosauro)}.
//	 */
//	@Test
//	public void testEseguiDeposizionedeponiUovo() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#incrementaEtaAttuali()}.
//	 */
//	@Test
//	public void testIncrementaEtaAttuali() {
//		fail("Not yet implemented");
//	}
//	
//	
//	
//	
///////////////////////////////// get e set da togliere
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#getTurnoNascita()}.
////	 */
////	@Test
////	public void testGetTurnoNascita() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#setTurnoNascita(int)}.
////	 */
////	@Test
////	public void testSetTurnoNascita() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#getEtaAttuale()}.
////	 */
////	@Test
////	public void testGetEtaAttuale() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#setEtaAttuale(int)}.
////	 */
////	@Test
////	public void testSetEtaAttuale() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#getDinosauri()}.
////	 */
////	@Test
////	public void testGetDinosauri() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#getMappaVisibile()}.
////	 */
////	@Test
////	public void testGetMappaVisibile() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#getNomeSpecie()}.
////	 */
////	@Test
////	public void testGetNomeSpecie() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#setNomeSpecie(java.lang.String)}.
////	 */
////	@Test
////	public void testSetNomeSpecie() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#getUtente()}.
////	 */
////	@Test
////	public void testGetUtente() {
////		fail("Not yet implemented");
////	}
//
////	/**
////	 * Test method for {@link isoladinosauri.Giocatore#setUtente(isoladinosauri.Utente)}.
////	 */
////	@Test
////	public void testSetUtente() {
////		fail("Not yet implemented");
////	}
//
//}
