package plugins.displays;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import appli.interfaces.IConsumableFactory;
import appli.interfaces.IFarmerClickerDisplay;
import appli.interfaces.IProductCo2Factory;
import appli.interfaces.IProductsSimpleFactory;
import appli.models.Consumables;
import appli.models.Products;
import appli.models.ProductsSimple;
import appli.models.ProductsCo2;
import appli.plateform.Loader;
import plugins.factories.ConsumableFactory;
import plugins.factories.ProductsSimpleFactory;

public class FarmerClickerDisplay implements IFarmerClickerDisplay {
	
	private IProductsSimpleFactory productSimpleFactory;
	private IConsumableFactory consumableFactory;

	public static int nbWheat;
	public static int nbCo2;
	private int nbWheatByClick;
	
	// Window farmerClicker
	private JFrame farmerFrame; 
	private JPanel farmerPanel;
	private GridBagConstraints c;

	private final int NB_MAX_OBJECTS_BY_LINE = 3;
	private final int NB_MAX_ITEMS = 100;
	private static final int NB_CO2_MAX = 1000;
	private int objectXDisplay = 0;
	private int objectYDisplay = 0;
	
	// show number of wheat
	private static JTextField nbWheatText; 
	// show number of co2
	private static JTextField nbCo2Text; 
	private static ArrayList<JProgressBar> listProgressBar = new ArrayList<JProgressBar>();
	private static JProgressBar co2ProgressBar = new JProgressBar();
	
	private static boolean isProductCo2 = false;

	public FarmerClickerDisplay(IProductsSimpleFactory productSimpleFactory, IConsumableFactory consumableFactory) {
		super();
		FarmerClickerDisplay.nbWheat = 0;
		this.nbWheatByClick = 1000;
		this.productSimpleFactory = productSimpleFactory;
		this.consumableFactory = consumableFactory;
		initialisationDisplay();
	}
	
	public void initialisationDisplay() {
		this.isProductCo2 = false;
		
		this.farmerFrame = new JFrame();
		this.farmerPanel = new JPanel();
		this.farmerPanel.setLayout(new GridBagLayout());
		
		this.c = new GridBagConstraints();
		c.insets = new Insets(3,3,3,3);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		this.farmerFrame.add(farmerPanel);
		farmerFrame.setTitle("Farmer Clicker");
		displayProducts(productSimpleFactory.createFarmerProductsSimple());
		displayConsumables(consumableFactory.createFarmerConsumables());
		nbWheatText = new JTextField("Wheat :" + Integer.toString(FarmerClickerDisplay.nbWheat) + "$");
		nbWheatText.setHorizontalAlignment(JTextField.CENTER);
		
		nbWheatText.setEditable(false);
		c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
		c.gridy = NB_MAX_ITEMS;
		this.farmerPanel.add(nbWheatText, c);
		
		
		
		clickerEvent();
		farmerFrame.setPreferredSize(new Dimension(1200, 1000));
		farmerFrame.setLocation(200, 150); // window position at launch
		farmerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // action launched when exit clicked
		farmerFrame.pack();
		farmerFrame.setVisible(true);
	}

	@Override
	public void displayProducts(List<?> products) {
		JLabel productsLabel = new JLabel();
		switch(products.get(0).getClass().getSimpleName()) {
			case "ProductsSimple":
				productsLabel = new JLabel("Produits :");
				break;
			case "ProductsCo2":
				productsLabel = new JLabel("Produits ecolos :");
				break;
			default: break;
		}
		System.out.println(products.get(0).getClass().getSimpleName());
		
		c.gridx = objectXDisplay;
		c.gridy = objectYDisplay;
		this.farmerPanel.add(productsLabel, c);
		
		objectXDisplay++;
		
		if (products != null && !products.isEmpty()) {
			for (Object productObject : products) {
				if (objectXDisplay > NB_MAX_OBJECTS_BY_LINE) {
					objectXDisplay = 1;
					objectYDisplay += 2;
				}
				
				Products product =  (Products) productObject; 
				String messageButton = product.getPrice() + "$ : " + product.getLabel() + " (+ " + (int) product.getWheatAugmentation() + "/c";
				switch(products.get(0).getClass().getSimpleName()) {
					case "ProductsSimple":
						messageButton += " | +" +((ProductsSimple) product).getCo2Production() + "co2)";
						break;
					case "ProductsCo2":
						messageButton += " | -" +((ProductsCo2) product).getCo2Reduction() + "co2)";
						break;
					default: break;
				}

				JButton buttonProduct = new JButton(messageButton);
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay;
				this.farmerPanel.add(buttonProduct, c);
				
				JProgressBar progressBar = new JProgressBar(0, product.getPrice());
				progressBar.setValue(nbWheat); 
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay + 1;
				this.farmerPanel.add(progressBar, c);
				listProgressBar.add(progressBar);
				
				buttonProduct.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (nbWheat >= product.getPrice()) {
							nbWheatByClick += product.getWheatAugmentation();
							nbWheat -= product.getPrice();
							if(product.getClass().getSimpleName().equals(ProductsSimple.class.getSimpleName()) ) {
								if(((ProductsSimple) product).getCo2Production() > 0) {
									addCo2Products();
									nbCo2 += ((ProductsSimple) product).getCo2Production();
								}
									
							}
							if(product.getClass().getSimpleName().equals(ProductsCo2.class.getSimpleName()) ) {
								nbCo2 -= ((ProductsCo2) product).getCo2Reduction();
							}
							updateComponents();
						}
						
					}
				});
				System.out.println(product.toString());
				
