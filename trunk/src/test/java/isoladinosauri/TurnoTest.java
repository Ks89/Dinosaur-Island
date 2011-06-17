package isoladinosauri;

import static org.junit.Assert.*;
import isoladinosauri.modellodati.Dinosauro;

import org.junit.Test;

import Eccezioni.MovimentoException;

public class TurnoTest {

	private Partita inizializzaPartita() {
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle;
		mappaCelle = cm.caricaDaFile();
		Isola i = new Isola(mappaCelle);
		Partita p = new Partita(i);
		return p;
	}
	
	private Dinosauro inizializzaDinosauro(Dinosauro d, int energia, int r, int c) {
		d.setEnergiaMax(energia);
		d.setEnergia(energia);
		d.setRiga(r);
		d.setColonna(c);
		return d;
	}
	
//	/** TODO:
//	 * Test method for {@link isoladinosauri.Turno#illuminaMappa(isoladinosauri.Giocatore, int, int, int)}.
//	 */
//	@Test
//	public void testIlluminaMappa() {
//		fail("Not yet implemented");
//	}

//	/** TODO:
//	 * Test method for {@link isoladinosauri.Turno#ottieniVisuale(int, int, int)}.
//	 */
//	@Test
//	public void testOttieniVisuale() {
//		fail("Not yet implemented");
//	}

//	/** TODO:
//	 * Test method for {@link isoladinosauri.Turno#ottieniOrigineVisuale(int, int, int)}.
//	 */
//	@Test
//	public void testOttieniOrigineVisuale() {
//		fail("Not yet implemented");
//	}


//	/** TODO:
//	 * Test method for {@link isoladinosauri.Turno#ottieniEstremoVisuale(int, int, int)}.
//	 */
//	@Test
//	public void testOttieniEstremoVisuale() {
//		fail("Not yet implemented");
//	}

//	/**
//	 * Test method for {@link isoladinosauri.Turno#ottieniRaggiungibilita(int, int)}.
//	 */
//	@Test //FIXME: forse non va fatto
//	public void testOttieniRaggiungibilita() {
//		Partita p = inizializzaPartita();
//		Turno t = new Turno(p);
//	}

