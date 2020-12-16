package plugins.factories;

import java.util.ArrayList;
import java.util.List;

import appli.interfaces.IProductFactory;
import appli.models.Products;

public class ProductFactory implements IProductFactory {
	
	@Override
	public List<Products> createFarmerProducts() {
		
		List<Products> productsFarmer = new ArrayList<Products>();
		
		productsFarmer.add(new Products(1, 0.1 , "Rateau"));
		productsFarmer.add(new Products(1, 0.9 , "Moissonneuse"));
		productsFarmer.add(new Products(1, 0.5 , "tracteur"));
		
		
		return productsFarmer;
	}

	@Override
	public List<Products> createBrewerProducts() {
		return null;
	}

}
