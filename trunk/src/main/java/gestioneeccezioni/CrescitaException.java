package gestioneeccezioni;

/**
 * Classe per gestire le eccezioni sulla crescita di un Dinosauro.
 */
public class CrescitaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum Causa {MORTE,DIMENSIONEMASSIMA}; 
	private Causa causa;

	/**
	 * Richiama la superclasse.
	 */
	public CrescitaException() {
		super();
	}

	/**
	 * Richiama la superclasse.
	 * @param message
	 * @param cause
	 */
	public CrescitaException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Richiama la superclasse.
	 * @param message
	 */
	public CrescitaException(String message) {
		super(message);
	}

	/**
	 * Richiama la superclasse.
	 * @param cause
	 */
	public CrescitaException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Costruttore della classe CrescitaException che inizializza la causa che ha sollevato l'eccezione.
	 * @param causa Tipo enumerativo che rappresenta la causa che ha soolevato l'eccezione.
	 */
	public CrescitaException(Causa causa) {
		this.causa = causa;
	}
	
	/**
	 * @return Tipo enumerativo che rappresenta la causa che ha sollevato l'eccezione.
	 */
	public Causa getCausa() {
		return causa;
	}

}
