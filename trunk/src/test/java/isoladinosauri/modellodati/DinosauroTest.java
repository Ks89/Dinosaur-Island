//package isoladinosauri.modellodati;
//
//import static org.junit.Assert.*;
//import isoladinosauri.CaricamentoMappa;
//import isoladinosauri.Cella;
//import isoladinosauri.Giocatore;
//import isoladinosauri.Isola;
//import isoladinosauri.Partita;
//
//import org.junit.Test;
//
//import Eccezioni.CrescitaException;
//
//public class DinosauroTest {
//	
//	public Partita inizializzaPartita() {
//		CaricamentoMappa cm = new CaricamentoMappa();
//		Cella[][] mappaCelle;
//		mappaCelle = cm.caricaDaFile();
//		Isola i = new Isola(mappaCelle);
//		Partita p = new Partita(i);
//		return p;
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#calcolaRaggioVisibilita()}.
//	 */
//	@Test
//	public void testCalcolaRaggioVisibilita() {
//		Partita p = inizializzaPartita();
//		Giocatore g = new Giocatore(1,"stego","c");
//		Dinosauro d = new Carnivoro("11",1,1,1);
//		p.aggiungiGiocatore(g);
//		g.aggiungiDinosauro(d);
//		assertEquals(2, d.calcolaRaggioVisibilita());
//		try {
//			d.aumentaDimensione();
//		} catch (CrescitaException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(3, d.calcolaRaggioVisibilita());
//		try {
//			d.aumentaDimensione();
//		} catch (CrescitaException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(3, d.calcolaRaggioVisibilita());
//	}
//
////	/**
////	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#aumentaDimensione()}.
////	 */
////	@Test
////	public void testAumentaDimensione() {
////		Partita p = inizializzaPartita();
////		Giocatore g = new Giocatore(1,"pippo","carnivoro");
////		Dinosauro d = new Carnivoro("11",1,1,1);
////		p.aggiungiGiocatore(g);
////		g.aggiungiDinosauro(d);
////		assertEquals(1, d.aumentaDimensione());
////		d.setEnergia(d.getEnergiaMax());
////		assertEquals(1, d.aumentaDimensione());
////		d.setEnergia(d.getEnergiaMax());
////		assertEquals(1, d.aumentaDimensione());
////		assertEquals(-1, d.aumentaDimensione());
////		d.setEnergia(d.getEnergiaMax());
////		assertEquals(1, d.aumentaDimensione());
////		d.setEnergia(d.getEnergiaMax());
////		assertEquals(0, d.aumentaDimensione());
////	}
//
//	/**
//	 * Test method for {@link isoladinosauri.modellodati.Dinosauro#aggCordinate(int, int)}.
//	 */
//	@Test
//	public void testAggCordinate() {
//		Partita p = inizializzaPartita();
//		Giocatore g = new Giocatore(1,"pippo","carnivoro");
//		Dinosauro d = new Carnivoro("11",1,1,1);
//		p.aggiungiGiocatore(g);
//		g.aggiungiDinosauro(d);
////FIXME:assertFalse(d.aggCordinate(0, 0));
//	}
//
//	
//}
