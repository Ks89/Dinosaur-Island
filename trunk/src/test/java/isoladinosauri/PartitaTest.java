package isoladinosauri;

import static org.junit.Assert.*;
import isoladinosauri.modellodati.Dinosauro;

import org.junit.Test;

import Eccezioni.DeposizioneException;

public class PartitaTest {

	/**
	 * Test method for {@link isoladinosauri.Partita#aggiungiGiocatore(isoladinosauri.Giocatore)}.
	 */
	
	private Partita inizializzaPartita() {
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
		Giocatore g = new Giocatore(1,"pippo","e");
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
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"pippo","c");
		g.aggiungiInPartita(p);
		assertNull(p.identificaDinosauro(null));
		Dinosauro d = g.getDinosauri().get(0);
		assertEquals(g, p.identificaDinosauro(d));
	}

	/**
	 * Test method for {@link isoladinosauri.Partita#nascitaDinosauro(int)}.
	 */
	@Test
	public void testNascitaDinosauro() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"stego","c");
		g.aggiungiInPartita(p);
		assertNull(p.identificaDinosauro(null));
		Dinosauro d = g.getDinosauri().get(0);
		d.setEnergiaMax(4000);
		d.setEnergia(4000);
		try {
			g.eseguiDeposizionedeponiUovo(d);
			p.nascitaDinosauro(1);//totale dinosauri=2
		} catch (DeposizioneException e) {
			fail("eccezione deposizione uovo");
		}
		for(int i=0; i<3; i++) {
			d.setEnergiaMax(4000);
			d.setEnergia(4000);
			try {
				g.eseguiDeposizionedeponiUovo(d);
				p.nascitaDinosauro(1); //tot=3,4,5
			} catch (DeposizioneException e) {
				fail("eccezione deposizione uovo");
			}
		}
		d.setEnergiaMax(4000);
		d.setEnergia(4000);
		try {
			g.eseguiDeposizionedeponiUovo(d);
			p.nascitaDinosauro(1);//e' il sesto dinosauro
			fail("non ha generato eccezione deposizione uovo");
		} catch (DeposizioneException e) {
			System.out.println("ok generata eccezione deposizione uovo (squadra completa)");
		}
		try {
			d.setEnergia(1000);
			g.eseguiDeposizionedeponiUovo(d);
			fail("non ha generato eccezione morte per energia insufficiente");
		} catch (DeposizioneException e1) {
			System.out.println("ok generata eccezione morte per energia insufficiente");
		}
		
	}
	
//	/** TODO:
//	 * Test method for {@link isoladinosauri.Partita#generaCoordinateNascituro(int, int)}.
//	 */
//	@Test
//	public void testgeneraCoordinateNascituro() {
//		fail("da fare");
//	}
}
