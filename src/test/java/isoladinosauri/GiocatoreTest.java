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
		Giocatore g = new Giocatore(1,"stego","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		assertEquals(0, p.getGiocatori().size());
		g.aggiungiInPartita(p);
		assertEquals(1, p.getGiocatori().size());
	}

	/**
	 * Test method for {@link isoladinosauri.Giocatore#aggiungiDinosauro(isoladinosauri.modellodati.Dinosauro)}.
	 */
	@Test
	public void testAggiungiDinosauro() {
		Giocatore g = new Giocatore(1,"stego","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
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
	@Test
	public void testRimuoviDinosauro() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"stego","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		Dinosauro d = g.getDinosauri().get(0);
		g.rimuoviDinosauro(d);
		assertEquals(0, g.getDinosauri().size());
	}

	/**
	 * Test method for {@link isoladinosauri.Giocatore#eseguiDeposizionedeponiUovo(isoladinosauri.modellodati.Dinosauro)}.
	 */
	@Test
	public void testEseguiDeposizionedeponiUovo() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"stego","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		Dinosauro d = g.getDinosauri().get(0);
		try {
			g.aggiungiUovo(d.getRiga(), d.getColonna(), d.getId());
			d.setEnergia(4000);
			g.eseguiDeposizionedeponiUovo(d);
			assertEquals(2500,d.getEnergia());
		} catch (DeposizioneException e) {
			fail("eccezione");
		}		
		try {
			g.aggiungiUovo(d.getRiga(), d.getColonna(), d.getId());
			d.setEnergia(10);
			g.eseguiDeposizionedeponiUovo(d);
			fail("non ha generato eccezione x mancanza di energia");
		} catch (DeposizioneException e) {
			System.out.println("ok generata eccezione x mancanza di energia");
		} 
	}
}
