package isoladinosauri.salvataggio;

import isoladinosauri.Giocatore;
import isoladinosauri.Utente;
import isoladinosauri.modellodati.Carnivoro;
import isoladinosauri.modellodati.Dinosauro;
import isoladinosauri.modellodati.Erbivoro;

import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.query.Predicate;

public class StatoGiocatoreDB {
    
    private static final int PORTA = 4444;
    private static final String NOME_FILE = "isola.db4o";
    private static final String NOMEUTENTE = "isola";
    private static final String PASSWORD = "isola";
    private static ObjectServer server;
    private static ObjectContainer client;

    private static void startDBServer() {
        if (server == null) {
            Configuration config = Db4o.newConfiguration();
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
            
            server = Db4o.openServer(config, NOME_FILE, PORTA);
            server.grantAccess(NOMEUTENTE, PASSWORD);
            
            client = server.openClient();

            System.out.println("DB4o Server avviato");
        } else {
            System.out.println("DB4o Server gia' started");
        }
    }

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
    
    public static synchronized void store(String subcriptionID, Object data) {
        if (server == null) {
            startDBServer();
        }
        SalvataggioGiocatore subscr = new SalvataggioGiocatore();
        subscr.setChiave(subcriptionID);
        subscr.setOggetto(data);

        client.store(subscr);
        client.commit();
        System.out.println("DB4o oggetto salvato con ID: " + subcriptionID);
    }
    
    
    public static synchronized void delete(final String subcriptionID) {
        if (server == null) {
            startDBServer();
        }
        ObjectSet<SalvataggioGiocatore> result = client.query(new Predicate<SalvataggioGiocatore>() {
            @Override
            public boolean match(SalvataggioGiocatore sub) {
                return subcriptionID.equals(sub.getChiave());
            }
        });

        if (result == null || result.size() == 0 || result.size() > 1) {
            System.err.println("DB4o oggetto non trovato per ID: " + subcriptionID);
            return;
        }
        Object toRet = result.get(0);
        client.delete(toRet);
        client.commit();

        System.out.println("DB4o oggetto cancellato per ID: " + subcriptionID);
    }

    @SuppressWarnings("unchecked")
    public static synchronized List<SalvataggioGiocatore> load(Class clazz) {
        if (server == null) {
            startDBServer();
        }
        ObjectSet<SalvataggioGiocatore> result = client.query(SalvataggioGiocatore.class);
        if (result == null || result.size() == 0) {
            System.err.println("DB4o oggetto non trovato per la classe:" + clazz.getName());
            return new ArrayList<SalvataggioGiocatore>(0);
        }

        List<SalvataggioGiocatore> toRet = new ArrayList<SalvataggioGiocatore>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getOggetto().getClass().getName().equals(clazz.getName())) {
                toRet.add(result.get(i));
            }
        }

        System.out.println("DB4o ottenuto: " + toRet.size() + " oggetto della classe " + clazz.getName());

        return toRet;

    }
    
    public static synchronized SalvataggioGiocatore loadGiocatore(final String utente) {
        if (server == null) {
            startDBServer();
        }
        
        
        ObjectSet<SalvataggioGiocatore> risultato = client.query(new Predicate<SalvataggioGiocatore>() {
            @Override
            public boolean match(SalvataggioGiocatore sub) {
                return utente.equals(sub.getChiave());
            }
        });
        if (risultato.size() == 0) {
            return null;
        }
        return risultato.get(0);
    }

}
