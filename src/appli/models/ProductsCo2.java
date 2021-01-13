package appli.models;

public class ProductsCo2 extends Products{
	
	private int co2Reduction;

	public ProductsCo2(int price, double wheatAugmentation, String label, int co2Reduction) {
		super(price, wheatAugmentation, label);
		this.co2Reduction = co2Reduction;
	}

	public int getCo2Reduction() {
		return co2Reduction;
	}

	public void setCo2Reduction(int co2Reduction) {
		this.co2Reduction = co2Reduction;
	}

	
}
