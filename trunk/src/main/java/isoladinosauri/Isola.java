package isoladinosauri;

import isoladinosauri.modellodati.Carogna;
import isoladinosauri.modellodati.Vegetale;


/**
 * Classe Isola costituita dal solo attributo mappa.
 * Essa contiene tutti i metodi per generare la mappa di gioco e/o
 * caricarla da file esterno.
 */ 
public class Isola {

	private static final int MAX = 40;

	private Cella[][] mappa; 
	

	public Isola(Cella[][] mappa) {
		this.setMappa(mappa);
	}
	
	public Cella[][] getMappa() {
		return mappa;        
	}
	
	public void setMappa(Cella[][] mappa) {
		this.mappa = mappa;
	}


	public void stampaMappa() {
		//metodo che esiste solo per testare il caricamento
		//presto sara' rimosso e trasformato in test junit
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				if (mappa[i][j] == null) { //e' acqua
					System.out.print("a:    ");
				}
				else { //se e' terra puo' essere carogna o vegetale
					Cella cella = mappa[i][j];
					if(cella.getDinosauro()!=null) {
						System.out.print("d:" + + cella.getDinosauro().getEnergiaMax() + " ");
					} else {
						if(cella.getOccupante() instanceof Carogna)
						{
							Carogna carogna = (Carogna)cella.getOccupante();
							System.out.print("c:" + carogna.getEnergia() + " ");
						}
						if(cella.getOccupante() instanceof Vegetale)
						{
							Vegetale vegetale = (Vegetale)cella.getOccupante();
							System.out.print("v:" + vegetale.getEnergia() + " ");
						}
						if(!(cella.getOccupante() instanceof Vegetale) &&
								!(cella.getOccupante() instanceof Carogna)) {
							System.out.print("t:    ");
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
		for(int j=0;j<MAX;j++) {
			if(j<10) {
				System.out.print("0" + j + " ");
			} else {
				System.out.print(j + " ");
			}
		}
		System.out.println();
		for(int i=0;i<MAX;i++) {
			if(i<10) {
				System.out.print("0" + i + " ");
			} else {
				System.out.print(i + " ");
			}
			for(int j=0;j<MAX;j++) {
				if (mappa[i][j] == null) { //e' acqua
					System.out.print("   ");
				} else { //se e' terra puo' essere carogna o vegetale
					Cella cella = mappa[i][j];
					if(cella.getDinosauro()!=null) {
						System.out.print(cella.getDinosauro().getId() + " ");
					} else {
						if(cella.getOccupante() instanceof Carogna) {
							System.out.print(" c ");
						}
						if(cella.getOccupante() instanceof Vegetale) {
							System.out.print(" v ");
						}
						if(!(cella.getOccupante() instanceof Vegetale) &&
								!(cella.getOccupante() instanceof Carogna)) {
							System.out.print(" . ");
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
		for(int j=0;j<MAX;j++) {
			if(j<10) {
				System.out.print("0" + j + " ");
			} else {
				System.out.print(j + " ");
			}
		}
		System.out.println();
		for(int i=0;i<MAX;i++) {
			if(i<10) {
				System.out.print("0" + i + " ");
			} else {
				System.out.print(i + " ");
			}
			for(int j=0;j<MAX;j++) {
				if(giocatore.getMappaVisibile()[i][j]==true) {
					if (mappa[i][j] == null) { //e' acqua
						System.out.print("   ");
					} else { //se e' terra puo' essere carogna o vegetale
						Cella cella = mappa[i][j];
						if(cella.getDinosauro()!=null) {
							System.out.print(cella.getDinosauro().getId() + " ");
						} else {
							if(cella.getOccupante() instanceof Carogna) {
								System.out.print(" c ");
							}
							if(cella.getOccupante() instanceof Vegetale) {
								System.out.print(" v ");
							}
							if(!(cella.getOccupante() instanceof Vegetale) &&
									!(cella.getOccupante() instanceof Carogna)) {
								System.out.print(" . ");
							}
						}

					}
				} else {
					System.out.print("   ");
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}	

	public void stampaMappaRaggiungibilita(int inizioRiga, int inizioColonna, int fineRiga, int fineColonna, int[][]raggiungibilita) {
		System.out.print("   ");
		for(int j=0;j<MAX;j++) {
			if(j<10) {
				System.out.print("0" + j + " ");
			} else {
				System.out.print(j + " ");
			}
		}
		System.out.println();
		for(int i=0;i<MAX;i++) {
			if(i<10) {
				System.out.print("0" + i + " ");
			} else {
				System.out.print(i + " ");
			}
			for(int j=0;j<MAX;j++) {
				if (mappa[i][j] == null) { //e' acqua
					System.out.print("   ");
				} else { //se e' terra puo' essere carogna o vegetale
					Cella cella = mappa[i][j];
					if((i>=inizioRiga && i<=fineRiga) && (j>=inizioColonna && j<=fineColonna))  {
						if((raggiungibilita[i - inizioRiga][j - inizioColonna]!=9) &&
								(raggiungibilita[i - inizioRiga][j - inizioColonna]!=8)) {
							System.out.print(" " + raggiungibilita[i - inizioRiga][j - inizioColonna] + " ");
						} else { 
							if(raggiungibilita[i - inizioRiga][j - inizioColonna]==9 ||
									raggiungibilita[i - inizioRiga][j - inizioColonna]==8) {
								System.out.print(" " + raggiungibilita[i - inizioRiga][j - inizioColonna] + " ");
							}
						}
					} else {
						if(cella.getDinosauro()!=null) {
							System.out.print(cella.getDinosauro().getId() + " ");
						} else {
							if(cella.getOccupante() instanceof Carogna) {
								System.out.print(" c ");
							}
							if(cella.getOccupante() instanceof Vegetale) {
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

	public void stampaMappaStradaPercorsa(int inizioRiga, int inizioColonna, int fineRiga, int fineColonna, int[][]stradaPercorsa) {
		System.out.print("   ");
		for(int j=0;j<MAX;j++) {
			if(j<10) {
				System.out.print("0" + j + " ");
			} else {
				System.out.print(j + " ");
			}
		}
		System.out.println();
		for(int i=0;i<MAX;i++) {
			if(i<10) {
				System.out.print("0" + i + " ");
			} else {
				System.out.print(i + " ");
			}
			for(int j=0;j<MAX;j++) {
				if (mappa[i][j] == null) { //e' acqua
					System.out.print("   ");
				}
				else { //se e' terra puo' essere carogna o vegetale
					Cella cella = mappa[i][j];
					if((i>=inizioRiga && i<=fineRiga) && (j>=inizioColonna && j<=fineColonna))  {	
						if((stradaPercorsa[i - inizioRiga][j - inizioColonna]!=9) && (stradaPercorsa[i - inizioRiga][j - inizioColonna]!=8)) {
							System.out.print(" " + stradaPercorsa[i - inizioRiga][j - inizioColonna] + " ");
						} else if(stradaPercorsa[i - inizioRiga][j - inizioColonna]==9 || stradaPercorsa[i - inizioRiga][j - inizioColonna]==8) {
							System.out.print(" " + stradaPercorsa[i - inizioRiga][j - inizioColonna] + " ");
						}
					} else {
						if(cella.getDinosauro()!=null) {
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

}
