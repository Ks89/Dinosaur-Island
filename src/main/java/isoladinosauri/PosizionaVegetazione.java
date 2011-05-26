package isoladinosauri;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Questa classe viene utilizzata solo per posizionare
 * la vegetazione in modo casuale.
 * E' solo una beta che verra' rimossa prossimamente
 */
public class PosizionaVegetazione {

	//serve solo per posizionare la vegetazione sulla mappa con gia' l'acqua, terra e carogne nel numero giusto e messe in modo corretto	
	public void posizionaVegetazione (String[][] mappa) {

//		//carico la mappa
//		try
//		{  	
//			BufferedReader br = new BufferedReader(new FileReader("mappaAcquosa.txt"));
//			String riga = br.readLine();
//			StringTokenizer st = null;	
//
//			for(int i=0;i<40;i++) {
//				st = new StringTokenizer(riga);
//				for(int j=0;j<40;j++) {
//					mappa[i][j] = st.nextToken();           	
//				}
//				riga = br.readLine();
//			}
//			br.close();
//		}
//		catch(IOException ioException)
//		{
//			System.err.println("Errore lettura file.");
//		}

		//carico la vegetazione nella mappa
		int cont=0;
		Random random = new Random();
		Random r1 = new Random(103);
		Random r2 = new Random(3);
		Random r3 =  new Random(12);
		for(int w=0;w<100;w++){
			for(int i=0; i<40;i++) {
				for(int j=0; j<40 ;j++) {
					random.setSeed(r1.nextInt() * r2.nextInt() + i*2 + w + r3.nextInt());
					if(mappa[i][j].equals("t")) {
						if(random.nextInt(5)==0 && cont<512) {
							mappa[i][j]="v"; 
							cont++;
						}
					}
				}
			}
		}

		//verifico validaita' mappa
		int acqua=0, terra=0, car=0, veg=0;
		for(int i=0;i<40;i++) {
			for(int j=0;j<40;j++) {
				if (mappa[i][j].equals("a")) {
					acqua++;
				}
				if (mappa[i][j].equals("t")) {
					terra++;
				}
				if (mappa[i][j].equals("c")) {
					car++;
				}
				if (mappa[i][j].equals("v")) {
					veg++;
				}
			}
		}

		System.out.println(acqua + " " + terra + " " + veg + " " + car);
	}
}
