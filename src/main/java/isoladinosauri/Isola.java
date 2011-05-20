package isoladinosauri;

import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Vegetale;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Isola {

	private Cella[][] mappa;

	public Cella[][] getMappa() {
		return mappa;        
	}

	public void caricaMappa() {
		//esegue il caricamento della Mappa come file di testo (.txt)
		//e riempie l'array Mappa con gli elementi letti dal file
		//richiama anche i metodi predefiniti per inizializzare l'energiaMax al momento
		//della creazione di un Occupante (vegetazione e/o carogna)
		try
		{  	
			BufferedReader br = new BufferedReader(new FileReader("mappaTestAcquaUovo.txt"));
			String riga = br.readLine();
			StringTokenizer st = null;	
			String cellaLetta;

			mappa = new Cella[40][40];

			for(int i=0;i<40;i++) {
				st = new StringTokenizer(riga);

				for(int j=0;j<40;j++) {
					cellaLetta = st.nextToken();

					if(cellaLetta.equals("a"))	{
						//se acqua mette null senza fare la cella
						mappa[i][j]=null;
					}
					if(cellaLetta.equals("t")) {
						//se terra crea cella ma vuota
						Cella cella=new Cella();
						mappa[i][j]=cella;
					}
					if(cellaLetta.equals("v")) {
						//se vegetale crea cella e mette come occupante il vegetale
						Cella cella=new Cella();
						//chiamo costruttore predef che inzializza energiaMax random
						cella.setOccupante(new Vegetale()); 
						mappa[i][j]=cella;
					}
					if(cellaLetta.equals("c")) {
						//se carogna crea cella e mette come occupante la carogna
						Cella cella=new Cella();
						//chiamo costruttore predef che inzializza energiaMax random
						cella.setOccupante(new Carogna());
						mappa[i][j]=cella;
					}
				}
				riga = br.readLine();
				//System.out.println();
			}
			br.close();
		}
		catch(IOException ioException)
		{
			System.err.println("Errore lettura file.");
		}
	}


	public void stampaMappa() {
		//metodo che esiste solo per testare il caricamento
		//presto sara' rimosso e trasformato in test junit
		for(int i=0;i<40;i++) {
			for(int j=0;j<40;j++) {
				if (mappa[i][j] == null) { //e' acqua
					System.out.print("a:    ");
				}
				else { //se e' terra puo' essere carogna o vegetale
					if (mappa[i][j] instanceof Cella) {
						Cella cella = mappa[i][j];
						if((cella.getDinosauro()!=null) && cella.getDinosauro() instanceof Dinosauro) {
							System.out.print("d:" + + cella.getDinosauro().getEnergiaMax() + " ");
						} else {
							if(cella.getOccupante() instanceof Carogna)
							{
								Carogna carogna = (Carogna)cella.getOccupante();
								System.out.print("c:" + carogna.getEnergiaMax() + " ");
							}
							if(cella.getOccupante() instanceof Vegetale)
							{
								Vegetale vegetale = (Vegetale)cella.getOccupante();
								System.out.print("v:" + vegetale.getEnergiaMax() + " ");
							}
							if(!(cella.getOccupante() instanceof Vegetale) &&
									!(cella.getOccupante() instanceof Carogna)) {
								System.out.print("t:    ");
							}
						}
					}
				}
			}
			System.out.println();
		}
	}	


	public void stampaMappaRidotta() {
		//metodo che esiste solo per testare il caricamento
		//presto sara' rimosso e trasformato in test junit
		System.out.print("   ");
		for(int j=0;j<40;j++) {
			if(j<10) System.out.print("0" + j + " ");
			else System.out.print(j + " ");
		}
		System.out.println();
		for(int i=0;i<40;i++) {
			if(i<10) System.out.print("0" + i + " ");
			else System.out.print(i + " ");
			for(int j=0;j<40;j++) {
				if (mappa[i][j] == null) { //e' acqua
					System.out.print("   ");
				}
				else { //se e' terra puo' essere carogna o vegetale
					if (mappa[i][j] instanceof Cella) {
						Cella cella = mappa[i][j];
						if((cella.getDinosauro()!=null) && cella.getDinosauro() instanceof Dinosauro) {
							System.out.print(cella.getDinosauro().getId() + " ");
						} else {
							if(cella.getOccupante() instanceof Carogna)
							{
								System.out.print(" c ");
							}
							if(cella.getOccupante() instanceof Vegetale)
							{
								System.out.print(" v ");
							}
							if(!(cella.getOccupante() instanceof Vegetale) &&
									!(cella.getOccupante() instanceof Carogna)) {
								System.out.print(" . ");
							}
						}
					}
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}	

	
	public void stampaMappaRidottaVisibilita(Giocatore giocatore) {
		//metodo che esiste solo per testare il caricamento
		//presto sara' rimosso e trasformato in test junit
		System.out.print("   ");
		for(int j=0;j<40;j++) {
			if(j<10) System.out.print("0" + j + " ");
			else System.out.print(j + " ");
		}
		System.out.println();
		for(int i=0;i<40;i++) {
			if(i<10) System.out.print("0" + i + " ");
			else System.out.print(i + " ");
			for(int j=0;j<40;j++) {
				if(giocatore.getMappaVisibile()[i][j]==true) {
					if (mappa[i][j] == null) { //e' acqua
						System.out.print("   ");
					}
					else { //se e' terra puo' essere carogna o vegetale
						if (mappa[i][j] instanceof Cella) {
							Cella cella = mappa[i][j];
							if((cella.getDinosauro()!=null) && cella.getDinosauro() instanceof Dinosauro) {
								System.out.print(cella.getDinosauro().getId() + " ");
							} else {
								if(cella.getOccupante() instanceof Carogna)
								{
									System.out.print(" c ");
								}
								if(cella.getOccupante() instanceof Vegetale)
								{
									System.out.print(" v ");
								}
								if(!(cella.getOccupante() instanceof Vegetale) &&
										!(cella.getOccupante() instanceof Carogna)) {
									System.out.print(" . ");
								}
							}
						}
					}
				} else System.out.print("   ");
				
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}	

	public void stampaMappaRaggiungibilita(int inizioRiga, int inizioColonna, int fineRiga, int fineColonna, int[][]raggiungibilita) {
		int cont=0;
		System.out.print("   ");
		for(int j=0;j<40;j++) {
			if(j<10) System.out.print("0" + j + " ");
			else System.out.print(j + " ");
		}
		System.out.println();
		for(int i=0;i<40;i++) {
			if(i<10) System.out.print("0" + i + " ");
			else System.out.print(i + " ");
			for(int j=0;j<40;j++) {
				if (mappa[i][j] == null) { //e' acqua
					System.out.print("   ");
				}
				else { //se e' terra puo' essere carogna o vegetale
					if (mappa[i][j] instanceof Cella) {
						Cella cella = mappa[i][j];
						if((i>=inizioRiga && i<=fineRiga) && (j>=inizioColonna && j<=fineColonna))  {
							cont++;
							if((raggiungibilita[i - inizioRiga][j - inizioColonna]!=9) && (raggiungibilita[i - inizioRiga][j - inizioColonna]!=8))
								System.out.print(" " + raggiungibilita[i - inizioRiga][j - inizioColonna] + " ");
							else if(raggiungibilita[i - inizioRiga][j - inizioColonna]==9 || raggiungibilita[i - inizioRiga][j - inizioColonna]==8)
								System.out.print(" " + raggiungibilita[i - inizioRiga][j - inizioColonna] + " ");
						} else {
							if((cella.getDinosauro()!=null) && cella.getDinosauro() instanceof Dinosauro) {
								System.out.print(cella.getDinosauro().getId() + " ");
							} else {
								if(cella.getOccupante() instanceof Carogna)
								{
									System.out.print(" c ");
								}
								if(cella.getOccupante() instanceof Vegetale)
								{
									System.out.print(" v ");
								}
								if(!(cella.getOccupante() instanceof Vegetale) &&
										!(cella.getOccupante() instanceof Carogna)) {
									System.out.print(" . ");
								}
							}
						}
					}
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println(cont);
	}
	
	public void stampaMappaStradaPercorsa(int inizioRiga, int inizioColonna, int fineRiga, int fineColonna, int[][]stradaPercorsa) {
		int cont=0;
		System.out.print("   ");
		for(int j=0;j<40;j++) {
			if(j<10) System.out.print("0" + j + " ");
			else System.out.print(j + " ");
		}
		System.out.println();
		for(int i=0;i<40;i++) {
			if(i<10) System.out.print("0" + i + " ");
			else System.out.print(i + " ");
			for(int j=0;j<40;j++) {
				if (mappa[i][j] == null) { //e' acqua
					System.out.print("   ");
				}
				else { //se e' terra puo' essere carogna o vegetale
					if (mappa[i][j] instanceof Cella) {
						Cella cella = mappa[i][j];
						if((i>=inizioRiga && i<=fineRiga) && (j>=inizioColonna && j<=fineColonna))  {
							cont++;
							if((stradaPercorsa[i - inizioRiga][j - inizioColonna]!=9) && (stradaPercorsa[i - inizioRiga][j - inizioColonna]!=8))
								System.out.print(" " + stradaPercorsa[i - inizioRiga][j - inizioColonna] + " ");
							else if(stradaPercorsa[i - inizioRiga][j - inizioColonna]==9 || stradaPercorsa[i - inizioRiga][j - inizioColonna]==8)
								System.out.print(" " + stradaPercorsa[i - inizioRiga][j - inizioColonna] + " ");
						} else {
							if((cella.getDinosauro()!=null) && cella.getDinosauro() instanceof Dinosauro) {
								System.out.print(cella.getDinosauro().getId() + " ");
							} else {
								if(cella.getOccupante() instanceof Carogna)
								{
									System.out.print(" c ");
								}
								if(cella.getOccupante() instanceof Vegetale)
								{
									System.out.print(" v ");
								}
								if(!(cella.getOccupante() instanceof Vegetale) &&
										!(cella.getOccupante() instanceof Carogna)) {
									System.out.print(" . ");
								}
							}
						}
					}
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println(cont);
	}
	
}
