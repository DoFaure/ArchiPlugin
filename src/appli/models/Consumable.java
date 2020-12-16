package appli.models;

public class Consumable {
	private int price;
	private int duration;
	private float wheatAugmentation;
	private String label;
	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @return the wheatAugmentation
	 */
	public float getWheatAugmentation() {
		return wheatAugmentation;
	}
	/**
	 * @param wheatAugmentation the wheatAugmentation to set
	 */
	public void setWheatAugmentation(float wheatAugmentation) {
		this.wheatAugmentation = wheatAugmentation;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	public Consumable(int price, int duration, float wheatAugmentation, String label) {
		super();
		this.price = price;
		this.duration = duration;
		this.wheatAugmentation = wheatAugmentation;
		this.label = label;
	}
	
	
}
