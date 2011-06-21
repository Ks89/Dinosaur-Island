package isoladinosauri;

import org.junit.Test;

import server.logica.CaricamentoMappa;
import server.logica.Cella;
import server.logica.Isola;

public class IsolaTest {

	/**
	 * Test method for {@link server.logica.Isola#cresciEConsuma()}.
	 */
	@Test
	public void testCresciEConsuma() {
		CaricamentoMappa cm = new CaricamentoMappa();
		Cella[][] mappaCelle;
		mappaCelle = cm.caricaDaFile("mappaOK.txt");
		Isola i = new Isola(mappaCelle);
		i.cresciEConsuma();
	}

}
