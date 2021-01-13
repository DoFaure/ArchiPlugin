package plugins.displays;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import appli.interfaces.ICo2Management;
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

	public static int nbWheat;
	private int nbWheatByClick;

	private JFrame farmerFrame; // Window farmerClicker
	private JPanel farmerPanel;
	private GridBagConstraints c;
	
	private static JTextField nbWheatText; // show number of wheat
	private static ArrayList<JProgressBar> listProgressBar = new ArrayList<JProgressBar>();

	public FarmerClickerDisplay(IProductFactory productFactory, IConsumableFactory consumableFactory, ICo2Management Co2management) {
		super();
		FarmerClickerDisplay.nbWheat = 0;
		this.nbWheatByClick = 1;
		this.productFactory = productFactory;
		this.consumableFactory = consumableFactory;
		initialisationDisplay();
	}
	
	public void initialisationDisplay() {
		this.farmerFrame = new JFrame();
		this.farmerPanel = new JPanel();
		this.farmerPanel.setLayout(new GridBagLayout());
		
		this.c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		
		JLabel testLabel = new JLabel("Farmer Label");
		this.farmerPanel.add(testLabel, c);
		
		this.farmerFrame.add(farmerPanel);
		
		
		farmerFrame.setTitle("Farmer Clicker");
		nbWheatText = new JTextField("Wheat :" + Integer.toString(FarmerClickerDisplay.nbWheat));
		nbWheatText.setEditable(false);
		this.farmerPanel.add(nbWheatText);
		displayProducts(productFactory.createFarmerProducts());
		displayConsumables(consumableFactory.createFarmerConsumables());
		clickerEvent();
		farmerFrame.setPreferredSize(new Dimension(800, 800));
		farmerFrame.setLocation(700, 400); // window position at launch
		farmerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // action launched when exit clicked
		farmerFrame.pack();
		farmerFrame.setVisible(true);
	}

	@Override
	public void displayProducts(List<Products> products) {
		if (products != null && !products.isEmpty()) {
			int positionXPrdt = 0;
			int positionYPrdt = 0;
			
			for (Products product : products) {
				JButton buttonProduct = new JButton(product.getLabel());
				JProgressBar progressBar = new JProgressBar(0, product.getPrice());
				progressBar.setValue(nbWheat); 
				listProgressBar.add(progressBar);
				
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = positionXPrdt;
				c.gridy = positionYPrdt;

				this.farmerPanel.add(buttonProduct, c);
				c.gridx = positionXPrdt++;

				this.farmerPanel.add(progressBar, c);
				c.gridx = positionXPrdt++;

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
			}
		}
	}

	@Override
	public void displayConsumables(List<Consumables> consumables) {
		if (consumables != null && !consumables.isEmpty()) {
			for (Consumables consumable : consumables) {
				JButton buttonConsumable = new JButton(consumable.getLabel());
				JProgressBar progressBar = new JProgressBar(0, consumable.getPrice());
				progressBar.setValue(nbWheat); 
				listProgressBar.add(progressBar);
				
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				
				this.farmerPanel.add(buttonConsumable, c);
				this.farmerPanel.add(progressBar, c);
				buttonConsumable.addActionListener(new ActionListener() {

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
			}
		}
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
