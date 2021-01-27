package appli.models;

public abstract class Products {
	
	private int price;
	private double wheatAugmentation;
	private String label;
	
	/**
	 * Constructor
	 * @param price
	 * @param wheatAugmentation
	 * @param label
	 */
	
	public Products(int price, double wheatAugmentation, String label) {
		super();
		this.price = price;
		this.wheatAugmentation = wheatAugmentation;
		this.label = label;
	}
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
	 * @return the wheatAugmentation
	 */
	public double getWheatAugmentation() {
		return wheatAugmentation;
	}
	/**
	 * @param wheatAugmentation the wheatAugmentation to set
	 */
	public void setWheatAugmentation(double wheatAugmentation) {
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
	
	@Override
	public String toString() {
		return "Products [price=" + price + ", wheatAugmentation=" + wheatAugmentation + ", label=" + label + "]";
	}
	
	
	
	
	
}
