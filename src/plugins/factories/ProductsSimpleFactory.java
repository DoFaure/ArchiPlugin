package plugins.factories;

import java.util.ArrayList;
import java.util.List;

import appli.interfaces.IProductsSimpleFactory;
import appli.models.Products;
import appli.models.ProductsSimple;

public class ProductsSimpleFactory implements IProductsSimpleFactory {
	
	public List<ProductsSimple> createFarmerProductsSimple() {
		
		List<ProductsSimple> ProductsSimplesFarmer = new ArrayList<ProductsSimple>();

		ProductsSimplesFarmer.add(new ProductsSimple(5, 1 , "Rateau", 0));
		ProductsSimplesFarmer.add(new ProductsSimple(25, 2 , "Brouette", 0));
		ProductsSimplesFarmer.add(new ProductsSimple(75, 4 , "Tracteur", 10));
		ProductsSimplesFarmer.add(new ProductsSimple(150, 8 , "Serre", 50));
		ProductsSimplesFarmer.add(new ProductsSimple(200, 10 , "Moissonneuse", 100));
		ProductsSimplesFarmer.add(new ProductsSimple(1500, 100 , "Ferme du turfu", 500));
		
		
		return ProductsSimplesFarmer;
	}

}
