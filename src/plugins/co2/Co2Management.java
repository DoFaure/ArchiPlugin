package plugins.co2;

import appli.interfaces.ICo2Management;
import appli.interfaces.IProductCo2Factory;

public class Co2Management implements ICo2Management {
	
	private IProductCo2Factory productCo2Factory;
	

	public Co2Management(IProductCo2Factory productCo2Factory) {
		this.productCo2Factory = productCo2Factory;
	}
	
	@Override
	public void checkCo2() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeProducts() {
		// TODO Auto-generated method stub
		
	}
	
	
}
