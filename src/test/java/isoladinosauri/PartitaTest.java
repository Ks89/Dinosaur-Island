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
	 * Test method for {@link isoladinosauri.Partita#identificaDinosauro(isoladinosauri.modellodati.Dinosauro)}.
	 */
	@Test
	public void testIdentificaDinosauro() {
		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"pippo","carnivoro");
		assertNull(p.identificaDinosauro(null));
		Dinosauro d = new Carnivoro("11",1,1,1);
		p.aggiungiGiocatore(g);
		g.aggiungiDinosauro(d);
		assertEquals(g, p.identificaDinosauro(d));
	}

	/**
	 * Test method for {@link isoladinosauri.Partita#nascitaDinosauro(int)}.
	 */
	@Test
	public void testNascitaDinosauro() {
		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"pippo","carnivoro");
		assertNull(p.identificaDinosauro(null));
		Dinosauro d = new Carnivoro("11",1,1,1);
		p.aggiungiGiocatore(g);
		g.aggiungiDinosauro(d);
		p.nascitaDinosauro(0); //FIXME: accetta anche un turno = 0
	}
}
