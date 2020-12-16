package plugins.application;

import java.util.List;

import appli.interfaces.IAppClicker;
import appli.interfaces.IConsumableFactory;
import appli.interfaces.IDisplay1;
import appli.interfaces.IProductFactory;
import appli.models.Consumables;
import appli.models.Products;
import appli.plateform.Loader;

public class AppClicker implements IAppClicker{
	
	private Loader loader;
	
	private IDisplay1 display1;
	private IProductFactory productFactory;
	private IConsumableFactory consumableFactory;
	
	public AppClicker(IDisplay1 display1, IProductFactory productFactory, IConsumableFactory consumableFactory ) {
		super();
		this.display1 = display1;
		this.productFactory = productFactory;
		this.consumableFactory = consumableFactory;
	}
	@Override
	public void run() {
		List<Products> farmerProducts = this.productFactory.createFarmerProducts();
		this.display1.displayProducts(farmerProducts);
		
		List<Consumables> farmerConsumables = this.consumableFactory.createFarmerConsumables();
		this.display1.displayConsumables(farmerConsumables);

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
