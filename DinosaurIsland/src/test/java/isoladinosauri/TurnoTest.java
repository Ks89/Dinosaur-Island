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
import server.modellodati.Carogna;
import server.modellodati.Dinosauro;
import server.modellodati.Vegetale;
import gestioneeccezioni.DeposizioneException;
import gestioneeccezioni.MovimentoException;

public class TurnoTest {

	private Partita inizializzaPartita() {
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle;
		mappaCelle = cm.caricaDaFile("mappaTestAcquaUovo.txt");
		Isola i = new Isola(mappaCelle);
		Partita p = new Partita(i);
		return p;
	}
	
	private Dinosauro inizializzaDinosauro(Partita p,Dinosauro d, int energia, int r, int c) {
		d.setEnergiaMax(energia);
		d.setEnergia(energia);
		d.setRiga(r);
		d.setColonna(c);
		p.getIsola().getMappa()[r][c].setDinosauro(d);
		return d;
	}
	
	/**
	 * Test method for {@link server.logica.Turno#illuminaMappa(server.logica.Giocatore, int, int, int)}.
	 */
	@Test
	public void illuminaMappa() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		t.illuminaMappa(g, 1, 1, 3);
		t.illuminaMappa(g, 20, 20, 2);
		t.illuminaMappa(g, 38, 38, 3);
	}
	
	/**
	 * Test method for {@link server.logica.Turno#ottieniStradaPercorsa(int, int, int, int)}.
	 */
	@Test
	public void testOttieniStradaPercorsa() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		//creo un nuovo giocatore con un dinosauro (carnivoro)
		Giocatore g = new Giocatore(1,"trex","c");
		Utente u = new Utente("nomeUtente","pass");
		g.setUtente(u);
		g.aggiungiInPartita(p);
		Dinosauro d = g.getDinosauri().get(0);
		d = inizializzaDinosauro(p,d,4000,1,2);
		try {
			t.spostaDinosauro(d, 1, 1);
		} catch (MovimentoException e) {
			fail("eccezione");
		}
		t.ottieniStradaPercorsa(1, 2, 1, 1);
		// creo un altro giocatore con un dinosauro (erbivoro):
		Giocatore g2 = new Giocatore(1,"stego2","e");
		Utente u2 = new Utente("nomeUtente2","pass");
		g2.setUtente(u2);
		g2.aggiungiInPartita(p);
		Dinosauro dG2 = g2.getDinosauri().get(0);
		dG2 = inizializzaDinosauro(p,dG2,4000,1,2);
		try {
			t.spostaDinosauro(dG2, 2, 3);
		} catch (MovimentoException e) {
			fail("eccezione");
		}
		t.ottieniStradaPercorsa(1, 2, 2, 3);
		
		//faccio ottenere un null x una strada non accettabile
		assertNull(t.ottieniStradaPercorsa(1, 1, 5, 5));
		
	}

	/**
	 * Test method for {@link server.logica.Turno#spostaDinosauro(server.modellodati.Dinosauro, int, int)}.
	 */
	@Test
	public void testSpostaDinosauro() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		//creo un nuovo giocatore con un dinosauro (carnivoro)
		Giocatore g1 = new Giocatore(1,"trex1","c");
		Utente u1 = new Utente("nomeUtente1","pass");
		g1.setUtente(u1);
		g1.aggiungiInPartita(p);
		Dinosauro dG1 = g1.getDinosauri().get(0);
		
		// genera eccezione per spostamento in acqua (1,4)
		dG1 = inizializzaDinosauro(p,dG1,4000,1,1);
		try {
			t.spostaDinosauro(dG1, 1, 4); // in (1,4) c'e' acqua (origine in alto a sx)
			fail("non ha generato eccezione spostamento su acqua");
		} catch (MovimentoException e) {
			System.out.println("ok generata eccezione spostamento su acqua");
		}
		
		//spostamento su terra semplice con morte x energia insufficiente
		dG1.setEnergia(10);
		try {
			t.spostaDinosauro(dG1, 2, 1);
			fail("non ha generato eccezione morte per energia insufficiente ");
		} catch (MovimentoException e) {
			System.out.println("ok generata eccezione morte per energia insufficiente");
			//rimetto in vita il dino riassegnandolo a g1
			dG1 = inizializzaDinosauro(p,dG1,4000,1,1);
			g1.aggiungiDinosauro(dG1);
		}
		
		//spostamento su terra semplice
		try {
			t.spostaDinosauro(dG1, 2, 1);
		} catch (MovimentoException e) {
			fail("eccezione movimento");

		}
		
		//spostamento su vegetazione con carnivoro con morte per insufficiente energia
		dG1.setEnergia(10);
		try {
			t.spostaDinosauro(dG1, 1, 3); // in (1,3) c'e' vegetazione (origine in alto a sx)
			fail("non ha generato eccezione per morte causata da insufficiente energia");
		} catch (MovimentoException e) {
			System.out.println("ok generata eccezione per morte causata da insufficiente energia");
		}
		//rimetto in vita il dino riassegnandolo a g1
		dG1 = inizializzaDinosauro(p,dG1,4000,2,1);
		g1.aggiungiDinosauro(dG1);
		
		//spostamento su vegetazione con carnivoro quindi senza mangiarla
		try {
			t.spostaDinosauro(dG1, 1, 3); // in (1,3) c'e' vegetazione (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
			
		
		//spostamento su carogna con carnivoro con piena energia quindi non la mangia
		dG1 = inizializzaDinosauro(p,dG1,4000,38,37);
		try {
			t.spostaDinosauro(dG1, 38, 38); // in (38,38) c'e' una carogna (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
		Carogna carogna0 = new Carogna();
		p.getIsola().getMappa()[38][38].setDinosauro(null);
		p.getIsola().getMappa()[38][38].setOccupante(carogna0);
		
		//spostamento su carogna con carnivoro con energia insufficiente causando morte
		dG1 = inizializzaDinosauro(p,dG1,10,38,37);
		try {
			t.spostaDinosauro(dG1, 38, 38); // in (38,38) c'e' una carogna (origine in alto a sx)
			fail("non ha generato eccezione per morte causata da insufficiente energia");
		} catch (MovimentoException e) {
			System.out.println("ok generata eccezione per morte causata da insufficiente energia");
		}
		//rimetto in vita il dino riassegnandolo a g1
		dG1 = inizializzaDinosauro(p,dG1,1000,37,38);
		g1.aggiungiDinosauro(dG1);
	
		//spostamento su carogna con carnivoro con poca energia e in condizione di mangiarla tutta
		dG1.setEnergia(100);
		//rimetto nella mappa una carogna sovrascrivendola al dino mosso in precedenza
		Carogna carogna1 = new Carogna();
		p.getIsola().getMappa()[38][38].setDinosauro(null);
		p.getIsola().getMappa()[38][38].setOccupante(carogna1);
		try {
			t.spostaDinosauro(dG1, 38, 38); // in (38,38) c'e' una carogna (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
		
		//spostamento su carogna con carnivoro con poca energia quindi la mangia (non tutta)
		dG1 = inizializzaDinosauro(p,dG1,100,38,37);
		dG1.setEnergia(50);
		//rimetto nella mappa una carogna sovrascrivendola al dino mosso in precedenza
		Carogna carogna2 = new Carogna();
		p.getIsola().getMappa()[38][38].setDinosauro(null);
		p.getIsola().getMappa()[38][38].setOccupante(carogna2);
		try {
			t.spostaDinosauro(dG1, 38, 38); // in (38,38) c'e' una carogna (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
		
		// creo un altro giocatore g8 con un dinosauro (erbivoro):
		Giocatore g8 = new Giocatore(1,"stego8","e");
		Utente u8 = new Utente("nomeUtente8","pass");
		g8.setUtente(u8);
		g8.aggiungiInPartita(p);
		Dinosauro dG8 = g8.getDinosauri().get(0);
		
		//spostamento su vegetazione con erbivoro con max energia quindi non la mangia
		dG8 = inizializzaDinosauro(p,dG8,2000,1,2); // lo posiziono in (1,2) dove c'e' terra semplice
		//rimetto nella mappa una carogna sovrascrivendola al dino mosso in precedenza
		Vegetale vegetale0 = new Vegetale();
		p.getIsola().getMappa()[1][3].setDinosauro(null);
		p.getIsola().getMappa()[1][3].setOccupante(vegetale0);
		try {
			t.spostaDinosauro(dG8, 1, 3); // in (1,3) c'e' vegetazione (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
		
		//spostamento su vegetazione con erbivoro con poca energia e in condizione di mangiarla tutta
		dG8 = inizializzaDinosauro(p,dG8,1000,1,2);
		dG8.setEnergia(100);
		//rimetto nella mappa una carogna sovrascrivendola al dino mosso in precedenza
		Vegetale vegetale1 = new Vegetale();
		p.getIsola().getMappa()[1][3].setDinosauro(null);
		p.getIsola().getMappa()[1][3].setOccupante(vegetale1);
		try {
			t.spostaDinosauro(dG8, 1, 3); // in (1,3) c'e' vegetazione (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
		
		//spostamento su vegetazione con erbivoro con poca energia quindi la mangia (non tutta)
		dG8 = inizializzaDinosauro(p,dG8,100,1,2);
		dG8.setEnergia(50);
		//rimetto nella mappa una carogna sovrascrivendola al dino mosso in precedenza
		Vegetale vegetale2 = new Vegetale();
		p.getIsola().getMappa()[1][3].setDinosauro(null);
		p.getIsola().getMappa()[1][3].setOccupante(vegetale2);
		try {
			t.spostaDinosauro(dG8, 1, 3); // in (1,3) c'e' vegetazione (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
		
		//spostamento su vegetazione con erbivoro con energia insufficiente quindi muore
		dG8 = inizializzaDinosauro(p,dG8,10,1,2);
		//rimetto nella mappa una carogna sovrascrivendola al dino mosso in precedenza
		Vegetale vegetale3 = new Vegetale();
		p.getIsola().getMappa()[1][3].setDinosauro(null);
		p.getIsola().getMappa()[1][3].setOccupante(vegetale3);
		try {
			t.spostaDinosauro(dG8, 1, 3); // in (1,3) c'e' vegetazione (origine in alto a sx)
			fail("non ha generato eccezione per morte causata da insufficiente energia");
		} catch (MovimentoException e) {
			System.out.println("ok generata eccezione per morte causata da insufficiente energia");
		}
		//rimetto in vita il dino riassegnandolo a g8
		dG8 = inizializzaDinosauro(p,dG8,4000,38,37);
		g8.aggiungiDinosauro(dG8);
		
		//spostamento su carogna con erbivoro quindi non la mangia
		//rimetto nella mappa una carogna sovrascrivendola al dino mosso in precedenza
		Carogna carogna = new Carogna();
		p.getIsola().getMappa()[38][38].setDinosauro(null);
		p.getIsola().getMappa()[38][38].setOccupante(carogna);
		try {
			t.spostaDinosauro(dG8, 38, 38); // in (38,38) c'e' vegetazione (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
		
		//spostamento su carogna con erbivoro con insufficiente energia quindi muore
		dG8 = inizializzaDinosauro(p,dG8,10,38,37);
		//rimetto nella mappa una carogna sovrascrivendola al dino mosso in precedenza
		Carogna carogna3 = new Carogna();
		p.getIsola().getMappa()[38][38].setDinosauro(null);
		p.getIsola().getMappa()[38][38].setOccupante(carogna3);
		try {
			t.spostaDinosauro(dG8, 38, 38); // in (1,3) c'e' carogna (origine in alto a sx)
			fail("non ha generato eccezione per morte causata da insufficiente energia");
		} catch (MovimentoException e) {
			System.out.println("ok generata eccezione per morte causata da insufficiente energia");
		}
		//rimetto in vita il dino riassegnandolo a g8
		dG8 = inizializzaDinosauro(p,dG8,4000,38,37);
		g8.aggiungiDinosauro(dG8);
		
		// creo un altro giocatore g2 con un dinosauro (erbivoro):
		Giocatore g2 = new Giocatore(1,"stego2","e");
		Utente u2 = new Utente("nomeUtente2","pass");
		g2.setUtente(u2);
		g2.aggiungiInPartita(p);
		Dinosauro dG2 = g2.getDinosauri().get(0);
		
		dG1 = inizializzaDinosauro(p,dG1,2000,1,1);
		
		// spostamento di un erbivoro su un carnivoro con energia insufficiente quindi muore
		dG2 = inizializzaDinosauro(p,dG2,10,1,2); // lo posiziono in (1,2) dove c'e' terra semplice
		try {
			t.spostaDinosauro(dG2, 1, 1); //in 1,1 c'e' il carnivoro di g1
			fail("non ha generato eccezione per morte causata da insufficiente energia");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.MORTE) {
				System.out.println("ok generata eccezione per morte causata da insufficiente energia"); //muore dG2
			}
		}
		//rimetto in vita il dino riassegnandolo a g2
		dG2 = inizializzaDinosauro(p,dG2,1000,1,2);
		g2.aggiungiDinosauro(dG2);
		
		// spostamento di un erbivoro su un carnivoro piu' forte di lui presente in (1,1)
		try {
			t.spostaDinosauro(dG2, 1, 1);
			fail("non ha generato eccezione per morte dell'attaccante (erbivoro)");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCANTE) {
				System.out.println("ok generata eccezione per morte dell'attaccante (erbivoro)"); //muore dG2
			}
		}
		
		//simile a prima ma questa volta la forza dell'erbivoro attaccante e' maggiore della forza del carnivoro attaccato
		// creo un altro giocatore g3 con un dinosauro (erbivoro):
		Giocatore g3 = new Giocatore(1,"stego3","e");
		Utente u3 = new Utente("nomeUtente3","pass");
		g3.setUtente(u3);
		g3.aggiungiInPartita(p);
		Dinosauro dG3 = g3.getDinosauri().get(0);
		
		dG3 = inizializzaDinosauro(p,dG3,5000,1,2); // lo posiziono in (1,2) dove c'e' terra semplice
		// spostamento di un erbivoro su un carnivoro piu' debole di lui presente in (1,1)
		try {
			t.spostaDinosauro(dG3, 1, 1);
			fail("non ha generato eccezione per sconfitta attaccato (carnivoro)");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCATO) {
				System.out.println("ok generata eccezione per morte dell'attaccato (carnivoro)"); //muore dG1
			}
		}
		
		// creo un altro giocatore g4 con un dinosauro (carnivoro):
		Giocatore g4 = new Giocatore(1,"trex4","c");
		Utente u4 = new Utente("nomeUtente4","pass");
		g4.setUtente(u4);
		g4.aggiungiInPartita(p);
		Dinosauro dG4 = g4.getDinosauri().get(0);
		
		dG3.setEnergiaMax(200);
		dG3.setEnergia(200);
		
		//combattimenti tra carnivori:
		
		//spostamento di un carnivoro su un carnivoro ma con insufficiente energia quindi muore
		dG4 = inizializzaDinosauro(p,dG4,10,1,2);
		try {
			t.spostaDinosauro(dG4, 1, 1);
			fail("non ha generato eccezione morte per insufficiente energia");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.MORTE) { //muore dG4
				System.out.println("ok generata eccezione morte per insufficiente energia");
			}
		}
		//rimetto in vita il dino riassegnandolo a g4
		dG4 = inizializzaDinosauro(p,dG4,4000,1,2);
		g4.aggiungiDinosauro(dG4);
		dG4.setEnergia(3000);
		
		//spostamento di un carnivoro piu' forte su un carnivoro piu' debole + mangiata della vittima
		try {
			t.spostaDinosauro(dG4, 1, 1);
			fail("non ha generato eccezione per sconfitta attaccato (carnivoro)");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCATO) { //muore dG3
				System.out.println("ok generata eccezione per morte dell'attaccato (carnivoro)");
			}
		}
		
		// creo un altro giocatore g5 con un dinosauro (carnivoro):
		Giocatore g5 = new Giocatore(1,"trex5","c");
		Utente u5 = new Utente("nomeUtente5","pass");
		g5.setUtente(u5);
		g5.aggiungiInPartita(p);
		Dinosauro dG5 = g5.getDinosauri().get(0);
		
		dG5 = inizializzaDinosauro(p,dG5,500,1,2);
		dG4.setEnergiaMax(4000);
		dG4.setEnergia(3000);
		//spostamento di un carnivoro piu' debole su un carnivoro piu' forte + mangiata della vittima
		try {
			t.spostaDinosauro(dG5, 1, 1);
			fail("non ha generato eccezione per sconfitta attaccante (carnivoro)");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCANTE) { //muore dG5
				System.out.println("ok generata eccezione per morte dell'attaccante (carnivoro)");
			}
		}
		
		//rimetto in vita il dino riassegnandolo a g3
		dG3 = inizializzaDinosauro(p,dG3,100,1,2); //erbivoro
		g3.aggiungiDinosauro(dG3);
		//rimetto in vita il dino riassegnandolo a g5
		dG5 = inizializzaDinosauro(p,dG5,2000,3,1); //carnivoro
		g5.aggiungiDinosauro(dG5);
		dG5.setEnergia(1500);
		
		//spostamento di un carnivoro su un erbivoro: il carnivoro vince e mangia la vittima
		try {
			t.spostaDinosauro(dG5, 1, 2);
			fail("non ha generato eccezione per morte dell'attaccato (erbivoro)");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCATO) { //muore dG3
				System.out.println("ok generata eccezione per morte dell'attaccato (erbivoro)");
			}
		}
		
		//rimetto in vita il dino riassegnandolo a g3
		dG3 = inizializzaDinosauro(p,dG3,200,3,1); //erbivoro
		g3.aggiungiDinosauro(dG3);
		dG5.setEnergia(1500);
		//spostamento di un erbivoro su un carnivoro piu' forte: l'erbivoro perde e viene mangiato
		try {
			t.spostaDinosauro(dG3, 1, 2);
			fail("non ha generato eccezione per morte dell'attaccante (erbivoro)");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCANTE) { //muore dG3
				System.out.println("ok generata eccezione per morte dell'attaccante (erbivoro)");
			}
		}
			
		// creo un altro giocatore g6 con un dinosauro (erbivoro):
		Giocatore g6 = new Giocatore(1,"stego6","e");
		Utente u6 = new Utente("nomeUtente6","pass");
		g6.setUtente(u6);
		g6.aggiungiInPartita(p);
		Dinosauro dG6 = g6.getDinosauri().get(0);
		// creo un altro giocatore g7 con un dinosauro (erbivoro):
		Giocatore g7 = new Giocatore(1,"stego7","e");
		Utente u7 = new Utente("nomeUtente7","pass");
		g7.setUtente(u7);
		g7.aggiungiInPartita(p);
		Dinosauro dG7 = g7.getDinosauri().get(0);
		
		dG6 = inizializzaDinosauro(p,dG6,2000,1,2);
		dG7 = inizializzaDinosauro(p,dG7,3000,2,1);
		
		//spostamenti tra erbivori
		//spostamento di un erbivoro su un altro erbivoro
		try {
			t.spostaDinosauro(dG7, 1, 2);
			fail("non ha generato eccezione erbivoro su erbivoro");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.DESTINAZIONEERRATA) {
				System.out.println("ok generata eccezione per destinazione errata (erbivoro su erbivoro)");
			}
		}
		
		
		//faccio generare un figlio per poi far muovere il padre sopra di lui x generare eccezione
		String idDinoNato = new String("");
		try {
			idDinoNato = g7.eseguiDeposizionedeponiUovo(dG7);
		} catch (DeposizioneException e) {
			fail("eccezione");
		}
		p.nascitaDinosauro(1);
		Dinosauro dinoNato = g7.getDinosauri().get(1);
		assertEquals(idDinoNato, dinoNato.getId());
//		System.out.println("coord dG7="+dG7.getRiga()+","+dG7.getColonna());
//		System.out.println("coord dinoNato="+dinoNato.getRiga()+","+dinoNato.getColonna());
		//faccio spostare due dinosauri dello stesso giocatore uno sopra l'altro x fare generare l'eccezione
		try {
			t.spostaDinosauro(dG7, dinoNato.getRiga(), dinoNato.getColonna());
			fail("non ha generato eccezione x movimento sopra un proprio dino");
		} catch (MovimentoException e) {
			System.out.println("ok generata eccezione x movimento sopra un proprio dino");
		}
	}

	/**
	 * Test method for {@link server.logica.Turno#ricreaCarogne(server.logica.Cella[][])}.
	 */
	@Test
	public void testRicreaCarogne() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		Carogna c = new Carogna();
		c.setEnergia(0);
		p.getIsola().getMappa()[2][2].setOccupante(c);
		t.ricreaCarogne(p.getIsola().getMappa());
	}
}