package isoladinosauri;

import static org.junit.Assert.*;
import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;

import org.junit.Test;

import Eccezioni.DeposizioneException;

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
		Giocatore g = new Giocatore(1,"pippo","c");
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
		Giocatore g = new Giocatore(1,"pippo","c");
		assertEquals("01", g.generaIdDinosauro());
	}


	/**
	 * Test method for {@link isoladinosauri.Giocatore#aggiungiDinosauro(isoladinosauri.modellodati.Dinosauro)}.
	 */
	@Test
	public void testAggiungiDinosauro() {
		Giocatore g = new Giocatore(1,"stego","c");
		Dinosauro d1 = new Carnivoro("11",1,1,1);
		Dinosauro d2 = new Carnivoro("12",1,2,1);
		Dinosauro d3 = new Carnivoro("13",2,1,1);
		Dinosauro d4 = new Carnivoro("14",3,1,1);
		Dinosauro d5 = new Carnivoro("15",3,2,1);
		Dinosauro d6 = new Carnivoro("16",3,3,1);
		g.aggiungiDinosauro(d1);
		g.aggiungiDinosauro(d2);
		g.aggiungiDinosauro(d3);
		g.aggiungiDinosauro(d4);
		g.aggiungiDinosauro(d5);
		assertFalse(g.aggiungiDinosauro(d6));
	}

	/**
	 * Test method for {@link isoladinosauri.Giocatore#rimuoviDinosauro(isoladinosauri.modellodati.Dinosauro)}.
	 */
	@Test //TODO: rivedere x l'uso di assert
	public void testRimuoviDinosauro() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"stego","c");
		g.aggiungiInPartita(p);
		Dinosauro d = g.getDinosauri().get(0);
		g.rimuoviDinosauro(d);
	}

	/**
	 * Test method for {@link isoladinosauri.Giocatore#eseguiDeposizionedeponiUovo(isoladinosauri.modellodati.Dinosauro)}.
	 */
	@Test //FIXME: non lo trova nella mappa
	public void testEseguiDeposizionedeponiUovo() {
		Giocatore g = new Giocatore(1,"stego","c");
		Dinosauro d = new Carnivoro("11",1,1,1);
		try {
			g.eseguiDeposizionedeponiUovo(d);
			fail("non ha generato eccezioni");
		} catch (DeposizioneException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}
//		try {
//			g.aggiungiUovo(d.getRiga(), d.getColonna(), d.getId());
//			d.setEnergia(10);
//			g.eseguiDeposizionedeponiUovo(d);
//			fail("non ha generato eccezioni");
//		} catch (DeposizioneException e) {
////			e.printStackTrace();
//		} catch (NullPointerException e) {
////			e.printStackTrace();
//		}
	}
}
