package isoladinosauri.modellodati;

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
