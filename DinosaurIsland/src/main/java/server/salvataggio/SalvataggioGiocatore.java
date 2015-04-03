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

package server.salvataggio;

/**
 *	Dato da salvare. Contiene la chiave e l'oggetto.
 */
public class SalvataggioGiocatore {
    
    private String chiave;
    private Object oggetto;
    
    public String getChiave() {
        return chiave;
    }
    public void setChiave(String chiave) {
        this.chiave = chiave;
    }
    public Object getOggetto() {
        return oggetto;
    }
    public void setOggetto(Object oggetto) {
        this.oggetto = oggetto;
    }
    
    

}
