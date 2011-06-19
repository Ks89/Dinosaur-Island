package isoladinosauri.modellodati;

import static org.junit.Assert.*;
import isoladinosauri.CaricamentoMappa;
import isoladinosauri.Cella;
import isoladinosauri.Giocatore;
import isoladinosauri.Isola;
import isoladinosauri.Partita;
import isoladinosauri.Turno;
import isoladinosauri.Utente;

import org.junit.Test;

import gestioneeccezioni.CrescitaException;

public class DinosauroTest {
	
	public Partita inizializzaPartita() {
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle;
		mappaCelle = cm.caricaDaFile();
		Isola i = new Isola(mappaCelle);
		Partita p = new Partita(i);
		return p;
	}

	/**
	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#calcolaRaggioVisibilita()}.
	 */
	@Test
	public void testCalcolaRaggioVisibilita() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		Dinosauro d = g.getDinosauri().get(0);
		d.setEnergiaMax(1000);
		d.setEnergia(1000);
		assertEquals(2, d.calcolaRaggioVisibilita());
		d.setEnergia(d.getEnergiaMax());
		try {
			d.aumentaDimensione();
		} catch (CrescitaException e) {
			fail("eccezione");
		}
		assertEquals(3, d.calcolaRaggioVisibilita());
		d.setEnergia(d.getEnergiaMax());
		try {
			d.aumentaDimensione();
		} catch (CrescitaException e) {
			fail("eccezione");
		}
		assertEquals(3, d.calcolaRaggioVisibilita());
		d.setEnergia(d.getEnergiaMax());
		try {
			d.aumentaDimensione();
		} catch (CrescitaException e) {
			fail("eccezione");
		}
		assertEquals(4, d.calcolaRaggioVisibilita());
	}

	/**
	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#aumentaDimensione()}.
	 */
	@Test
	public void testAumentaDimensione() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		Dinosauro d = g.getDinosauri().get(0);
		assertEquals(1, d.getEnergiaMax()/1000);
		d.setEnergia(d.getEnergiaMax());
		try {
			d.aumentaDimensione();
		} catch (CrescitaException e) {
			fail("eccezione");
		}
		assertEquals(2, d.getEnergiaMax()/1000);
		d.setEnergia(d.getEnergiaMax());
		try {
			d.aumentaDimensione();
		} catch (CrescitaException e) {
			fail("eccezione");
		}
		assertEquals(3, d.getEnergiaMax()/1000);
		d.setEnergia(d.getEnergiaMax());
		try {
			d.aumentaDimensione();
		} catch (CrescitaException e) {
			fail("eccezione");
		}
		assertEquals(4, d.getEnergiaMax()/1000);
		d.setEnergia(d.getEnergiaMax());
		try {
			d.aumentaDimensione();
		} catch (CrescitaException e) {
			fail("eccezione");
		}
		assertEquals(5, d.getEnergiaMax()/1000);
		d.setEnergia(d.getEnergiaMax());
		try {
			d.aumentaDimensione();
			fail("non ha generato eccezione per aver raggiunto la dimensione max");
		} catch (CrescitaException e) {
			System.out.println("ok generata eccezione per aver raggiunto la dimensione max");
		}
		//riporto il dino a dimensione 4:
		d.setEnergiaMax(4000);
		//gli do ora energia insufficiente per farlo morire nella prossima richiesta di aumento dimensione
		d.setEnergia(100);
		try {
			d.aumentaDimensione();
			fail("non ha generato eccezione per morte dinosauro (energia insufficiente per crescere)");
		} catch (CrescitaException e) {
			System.out.println("ok generata eccezione per morte dinosauro (energia insufficiente per crescere)");
		}
	}
}
