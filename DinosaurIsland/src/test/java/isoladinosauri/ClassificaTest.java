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
import gestioneeccezioni.CrescitaException;
import org.junit.Test;
import server.logica.CaricamentoMappa;
import server.logica.Cella;
import server.logica.Classifica;
import server.logica.Giocatore;
import server.logica.Isola;
import server.logica.Partita;
import server.logica.Turno;
import server.logica.Utente;
import server.modellodati.Dinosauro;

public class ClassificaTest {

	private Partita inizializzaPartita() {
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle;
		mappaCelle = cm.caricaDaFile("mappaTestAcquaUovo.txt");
		Isola i = new Isola(mappaCelle);
		Partita p = new Partita(i);
		return p;
	}
	
	/**
	 * Test method for {@link server.logica.Classifica#aggiungiTuplaClassifica(server.logica.Giocatore)}.
	 */
	@Test
	public void testAggiungiTuplaClassifica() { //questo metodo usa anche cercaInClassifica() e aggiornaPuntiTupla()
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		//creo un nuovo giocatore con un dinosauro (carnivoro)
		Giocatore g1 = new Giocatore(1,"trex","c");
		Utente u1 = new Utente("nomeUtente1","pass");
		g1.setUtente(u1);
		g1.aggiungiInPartita(p);
		Dinosauro dG1 = g1.getDinosauri().get(0);
		Classifica c = new Classifica(p);
		assertEquals(0, c.getClassificaGiocatori().size());
		c.aggiungiTuplaClassifica(g1); //aggiungo g1 in classifica
		assertEquals(1, c.getClassificaGiocatori().size());
		try {
			dG1.aumentaDimensione();
		} catch (CrescitaException e) {
			fail("eccezione");
		}
		c.aggiungiTuplaClassifica(g1); //faccio aggiornare la classifica
		//creo un nuovo giocatore con un dinosauro (erbivoro)
		Giocatore g2 = new Giocatore(1,"stego","e");
		Utente u2 = new Utente("nomeUtente2","pass");
		g2.setUtente(u2);
		g2.aggiungiInPartita(p);
		assertEquals(1, c.getClassificaGiocatori().size());
		c.aggiungiTuplaClassifica(g2); //aggiungo g2 in classifica
		assertEquals(2, c.getClassificaGiocatori().size());
	}

	/**
	 * Test method for {@link server.logica.Classifica#aggiornaClassificaStati()}.
	 */
	@Test
	public void testAggiornaClassificaStati() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		//creo un nuovo giocatore con un dinosauro (carnivoro)
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		assertEquals(1, p.getGiocatori().size());
		Classifica c = new Classifica(p);
		c.aggiungiTuplaClassifica(g);
		c.aggiornaClassificaStati();
		p.rimuoviGiocatore(g);
		assertEquals(0, p.getGiocatori().size());
		c.aggiornaClassificaStati();
	}
	
	/**
	 * Test method for {@link server.logica.Classifica#ottieniClassifica()}.
	 */
	@Test
	public void testOttieniClassifica() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		//creo un nuovo giocatore con un dinosauro (carnivoro)
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		assertEquals(1, p.getGiocatori().size());
		Classifica c = new Classifica(p);
		c.aggiungiTuplaClassifica(g);
		c.aggiornaClassificaStati();
		String classifica = c.ottieniClassifica();
	}
}
