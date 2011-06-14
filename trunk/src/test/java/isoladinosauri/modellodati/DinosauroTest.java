/**
 * 
 */
package isoladinosauri.modellodati;

import static org.junit.Assert.*;
import isoladinosauri.CaricamentoMappa;
import isoladinosauri.Cella;
import isoladinosauri.Giocatore;
import isoladinosauri.Isola;
import isoladinosauri.Partita;

import org.junit.Test;

public class DinosauroTest {
	
	public Partita inizializzaPartita() {
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle;
		mappaCelle = cm.caricaDaFile();
		Isola i = new Isola(mappaCelle);
		Partita p = new Partita(i);
		return p;
	}

//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#calcolaForza()}.
//	 */
//	@Test
//	public void testCalcolaForza() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#mangia(isoladinosauri.modellodati.Occupante)}.
//	 */
//	@Test
//	public void testMangia() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#combatti(isoladinosauri.modellodati.Dinosauro)}.
//	 */
//	@Test
//	public void testCombatti() {
//		fail("Not yet implemented");
//	}

	/**
	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#calcolaRaggioVisibilita()}.
	 */
	@Test
	public void testCalcolaRaggioVisibilita() {
		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"pippo","carnivoro");
		Dinosauro d = new Carnivoro("11",1,1,1);
		p.aggiungiGiocatore(g);
		g.aggiungiDinosauro(d);
		assertEquals(2, d.calcolaRaggioVisibilita());
		d.aumentaDimensione();
		assertEquals(3, d.calcolaRaggioVisibilita());
		d.aumentaDimensione();
		assertEquals(3, d.calcolaRaggioVisibilita());
	}

	/**
	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#aumentaDimensione()}.
	 */
	@Test
	public void testAumentaDimensione() {
		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"pippo","carnivoro");
		Dinosauro d = new Carnivoro("11",1,1,1);
		p.aggiungiGiocatore(g);
		g.aggiungiDinosauro(d);
		assertEquals(1, d.aumentaDimensione());
		d.setEnergia(d.getEnergiaMax());
		assertEquals(1, d.aumentaDimensione());
		d.setEnergia(d.getEnergiaMax());
		assertEquals(1, d.aumentaDimensione());
		assertEquals(-1, d.aumentaDimensione());
		d.setEnergia(d.getEnergiaMax());
		assertEquals(1, d.aumentaDimensione());
		d.setEnergia(d.getEnergiaMax());
		assertEquals(0, d.aumentaDimensione());
	}

	/**
	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#aggCordinate(int, int)}.
	 */
	@Test
	public void testAggCordinate() {
		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"pippo","carnivoro");
		Dinosauro d = new Carnivoro("11",1,1,1);
		p.aggiungiGiocatore(g);
		g.aggiungiDinosauro(d);
//FIXME:assertFalse(d.aggCordinate(0, 0));
	}

	///////////////////get e set da togliere
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#getEtaDinosauro()}.
//	 */
//	@Test
//	public void testGetEtaDinosauro() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#setEtaDinosauro(int)}.
//	 */
//	@Test
//	public void testSetEtaDinosauro() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#getTurnoNascita()}.
//	 */
//	@Test
//	public void testGetTurnoNascita() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#setTurnoNascita(int)}.
//	 */
//	@Test
//	public void testSetTurnoNascita() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#getDurataVita()}.
//	 */
//	@Test
//	public void testGetDurataVita() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#setDurataVita(int)}.
//	 */
//	@Test
//	public void testSetDurataVita() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#getId()}.
//	 */
//	@Test
//	public void testGetId() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#setId(java.lang.String)}.
//	 */
//	@Test
//	public void testSetId() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Organismo#getEnergiaMax()}.
//	 */
//	@Test
//	public void testGetEnergiaMax() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Organismo#setEnergiaMax(int)}.
//	 */
//	@Test
//	public void testSetEnergiaMax() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Organismo#getEnergia()}.
//	 */
//	@Test
//	public void testGetEnergia() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Organismo#setEnergia(int)}.
//	 */
//	@Test
//	public void testSetEnergia() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Organismo#getRiga()}.
//	 */
//	@Test
//	public void testGetRiga() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Organismo#setRiga(int)}.
//	 */
//	@Test
//	public void testSetRiga() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Organismo#getColonna()}.
//	 */
//	@Test
//	public void testGetColonna() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Organismo#setColonna(int)}.
//	 */
//	@Test
//	public void testSetColonna() {
//		fail("Not yet implemented");
//	}

}
