package isoladinosauri;

import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Vegetale;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class CaricamentoMappa {

	private static final int MAX = 40;

	private Cella[][] mappa; 

	public CaricamentoMappa() {
		mappa = new Cella[MAX][MAX];
	}

	
	public Cella[][] caricaDaFile() {
		//esegue il caricamento della Mappa come file di testo (.txt)
		//e riempie l'array Mappa con gli elementi letti dal file
		//richiama anche i metodi predefiniti per inizializzare l'energiaMax al momento
		//della creazione di un Occupante (vegetazione e/o carogna)
		BufferedReader br;
		try
		{  	
			FileReader fileReader = new FileReader("mappaTestAcquaUovo.txt");
			 br = new BufferedReader(fileReader);
			String riga = br.readLine();
			StringTokenizer st = null;	
			String cellaLetta;

			for(int i=0;i<MAX;i++) {
				st = new StringTokenizer(riga);
				for(int j=0;j<MAX;j++) {
					cellaLetta = st.nextToken();
					this.assegnaCelle(cellaLetta, i, j);
				}
				riga = br.readLine();
			}
			br.close();
		}
		catch(IOException ioException)
		{
			System.err.println("Errore lettura file.");
		}
		return mappa;
	}

	
	public Cella[][] caricaMappa(String[][] input) {
		//esegue il caricamento della Mappa da una mappa di Stringhe a una di celle
		//richiama anche i metodi predefiniti per inizializzare l'energiaMax al momento
		//della creazione di un Occupante (vegetazione e/o carogna)
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				this.assegnaCelle(input[i][j], i, j);
			}
		}
		return mappa;
	}
	
	
	private void assegnaCelle(String elemento, int riga, int colonna) {
		if(elemento.equals("a"))	{
			//se acqua mette null senza fare la cella
			this.mappa[riga][colonna]=null;
		}
		if(elemento.equals("t")) {
			//se terra crea cella ma vuota
			Cella cella=new Cella();
			this.mappa[riga][colonna]=cella;
		}
		if(elemento.equals("v")) {
			//se vegetale crea cella e mette come occupante il vegetale
			Cella cella=new Cella();
			//chiamo costruttore predef che inzializza energiaMax random
			cella.setOccupante(new Vegetale()); 
			this.mappa[riga][colonna]=cella;
		}
		if(elemento.equals("c")) {
			//se carogna crea cella e mette come occupante la carogna
			Cella cella=new Cella();
			//chiamo costruttore predef che inzializza energiaMax random
			cella.setOccupante(new Carogna());
			this.mappa[riga][colonna]=cella;
		}
	}
}
