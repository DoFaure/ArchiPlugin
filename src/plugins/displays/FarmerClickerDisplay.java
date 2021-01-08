package plugins.displays;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
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
	
	private JFrame farmerFrame; // Window farmerClicker
	private JPanel farmerPanel;
	private JLabel label;
	
	public FarmerClickerDisplay(IProductFactory productFactory, IConsumableFactory consumableFactory ) {
		super();
		this.farmerFrame = new JFrame();
		this.farmerPanel = new JPanel();
		this.label = new JLabel();
		farmerPanel.add(this.label);
		this.productFactory = productFactory;
		this.consumableFactory = consumableFactory;
		
		displayProducts(productFactory.createFarmerProducts());
		farmerFrame.setPreferredSize(new Dimension(400, 400));
		farmerFrame.setLocation(700, 400); // window position at launch
		farmerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // action launched when exit clicked
		farmerFrame.pack();
		farmerFrame.setVisible(true);
	
	}
	
	@Override
	public void displayProducts(List<Products> products) {
		if (products != null && !products.isEmpty()) {
			for (Products product : products) {
				this.label.setText(product.toString());
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
