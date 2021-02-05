package plugins.displays;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import appli.interfaces.IConsumableFactory;
import appli.interfaces.IFarmerClickerDisplay;
import appli.interfaces.IGameManager;
import appli.interfaces.IProductCo2Factory;
import appli.interfaces.IProductsSimpleFactory;
import appli.models.Consumables;
import appli.models.Products;
import appli.models.ProductsCo2;
import appli.models.ProductsSimple;
import appli.plateform.Loader;
import plugins.factories.ConsumableFactory;
import plugins.factories.ProductsSimpleFactory;

public class FarmerClickerDisplay implements IFarmerClickerDisplay {
	// Factories we need to start the app
	private IProductsSimpleFactory productSimpleFactory;
	private IConsumableFactory consumableFactory;
	private IGameManager gameManager;

	// Nb Wheat and co2 produce by the user
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
	// List of progressBar for all the products and consumables
	private static ArrayList<JProgressBar> listProgressBar = new ArrayList<JProgressBar>();
	// ProgressBar of the co2 production
	private static JProgressBar co2ProgressBar = new JProgressBar();

	private JButton buttonSave;

	private JButton buttonCharge;

	// Boolean to know if have already load the Co2 prodcuts
	private static boolean isProductCo2 = false;

	public FarmerClickerDisplay(IProductsSimpleFactory productSimpleFactory, IConsumableFactory consumableFactory) {
		super();
		FarmerClickerDisplay.nbWheat = 0;
		FarmerClickerDisplay.nbCo2 = 0;
		this.nbWheatByClick = 1;
		this.productSimpleFactory = productSimpleFactory;
		this.consumableFactory = consumableFactory;
		initialisationDisplay();
	}

	public void initialisationDisplay() {

		// Instanciate the Frame and the Panel
		this.farmerFrame = new JFrame();
		this.farmerPanel = new JPanel();
		// Set the Frame in a GridBagLayout
		this.farmerPanel.setLayout(new GridBagLayout());

		// Create the GridBagLayout with an insets of 3, center horizontally
		this.c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.fill = GridBagConstraints.HORIZONTAL;

		// Initiate the Frame with a title and the basic products and consumables
		this.farmerFrame.add(farmerPanel);
		farmerFrame.setTitle("Farmer Clicker");
		displayProducts(productSimpleFactory.createFarmerProductsSimple());
		displayConsumables(consumableFactory.createFarmerConsumables());

		// Add a TextField for the nb of wheat of the user
		nbWheatText = new JTextField("Blé :" + Integer.toString(FarmerClickerDisplay.nbWheat) + "$");
		nbWheatText.setHorizontalAlignment(JTextField.CENTER);
		nbWheatText.setEditable(false);
		c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
		c.gridy = NB_MAX_ITEMS;
		this.farmerPanel.add(nbWheatText, c);
		
		// Display save button
		buttonSave = new JButton("Sauvegarder la partie"); 
		c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
		c.gridy = NB_MAX_ITEMS + 4;
		this.farmerPanel.add(buttonSave, c);
		this.saveGame();
		
		// Display charge button
		buttonCharge = new JButton("Charger la dernière partie"); 
		c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
		c.gridy = NB_MAX_ITEMS + 5;
		this.farmerPanel.add(buttonCharge, c);
		this.loadGame();
		
		// We jump a line to make a separation 
		c.gridy = NB_MAX_ITEMS + 3;
		farmerPanel.add(Box.createVerticalStrut(25), c); 

		// Set the position and dimension of the frame
		clickerEvent();
		farmerFrame.setPreferredSize(new Dimension(1200, 1000));
		farmerFrame.setLocation(200, 150); // window position at launch
		farmerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // action launched when exit clicked
		farmerFrame.pack();
		farmerFrame.setVisible(true);
	}

