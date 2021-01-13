package appli.interfaces;

import java.util.List;

import appli.models.Products;
import appli.models.ProductsSimple;

public interface IProductsSimpleFactory {
	public List<ProductsSimple> createFarmerProductsSimple();
}
