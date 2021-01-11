package plugins.displays;

import java.awt.Dimension;
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
		this.farmerPanel.add(new JLabel("FARM"));
		this.farmerFrame.add(farmerPanel);

		farmerFrame.setTitle("Farmer Clicker");
		nbWheatText = new JTextField("Wheat :" + Integer.toString(FarmerClickerDisplay.nbWheat));
		nbWheatText.setEditable(false);
		this.farmerPanel.add(nbWheatText);
		displayProducts(productFactory.createFarmerProducts());
		displayConsumables(consumableFactory.createFarmerConsumables());
		clickerEvent();
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
				JButton buttonProduct = new JButton(product.getLabel());
				JProgressBar progressBar = new JProgressBar(0, product.getPrice());
				progressBar.setValue(nbWheat); 
				listProgressBar.add(progressBar);
				this.farmerPanel.add(buttonProduct);
				this.farmerPanel.add(progressBar);
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
				this.farmerPanel.add(buttonConsumable);
				this.farmerPanel.add(progressBar);
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
