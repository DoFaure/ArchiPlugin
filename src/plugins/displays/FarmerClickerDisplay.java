package plugins.displays;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import appli.interfaces.IConsumableFactory;
import appli.interfaces.IFarmerClickerDisplay;
import appli.interfaces.IProductFactory;
import appli.models.Consumables;
import appli.models.Products;
import plugins.factories.ConsumableFactory;
import plugins.factories.ProductFactory;

public class FarmerClickerDisplay implements IFarmerClickerDisplay {
	
	private IProductFactory productFactory;
	private IConsumableFactory consumableFactory;
	private JPanel farmerPanel;
	private JLabel label;
	
	public FarmerClickerDisplay(IProductFactory productFactory, IConsumableFactory consumableFactory ) {
		super();
		this.farmerPanel = new JPanel();
		this.label = new JLabel();
		farmerPanel.add(label);
		this.productFactory = productFactory;
		this.consumableFactory = consumableFactory;
		
		displayProducts(productFactory.createFarmerProducts());
	}
	
	@Override
	public void displayProducts(List<Products> products) {
		if (products != null && !products.isEmpty()) {
			for (Products product : products) {
				label.setText(product.toString());
				System.out.println(product.toString());
			}
		}
	}

	@Override
	public void displayConsumables(List<Consumables> consumables) {
		if (consumables != null && !consumables.isEmpty()) {
			for (Consumables consumable : consumables) {
				System.out.println(consumable.toString());
			}
		}
	}

	/**
	 * @return the productFactory
	 */
	public ProductFactory getProductFactory() {
		return (ProductFactory) productFactory;
	}
	
	/**
	 * @return the consumableFactory
	 */
	public ConsumableFactory getConsumableFactory() {
		return (ConsumableFactory) consumableFactory;
	}

	/**
	 * @return the farmerPanel
	 */
	public JPanel getFarmerPanel() {
		return farmerPanel;
	}

	/**
	 * @param farmerPanel the farmerPanel to set
	 */
	public void setFarmerPanel(JPanel farmerPanel) {
		this.farmerPanel = farmerPanel;
	}


}
