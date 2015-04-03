package gestioneeccezioni;

/**
 * Classe per gestire le eccezioni sul movimento di un Dinosauro.
 */
public class MovimentoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum Causa {MORTE,DESTINAZIONEERRATA,NESSUNVINCITORE,SCONFITTAATTACCANTE,SCONFITTAATTACCATO,ERRORE}; 
	private Causa causa;

	/**
	 * Richiama la superclasse.
	 */
	public MovimentoException() {
		super();
	}

	/**
	 * Richiama la superclasse.
	 * @param message
	 * @param cause
	 */
	public MovimentoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Richiama la superclasse.
	 * @param message
	 */
	public MovimentoException(String message) {
		super(message);
	}

	/**
	 * Richiama la superclasse.
	 * @param cause
	 */
	public MovimentoException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Costruttore della classe MovimentoException che inizializza la causa che ha sollevato l'eccezione.
	 * @param causa Tipo enumerativo che rappresenta la causa che ha sollevato l'eccezione.
	 */
	public MovimentoException(Causa causa) {
		this.causa = causa;
	}
	
	/**
	 * @return Tipo enumerativo che rappresenta la causa che ha soolevato l'eccezione.
	 */
	public Causa getCausa() {
		return causa;
	}

}