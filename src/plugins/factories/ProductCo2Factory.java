package plugins.factories;

import java.util.ArrayList;
import java.util.List;

import appli.interfaces.IProductCo2Factory;
import appli.models.Products;
import appli.models.ProductsCo2;
import appli.models.ProductsSimple;

public class ProductCo2Factory implements IProductCo2Factory{

	@Override
	public List<ProductsCo2> createFarmerProductsCo2() {
		
		List<ProductsCo2> ProductsCo2Farmer = new ArrayList<ProductsCo2>();

		ProductsCo2Farmer.add(new ProductsCo2(25, 2 , "Recolteur d'eau de pluie", 25));
		ProductsCo2Farmer.add(new ProductsCo2(150, 8 , "Serre", 50));
		ProductsCo2Farmer.add(new ProductsCo2(200, 10 , "Lombricomposteur", 100));
		ProductsCo2Farmer.add(new ProductsCo2(1500, 100 , "Ferme ecolo", 500));
		
		
		return ProductsCo2Farmer;
	}


}