				objectXDisplay++;
			}
		}
		
		objectXDisplay = 0;
		objectYDisplay += 2;

		c.gridy = objectYDisplay;
		farmerPanel.add(Box.createVerticalStrut(25), c);
		c.gridy = objectYDisplay++;
	}

	@Override
	public void displayConsumables(List<Consumables> consumables) {
		JLabel consommablesLabel = new JLabel("Consommables :");
		c.gridx = objectXDisplay;
		c.gridy = objectYDisplay;
		this.farmerPanel.add(consommablesLabel, c);
		
		objectXDisplay++;
		
		if (consumables != null && !consumables.isEmpty()) {
			for (Consumables consumable : consumables) {
				if (objectXDisplay > NB_MAX_OBJECTS_BY_LINE) {
					objectXDisplay = 1;
					objectYDisplay += 2;
				}
				
				JButton buttonConsommable = new JButton(consumable.getPrice() + "$ : " + consumable.getLabel() + " (+ " + (int) consumable.getWheatAugmentation() + "/s | " + (int) consumable.getDuration() + "s)");
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay;
				this.farmerPanel.add(buttonConsommable, c);
				
				JProgressBar progressBar = new JProgressBar(0, consumable.getPrice());
				progressBar.setValue(nbWheat); 
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay + 1;
				this.farmerPanel.add(progressBar, c);
				listProgressBar.add(progressBar);

				buttonConsommable.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (nbWheat >= consumable.getPrice()) {
							nbWheat -= consumable.getPrice();
							consumable.startTimer();
							updateComponents();
						}
					}
				});
				System.out.println(consumable.toString());
				
				objectXDisplay++;
			}
		}
		
		objectXDisplay = 0;
		objectYDisplay += 2;

		c.gridy = objectYDisplay;
		farmerPanel.add(Box.createVerticalStrut(25), c);
		c.gridy = objectYDisplay++;
	}

	public void clickerEvent() {
		this.farmerPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				nbWheat += nbWheatByClick;
				updateComponents();
			}
		});
	}
	
	public void addCo2Products() {
		if(isProductCo2 == false) {
			IProductCo2Factory productCo2Factory = (IProductCo2Factory) Loader.loadPlugin("product-co2-factory");
			displayProducts(productCo2Factory.createFarmerProductsCo2());
			c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
			c.gridy = NB_MAX_ITEMS;
			this.farmerPanel.add(nbWheatText, c);
			
			nbCo2Text = new JTextField("Co2 :" + Integer.toString(FarmerClickerDisplay.nbCo2));
			nbCo2Text.setHorizontalAlignment(JTextField.CENTER);
			
			nbCo2Text.setEditable(false);
			c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
			c.gridy = NB_MAX_ITEMS + 1;
			this.farmerPanel.add(nbCo2Text, c);
			
			co2ProgressBar = new JProgressBar(0, NB_CO2_MAX);
			co2ProgressBar.setValue(nbCo2); 
			c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
			c.gridy = NB_MAX_ITEMS + 2;
			this.farmerPanel.add(co2ProgressBar, c);
			
			SwingUtilities.updateComponentTreeUI(farmerFrame);
			isProductCo2 = true;
		}
	}
	
	public static void updateComponents() {
//		if(nbCo2 >= NB_CO2_MAX) {
//			isProductCo2 = false;
//			nbCo2 = 0;
//			nbWheat = 0;
//		}
		nbWheatText.setText("Wheat :" + Integer.toString(FarmerClickerDisplay.nbWheat)+ "$");
		for (JProgressBar progressBar : listProgressBar) {
			progressBar.setValue(nbWheat);
		}
		if(nbCo2 > 0){
			nbCo2Text.setText("Co2 :" + Integer.toString(FarmerClickerDisplay.nbCo2) + "/" + NB_CO2_MAX);
			co2ProgressBar.setValue(nbCo2);
		}
	}
	
	/**
	 * @return the productSimpleFactory
	 */
	public ProductsSimpleFactory getProductFactory() {
		return (ProductsSimpleFactory) productSimpleFactory;
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
