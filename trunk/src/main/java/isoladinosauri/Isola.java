package isoladinosauri;

//import isoladinosauri.modellodati.Carogna;
//import isoladinosauri.modellodati.Vegetale;


/**
 * Classe Isola costituita dal solo attributo mappa.
 * Essa contiene tutti i metodi per generare la mappa di gioco e/o
 * caricarla da file esterno.
 */ 
public class Isola {

//	private static final int MAX = 40;
//	private static final int DIECI = 10;
//	private static final int NONRAGG = 9;
//	private static final int CELLACQUA = 8;
//	private static final String TERRA = " . ";
//	private static final String ACQUA = "   ";
//	private static final String VEGETAZIONE = " v ";
//	private static final String CAROGNA = " c ";
	
	private Cella[][] mappa; 
	

	/**
	 * Costruttore per inizializzare la mappa.
	 * @param mappa Array bidimensionali di Celle per impostare la mappa.
	 */
	public Isola(Cella[][] mappa) {
		this.mappa = mappa.clone();
	}
	
	/**
	 * @return Un array bidimensionale di Celle che rappresenta la mappa di gioco.
	 */
	public Cella[][] getMappa() {
		return mappa.clone();        
	}

//	/**
//	 * Metodo per stampare la mappa di Gioco con l'energia di ogni elemento.
//	 */
//	public void stampaMappa() {
//		for(int i=0;i<MAX;i++) {
//			for(int j=0;j<MAX;j++) {
//				if (mappa[i][j] == null) { //e' acqua
//					System.out.print("a:    ");
//				}
//				else { //se e' terra puo' essere carogna o vegetale
//					Cella cella = mappa[i][j];
//					if(cella.getDinosauro()!=null) {
//						System.out.print("d:" + + cella.getDinosauro().getEnergiaMax() + " ");
//					} else {
//						if(cella.getOccupante() instanceof Carogna)
//						{
//							Carogna carogna = (Carogna)cella.getOccupante();
//							System.out.print("c:" + carogna.getEnergia() + " ");
//						}
//						if(cella.getOccupante() instanceof Vegetale)
//						{
//							Vegetale vegetale = (Vegetale)cella.getOccupante();
//							System.out.print("v:" + vegetale.getEnergia() + " ");
//						}
//						if(!(cella.getOccupante() instanceof Vegetale) &&
//								!(cella.getOccupante() instanceof Carogna)) {
//							System.out.print("t:    ");
//						}
//
//					}
//				}
//			}
//			System.out.println();
//		}
//	}	
//
//
//	/**
//	 * Metodo per stampare la mappa di Gioco senza l'energia degli elementi.
//	 */
//	public void stampaMappaRidotta() {
//		System.out.print(ACQUA);
//		for(int j=0;j<MAX;j++) {
//			if(j<DIECI) {
//				System.out.print("0" + j + " ");
//			} else {
//				System.out.print(j + " ");
//			}
//		}
//		System.out.println();
//		for(int i=0;i<MAX;i++) {
//			if(i<DIECI) {
//				System.out.print("0" + i + " ");
//			} else {
//				System.out.print(i + " ");
//			}
//			for(int j=0;j<MAX;j++) {
//				if (mappa[i][j] == null) { //e' acqua
//					System.out.print(ACQUA);
//				} else { //se e' terra puo' essere carogna o vegetale
//					Cella cella = mappa[i][j];
//					if(cella.getDinosauro()!=null) {
//						System.out.print(cella.getDinosauro().getId() + " ");
//					} else {
//						if(cella.getOccupante() instanceof Carogna) {
//							System.out.print(CAROGNA);
//						}
//						if(cella.getOccupante() instanceof Vegetale) {
//							System.out.print(VEGETAZIONE);
//						}
//						if(!(cella.getOccupante() instanceof Vegetale) &&
//								!(cella.getOccupante() instanceof Carogna)) {
//							System.out.print(TERRA);
//						}
//					}
//
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
//		System.out.println();
//	}	
//
//
//	/**
//	 * Metodo per stampare la mappa di Gioco con applicata la visibilita' del Giocatore.
//	 * @param giocatore riferimento al Giocatore per cui bisogna stampare la mappa della visibilita'.
//	 */
//	public void stampaMappaRidottaVisibilita(Giocatore giocatore) {
//		//metodo che esiste solo per testare il caricamento
//		//presto sara' rimosso e trasformato in test junit
//		System.out.print(ACQUA);
//		for(int j=0;j<MAX;j++) {
//			if(j<DIECI) {
//				System.out.print("0" + j + " ");
//			} else {
//				System.out.print(j + " ");
//			}
//		}
//		System.out.println();
//		for(int i=0;i<MAX;i++) {
//			if(i<DIECI) {
//				System.out.print("0" + i + " ");
//			} else {
//				System.out.print(i + " ");
//			}
//			for(int j=0;j<MAX;j++) {
//				if(giocatore.getMappaVisibile()[i][j]) {
//					if (mappa[i][j] == null) { //e' acqua
//						System.out.print(ACQUA);
//					} else { //se e' terra puo' essere carogna o vegetale
//						Cella cella = mappa[i][j];
//						if(cella.getDinosauro()!=null) {
//							System.out.print(cella.getDinosauro().getId() + " ");
//						} else {
//							if(cella.getOccupante() instanceof Carogna) {
//								System.out.print(CAROGNA);
//							}
//							if(cella.getOccupante() instanceof Vegetale) {
//								System.out.print(VEGETAZIONE);
//							}
//							if(!(cella.getOccupante() instanceof Vegetale) &&
//									!(cella.getOccupante() instanceof Carogna)) {
//								System.out.print(TERRA);
//							}
//						}
//
//					}
//				} else {
//					System.out.print(ACQUA);
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
//		System.out.println();
//	}	
//
//	/**
//	 * Stampa la mappa della raggiungibilita'.
//	 * @param inizioRiga int che rappresenta la riga di inzio dell'area raggiungibile.
//	 * @param inizioColonna int che rappresenta la colonna di inzio dell'area raggiungibile.
//	 * @param fineRiga int che rappresenta la riga di fine dell'area raggiungibile.
//	 * @param fineColonna int che rappresenta la colonna di fine dell'area raggiungibile.
//	 * @param raggiungibilita array bidimensionale di int che rappresenta la mappa di raggiuigibilita'.
//	 */
//	public void stampaMappaRaggiungibilita(int inizioRiga, int inizioColonna, int fineRiga, int fineColonna, int[][]raggiungibilita) {
//		System.out.print(ACQUA);
//		for(int j=0;j<MAX;j++) {
//			if(j<DIECI) {
//				System.out.print("0" + j + " ");
//			} else {
//				System.out.print(j + " ");
//			}
//		}
//		System.out.println();
//		for(int i=0;i<MAX;i++) {
//			if(i<DIECI) {
//				System.out.print("0" + i + " ");
//			} else {
//				System.out.print(i + " ");
//			}
//			for(int j=0;j<MAX;j++) {
//				if (mappa[i][j] == null) { //e' acqua
//					System.out.print(ACQUA);
//				} else { //se e' terra puo' essere carogna o vegetale
//					Cella cella = mappa[i][j];
//					if((i>=inizioRiga && i<=fineRiga) && (j>=inizioColonna && j<=fineColonna))  {
//						if((raggiungibilita[i - inizioRiga][j - inizioColonna]!=NONRAGG) &&
//								(raggiungibilita[i - inizioRiga][j - inizioColonna]!=CELLACQUA)) {
//							System.out.print(" " + raggiungibilita[i - inizioRiga][j - inizioColonna] + " ");
//						} else { 
//							if(raggiungibilita[i - inizioRiga][j - inizioColonna]==NONRAGG ||
//									raggiungibilita[i - inizioRiga][j - inizioColonna]==CELLACQUA) {
//								System.out.print(" " + raggiungibilita[i - inizioRiga][j - inizioColonna] + " ");
//							}
//						}
//					} else {
//						if(cella.getDinosauro()!=null) {
//							System.out.print(cella.getDinosauro().getId() + " ");
//						} else {
//							if(cella.getOccupante() instanceof Carogna) {
//								System.out.print(CAROGNA);
//							}
//							if(cella.getOccupante() instanceof Vegetale) {
//								System.out.print(VEGETAZIONE);
//							}
//							if(!(cella.getOccupante() instanceof Vegetale) &&
//									!(cella.getOccupante() instanceof Carogna)) {
//								System.out.print(TERRA);
//							}
//						}
//
//					}
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
//		System.out.println();
//	}
//
//	/**
//	 * Stampa la mappa della raggiungibilita' con applicata la strada percorsa.
//	 * @param inizioRiga int che rappresenta la riga di inzio dell'area raggiungibile.
//	 * @param inizioColonna int che rappresenta la colonna di inzio dell'area raggiungibile.
//	 * @param fineRiga int che rappresenta la riga di fine dell'area raggiungibile.
//	 * @param fineColonna int che rappresenta la colonna di fine dell'area raggiungibile.
//	 * @param stradaPercorsa array bidimensionale di int che rappresenta la strada percorsa dal Dinosauto durante uno spostamento.
//	 */
//	public void stampaMappaStradaPercorsa(int inizioRiga, int inizioColonna, int fineRiga, int fineColonna, int[][]stradaPercorsa) {
//		System.out.print(ACQUA);
//		for(int j=0;j<MAX;j++) {
//			if(j<DIECI) {
//				System.out.print("0" + j + " ");
//			} else {
//				System.out.print(j + " ");
//			}
//		}
//		System.out.println();
//		for(int i=0;i<MAX;i++) {
//			if(i<DIECI) {
//				System.out.print("0" + i + " ");
//			} else {
//				System.out.print(i + " ");
//			}
//			for(int j=0;j<MAX;j++) {
//				if (mappa[i][j] == null) { //e' acqua
//					System.out.print(ACQUA);
//				}
//				else { //se e' terra puo' essere carogna o vegetale
//					Cella cella = mappa[i][j];
//					if((i>=inizioRiga && i<=fineRiga) && (j>=inizioColonna && j<=fineColonna))  {	
//						if((stradaPercorsa[i - inizioRiga][j - inizioColonna]!=NONRAGG) && (stradaPercorsa[i - inizioRiga][j - inizioColonna]!=CELLACQUA)) {
//							System.out.print(" " + stradaPercorsa[i - inizioRiga][j - inizioColonna] + " ");
//						} else if(stradaPercorsa[i - inizioRiga][j - inizioColonna]==NONRAGG || stradaPercorsa[i - inizioRiga][j - inizioColonna]==CELLACQUA) {
//							System.out.print(" " + stradaPercorsa[i - inizioRiga][j - inizioColonna] + " ");
//						}
//					} else {
//						if(cella.getDinosauro()!=null) {
//							System.out.print(cella.getDinosauro().getId() + " ");
//						} else {
//							if(cella.getOccupante() instanceof Carogna)
//							{
//								System.out.print(CAROGNA);
//							}
//							if(cella.getOccupante() instanceof Vegetale)
//							{
//								System.out.print(VEGETAZIONE);
//							}
//							if(!(cella.getOccupante() instanceof Vegetale) &&
//									!(cella.getOccupante() instanceof Carogna)) {
//								System.out.print(TERRA);
//							}
//						}
//					}
//				}
//			}
//			System.out.println();
//		}
//		System.out.println();
//		System.out.println();
//	}

}
