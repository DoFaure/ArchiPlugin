package plugins.application;

import java.util.List;

import appli.interfaces.IApp;
import appli.interfaces.IProductFactory;
import appli.models.Products;
import appli.plateform.Loader;
import plugins.displays.Display1;

public class App implements IApp{
	
	private Loader loader;
	
	private Display1 display1;
	private IProductFactory productFactory;
	
	@Override
	public void run() {
		List<Products> farmerProducts = this.productFactory.createFarmerProducts();
		this.display1.displayProducts(farmerProducts);

	}
	@Override
	public void setLoader(Loader loader) {
		this.loader = loader;
		
	}
	@Override
	public Loader getLoader() {
		return this.loader;
		
	}
	
	
	
	
}
