/*
Copyright 2011-2015 Stefano Cappa
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package isoladinosauri;

import static org.junit.Assert.*;
import org.junit.Test;
import server.logica.CaricamentoMappa;
import server.logica.Cella;
import server.logica.Giocatore;
import server.logica.Isola;
import server.logica.Partita;
import server.logica.Turno;
import server.logica.Utente;
import server.modellodati.Dinosauro;
import gestioneeccezioni.DeposizioneException;

public class PartitaTest {

	/**
	 * Test method for {@link server.logica.Partita#aggiungiGiocatore(server.logica.Giocatore)}.
	 */
	
	private Partita inizializzaPartita() {
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle;
		mappaCelle = cm.caricaDaFile("mappaTestAcquaUovo.txt");
		Isola i = new Isola(mappaCelle);
		Partita p = new Partita(i);
		return p;
	}
	
	@Test
	public void testAggiungiGiocatore() {

		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"stego","e");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		p.aggiungiGiocatore(g);
		assertEquals(g, p.getGiocatori().get(0));
	}

	/**
	 * Test method for {@link server.logica.Partita#rimuoviGiocatore(server.logica.Giocatore)}.
	 */
	@Test
	public void testRimuoviGiocatore() {
		Partita p = inizializzaPartita();
		Giocatore g = new Giocatore(1,"stego","e");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		p.aggiungiGiocatore(g);
		assertEquals(g, p.getGiocatori().get(0));
		p.rimuoviGiocatore(g);
		assertEquals(true, p.getGiocatori().isEmpty());
	}

	/**
	 * Test method for {@link server.logica.Partita#identificaDinosauro(server.modellodati.Dinosauro)}.
	 */
	@Test
	public void testIdentificaDinosauro() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		assertNull(p.identificaDinosauro(null));
		Dinosauro d = g.getDinosauri().get(0);
		assertEquals(g, p.identificaDinosauro(d));
	}

	/**
	 * Test method for {@link server.logica.Partita#nascitaDinosauro(int)}.
	 */
	@Test
	public void testNascitaDinosauro() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
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
}
