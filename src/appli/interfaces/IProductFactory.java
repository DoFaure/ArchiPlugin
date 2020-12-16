package appli.interfaces;

import java.util.List;

import appli.models.Products;

public interface IProductFactory {
	
	public List<Products> createFarmerProducts();
	public List<Products> createBrewerProducts(); 
}
