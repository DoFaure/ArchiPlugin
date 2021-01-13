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
		productsFarmer.add(new Products(25, 2 , "Brouette"));
		productsFarmer.add(new Products(75, 4 , "Tracteur"));
		productsFarmer.add(new Products(150, 8 , "Serre"));
		productsFarmer.add(new Products(200, 10 , "Moissonneuse"));
		productsFarmer.add(new Products(1500, 100 , "Ferme du turfu"));
		
		
		return productsFarmer;
	}

	@Override
	public List<Products> createBrewerProducts() {
		return null;
	}

}
