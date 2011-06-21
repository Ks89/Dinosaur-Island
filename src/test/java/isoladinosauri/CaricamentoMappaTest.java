package isoladinosauri;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;

import org.junit.Test;

import server.logica.CaricamentoMappa;
import server.logica.Cella;

public class CaricamentoMappaTest {

	private static final int MAX = 40;
	private String[][] caricaDaFileString(String nomeFile) {
		BufferedReader br = null;
		Reader reader = null;
		String[][] mappa = new String[MAX][MAX];
		try {  	
			//fr = new FileReader("mappaTestAcquaUovo.txt");
			reader = new InputStreamReader(this.getClass().getResourceAsStream("/"+nomeFile));
			br = new BufferedReader(reader);
			String riga = br.readLine();
			StringTokenizer st = null;	
			String cellaLetta;

			for(int i=0;i<MAX;i++) {
				st = new StringTokenizer(riga);
				for(int j=0;j<MAX;j++) {
					cellaLetta = st.nextToken();
					mappa[i][j] = cellaLetta;
				}
				riga = br.readLine();
			}
		}	
		catch(IOException ioException) {
			System.err.println("Errore lettura bufferedReader");
		} finally {
			try {
				if(reader!=null) {
					reader.close();
				}
				if(br!=null) {

					br.close();
				}
			} catch (IOException e) {
				System.err.println("Errore chiusura reader");
				e.printStackTrace();
			}
		}
		return mappa;
	}
	
	/**
	 * Test method for {@link server.logica.CaricamentoMappa#caricaMappa(java.lang.String[][])}.
	 */
	@Test
	public void testCaricaMappa() {
		CaricamentoMappa cm = new CaricamentoMappa();
		
		String[][] mappa = caricaDaFileString("mappaTestAcquaUovo.txt");
		Cella[][] mappaCelle = cm.caricaMappa(mappa);
		assertEquals(40, mappaCelle.length);
		assertEquals(40, mappaCelle[0].length);
	}

}
