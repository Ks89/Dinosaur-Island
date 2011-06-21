package isoladinosauri.modellodati;

import org.junit.Test;

import server.modellodati.Carogna;

public class CarognaTest {

	/**
	 * Test method for {@link isoladinosauri.modellodati.Carogna#consuma()}.
	 */
	@Test
	public void testConsuma() {
		Carogna c = new Carogna();
		c.consuma();
		c.setEnergia(10); //abbasso l'energia per farla arrivare a 0
		c.consuma();
	}
}
