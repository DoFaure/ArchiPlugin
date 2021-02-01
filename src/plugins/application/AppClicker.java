package plugins.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import appli.interfaces.IAppClicker;
import appli.interfaces.IFarmerClickerDisplay;

public class AppClicker implements IAppClicker {

	IFarmerClickerDisplay farmerDisplay;

	/**
	 * Constructor
	 */
	public AppClicker(IFarmerClickerDisplay farmerDisplay) {
		this.farmerDisplay = farmerDisplay;
	}

}
