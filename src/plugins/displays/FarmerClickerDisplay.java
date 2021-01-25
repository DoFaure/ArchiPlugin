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
		switch(products.get(0).getClass().getSimpleName()) { // Chargement du label de la cat�gorie de l'objet
			case "ProductsSimple":
				productsLabel = new JLabel("Produits :");
				break;
			case "ProductsCo2":
				productsLabel = new JLabel("Produits ecolos :");
				break;
			default: break;
		}
		
		c.gridx = objectXDisplay;
		c.gridy = objectYDisplay;
		this.farmerPanel.add(productsLabel, c); // Affichage du label de la cat�gorie de l'objet
		
		objectXDisplay++;
		
		if (products != null && !products.isEmpty()) {
			for (Object productObject : products) { // Pour chaque produit
				
				if (objectXDisplay > NB_MAX_OBJECTS_BY_LINE) { // On choisit son emplacement d'affichage, en respectant le nombre max d'objets par ligne
					objectXDisplay = 1;
					objectYDisplay += 2;
				}
				
				Products product = (Products) productObject; 
				
				String messageButton = product.getPrice() + "$ : " + product.getLabel() + " (+ " + (int) product.getWheatAugmentation() + "/c";
				
				switch(products.get(0).getClass().getSimpleName()) { // Selon le type du produit (Simple ou Co2), on affiche des infos diff�rentes sur les boutons d'achat du produit
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
				
				buttonProduct.addActionListener(new ActionListener() { // Lorsque l'on ach�te un produit
					@Override
					public void actionPerformed(ActionEvent e) {
						if (nbWheat >= product.getPrice()) { // Si on a assez d'argent pour le produit
							nbWheatByClick += product.getWheatAugmentation();
							nbWheat -= product.getPrice();
							
							if(product.getClass().getSimpleName().equals(ProductsSimple.class.getSimpleName()) ) { // Si c'est un Produit Simple
								if(((ProductsSimple) product).getCo2Production() > 0) { // Et que le produit achet� est un produit qui cr�e du Co2
									addCo2Products();
									nbCo2 += ((ProductsSimple) product).getCo2Production(); // Alors on affiche les produits Co2 et on ajoute la quantit� de Co2
								}
									
							}
							if(product.getClass().getSimpleName().equals(ProductsCo2.class.getSimpleName()) ) { // Si c'est un Produit Co2
								nbCo2 -= ((ProductsCo2) product).getCo2Reduction(); // Alors on affiche les produits Co2 et on soustrait la quantit� de Co2
							}
							updateComponents(); // Et enfin, on met � jour les progressBar
						}
						
					}
				});
				objectXDisplay++; // Puis on incr�mente une variable qui indique que le produit suivant sera affich� � droite de celui-ci
			}
		}
		
		objectXDisplay = 0;
		objectYDisplay += 2;

		c.gridy = objectYDisplay;
		farmerPanel.add(Box.createVerticalStrut(25), c); // Puis on saute une ligne pour faire une s�paration dans l'affichage
		c.gridy = objectYDisplay++;
	}

	@Override
	public void displayConsumables(List<Consumables> consumables) {
		JLabel consommablesLabel = new JLabel("Consommables :");
		c.gridx = objectXDisplay;
		c.gridy = objectYDisplay;
		this.farmerPanel.add(consommablesLabel, c); // Affichage du label de la cat�gorie de l'objet
		
		objectXDisplay++;
		
		if (consumables != null && !consumables.isEmpty()) {
			for (Consumables consumable : consumables) { // Pour chaque consommable
				if (objectXDisplay > NB_MAX_OBJECTS_BY_LINE) { // On choisit son emplacement d'affichage, en respectant le nombre max d'objets par ligne
					objectXDisplay = 1;
					objectYDisplay += 2;
				}
				
				JButton buttonConsommable = new JButton(consumable.getPrice() + "$ : " + consumable.getLabel() + " (+ " + (int) consumable.getWheatAugmentation() + "/s | " + (int) consumable.getDuration() + "s)");
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay;
				this.farmerPanel.add(buttonConsommable, c); // ON affiche le bouton du consommable
				
				JProgressBar progressBar = new JProgressBar(0, consumable.getPrice());
				progressBar.setValue(nbWheat); 
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay + 1;
				this.farmerPanel.add(progressBar, c);
				listProgressBar.add(progressBar); // ON affiche la progressBar du consommable

				buttonConsommable.addActionListener(new ActionListener() { // Si on ach�te le consommable
					@Override
					public void actionPerformed(ActionEvent e) {
						if (nbWheat >= consumable.getPrice()) { // Si on a assez d'argent
							nbWheat -= consumable.getPrice();
							consumable.startTimer(); // Alors on lance le chronom�tre et la m�thode ajoute toute seule le bl� toutes les secondes
							updateComponents(); // Puis on met � jour les progressBar des objets
						}
					}
				});				
				objectXDisplay++;// Puis on incr�mente une variable qui indique que le produit suivant sera affich� � droite de celui-ci
			}
		}
		
		objectXDisplay = 0;
		objectYDisplay += 2;

		c.gridy = objectYDisplay;
		farmerPanel.add(Box.createVerticalStrut(25), c); // Puis on saute une ligne pour faire une s�paration dans l'affichage
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
