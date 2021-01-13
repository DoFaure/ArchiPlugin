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

import appli.interfaces.ICo2Management;
import appli.interfaces.IConsumableFactory;
import appli.interfaces.IFarmerClickerDisplay;
import appli.interfaces.IProductsSimpleFactory;
import appli.models.Consumables;
import appli.models.Products;
import plugins.factories.ConsumableFactory;
import plugins.factories.ProductsSimpleFactory;

public class FarmerClickerDisplay implements IFarmerClickerDisplay {

	private IProductsSimpleFactory productSimpleFactory;
	private IConsumableFactory consumableFactory;

	public static int nbWheat;
	private int nbWheatByClick;

	private JFrame farmerFrame; // Window farmerClicker
	private JPanel farmerPanel;
	private GridBagConstraints c;

	private final int NB_MAX_OBJECTS_BY_LINE = 3;
	private int objectXDisplay = 0;
	private int objectYDisplay = 0;
	
	private static JTextField nbWheatText; // show number of wheat
	private static ArrayList<JProgressBar> listProgressBar = new ArrayList<JProgressBar>();

	public FarmerClickerDisplay(IProductsSimpleFactory productSimpleFactory, IConsumableFactory consumableFactory, ICo2Management Co2management) {
		super();
		FarmerClickerDisplay.nbWheat = 0;
		this.nbWheatByClick = 1;
		this.productSimpleFactory = productSimpleFactory;
		this.consumableFactory = consumableFactory;
		initialisationDisplay();
	}
	
	public void initialisationDisplay() {
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
		nbWheatText = new JTextField("Wheat :" + Integer.toString(FarmerClickerDisplay.nbWheat));
		nbWheatText.setHorizontalAlignment(JTextField.CENTER);
		
		nbWheatText.setEditable(false);
		c.gridx = 1;
		c.gridy = objectYDisplay;
		c.gridwidth = NB_MAX_OBJECTS_BY_LINE;
		this.farmerPanel.add(nbWheatText, c);
		
		clickerEvent();
		farmerFrame.setPreferredSize(new Dimension(800, 800));
		farmerFrame.setLocation(0, 0); // window position at launch
		farmerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // action launched when exit clicked
		farmerFrame.pack();
		farmerFrame.setVisible(true);
	}

	@Override
	public void displayProducts(List<?> products) {
		JLabel productsLabel = new JLabel("Produits :");
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
				Products product = (Products) productObject; 
				
				JButton buttonProduct = new JButton(product.getLabel() + " (+ " + (int) product.getWheatAugmentation() + "/c)");
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay;
				this.farmerPanel.add(buttonProduct, c);
				
				JProgressBar progressBar = new JProgressBar(0, product.getPrice());
				progressBar.setValue(nbWheat); 
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay + 1;
				this.farmerPanel.add(progressBar, c);
				System.out.println(c.gridx);
				System.out.println(c.gridy);
				listProgressBar.add(progressBar);
	
				buttonProduct.addActionListener(new ActionListener() {
	
					@Override
					public void actionPerformed(ActionEvent e) {
						if (nbWheat >= product.getPrice()) {
							nbWheatByClick += product.getWheatAugmentation();
							nbWheat -= product.getPrice();
							
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
				
				JButton buttonConsommable = new JButton(consumable.getLabel() + " (+ " + (int) consumable.getWheatAugmentation() + "/s | " + (int) consumable.getDuration() + "s)");
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay;
				this.farmerPanel.add(buttonConsommable, c);
				
				JProgressBar progressBar = new JProgressBar(0, consumable.getPrice());
				progressBar.setValue(nbWheat); 
				c.gridx = objectXDisplay;
				c.gridy = objectYDisplay + 1;
				this.farmerPanel.add(progressBar, c);
				System.out.println(c.gridx);
				System.out.println(c.gridy);
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
	
	public static void updateComponents() {
		nbWheatText.setText("Wheat :" + Integer.toString(FarmerClickerDisplay.nbWheat));
		for (JProgressBar progressBar : listProgressBar) {
			progressBar.setValue(nbWheat);
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
