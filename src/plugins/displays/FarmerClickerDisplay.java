package plugins.displays;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
import appli.interfaces.IMonitor;
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
	
	// Boolean to know if have already load the Co2 prodcuts 
	private static boolean isProductCo2 = false;

	
	public FarmerClickerDisplay(IProductsSimpleFactory productSimpleFactory, IConsumableFactory consumableFactory) {
		super();
		FarmerClickerDisplay.nbWheat = 0;
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
		c.insets = new Insets(3,3,3,3);
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

		c.gridy = NB_MAX_ITEMS + 3;
		farmerPanel.add(Box.createVerticalStrut(25), c); // Puis on saute une ligne pour faire une séparation dans l'affichage
		
		// Set the position and dimension of the frame
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
		switch(products.get(0).getClass().getSimpleName()) { // Chargement du label de la catégorie de l'objet
			case "ProductsSimple":
				productsLabel = new JLabel("Produits :");
				break;
			case "ProductsCo2":
				productsLabel = new JLabel("Produits écolos :");
				break;
			default: break;
		}
		
		c.gridx = objectXDisplay;
		c.gridy = objectYDisplay;
		this.farmerPanel.add(productsLabel, c); // Affichage du label de la catégorie de l'objet
		
		objectXDisplay++;
		
		if (products != null && !products.isEmpty()) {
			for (Object productObject : products) { // Pour chaque produit
				
				if (objectXDisplay > NB_MAX_OBJECTS_BY_LINE) { // On choisit son emplacement d'affichage, en respectant le nombre max d'objets par ligne
					objectXDisplay = 1;
					objectYDisplay += 2;
				}
				
				Products product = (Products) productObject; 
				
				String messageButton = product.getPrice() + "$ : " + product.getLabel() + " (+ " + (int) product.getWheatAugmentation() + "/c";
				
				switch(products.get(0).getClass().getSimpleName()) { // Selon le type du produit (Simple ou Co2), on affiche des infos différentes sur les boutons d'achat du produit
					case "ProductsSimple":
						messageButton += " | +" +((ProductsSimple) product).getCo2Production() + "co2)";
						break;
					case "ProductsCo2":
						messageButton += " | -" +((ProductsCo2) product).getCo2Reduction() + "co2)";
						break;
					default: break;
				}

				JButton buttonProduct = new JButton(messageButton); // On affiche le bouton d'achat du produit
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay;
				this.farmerPanel.add(buttonProduct, c);
				
				JProgressBar progressBar = new JProgressBar(0, product.getPrice()); // On affiche la progressBar d'achat du produit
				progressBar.setValue(nbWheat); 
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay + 1;
				this.farmerPanel.add(progressBar, c);
				listProgressBar.add(progressBar);
				
				buttonProduct.addActionListener(new ActionListener() { // Lorsque l'on achète un produit
					@Override
					public void actionPerformed(ActionEvent e) {
						if (nbWheat >= product.getPrice()) { // Si on a assez d'argent pour le produit
							nbWheatByClick += product.getWheatAugmentation();
							nbWheat -= product.getPrice();
							
							if(product.getClass().getSimpleName().equals(ProductsSimple.class.getSimpleName()) ) { // Si c'est un Produit Simple
								if(((ProductsSimple) product).getCo2Production() > 0) { // Et que le produit acheté est un produit qui crée du Co2
									addCo2Products();
									nbCo2 += ((ProductsSimple) product).getCo2Production(); // Alors on affiche les produits Co2 et on ajoute la quantité de Co2
								}
									
							}
							if(product.getClass().getSimpleName().equals(ProductsCo2.class.getSimpleName()) ) { // Si c'est un Produit Co2
								nbCo2 -= ((ProductsCo2) product).getCo2Reduction(); // Alors on affiche les produits Co2 et on soustrait la quantité de Co2
							}
							updateComponents(); // Et enfin, on met à jour les progressBar
						}
						
					}
				});
				objectXDisplay++; // Puis on incrémente une variable qui indique que le produit suivant sera affiché à droite de celui-ci
			}
		}
		
		objectXDisplay = 0;
		objectYDisplay += 2;

		c.gridy = objectYDisplay;
		farmerPanel.add(Box.createVerticalStrut(25), c); // Puis on saute une ligne pour faire une séparation dans l'affichage
		c.gridy = objectYDisplay++;
	}

	@Override
	public void displayConsumables(List<Consumables> consumables) {
		JLabel consumablesLabel = new JLabel("Consommables :");
		c.gridx = objectXDisplay;
		c.gridy = objectYDisplay;
		this.farmerPanel.add(consumablesLabel, c); // Affichage du label de la catégorie de l'objet
		
		objectXDisplay++;
		
		if (consumables != null && !consumables.isEmpty()) {
			for (Consumables consumable : consumables) { // Pour chaque consommable
				if (objectXDisplay > NB_MAX_OBJECTS_BY_LINE) { // On choisit son emplacement d'affichage, en respectant le nombre max d'objets par ligne
					objectXDisplay = 1;
					objectYDisplay += 2;
				}
				
				JButton buttonConsumable = new JButton(consumable.getPrice() + "$ : " + consumable.getLabel() + " (+ " + (int) consumable.getWheatAugmentation() + "/s | " + (int) consumable.getDuration() + "s)");
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay;
				this.farmerPanel.add(buttonConsumable, c); // ON affiche le bouton du consommable
				
				JProgressBar progressBar = new JProgressBar(0, consumable.getPrice());
				progressBar.setValue(nbWheat); 
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay + 1;
				this.farmerPanel.add(progressBar, c);
				listProgressBar.add(progressBar); // ON affiche la progressBar du consommable

				buttonConsumable.addActionListener(new ActionListener() { // Si on achète le consommable
					@Override
					public void actionPerformed(ActionEvent e) {
						if (nbWheat >= consumable.getPrice()) { // Si on a assez d'argent
							nbWheat -= consumable.getPrice();
							consumable.startTimer(); // Alors on lance le chronomètre et la méthode ajoute toute seule le blé toutes les secondes
							updateComponents(); // Puis on met à jour les progressBar des objets
						}
					}
				});				
				objectXDisplay++;// Puis on incrémente une variable qui indique que le produit suivant sera affiché à droite de celui-ci
			}
		}
		
		objectXDisplay = 0;
		objectYDisplay += 2;

		c.gridy = objectYDisplay;
		farmerPanel.add(Box.createVerticalStrut(25), c); // Puis on saute une ligne pour faire une séparation dans l'affichage
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
		if(isProductCo2 == false) {
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
	
	public static void updateComponents() {
//		if(nbCo2 >= NB_CO2_MAX) {
//			isProductCo2 = false;
//			nbCo2 = 0;
//			nbWheat = 0;
//		}
		// Update the value of each progress bar
		nbWheatText.setText("Blé :" + Integer.toString(FarmerClickerDisplay.nbWheat)+ "$");
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

	@Override
	public void setFarmerPanel(JPanel farmerPanel) {
		// TODO Auto-generated method stub
		
	}


}
