
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

package isoladinosauri.modellodati;

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
import gestioneeccezioni.CrescitaException;

public class DinosauroTest {
	
	public Partita inizializzaPartita() {
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle;
		mappaCelle = cm.caricaDaFile("mappaTestAcquaUovo.txt");
		Isola i = new Isola(mappaCelle);
		Partita p = new Partita(i);
		return p;
	}

	/**
	 * Test method for {@link server.modellodati.Dinosauro#calcolaRaggioVisibilita()}.
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
	 * Test method for {@link server.modellodati.Dinosauro#aumentaDimensione()}.
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
