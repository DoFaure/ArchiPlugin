package appli.interfaces;

import java.util.List;

import appli.plateform.DescripteurPlugin;

public interface IMonitor {
	public void displayPlugins(List<DescripteurPlugin> plugins);

	public String getPlugin();
}
