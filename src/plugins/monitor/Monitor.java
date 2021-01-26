package plugins.monitor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import appli.interfaces.IMonitor;
import appli.plateform.DescripteurPlugin;

public class Monitor implements IMonitor, PropertyChangeListener {
	
	private String plugin;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		this.setPlugin((String) evt.getNewValue());
		System.out.println("Plugin chargé : " + plugin);
	}

	private void setPlugin(String plugin) {
		// TODO Auto-generated method stub
		this.plugin = plugin;
	}

	@Override
	public void displayPlugins(List<DescripteurPlugin> plugins) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getPlugin() {
		// TODO Auto-generated method stub
		return plugin;
	}
	
	


}
