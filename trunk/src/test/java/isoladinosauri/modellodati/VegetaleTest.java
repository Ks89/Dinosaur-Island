package isoladinosauri.modellodati;

import org.junit.Test;
import server.modellodati.Vegetale;

public class VegetaleTest {

	/**
	 * Test method for {@link isoladinosauri.modellodati.Vegetale#cresci()}.
	 */
	@Test
	public void testCresci() {
		Vegetale v = new Vegetale();
		v.cresci();
		v.setEnergia(10); //abbasso l'energia per poi farla ricrescere
		v.cresci();
	}
}
