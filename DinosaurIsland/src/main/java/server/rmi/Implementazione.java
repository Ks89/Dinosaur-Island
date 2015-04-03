package server.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import comune.InterfacciaRmi;


/**
 *	Classe che implementa i metodi che il client puo' richiamare tramite l'interfaccia con rmi.
 */
public class Implementazione extends UnicastRemoteObject implements InterfacciaRmi {

	private static final long serialVersionUID = 8791232638865094305L;

	/**
	 * Costruttore che richiama quello della superclasse.
	 * @throws RemoteException
	 */
	public Implementazione() throws RemoteException {
        super();
    }

	@Override
    public int somma(int a, int b)  throws RemoteException {
        System.out.println("Sommo " + a + " e " + b);
       return a+b;
    }

}
