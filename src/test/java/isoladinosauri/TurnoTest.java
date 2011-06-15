///**
// * 
// */
//package isoladinosauri;
//
//import static org.junit.Assert.*;
//import isoladinosauri.modellodati.Carnivoro;
//import isoladinosauri.modellodati.Dinosauro;
//
//import org.junit.Test;
//
//public class TurnoTest {
//
//	/**
//	 * Test method for {@link isoladinosauri.Turno#illuminaMappa(isoladinosauri.Giocatore, int, int, int)}.
//	 */
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
//	@Test
//	public void testIlluminaMappa() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Turno#ottieniVisuale(int, int, int)}.
//	 */
//	@Test
//	public void testOttieniVisuale() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Turno#ottieniOrigineVisuale(int, int, int)}.
//	 */
//	@Test
//	public void testOttieniOrigineVisuale() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Turno#ottieniEstremoVisuale(int, int, int)}.
//	 */
//	@Test
//	public void testOttieniEstremoVisuale() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Turno#ottieniRaggiungibilita(int, int)}.
//	 */
//	@Test //FIXME: forse non va fatto
//	public void testOttieniRaggiungibilita() {
//		Partita p = inizializzaPartita();
//		Turno t = new Turno(p);
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Turno#ottieniStradaPercorsa(int, int, int, int)}.
//	 */
//	@Test //FIXME: assertNull dovrebbe verificare ke restituisca null ma restituisce sempre null anche quando non dovrebbe
//	public void testOttieniStradaPercorsa() {
//		Partita p = inizializzaPartita();
//		Turno t = new Turno(p);
//		assertNull(t.ottieniStradaPercorsa(1, 1, 2, 2));
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Turno#spostaDinosauro(isoladinosauri.modellodati.Dinosauro, int, int)}.
//	 */
//	@Test
//	public void testSpostaDinosauro() {
//		Partita p = inizializzaPartita();
//		Turno t = new Turno(p);
//		Dinosauro d1 = new Carnivoro("11",1,1,1);
//		assertEquals(-1, t.spostaDinosauro(d1, 0, 0));
//		Dinosauro d2 = new Carnivoro("12",1,1,1);
//		assertEquals(1, t.spostaDinosauro(d2, 2, 2));
//	}
//
//	/**
//	 * Test method for {@link isoladinosauri.Turno#ricreaCarogne(isoladinosauri.Cella[][])}.
//	 */
//	@Test
//	public void testRicreaCarogne() {
//		fail("Not yet implemented");
//	}
//
//}
