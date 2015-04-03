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

package server.modellodati;

/**
 * Interfaccia per gestire Vegetazione e Carogna.
 * E' utile per rifersi ad un generico Occupante delle cella
 * che differisce da un Organismo (vegetazione, carogna, dinosauro), in quanto non
 * contempla la classe Dinosauro. Infatti, un Occupante delle cella NON puo' essere
 * un Dinosaro (Erbivoro e/o Carnivoro).
 * Di conseguenza, Occupante e' implementato SOLO da Carogna e Vegetazione.
 */
public interface Occupante {

}