	public void loadGame() {
		// Triggered when a click on buttonCharge
		buttonCharge.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] array = new int[3];
				if (gameManager == null) {
					gameManager = (IGameManager) Loader.loadPlugin("game-manager");
					try {
						array = gameManager.loadGame();
					} catch (IOException ex) {
						showDialog();
					}
				} else {
					try {
						array = gameManager.loadGame();
					} catch (IOException ex) {
						showDialog();
					}
				}
				nbWheat = array[0];
				nbWheatByClick = array[1];
				nbCo2 = array[2];
				updateComponents();
			}
		});
	}
	
	/**
	 * Show a dialog error 
	 */
	public void showDialog() {
		String frameTitle = "Error";
		final JDialog frame = new JDialog(farmerFrame, frameTitle, true);
		final JPanel dialogPanel = new JPanel();
        final JLabel error  = new JLabel("Aucune sauvegarde trouvée"); 
		dialogPanel.add(error);
		frame.getContentPane().add(dialogPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}

	public void saveGame() {
		// Triggered when we click on buttonSave
		buttonSave.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameManager == null) {
					gameManager = (IGameManager) Loader.loadPlugin("game-manager");
					gameManager.saveGame(nbWheat, nbWheatByClick, nbCo2);
				} else {
					gameManager.saveGame(nbWheat, nbWheatByClick, nbCo2);
				}
			}
		});
	}

	@Override
	public void displayProducts(List<?> products) {
		JLabel productsLabel = new JLabel();
		// Charge the category label of the object
		switch (products.get(0).getClass().getSimpleName()) { 
			case "ProductsSimple":
				productsLabel = new JLabel("Produits :");
				break;
			case "ProductsCo2":
				productsLabel = new JLabel("Produits écolos :");
				break;
			default:
				break;
		}

		c.gridx = objectXDisplay;
		c.gridy = objectYDisplay;
		
		// Display the category label of the object 
		this.farmerPanel.add(productsLabel, c); 

		objectXDisplay++;

		if (products != null && !products.isEmpty()) {
			for (Object productObject : products) { 
				
				// We choose location of the display by respecting the max objects per line
				if (objectXDisplay > NB_MAX_OBJECTS_BY_LINE) { 
					objectXDisplay = 1;
					objectYDisplay += 2;
				}

				Products product = (Products) productObject;

				String messageButton = product.getPrice() + "$ : " + product.getLabel() + " (+ " + (int) product.getWheatAugmentation() + "/c | " ;
				
				// According to the product type (Simple or Co2) we display differents informations on on the buttonProduct
				switch (products.get(0).getClass().getSimpleName()) { 
				case "ProductsSimple":
					messageButton += "+ " + ((ProductsSimple) product).getCo2Production() + "co2)";
					break;
				case "ProductsCo2":
					messageButton += "-" +  ((ProductsCo2) product).getCo2Reduction() + "co2)";
					break;
				default:
					break;
				}
				
				// Display the product button 
				JButton buttonProduct = new JButton(messageButton); 
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay;
				this.farmerPanel.add(buttonProduct, c);
				
				// Display the progressBar of the product
				JProgressBar progressBar = new JProgressBar(0, product.getPrice()); 
				progressBar.setValue(nbWheat);
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay + 1;
				this.farmerPanel.add(progressBar, c);
				listProgressBar.add(progressBar);
				
				// When we buy a product
				buttonProduct.addActionListener(new ActionListener() { 
					@Override
					public void actionPerformed(ActionEvent e) {
						// If we have enough money
						if (nbWheat >= product.getPrice()) { 
							nbWheatByClick += product.getWheatAugmentation();
							nbWheat -= product.getPrice();
							// If it's a simple product
							if (product.getClass().getSimpleName().equals(ProductsSimple.class.getSimpleName())) { 
								// And the product create Co2
								if (((ProductsSimple) product).getCo2Production() > 0) { 
									addCo2Products();
									// Then we display Co2 products and we change the number of Co2
									nbCo2 += ((ProductsSimple) product).getCo2Production(); 
								}

							}
							// Si c'est un Produit Co2
							if (product.getClass().getSimpleName().equals(ProductsCo2.class.getSimpleName())) { 
								// Then we change the number of Co2
								nbCo2 -= ((ProductsCo2) product).getCo2Reduction(); 
							}
							// And we display it
							updateComponents();
						}

					}
				});
				//Then we increment a variable which means that the next product will be displayed at the right oh this one
				objectXDisplay++;
			}
		}

		objectXDisplay = 0;
		objectYDisplay += 2;

		c.gridy = objectYDisplay;
		// We jump a line to do a separation between different lines
		farmerPanel.add(Box.createVerticalStrut(25), c);
		c.gridy = objectYDisplay++;
	}

	@Override
	public void displayConsumables(List<Consumables> consumables) {
		// Display the category label of the object 
		JLabel consumablesLabel = new JLabel("Consommables :");
		c.gridx = objectXDisplay;
		c.gridy = objectYDisplay;
		this.farmerPanel.add(consumablesLabel, c);

		objectXDisplay++;

		if (consumables != null && !consumables.isEmpty()) {
			for (Consumables consumable : consumables) { // For each consumable
				if (objectXDisplay > NB_MAX_OBJECTS_BY_LINE) {
					// We choose location of the display by respecting the max objects per line
					objectXDisplay = 1;
					objectYDisplay += 2;
				}

				// Display the consumable button 
				JButton buttonConsumable = new JButton(consumable.getPrice() + "$ : " + consumable.getLabel() + " (+ "
						+ (int) consumable.getWheatAugmentation() + "/s | " + (int) consumable.getDuration() + "s)");
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay;
				this.farmerPanel.add(buttonConsumable, c);

				// Display the progressBar of the consumable
				JProgressBar progressBar = new JProgressBar(0, consumable.getPrice());
				progressBar.setValue(nbWheat);
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay + 1;
				this.farmerPanel.add(progressBar, c);
				listProgressBar.add(progressBar);

				// When we buy a consumable
				buttonConsumable.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// If we have enough money
						if (nbWheat >= consumable.getPrice()) {
							// Then we start a stopwatch and the method add wheat every seconds
							nbWheat -= consumable.getPrice();
							consumable.startTimer();
							// And we update progessBars
							updateComponents();
						}
					}
				});
				//Then we increment a variable which means that the next product will be displayed at the right oh this one
				objectXDisplay++;
			}
		}

		objectXDisplay = 0;
		objectYDisplay += 2;

		c.gridy = objectYDisplay;
		// We jump a line to do a separation between different lines
		farmerPanel.add(Box.createVerticalStrut(25), c); 
		c.gridy = objectYDisplay++;
	}

	public void clickerEvent() {
		this.farmerPanel.addMouseListener(new MouseAdapter() {
			// Increase the wheat to each click
			public void mousePressed(MouseEvent me) {
				nbWheat += nbWheatByClick;
				updateComponents();
			}
		});
	}

	public void addCo2Products() {
		if (isProductCo2 == false) {
			// Load the new consummable ans add it to the frame
			IProductCo2Factory productCo2Factory = (IProductCo2Factory) Loader.loadPlugin("product-co2-factory");
			displayProducts(productCo2Factory.createFarmerProductsCo2());
			c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
			c.gridy = NB_MAX_ITEMS;
			this.farmerPanel.add(nbWheatText, c);

			// Instanciate the new textField for nbCo2 and add it to the frame
			nbCo2Text = new JTextField("Co2 :" + Integer.toString(FarmerClickerDisplay.nbCo2));
			nbCo2Text.setHorizontalAlignment(JTextField.CENTER);
			nbCo2Text.setEditable(false);
			c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
			c.gridy = NB_MAX_ITEMS + 1;
			this.farmerPanel.add(nbCo2Text, c);

			// Instanciate the progressBar corresponding to the co2 of the user
			co2ProgressBar = new JProgressBar(0, NB_CO2_MAX);
			co2ProgressBar.setValue(nbCo2);
			c.gridx = (NB_MAX_OBJECTS_BY_LINE + 1) / 2;
			c.gridy = NB_MAX_ITEMS + 2;
			this.farmerPanel.add(co2ProgressBar, c);

			// Reload the Frame
			SwingUtilities.updateComponentTreeUI(farmerFrame);
			isProductCo2 = true;
		}
	}
	public static void updateWheat() {
		// Update the value of each progress bar
		nbWheatText.setText("Blé :" + Integer.toString(FarmerClickerDisplay.nbWheat) + "$");
		for (JProgressBar progressBar : listProgressBar) {
			progressBar.setValue(nbWheat);
		}
	}

	// Change nb Co2 and nb Wheat displayed 
	public void updateComponents() {
		if (nbCo2 > 0) {
			// Detect if Co2_MAX is reached
			if (nbCo2 >= NB_CO2_MAX) {
				nbCo2 = NB_CO2_MAX / 2;
				nbWheat = 0;
				nbWheatByClick = 1;
			} else {
				// Add Co2 products one time only
				if (isProductCo2 == false) {
					addCo2Products();
				}
			}
			nbCo2Text.setText("Co2 :" + Integer.toString(FarmerClickerDisplay.nbCo2) + "/" + NB_CO2_MAX);
			co2ProgressBar.setValue(nbCo2);
		}
		updateWheat();
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

	@Override
	public void setFarmerPanel(JPanel farmerPanel) {
		// TODO Auto-generated method stub

	}

}
