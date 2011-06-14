package isoladinosauri;

import static org.junit.Assert.*;
import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;

import org.junit.Test;

public class PartitaTest {

	/**
	 * Test method for {@link isoladinosauri.Partita#aggiungiGiocatore(isoladinosauri.Giocatore)}.
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
	public void testAggiungiGiocatore() {

		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"pippo","erbivoro");
		p.aggiungiGiocatore(g);
		assertEquals(g, p.getGiocatori().get(0));
	}

	/**
	 * Test method for {@link isoladinosauri.Partita#rimuoviGiocatore(isoladinosauri.Giocatore)}.
	 */
	@Test
	public void testRimuoviGiocatore() {
		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"pippo","e");
		p.aggiungiGiocatore(g);
		assertEquals(g, p.getGiocatori().get(0));
		p.rimuoviGiocatore(g);
		assertEquals(true, p.getGiocatori().isEmpty());
	}

	/**
	 * Test method for {@link isoladinosauri.Partita#incrementaEtaGiocatori()}.
	 */
	@Test //FIXME:
	public void testIncrementaEtaGiocatori() { //incrementa eta' di tutti i giocatori
//		Partita p = inizializzaPartita();
//		assertEquals(p.)
//		assertEquals(1, p.incrementaEtaGiocatori());
	}

	/**
	 * Test method for {@link isoladinosauri.Partita#identificaDinosauro(isoladinosauri.modellodati.Dinosauro)}.
	 */
	@Test
	public void testIdentificaDinosauro() { // identifica il giocatore piu' ke il dinosauro
		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"pippo","carnivoro");
		assertNull(p.identificaDinosauro(null));
		Dinosauro d = new Carnivoro("11",0,0,1);
		p.aggiungiGiocatore(g);
		g.aggiungiDinosauro(d);
		assertEquals(g, p.identificaDinosauro(d));
	}

	/**
	 * Test method for {@link isoladinosauri.Partita#nascitaDinosauro(int)}.
	 */
	@Test
	public void testNascitaDinosauro() {
		fail("Not yet implemented");
	}

	
///////////////////////////////get e set da togliere	
	
//	/**
//	 * Test method for {@link isoladinosauri.Partita#getTurnoCorrente()}.
//	 */
//	@Test
//	public void testGetTurnoCorrente() {
//		Partita p = inizializzaPartita();
//		
//		p.setTurnoCorrente(1);
//		assertEquals(1, p.getTurnoCorrente());
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Partita#setTurnoCorrente(isoladinosauri.Turno)}.
//	 */
//	@Test
//	public void testSetTurnoCorrente() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Partita#getIsola()}.
//	 */
//	@Test
//	public void testGetIsola() {
//		CaricamentoMappa cm = new CaricamentoMappa();
//		Cella[][] mappaCelle = cm.caricaDaFile();
//		Isola i = new Isola(mappaCelle);
//		Partita p = new Partita(i);
//		assertEquals(i, p.getIsola());
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Partita#setIsola(isoladinosauri.Isola)}.
//	 */
//	@Test
//	public void testSetIsola() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Partita#getGiocatori()}.
//	 */
//	@Test
//	public void testGetGiocatori() {
//		Partita p = inizializzaPartita();
//		Giocatore g = new Giocatore(1,"pippo","e");
//		p.aggiungiGiocatore(g);
//	}
//	
//	/**
//	 * Test method for {@link isoladinosauri.Partita#getContatoreTurni()}.
//	 */
//	@Test
//	public void testGetContatoreTurni() {
//		Partita p = inizializzaPartita();
//		p.setContatoreTurni(1);
//		assertEquals(1, p.getContatoreTurni());
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Partita#setContatoreTurni(int)}.
//	 */
//	@Test
//	public void testSetContatoreTurni() {
//		Partita p = inizializzaPartita();
//		p.setContatoreTurni(1);
//		assertEquals(1, p.getContatoreTurni());
//	}

}
