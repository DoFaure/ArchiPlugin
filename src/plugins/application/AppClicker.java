package plugins.application;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import appli.interfaces.IAppClicker;
import appli.interfaces.IBrewerClickerDisplay;
import appli.interfaces.IFarmerClickerDisplay;
import appli.models.Products;
import appli.plateform.Loader;
import plugins.displays.FarmerClickerDisplay;

public class AppClicker implements IAppClicker {

	JFrame frame = new JFrame(); // Window
	JPanel panel = new JPanel(); // Panel contains components (buttons, text boxes, ...)
	Container jframeContainer;
	JButton buttonFarmerClicker = new JButton("Farmer Clicker"); // First button that launch Farmer clicker onClick
	JButton buttonBrewerClicker = new JButton("Brewer Clicker"); // Second button that launch Brewer clicker onClick

	private Loader loader;
	private FarmerClickerDisplay farmerDisplay;
	private IFarmerClickerDisplay iFarmerDisplay;

	// DEFAULT CONSTRUCTOR
	public AppClicker() {
		jframeContainer = frame.getContentPane();
		panel.add(buttonFarmerClicker);
		panel.add(buttonBrewerClicker);
		frame.setContentPane(panel);

		frame.setPreferredSize(new Dimension(400, 200));
		frame.setLocation(700, 400); // window position at launch
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // action launched when exit clicked
		frame.pack();
		frame.setVisible(true);

	}

	// CONSTRUCTOR FARMER CLICKER
	public AppClicker(IFarmerClickerDisplay iFarmerDisplay) {
		super();
		this.iFarmerDisplay = iFarmerDisplay;
	}

	// EXAMPLE --> CONSTRUCTOR BREWER CLICKER
	public AppClicker(IBrewerClickerDisplay breweryDisplay) {
		super();
	}

	/*
	 * Fonction principale
	 */
	@Override
	public void run() {

		
		// TODO modifie et remplacer PANEL par une nouvelle Frame
		buttonFarmerClicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Loader loader = new Loader();
				List<Object> pluginDependencies = new ArrayList<Object>();
				pluginDependencies
						.add(loader.instanciatePlugin(loader.getDescriptionDisplayDisponibles().get(1), null));
				pluginDependencies
						.add(loader.instanciatePlugin(loader.getDescriptionDisplayDisponibles().get(2), null));
				FarmerClickerDisplay farmerDisplay = (FarmerClickerDisplay) loader
						.instanciatePlugin(loader.getDescriptionDisplayDisponibles().get(0), pluginDependencies);
				AppClicker.this.setFarmerDisplay(farmerDisplay);
				frame.getContentPane().removeAll();
				AppClicker.this.setPanel(AppClicker.this.farmerDisplay.getFarmerPanel());
				frame.setContentPane(getPanel());
				frame.getContentPane().revalidate();
			}
		});
//			List<Products> farmerProducts = this.farmerDisplay.getProductFactory().createFarmerProducts();
//			this.farmerDisplay.displayProducts(farmerProducts);

//
//		List<Consumables> farmerConsumables = this.farmerDisplay.getConsumableFactory().createFarmerConsumables();
//		this.farmerDisplay.displayConsumables(farmerConsumables);

	}

	/**
	 * @return the loader
	 */
	public Loader getLoader() {
		return loader;
	}

	/**
	 * @param loader the loader to set
	 */
	public void setLoader(Loader loader) {
		this.loader = loader;
	}

	/**
	 * @return the farmerDisplay
	 */
	public FarmerClickerDisplay getFarmerDisplay() {
		return farmerDisplay;
	}

	/**
	 * @param farmerDisplay the farmerDisplay to set
	 */
	public void setFarmerDisplay(FarmerClickerDisplay farmerDisplay) {
		this.farmerDisplay = farmerDisplay;
	}

	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @return the panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * @param panel the panel to set
	 */
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

}
