package isoladinosauri;

import static org.junit.Assert.*;
import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;

import org.junit.Test;

public class GiocatoreTest {

	/**
	 * Test method for {@link isoladinosauri.Giocatore#aggiungiInPartita(isoladinosauri.Partita)}.
	 */
	
	public Partita inizializzaPartita() {
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle;
		mappaCelle = cm.caricaDaFile();
		Isola i = new Isola(mappaCelle);
		Partita p = new Partita(i);
		return p;
	}
	
	@Test
	public void testAggiungiInPartita() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"pippo","carnivoro");
		Utente u = new Utente("nomeUtente","pass");
		Dinosauro d = new Carnivoro("11",1,1,1);
		g.aggiungiDinosauro(d);
		g.setUtente(u);
		assertEquals(0, p.getGiocatori().size());
		g.aggiungiInPartita(p);
		assertEquals(1, p.getGiocatori().size());
	}

	/**
	 * Test method for {@link isoladinosauri.Giocatore#generaIdDinosauro()}.
	 */
	@Test //FIXME: controllare
	public void testGeneraIdDinosauro() {
		Giocatore g = new Giocatore(1,"pippo","carnivoro");
		assertEquals("01", g.generaIdDinosauro());
	}


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
//	@Test //TODO: aspettare x le uova
//	public void testRimuoviDinosauro() {
//		Dinosauro d = new Carnivoro("11",1,1,1);
//	}

//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#aggiungiUovo(int, int)}.
//	 */
//	@Test //TODO: aspettare x le uova
//	public void testAggiungiUovo() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#rimuoviUova()}.
//	 */
//	@Test //TODO: aspettare x le uova
//	public void testRimuoviUova() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Giocatore#eseguiDeposizionedeponiUovo(isoladinosauri.modellodati.Dinosauro)}.
//	 */
//	@Test //TODO: aspettare x le uova
//	public void testEseguiDeposizionedeponiUovo() {
//		fail("Not yet implemented");
//	}
}
