package plugins.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import appli.interfaces.IAppClicker;
import appli.interfaces.IFarmerClickerDisplay;

public class AppClicker implements IAppClicker {

	JFrame menuFrame = new JFrame(); // Window
	JPanel panel = new JPanel(); // Panel contains components (buttons, text boxes, ...)
	JButton buttonFarmerClicker = new JButton("Farmer Clicker"); // First button that launch Farmer clicker onClick
	JButton buttonBrewerClicker = new JButton("Brewer Clicker"); // Second button that launch Brewer clicker onClick
	IFarmerClickerDisplay farmerDisplay;

	// DEFAULT CONSTRUCTOR
	public AppClicker(IFarmerClickerDisplay farmerDisplay) {
		this.farmerDisplay = farmerDisplay;
	}

	/*
	 * Fonction principale
	 */
	@Override
	public void run() {

		
		// TODO modifie et remplacer PANEL par une nouvelle Frame
		buttonFarmerClicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				Loader loader = new Loader();
//				List<Object> pluginDependencies = new ArrayList<Object>();
//				pluginDependencies
//						.add(loader.instanciatePlugin(loader.getDescriptionDisplayDisponibles().get(1), null));
//				pluginDependencies
//						.add(loader.instanciatePlugin(loader.getDescriptionDisplayDisponibles().get(2), null));
//				pluginDependencies
//						.add(loader.instanciatePlugin(loader.getDescriptionDisplayDisponibles().get(3), null));
//				FarmerClickerDisplay farmerDisplay = (FarmerClickerDisplay) loader
//						.instanciatePlugin(loader.getDescriptionDisplayDisponibles().get(0), pluginDependencies);
				menuFrame.dispose();
			}
		});

	}

	/**
	 * @return the menuFrame
	 */
	public JFrame getMenuFrame() {
		return menuFrame;
	}

	/**
	 * @param menuFrame the menuFrame to set
	 */
	public void setMenuFrame(JFrame menuFrame) {
		this.menuFrame = menuFrame;
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
