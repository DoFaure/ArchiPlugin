package plugins.factories;

import java.util.ArrayList;
import java.util.List;

import appli.interfaces.IProductFactory;
import appli.models.Products;

public class ProductFactory implements IProductFactory {
	
	@Override
	public List<Products> createFarmerProducts() {
		
		List<Products> productsFarmer = new ArrayList<Products>();
		
		productsFarmer.add(new Products(5, 1 , "Rateau"));
		productsFarmer.add(new Products(200, 10 , "Moissonneuse"));
		productsFarmer.add(new Products(75, 4 , "Tracteur"));
		
		
		return productsFarmer;
	}

	@Override
	public List<Products> createBrewerProducts() {
		return null;
	}

}
