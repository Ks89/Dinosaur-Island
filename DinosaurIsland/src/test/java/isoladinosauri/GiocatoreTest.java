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
import server.modellodati.Carnivoro;
import server.modellodati.Dinosauro;
import gestioneeccezioni.DeposizioneException;

public class GiocatoreTest {

	/**
	 * Test method for {@link server.logica.Giocatore#aggiungiInPartita(server.logica.Partita)}.
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
	public void testAggiungiInPartita() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		assertEquals(0, p.getGiocatori().size());
		g.aggiungiInPartita(p);
		assertEquals(1, p.getGiocatori().size());
	}

	/**
	 * Test method for {@link server.logica.Giocatore#aggiungiDinosauro(server.modellodati.Dinosauro)}.
	 */
	@Test
	public void testAggiungiDinosauro() {
		Giocatore g = new Giocatore(1,"trex","c");
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
	 * Test method for {@link server.logica.Giocatore#rimuoviDinosauro(server.modellodati.Dinosauro)}.
	 */
	@Test
	public void testRimuoviDinosauro() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		Dinosauro d = g.getDinosauri().get(0);
		g.rimuoviDinosauro(d);
		assertEquals(0, g.getDinosauri().size());
	}

	/**
	 * Test method for {@link server.logica.Giocatore#eseguiDeposizionedeponiUovo(server.modellodati.Dinosauro)}.
	 */
	@Test
	public void testEseguiDeposizionedeponiUovo() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		Dinosauro d = g.getDinosauri().get(0);
		try {
			d.setEnergia(4000);
			String idPrimoNato = g.eseguiDeposizionedeponiUovo(d);
			assertEquals(2500,d.getEnergia());
			assertEquals("12",idPrimoNato);
		} catch (DeposizioneException e) {
			fail("eccezione");
		}
		String idSecondoNato = new String("");
		try {
			d.setEnergia(10);
			idSecondoNato =	g.eseguiDeposizionedeponiUovo(d);
			fail("non ha generato eccezione x mancanza di energia");
		} catch (DeposizioneException e) {
			System.out.println("ok generata eccezione x mancanza di energia");
			assertEquals("", idSecondoNato);
		}
	}
}