	/**
	 * Test method for {@link isoladinosauri.Turno#ottieniStradaPercorsa(int, int, int, int)}.
	 */
	@Test //FIXME: assertNull dovrebbe verificare ke restituisca null ma restituisce sempre null anche quando non dovrebbe
	public void testOttieniStradaPercorsa() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		assertNull(t.ottieniStradaPercorsa(1, 1, 2, 2));
	}

	/**
	 * Test method for {@link isoladinosauri.Turno#spostaDinosauro(isoladinosauri.modellodati.Dinosauro, int, int)}.
	 */
	@Test //TODO: da finire!!
	public void testSpostaDinosauro() {
		Partita p = inizializzaPartita();
		Turno t = new Turno(p);
		p.setTurnoCorrente(t);
		//creo un nuovo giocatore con un dinosauro (carnivoro)
		Giocatore g1 = new Giocatore(1,"pippo","c");
		g1.aggiungiInPartita(p);
		Dinosauro dG1 = g1.getDinosauri().get(0);
		
		// genera eccezione per spostamento in acqua (0,0)
		dG1 = inizializzaDinosauro(dG1,4000,1,1);
		try {
			t.spostaDinosauro(dG1, 1, 4); // in (1,4) c'e' acqua (origine in alto a sx)
			fail("non ha generato eccezione spostamento su acqua");
		} catch (MovimentoException e) {
			System.out.println("ok generata eccezione spostamento su acqua");
		}
		
		//spostamento su terra semplice
		dG1 = inizializzaDinosauro(dG1,4000,1,1);
		try {
			t.spostaDinosauro(dG1, 2, 2);
		} catch (MovimentoException e) {
			fail("eccezione movimento");

		}
		
		//spostamento su vegetazione
		dG1 = inizializzaDinosauro(dG1,4000,1,1);
		try {
			t.spostaDinosauro(dG1, 1, 3); // in (1,3) c'e' vegetazione (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
		
		
		//spostamento su carogna
		dG1 = inizializzaDinosauro(dG1,4000,38,37);
		try {
			t.spostaDinosauro(dG1, 38, 38); // in (38,38) c'e' una carogna (origine in alto a sx)
		} catch (MovimentoException e) {
			fail("eccezione movimento");
		}
		
		// creo un altro giocatore g2 con un dinosauro (erbivoro):
		Giocatore g2 = new Giocatore(1,"pluto","e");
		g2.aggiungiInPartita(p);
		Dinosauro dG2 = g2.getDinosauri().get(0);
		dG2 = inizializzaDinosauro(dG2,2000,1,2); // lo posiziono in (1,2) dove c'e' terra semplice
		dG1 = inizializzaDinosauro(dG1,4000,2,1); // posiziono il dinosauro carnivoro del giocatore pippo in (2,1)
		//sposto il carnivoro in (1,1) e gli assegno 4000 di energia
		try {
			t.spostaDinosauro(dG1, 1, 1);
			dG1.setEnergiaMax(4000);
			dG1.setEnergia(4000);
		} catch (MovimentoException e1) {
			fail("eccezione movimento");
		}
	
		
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
		Giocatore g3 = new Giocatore(1,"paperino","e");
		g3.aggiungiInPartita(p);
		Dinosauro dG3 = g3.getDinosauri().get(0);
		
		dG3 = inizializzaDinosauro(dG3,5000,1,2); // lo posiziono in (1,2) dove c'e' terra semplice
		dG1 = inizializzaDinosauro(dG1,2000,2,1); // posiziono il dinosauro carnivoro attaccato del giocatore pippo in (2,1)

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
		Giocatore g4 = new Giocatore(1,"topolino","c");
		g4.aggiungiInPartita(p);
		Dinosauro dG4 = g4.getDinosauri().get(0);
		
		dG4 = inizializzaDinosauro(dG4,4000,1,2);
		dG3.setEnergiaMax(2000);
		dG3.setEnergia(2000);
		
		//combattimenti tra carnivori:
		//spostamento di un carnivoro piu' forte su un carnivoro piu' debole
		try {
			t.spostaDinosauro(dG4, 1, 1);
			fail("non ha generato eccezione per sconfitta attaccato (carnivoro)");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCATO) { //muore dG3
				System.out.println("ok generata eccezione per morte dell'attaccato (carnivoro)");
			}
		}
		
		// creo un altro giocatore g5 con un dinosauro (carnivoro):
		Giocatore g5 = new Giocatore(1,"paperone","c");
		g5.aggiungiInPartita(p);
		Dinosauro dG5 = g5.getDinosauri().get(0);
		
		dG5 = inizializzaDinosauro(dG5,2000,1,2);
		dG4.setEnergiaMax(4000);
		dG4.setEnergia(4000);
		//spostamento di un carnivoro piu' debole su un carnivoro piu' forte
		try {
			t.spostaDinosauro(dG5, 1, 1);
			fail("non ha generato eccezione per sconfitta attaccante (carnivoro)");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.SCONFITTAATTACCANTE) { //muore dG5
				System.out.println("ok generata eccezione per morte dell'attaccante (carnivoro)");
			}
		}
		
		// creo un altro giocatore g6 con un dinosauro (erbivoro):
		Giocatore g6 = new Giocatore(1,"giocatore6","e");
		g6.aggiungiInPartita(p);
		Dinosauro dG6 = g6.getDinosauri().get(0);
		// creo un altro giocatore g7 con un dinosauro (erbivoro):
		Giocatore g7 = new Giocatore(1,"giocatore7","e");
		g7.aggiungiInPartita(p);
		Dinosauro dG7 = g7.getDinosauri().get(0);
		
		dG6 = inizializzaDinosauro(dG6,2000,1,2);
		try {
			t.spostaDinosauro(dG6, 1, 2); // lo sposto fisicamente sulla mappa in (1,2)
			dG6.setEnergiaMax(2000);
			dG6.setEnergia(2000);
		} catch (MovimentoException e1) {
			fail("eccezione movimento");
		}
		dG7 = inizializzaDinosauro(dG7,3000,2,1);
		try {
			t.spostaDinosauro(dG7, 2, 1); // lo sposto fisicamente sulla mappa in (2,1)
			dG7.setEnergiaMax(3000);
			dG7.setEnergia(3000);
		} catch (MovimentoException e1) {
			fail("eccezione movimento");
		}
		//spostamenti tra erbivori
		//spostamento di un erbivoro su un altro erbivoro
		try {
			t.spostaDinosauro(dG7, 1, 2);
			fail("non ha generato eccezione erbivoro su erbivoro");
		} catch (MovimentoException e) {
			if(e.getCausa()==MovimentoException.Causa.DESTINAZIONEERRATA) {
				System.out.println("ok generata eccezione per destinazione errata");
			}
		}
	}

//	/** TODO:
//	 * Test method for {@link isoladinosauri.Turno#ricreaCarogne(isoladinosauri.Cella[][])}.
//	 */
//	@Test
//	public void testRicreaCarogne() {
//		fail("Not yet implemented");
//	}

}
