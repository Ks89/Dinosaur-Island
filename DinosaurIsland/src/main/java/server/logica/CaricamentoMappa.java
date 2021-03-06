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

package server.logica;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;

import server.modellodati.Carogna;
import server.modellodati.Vegetale;


/**
 * Classe che si occupa del caricamento della mappa da file.
 *
 */
public class CaricamentoMappa {

	private static final int MAX = 40;
	private Cella[][] mappa; 

	/**
	 * Costruttore che inizializza la mappa di Celle.
	 */
	public CaricamentoMappa() {
		mappa = new Cella[MAX][MAX];
	}


	/**
	 * Metodo che esegue il caricamento della mappa come file di testo e riempie un array bidimensionale con 
	 * gli elementi letti da tale file. Inolte, richiama i metodi predefiniti per inizializzare l'energiaMax al momento
	 * della creazione di un Occupante.
	 * @return La mappa di gioco costituita da un array bidimensionali di Celle.
	 */
	public Cella[][] caricaDaFile(String nomeFile) {
		BufferedReader br = null;
		Reader reader = null;
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
					this.assegnaCelle(cellaLetta, i, j);
				}
				riga = br.readLine();
			}

		}	
		catch(IOException ioException) {
			System.err.println("Errore lettura file");
		} finally {
			try {
				if(reader!=null) {
					reader.close();
				}
				if(br!=null) {

					br.close();
				}
			} catch (IOException e) {
				System.err.println("Errore chiusura file");
			}
		}
		return mappa.clone();
	}


	/**
	 * Esegue il caricamento della mappa di Celle da un array di String e tramite il metodo
	 * assegnaCelle si praoccupa di creare i Vegetali e le Carogne, richiamandone i costruttori.
	 * @param input array bidimensionali di String che rappresenta la mappa letta da file.
	 * @return Un array bidimensionale di Celle che rappresenta la mappa di gioco.
	 */
	public Cella[][] caricaMappa(String[][] input) {
		for(int i=0;i<MAX;i++) {
			for(int j=0;j<MAX;j++) {
				this.assegnaCelle(input[i][j], i, j);
			}
		}
		return mappa.clone();
	}


	/**
	 * Metodo richiamato da caricaMappa per assegnare ad ogni String presente nella mappa letta da file
	 * l'oggetto Cella che puo' contenere una Carogna o un Vegetale.
	 * @param elemento String che rappresenta la cella della mappa letta da file.
	 * @param riga int che rappresenta la riga dell'elmento (la singola cella) letto dal file di testo.
	 * @param colonna int che rappresenta la colonna dell'elmento (la singola cella) letto dal file di testo.
	 */
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
			Vegetale vegetale = new Vegetale();
			vegetale.setRiga(riga);
			vegetale.setColonna(colonna);
			cella.setOccupante(vegetale); 
			this.mappa[riga][colonna]=cella;
		}
		if(elemento.equals("c")) {
			//se carogna crea cella e mette come occupante la carogna
			Cella cella=new Cella();
			//chiamo costruttore predef che inzializza energiaMax random
			Carogna carogna = new Carogna();
			carogna.setRiga(riga);
			carogna.setColonna(colonna);
			cella.setOccupante(carogna);
			this.mappa[riga][colonna]=cella;
		}
	}
}
