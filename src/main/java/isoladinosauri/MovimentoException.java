package isoladinosauri;

/**
 * Classe per gestire le eccezioni sul movimento di un Dinosauro.
 */
public class MovimentoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum Causa {MORTE,DESTINAZIONEERRATA}; 
	private Causa causa;

	public MovimentoException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MovimentoException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MovimentoException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MovimentoException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Costruttore della classe MovimentoException che inizializza la causa che ha sollevato l'eccezione.
	 * @param causa Tipo enumerativo che rappresenta la causa che ha soolevato l'eccezione. Puo' essere: MORTE o DESTINAZIONEERRATA.
	 */
	public MovimentoException(Causa causa) {
		this.causa = causa;
	}
	
	/**
	 * @return Tipo enumerativo che rappresenta la causa che ha soolevato l'eccezione. Puo' essere: MORTE o DESTINAZIONEERRATA.
	 */
	public Causa getCausa() {
		return causa;
	}

}
