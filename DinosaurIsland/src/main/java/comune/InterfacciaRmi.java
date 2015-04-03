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
