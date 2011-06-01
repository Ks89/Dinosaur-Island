package isoladinosauri;

import java.util.Random;

/**
 * Questa classe produce mappe "pseudo-casuali"
 * Infatti, ci sono 18 forme d'acqua predefinite che vengono
 * inserite nella mappa con cordinate casuali. 
 * Le forme non vengono mai ruotate, solo posizionate in modo casuale.
 * Ha un solo metodo che restituisce una mappa di String[][]
 */
public class GenerazioneMappa {

	private static final int MAX = 40;

	/**
	 * Metodo per generare la mappa in modo 'presuo-casuale'.
	 * @return La mappa genereta sotto forma di array bidimensionale di String.
	 */
	public String[][] creaMappaCasuale() {
		String mappa[][] = this.inserisciAcqua();
		this.inserisciCarogne(mappa);
		this.inserisciTerra(mappa);
		this.contaCarogne(mappa);
		PosizionaVegetazione tv = new PosizionaVegetazione();
		tv.posizionaVegetazione(mappa);
		return mappa;
		//		this.salvaMappa(mappa);
		//		this.stampaMappa(mappa);

	}

	/**
	 * Metodo per contare il numero di carogne all'interno della mappa di gioco.
	 * @param mappa array bidimensionale di String che rappresenta la mappa di gioco generate in modo casuale.
	 */
	private void contaCarogne(String[][] mappa) {
		int cont=0;
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				if(mappa[i][j].equals("c")) {
					cont++;
				}
			}
		}
		System.out.println(cont);
	}

	/**
	 * Metodo per inserire le Carogne all'interno della mappa generata in modo casuale.
	 * @param mappa array bidimensionale di String che rappresenta la mappa di gioco generate in modo casuale.
	 * @return Un array bidimensionale di String che rappresenta la mappa di gioco con inserite 20 carogne ('c').
	 */
	private String[][] inserisciCarogne(String[][] mappa) {
		Random random = new Random();
		int cont=0, riga, colonna;
		do {
			riga = random.nextInt(MAX);
			colonna = random.nextInt(MAX);
			if(!mappa[riga][colonna].equals("a") && !mappa[riga][colonna].equals("c")) {
				mappa[riga][colonna] = "c";
				cont++;
			}
		} while(cont<20);
		return mappa;
	}

	/**
	 * Metodo per inserire la terra nella mappa generata in modo casuale.
	 * @param mappa array bidimensionale di String che rappresenta la mappa di gioco generate in modo casuale.
	 * @return Un array bidimensionale di String che rappresenta la mappa di gioco con inserite le cella di terra ('t').
	 */
	private String[][] inserisciTerra(String[][] mappa) {
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				if(!mappa[i][j].equals("a") && !mappa[i][j].equals("c")) {
					mappa[i][j] = "t";
				}
			}
		}
		return mappa;
	}

	/**
	 * Metodo che inserisce le "pozze" di acqua nella mappa di gioco in modo "preuso-casuale".
	 * @return Un array bidimensionale di String che rappresenta la mappa di gioco con posizonate solo le celle d'acqua.
	 */
	private String[][] inserisciAcqua(){
		String[][] mappa = new String[MAX][MAX];
		/*
		 * La variabile numeroraggiungibili servira' dopo
		 * a capire se tutti i blocchi di terra sono 
		 * collegati tra di loro
		 */
		int numeroraggiungibili;

		do{
			numeroraggiungibili=0;
			//Inizializzazione mappa
			for(int i=0;i<=39;i++) {
				for(int j=0;j<=39;j++) {
					mappa[i][j]=" ";
				}

			}

			//Disegno il contorno d'acqua dell isola
			for(int j=0;j<=39;j++) {
				mappa[0][j]="a";
			}
			for(int j=0;j<=39;j++) {
				mappa[39][j]="a";
			}
			for(int i=0;i<=39;i++) {
				mappa[i][0]="a";
			}
			for(int i=0;i<=39;i++) {
				mappa[i][39]="a";
			}

			/*
			 * Per fare in modo che l'acqua sia del 20 percento e 
			 * in blocchi compresi di 5 e 15 quadretti sono
			 * state disegnate delle forme predefinite che 
			 * vengono inserite nella mappa in posizioni 
			 * casuali.
			 * Il numero di elementi dell'array di stringhe
			 * determina il numero di quadretti di cui e'
			 * composta la figura.
			 * I numeri si interpretano nel modo seguente:
			 * Per posizionare la figura si sceglie una 
			 * cordinata a caso (x,y)
			 * I numeri di ogni elemento dell array rappresentano
			 * di quanto bisogna spostarsi per disegnare la figura
			 * ESEMPIO FORMACINQUE
			 * 
			 * Cordinate scelte a caso X=2, Y=3
			 * primo quadretto (2,3)
			 * secondo (2+1,3+0)
			 * terzo (2+0,3+1)
			 * E cosi' via
			 */

			//ottengo l'array di forme di acqua da inserire
			String[][] arrayForme = this.ottieniFormeAcqua();


			/*
			 * Uno alla volta inserisco nella mappa
			 * tutti gli elementi contenuti nella 
			 * struttura arrayforme
			 */
			for(int numerofigure=0;numerofigure<arrayForme.length;numerofigure++){

				String arraydistringhe[]=arrayForme[numerofigure];

				/*
				 *cordinate generate a caso
				 *si parte dal punto generato
				 *e si disegna la figura verso
				 *il basso e verso sinistra 
				 */			
				int x=0;
				int y=0;

				/*
				 * I due offset determinano di quanto
				 *  ci si sposta dal punto generato
				 *  per disegnare la figura
				 */
				int offsetx=0;
				int offsety=0;

				Random random=new Random();
				/*
				 * Nel ciclo while si calcola se nelle coordinate generate a caso
				 * c'e' lo spazio per disegnare la figura d'acqua
				 */
				boolean posNonConcessa=true;

				while (posNonConcessa){
					posNonConcessa=false;
					x=random.nextInt(38);
					y=random.nextInt(38);
					int i=0;
					for(i=0;i<arraydistringhe.length;i++){
						offsetx=Integer.parseInt(arraydistringhe[i].charAt(0)+"");
						offsety=Integer.parseInt(arraydistringhe[i].charAt(1)+"");
						if((x+offsetx)>39) {
							posNonConcessa=true;
						} else { if((y+offsety)>39) {
							posNonConcessa=true;
						} else { if (mappa[x+offsetx][y+offsety].equals("a")||mappa[x+offsetx][y+offsety].equals("b")) {
							posNonConcessa=true;
						} else { 
							if (mappa[x+offsetx][y+offsety].equals("a")||mappa[x+offsetx][y+offsety].equals("b")) {
								posNonConcessa=true;
							}
						}
						}
						}
					} //chiusura FOR

				}//chiusura WHILE
				/*
				 * A questo punto disegno la figura nella mappa.
				 * Visto che le figure non si devono toccare, intorno
				 * ad esse metto un simbolo sul quale
				 * non si puo' mettere acqua
				 * (Per evitare blocchi di piu' di 15 caselle adiacenti
				 *  di acqua) 
				 */

				for(int i=0;i<arraydistringhe.length;i++){
					offsetx=Integer.parseInt(arraydistringhe[i].charAt(0)+"");
					offsety=Integer.parseInt(arraydistringhe[i].charAt(1)+"");

					mappa[x+offsetx][y+offsety]="a";
					if(offsetx==0) {
						mappa[x-1][y+offsety]="b"; 
					}
					if(offsety==0) {
						mappa[x+offsetx][y-1]="b";
					}
					mappa[x+offsetx+1][y+offsety]="b";
					mappa[x+offsetx][y+offsety+1]="b";
				}
				/*
				 * Devo controllare se tutte le zone di
				 * terra sono unite (raggiungibili)
				 */
			}//Chiusura ciclo for 

			/*
			 * Essendo che il posizionamento delle forme 
			 * d'acqua puo' intaccare il bordo dell isola
			 * Si faccia riferimento alla classe inserimento,
			 * quando si inserisce l'acqua si inserisce anche
			 * un "fossato" di guardia per evitare l'unione di
			 * due pozze d'acqua
			 */
			for(int j=0;j<=39;j++) {
				mappa[0][j]="a";
			}
			for(int j=0;j<=39;j++) {
				mappa[39][j]="a";
			}
			for(int i=0;i<=39;i++) {
				mappa[i][0]="a";
			}
			for(int i=0;i<=39;i++) {
				mappa[i][39]="a";
			}

			//Eliminazione dei fossati di guardia
			for(int i=0;i<=39;i++) {
				for(int j=0;j<=39;j++) {
					if (mappa[i][j].equals("b")) {
						mappa[i][j]=" " ;
					}
				}
			}

			/*
			 * Cerco il primo elemento di terra
			 * sulla prima riga. Quando lo trovo
			 * esco dal ciclo (break) e mi salvo le 
			 * cordinate. Cosi guardo se quell'
			 * elemento ne raggiunge altri 1280
			 * (tutti gli elementi di terra)
			 * Gli elementi raggiunti sono contrassegnati
			 * dalla lettera 't'
			 */

			int j;
			for (j=1;j<39;j++){
				if (mappa[1][j].equals(" ")) {
					break;
				}
			}
			int primoeleterray=j;
			mappa[1][primoeleterray]="t";

			/*
			 * In questo ciclo do while si "colorano" i 
			 * blocchi adiacenti a quelli che contengono 't'
			 * se sono vuoti. (Per la raggiungibilita')
			 */
			boolean modificato;
			do{
				modificato=false;
				for(int i=0;i<39;i++) {
					for(j=0;j<39;j++) {
						if (mappa[i][j].equals("t")) {
							if (mappa[i+1][j].equals(" ")) {
								mappa[i+1][j]="t";
								modificato=true;
							}

							if (mappa[i-1][j].equals(" ")) { 
								mappa[i-1][j]="t";
								modificato=true;
							}
							if (mappa[i][j+1].equals(" ")) { 
								mappa[i][j+1]="t";
								modificato=true;
							}
							if (mappa[i][j-1].equals(" ")) { 
								mappa[i][j-1]="t";
								modificato=true;
							}
						}//Chiusura IF
					}    //Chiusura FOR
				}		 //Chiusura FOR
			} while (modificato); //CHIUSURA DO-WHILE

			//Conta il numero di t
			for(int i=0;i<39;i++) {
				for(int k=0;k<39;k++) {
					if (mappa[i][k].equals("t")) {
						numeroraggiungibili++;
					}
				}
			}

		}while (numeroraggiungibili!=1280);
		return mappa;
	}

	/**
	 * Metodo che inserire in un ArrayList degli array si String contenti le coordinare delle celle di acqua che fanno
	 * parte di una determinata forma.
	 * @return Un ArrayList di array di String contenente tutte le forme di acqua.
	 */
	private String[][] ottieniFormeAcqua() {
//		ArrayList<String[]> arrayForme =new ArrayList<String[]>();
		String[][] arrayForme = new String[18][18];
		String formacinque[]={"00","10","01","11","20"};
		String formacinquebis[]={"00","10","20","30","40"};
		String formasei[]={"00","01","10","11","20","21"};
		String formaseibis[]={"00","10","01","11","02","12"};
		String formasette[]={"00","10","01","11","02","12","03"};
		String formasettebis[]={"00","01","10","11","20","21","30"};
		String formaotto[]={"00","01","02","03","10","11","12","13"};
		String formaottobis[]={"00","01","02","10","11","12","20","21"};
		String formanove[]={"00","01","02","10","11","12","20","21","22"};
		String formanovebis[]={"00","01","02","10","11","12","20","21","22"};
		String formadieci[]={"00","01","02","03","10","11","12","13","20","21"};
		String formaundici[]={"00","01","10","11","20","21","22","30","31","32","41"};
		String formaundicibis[]={"00","01","02","03","10","11","12","13","23","24","25"};
		String formaundicitris[]={"00","01","02","10","11","12","20","21","22","30","31"};
		String formadodici[]={"00","01","02","03","04","10","11","20","21","22","31","41"};
		String formadodicibis[]={"00","01","02","10","11","12","20","21","22","30","31","32"};
		String formadodicitris[]={"00","01","02","03","04","10","11","20","21","22","31","41"};
		String formaquindici[]={"00","01","02","10","11","20","21","22","23","24","30","31","32","33","42"};

		/*
		 * Aggiungo tutte le forme a una struttura 
		 * dati di tipo ArrayList che puo' contenere
		 * array di stringhe come oggetti
		 */
		arrayForme[0] = formacinque;
		arrayForme[1] = formacinquebis;
		arrayForme[2] = formasei;
		arrayForme[3] = formaseibis;
		arrayForme[4] = formasette;
		arrayForme[5] = formasettebis;
		arrayForme[6] = formaotto;
		arrayForme[7] = formaottobis;
		arrayForme[8] = formanove;
		arrayForme[9] = formanovebis;
		arrayForme[10] = formadieci;
		arrayForme[11] = formaundici;
		arrayForme[12] = formaundicibis;
		arrayForme[13] = formaundicitris;
		arrayForme[14] = formadodici;
		arrayForme[15] = formadodicitris;
		arrayForme[16] = formadodicibis;
		arrayForme[17] = formaquindici;
		return arrayForme;
	}

	//	private void salvaMappa(String[][] mappa) {
	//		//salvo la mappa
	//		try{
	//			Formatter out = new Formatter("mappaTest.txt");
	//			//attenzione qui prima di modificare il codice c'era salvaRiga= new String(); e non null
	//			String salvaRiga = null;
	//			for(int i=0;i<40;i++) {
	//				for(int j=0;j<40;j++) {
	//					salvaRiga = salvaRiga + mappa[i][j];
	//				}
	//				out.format("%s\n", salvaRiga);
	//				salvaRiga="";
	//			}
	//
	//			out.close();
	//		}
	//		catch (SecurityException securityException)
	//		{
	//			System.err.println("Non hai accesso al file");
	//			System.err.println("Il programma e' stato terminato");
	//		}
	//		catch (FileNotFoundException filesNotFoundException)
	//		{
	//			System.err.println("Errore nella creazione del file");
	//			System.err.println("Il programma e' stato terminato");
	//		}
	//	}

	//	//metodo stampa mappa
	//	private void stampaMappa(String mappa [][]){
	//		for(int i=0;i<=39;i++) {
	//			for(int j=0;j<=39;j++) {
	//				System.out.print(mappa[i][j]+" ");
	//			}
	//			System.out.println();
	//		}
	//	}


}
