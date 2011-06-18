package gestioneeccezioni;

/**
 * Classe per gestire le eccezioni sulla deposizione di un Dinosauro.
 */
public class DeposizioneException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum Causa {MORTE,SQUADRACOMPLETA}; 
	private Causa causa;

	public DeposizioneException() {
		super();
	}

	public DeposizioneException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeposizioneException(String message) {
		super(message);
	}

	public DeposizioneException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Costruttore che inizializza la causa che ha sollevato l'eccezione.
	 * @param causa Tipo enumerativo che rappresenta la causa che ha sollevato l'eccezione.
	 */
	public DeposizioneException(Causa causa) {
		this.causa = causa;
	}
	
	/**
	 * @return Tipo enumerativo che rappresenta la causa che ha soolevato l'eccezione.
	 */
	public Causa getCausa() {
		return causa;
	}

}
