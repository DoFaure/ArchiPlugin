package appli.models;

public class ProductsSimple extends Products {
	
	private int co2Production;
	
	public ProductsSimple(int price, double wheatAugmentation, String label, int co2Production) {
		super(price, wheatAugmentation, label);
		this.co2Production = co2Production;
	}

	public int getCo2Production() {
		return co2Production;
	}

	public void setCo2Production(int co2Production) {
		this.co2Production = co2Production;
	}

}
