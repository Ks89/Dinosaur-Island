package comune;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *	Interfaccia per Rmi.
 */
public interface InterfacciaRmi extends Remote {
    
    /**
     * Metodo che esegue la somma tra 2 numeri, solo per test.
     * @param a int
     * @param b int
     * @return Un int rappresentante la somma.
     * @throws RemoteException
     */
    public int somma(int a, int b) throws RemoteException ;

}
