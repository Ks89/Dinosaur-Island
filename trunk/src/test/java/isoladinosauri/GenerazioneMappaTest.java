package isoladinosauri;

import org.junit.Test;
import server.logica.GenerazioneMappa;

public class GenerazioneMappaTest {

	/**
	 * Test method for {@link server.logica.GenerazioneMappa#creaMappaCasuale()}.
	 */
	@Test
	public void testCreaMappaCasuale() {
		GenerazioneMappa gm = new GenerazioneMappa();
		String[][] mappaCasuale = gm.creaMappaCasuale();
	}

	/**
	 * Test method for {@link server.logica.GenerazioneMappa#gestisciFossatiGuardia()}.
	 */
	@Test
	public void testGestisciFossatiGuardia() {
		GenerazioneMappa gm = new GenerazioneMappa();
		String[][] mappaCasuale = gm.creaMappaCasuale();
		gm.gestisciFossatiGuardia();
	}

}
