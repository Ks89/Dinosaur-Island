package isoladinosauri.modellodati;

public abstract class Organismo {
	
	protected int posX;
	protected int posY;
	protected int energiaMax;
	protected int energia;
	/**
	 * @return the energiaMax
	 */
	public int getEnergiaMax() {
		return energiaMax;
	}
	/**
	 * @param energiaMax the energiaMax to set
	 */
	public void setEnergiaMax(int energiaMax) {
		this.energiaMax = energiaMax;
	}
	/**
	 * @return the energia
	 */
	public int getEnergia() {
		return energia;
	}
	/**
	 * @param energia the energia to set
	 */
	public void setEnergia(int energia) {
		this.energia = energia;
	}
	/**
	 * @return the posX
	 */
	public int getPosX() {
		return posX;
	}
	/**
	 * @param posX the posX to set
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}
	/**
	 * @return the posY
	 */
	public int getPosY() {
		return posY;
	}
	/**
	 * @param posY the posY to set
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	

}
