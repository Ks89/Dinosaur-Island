package server.salvataggio;

import server.logica.Giocatore;
import server.logica.Utente;
import server.modellodati.Carnivoro;
import server.modellodati.Dinosauro;
import server.modellodati.Erbivoro;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.query.Predicate;

/**
 *	Classe per salvare e caricare un giocatore sul database di DB4O.
 */
public class StatoGiocatoreDB {

	private static final int PORTA = 4444; 
	private static final String NOMEFILE = "giocatori.db4o";
	private static final String USERNAME = "isola";
	private static final String PASSWORD = "isola";
	private static ObjectServer server;
	private static ObjectContainer client;

	/**
	 * Metodo per avviare il server.
	 */
	private static void avviaDBServer() {
		if (server == null) {
			//conigurazione database
			Configuration config = Db4o.newConfiguration();

			//imposto i cascadeOnDelete e cascadeOnUpdate
			config.objectClass(SalvataggioGiocatore.class.getCanonicalName()).cascadeOnDelete(true);
			config.objectClass(Giocatore.class.getCanonicalName()).cascadeOnDelete(true);
			config.objectClass(Dinosauro.class.getCanonicalName()).cascadeOnDelete(true);
			config.objectClass(Carnivoro.class.getCanonicalName()).cascadeOnDelete(true);
			config.objectClass(Erbivoro.class.getCanonicalName()).cascadeOnDelete(true);
			config.objectClass(Utente.class.getCanonicalName()).cascadeOnDelete(true);

			config.objectClass(SalvataggioGiocatore.class.getCanonicalName()).cascadeOnUpdate(true);
			config.objectClass(Giocatore.class.getCanonicalName()).cascadeOnUpdate(true);
			config.objectClass(Dinosauro.class.getCanonicalName()).cascadeOnUpdate(true);
			config.objectClass(Carnivoro.class.getCanonicalName()).cascadeOnUpdate(true);
			config.objectClass(Erbivoro.class.getCanonicalName()).cascadeOnUpdate(true);
			config.objectClass(Utente.class.getCanonicalName()).cascadeOnUpdate(true);

			//creo l'object server e apro il server con NOMEPORTA e PORTA
			server = Db4o.openServer(config, NOMEFILE, PORTA);
			//eseguo il login con USERNAME e PASSWORD
			server.grantAccess(USERNAME, PASSWORD);

			//apro il client
			client = server.openClient();

			System.out.println("DB4o Server avviato");
		} else {
			System.out.println("DB4o Server gia' avviato");
		}
	}

	/**
	 * Metodo per fermare il server.
	 */
	public static void stopDBServer() {
		if (server != null) {
			client.close();
			server.close();
			server = null;
			System.out.println("DB4o Server fermato");
		} else {
			System.out.println("DB4o Server non avviato");
		}
	}

	/**
	 * Metodo per salvare nel database.
	 * @param scID String che rappresenta un identificativo univoco dell'elemento da salvare (esempio il nomeUtente).
	 * @param oggetto Object che rappresenta l'oggetto da salvare, per quell'ID.
	 */
	public static synchronized void salva(String scID, Object oggetto) {
		/*
		 * La parola riservata synchronized consente una accurata gestione dei metodi
		 * che possono essere richiamati contemporaneamente da piu' thread, quando si
		 * presenta la necessita' di garantire un accesso sincronizzato ad eventuali
		 * risorse gestite da tali metodi. Quando un thread sta utilizzando un oggetto
		 * per mezzo di un metodo synchronized, tale oggetto viene bloccato (locked)
		 * per evitare altri accessi concorrenti.
		 */

		if (server == null) {
			avviaDBServer();
		}
		SalvataggioGiocatore sc = new SalvataggioGiocatore();
		sc.setChiave(scID);
		sc.setOggetto(oggetto);

		//salvo nel database l'oggetto con id associato
		client.store(sc);
		client.commit(); //bisogna metterlo, vedi documentazione
		System.out.println("DB4o oggetto salvato con ID: " + scID);
	}


	/**
	 * Metodo per rimuovere un elemento dal database.
	 * @param scID String che rappresenta un identificativo univoco dell'elemento da salvare.
	 */
	public static synchronized void cancella(final String scID) {
		if (server == null) {
			avviaDBServer();
		}
		ObjectSet<SalvataggioGiocatore> elemento = client.query(new Predicate<SalvataggioGiocatore>() {
			@Override
			public boolean match(SalvataggioGiocatore sub) {
				return scID.equals(sub.getChiave());
			}
		});

		if (elemento == null || elemento.size() == 0 || elemento.size() > 1) {
			System.err.println("DB4o oggetto non trovato per ID: " + scID);
			return;
		}
		Object toRet = elemento.get(0);
		client.delete(toRet);
		client.commit();
		System.out.println("DB4o oggetto cancellato per ID: " + scID);
	}

	//    /**
	//     * Metodo per ottenere un elemento dal database.
	//     * @param classe
	//     * @return
	//     */
	//    public static synchronized List<SalvataggioGiocatore> load(Class classe) {
	//        if (server == null) {
	//            avviaDBServer();
	//        }
	//        ObjectSet<SalvataggioGiocatore> result = client.query(SalvataggioGiocatore.class);
	//        if (result == null || result.size() == 0) {
	//            System.err.println("DB4o oggetto non trovato per la classe :" + classe.getName());
	//            return new ArrayList<SalvataggioGiocatore>(0);
	//        }
	//
	//        List<SalvataggioGiocatore> toRet = new ArrayList<SalvataggioGiocatore>();
	//        for (int i = 0; i < result.size(); i++) {
	//            if (result.get(i).getObj().getClass().getName().equals(classe.getName())) {
	//                toRet.add(result.get(i));
	//            }
	//        }
	//
	//        System.out.println("DB4o ottenuto: " + toRet.size() + " oggetto per classe " + classe.getName());
	//
	//        return toRet;
	//
	//    }

	/**
	 * Metodo per caricare un Giocatore. Fornendogli il nome utente, si occupa di estrarre la "tupla". Essa e' composta
	 * da una chiave e un oggetto (vedic lasse SalvataggioGiocatore). Da essa e' sufficiente usare i metodi get per ottenere
	 * i valori desiderati.
	 * @param utente String rappresentante il nome utente del Giocatore da estrarre dal database.
	 * @return Un oggetto SalvataggioGiocatore da cui si puo' estrarre la chiave (nome utente) e l'oggetto Giocatore stesso.
	 */
	public static synchronized SalvataggioGiocatore caricaGiocatore(final String utente) {
		if (server == null) {
			avviaDBServer();
		}

		ObjectSet<SalvataggioGiocatore> elemento = client.query(new Predicate<SalvataggioGiocatore>() {
			@Override
			public boolean match(SalvataggioGiocatore sub) {
				return utente.equals(sub.getChiave());
			}
		});
		if (elemento.size() == 0) {
			return null;
		}
		return elemento.get(0);
	}

}
