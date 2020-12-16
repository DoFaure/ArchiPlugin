package appli.interfaces;

import java.util.List;

import appli.models.Consumables;
import appli.models.Products;

public interface IDisplay1 {
	
	// TODO renommer la classe

	void displayProducts(List<Products> products);

	void displayConsumables(List<Consumables> consumables);

}