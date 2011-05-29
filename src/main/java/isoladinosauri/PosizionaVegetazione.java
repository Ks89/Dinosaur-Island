package isoladinosauri;

import java.util.Random;

/**
 * Questa classe viene utilizzata solo per posizionare
 * la vegetazione in modo casuale.
 * E' solo una beta che verra' rimossa prossimamente
 */
public class PosizionaVegetazione {

	private final int MAX = 40;
	private final int VEGETAZIONE = 512;
	
	/** 
	 * Metodo per posizionare in modo casuale la Vegetazione su una mappa 40x40 con gia' terra (t), acqua (a) e carogne (c).
	 * @param mappa - Array 2d di String che costituisce la mappa 40x40 di "t", "a" e "c".
	 */
	public void posizionaVegetazione (String[][] mappa) {
		//carico la vegetazione nella mappa
		int cont=0; //conta il numero di celle vegetazione inserite nella mappa
		Random random = new Random();
		Random r1 = new Random(12);
		Random r2 = new Random(3);
		
		//scansiono 50 volte la mappa ed inserisco la vegetazione nei posti liberi
		//FIXME usare un while che si basa su cont
		for(int w=0;w<100;w++){
			for(int i=0; i<MAX;i++) {
				for(int j=0; j<MAX ;j++) {
					random.setSeed(r1.nextInt() * r2.nextInt());
					if(mappa[i][j].equals("t")) {
						if(random.nextInt(5)==0 && cont<VEGETAZIONE) {
							mappa[i][j]="v"; 
							cont++;
						}
					}
				}
			}
		}

		//verifico validaita' della mappa
		int[] verificata = this.verificaValidita(mappa);

		System.out.println("Validita' mappa :" + verificata[0] + " " + verificata[1] + " " + verificata[2] + " " + verificata[3]);
	}
	
	/**
	 * Metodo per verificare la validita' della mappa, cioe' conta il numero di celle per ogni tipo.
	 * @param mappa - Array 2d di String che costituisce la mappa 40x40 di "t", "a" e "c".
	 * @return Un array di 4 elementi con i risultati della verifica, composto cosi': 
	 * 		[0] - acqua, 
	 * 		[1] - terra, 
	 * 		[2] - carogna, 
	 * 		[3] - vegetazione.
	 */
	private int[] verificaValidita(String[][] mappa) {
		//verifico validaita' della mappa
		int[] verifica = new int[4];
		int acqua=0, terra=0, carogna=0, vegetazione=0;
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				if (mappa[i][j].equals("a")) {
					acqua++;
				}
				if (mappa[i][j].equals("t")) {
					terra++;
				}
				if (mappa[i][j].equals("c")) {
					carogna++;
				}
				if (mappa[i][j].equals("v")) {
					vegetazione++;
				}
			}
		}
		verifica[0] = acqua;
		verifica[1] = terra;
		verifica[2] = carogna;
 		verifica[3] = vegetazione;
 		return verifica;

	}
}