package appli.interfaces;

import java.util.List;

import javax.swing.JPanel;

import appli.models.Consumables;
import appli.models.Products;
import plugins.factories.ConsumableFactory;
import plugins.factories.ProductsSimpleFactory;

public interface IFarmerClickerDisplay {
	
	// TODO renommer la classe

	void displayProducts(List<Products> products);

	void displayConsumables(List<Consumables> consumables);
	
	ProductsSimpleFactory getProductFactory();
	
	ConsumableFactory getConsumableFactory();
	
	JPanel getFarmerPanel(); 

	void setFarmerPanel(JPanel farmerPanel);

}