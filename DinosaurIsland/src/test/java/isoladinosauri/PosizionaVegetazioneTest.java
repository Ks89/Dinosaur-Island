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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;
import org.junit.Test;
import server.logica.PosizionaVegetazione;

public class PosizionaVegetazioneTest {
	private static final int MAX = 40;
	
	private String[][] caricaDaFileString(String nomeFile) {
		BufferedReader br = null;
		Reader reader = null;
		String[][] mappa = new String[MAX][MAX];
		try {  	
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
	 * Test method for {@link server.logica.PosizionaVegetazione#posizionaVegetazione(java.lang.String[][])}.
	 */
	@Test
	public void testPosizionaVegetazione() {
		String[][] mappa = this.caricaDaFileString("mappaAcquosa.txt");
		PosizionaVegetazione pv = new PosizionaVegetazione();
		pv.posizionaVegetazione(mappa);
	}

}
